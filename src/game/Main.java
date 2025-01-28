package game;

import java.net.InetAddress;
import java.net.UnknownHostException;

import graphics.GameClientWindow;
import networking.Client;
import networking.LoginWindow;
import networking.Server;

/**
 * This iteration uses the client-server model and is server authoritative.
 */
public class Main {

    public static final boolean DEBUG = true;
    public static LoginWindow loginWindow;

    public static void main(String[] args) {

        loginWindow = new LoginWindow();
 
        
    }
    
    public static void startServer() {

        // create a new server
        Server server = new Server();
        server.start();
        
    }
    
    public static void joinServer(String username) {
        
        try {

            InetAddress serverIpAddress = InetAddress.getByName(Server.ipAddress);
            
            // create a client for the user
            Client client = new Client(serverIpAddress, username);
            client.start();
            
            // make a window for this user's game client
            GameClientWindow gameWindow = new GameClientWindow(client);
            gameWindow.setVisible(true);
            
            loginWindow.dispose();

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    }
}
