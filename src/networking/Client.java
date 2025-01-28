package networking;

import java.util.ArrayList;
import java.util.List;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import entities.client.ClientEntity;
import entities.client.ClientPlayer;
import entities.server.EntityType;
import graphics.Depth;
import graphics.GamePanel;
import inputs.KeyInputHandler;
import networking.packets.*;
import utils.ConcurrentSortedList;

/**
 * This client is responsible for rendering the game and controlling the user's
 * player.
 * The client sends packets to the server when the player wishes to move.
 * The client receives packets from the server repeatedly to update the game
 * state.
 */
public class Client extends Thread {

    private static final int SERVER_PORT = Server.PORT; // port that the server is listening on

    private GamePanel gamePanel;
    private KeyInputHandler keyInputHandler;
    private ConcurrentSortedList<ClientEntity> entities;
    private List<ClientEntity> entityBuffer;
    private InetAddress serverIpAddress; // server's ip address
    private String username;
    private DatagramSocket socket;

    public Client(InetAddress serverIpAddress, String username) {
        this.username = username;
        try {
            this.socket = new DatagramSocket();
            this.serverIpAddress = serverIpAddress;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.entityBuffer = new ArrayList<>(); // Does this need to be thread safe?
        this.entities = new ConcurrentSortedList<>();
        ClientPlayer player = new ClientPlayer(new Rectangle(), "IDLE_LEFT", Depth.FOREGROUND, username, false);
        this.gamePanel = new GamePanel(entities, player);
        this.keyInputHandler = new KeyInputHandler(this);
        gamePanel.addKeyListener(keyInputHandler);

        LoginPacket loginPacket = new LoginPacket(username);
        sendData(loginPacket.serialize());
    }

    @Override
    public void run() {

        while (true) {

            byte[] data = new byte[2048];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            parsePacket(packet);

            keyInputHandler.handleKeys();
            gamePanel.updateCamera();

        }
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverIpAddress, SERVER_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsePacket(DatagramPacket datagramPacket) {

        byte[] data = datagramPacket.getData();
        Packet packet = Packet.deserialize(data);
        if (packet instanceof EntityPacket) {
            EntityPacket entityPacket = (EntityPacket) packet;
            if (entityPacket.entityType == EntityType.PLAYER) {
                ClientPlayer somePlayer = new ClientPlayer(entityPacket.hitbox, entityPacket.spriteName, entityPacket.depth, entityPacket.username, entityPacket.it);
                if (entityPacket.username.equals(username)) {
                    gamePanel.setFocus(somePlayer);
                }
                entityBuffer.add(somePlayer);
            } else {
                entityBuffer.add(new ClientEntity(entityPacket.hitbox, entityPacket.spriteName, entityPacket.depth));
            }
        } else if (packet instanceof LoginPacket) {
            LoginPacket loginPacket = (LoginPacket) packet;
            System.out.println("[" + datagramPacket.getAddress().getHostAddress() + ":" + datagramPacket.getPort()
                    + "] " + loginPacket.username + " has joined the game!");
        } else if (packet instanceof DisconnectPacket) {
            DisconnectPacket disconnectPacket = (DisconnectPacket) packet;
            System.out.println("[" + datagramPacket.getAddress().getHostAddress() + ":" + datagramPacket.getPort()
                    + "] " + disconnectPacket.username + " has left the game...");
        } else if (packet instanceof RedrawPacket) {
            entities.clear();
            entities.addAll(entityBuffer);
            entityBuffer.clear();
            draw();
        }
    }

    public InetAddress getServerIpAddress() {
        return serverIpAddress;
    }

    public String getUsername() {
        return username;
    }

    private void draw() {
        gamePanel.repaint();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

}
