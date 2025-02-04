package networking;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import entities.server.Player;
import entities.server.ServerEntity;
import graphics.TileMap;
import networking.packets.*;
import utils.ConcurrentSortedList;

public class Server extends Thread {

    public static final long NANOSECONDS_PER_SECOND = 1000000000; // 1s = 10^9 ns
    public static final long UPDATES_PER_SECOND = 100;
    public static final long NANOSECONDS_PER_UPDATE = NANOSECONDS_PER_SECOND / UPDATES_PER_SECOND;
    public static final double TIME_STEP = 0.04;

    private ConcurrentSortedList<ServerEntity> entities;
    private final TileMap TILE_MAP = TileMap.MAP_3;
    public boolean gameInProgress;

    public static final int PORT = 4999;
    public static final String ipAddress = "localhost";//"172.20.10.9";
    private DatagramSocket socket;
    private List<ClientHandler> clients;
    private PacketListener packetListener;

    /**
     * This server hosts all of the game logic for the given game. Clients send it
     * commands to control their player and receive the game state?
     */
    public Server() {

        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            socket = new DatagramSocket(PORT, address);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        
        clients = new ArrayList<>();
        packetListener = new PacketListener(socket);
        packetListener.start();

        entities = new ConcurrentSortedList<ServerEntity>();
        entities.addAll(TILE_MAP.getTiles());
        gameInProgress = true;
    }

    @Override
    public void run() {

        long currentTime = 0;
        long previousTime = System.nanoTime();
        long deltaTime = 0;
        long accumulatedTime = 0;

        while (gameInProgress) {

            currentTime = System.nanoTime();
            deltaTime = currentTime - previousTime;
            previousTime += deltaTime;
            accumulatedTime += deltaTime;

            // update at a fixed rate
            while (accumulatedTime > NANOSECONDS_PER_UPDATE) {

                update(TIME_STEP);
                accumulatedTime -= NANOSECONDS_PER_UPDATE;

                // broadcast game state to all clients
                for (ServerEntity entity : entities) {
                    EntityPacket entityPacket = new EntityPacket(entity);
                    sendDataToAllClients(entityPacket.serialize());
                }
                RedrawPacket redrawPacket = new RedrawPacket();
                sendDataToAllClients(redrawPacket.serialize());
            }

            // process incoming packets
            while (!packetListener.packets.isEmpty()) {
                DatagramPacket packet = packetListener.packets.poll();
                if (packet == null) {
                    break;
                }
                parsePacket(packet);
            }
        }
    }

    public void update(double deltaTime) {

        for (ServerEntity entity : entities) {
            entity.update(deltaTime);
        }

        for (ServerEntity entity : entities) {
            for (ServerEntity otherEntity : entities) {
                if (entity != otherEntity && entity.isTouching(otherEntity)) {
                    entity.addCollision(otherEntity);
                }
            }
            entity.handleQueuedCollisions();
        }

    }

    public void removePlayer(Player player) {
        entities.remove(player);
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (ClientHandler client : clients) {
            sendData(data, client.ipAddress, client.port);
        }
    }

    private void parsePacket(DatagramPacket datagramPacket) {

        byte[] data = datagramPacket.getData();
        Packet packet = Packet.deserialize(data);

        InetAddress address = datagramPacket.getAddress();
        int port = datagramPacket.getPort();

        if (packet instanceof LoginPacket) {
            LoginPacket loginPacket = (LoginPacket) packet;
            Point playerSpawn = TILE_MAP.getSpawnPoint();
            Player player = new Player(playerSpawn.x, playerSpawn.y, loginPacket.username);
            ClientHandler client = new ClientHandler(address, port, player, loginPacket.username);
            if (clients.isEmpty()) {client.player.it = true;}
            addConnection(client, loginPacket);
        } else if (packet instanceof DisconnectPacket) {
            DisconnectPacket disconnectPacket = (DisconnectPacket) packet;
            System.out.println("[" + address.getHostAddress() + ":" + port + "] " + disconnectPacket.username
                    + " has left the game ...");
            removeConnection(disconnectPacket);
        } else if (packet instanceof PlayerUpdatePacket) {
            PlayerUpdatePacket playerUpdatePacket = (PlayerUpdatePacket) packet;
            handlePlayerUpdate(playerUpdatePacket);
        }
    }

    /**
     * @return the client with the given username, or null if no such client exists
     */
    public ClientHandler getClient(String username) {
        for (ClientHandler client : clients) {
            if (client.username.equalsIgnoreCase(username)) {
                return client;
            }
        }
        return null;
    }

    private void addConnection(ClientHandler newClient, LoginPacket loginPacket) {

        boolean alreadyConnected = false;

        for (ClientHandler client : clients) {

            // check if the client is already connected
            if (newClient.username.equalsIgnoreCase(client.username)) {
                if (client.ipAddress == null) {
                    client.ipAddress = newClient.ipAddress;
                }
                if (client.port == -1) {
                    client.port = newClient.port;
                }
                alreadyConnected = true;
            } else {

                // notify other clients that a new client has connected
                sendData(loginPacket.serialize(), client.ipAddress, client.port);

                // notify the new client of the already connected clients
                LoginPacket existingClientPacket = new LoginPacket(client.username);
                sendData(existingClientPacket.serialize(), newClient.ipAddress, newClient.port);

            }
        }

        // add the new client if they are not already connected
        if (!alreadyConnected) {
            clients.add(newClient);
            entities.add(newClient.player);
        }

    }

    private void removeConnection(DisconnectPacket disconnectPacket) {
        ClientHandler client = getClient(disconnectPacket.username);
        if (client != null) {
            removePlayer(client.player);
            clients.remove(client);
            sendDataToAllClients(disconnectPacket.serialize());
        }
    }

    private void handlePlayerUpdate(PlayerUpdatePacket playerUpdatePacket) {
        ClientHandler client = getClient(playerUpdatePacket.username);
        if (client != null)
            client.handlePlayerUpdate(playerUpdatePacket);
    }

}
