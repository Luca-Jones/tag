package networking;

import java.awt.event.KeyEvent;
import java.net.InetAddress;

import entities.server.Player;
import networking.packets.PlayerUpdatePacket;

public class ClientHandler {

    public InetAddress ipAddress;   // ip address of the client
    public int port;                // port the server is using to communicate with the client
    public Player player;
    public String username;

    public ClientHandler(InetAddress ipAddress, int port, Player player, String username) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.player = player;
        this.username = username;
    }

    public void handlePlayerUpdate(PlayerUpdatePacket packet) {
        if (packet.isQuickKey) {
            switch (packet.key) {
                case KeyEvent.VK_UP:
                case 'W':
                    player.doubleJump();
                    break;
                case KeyEvent.VK_DOWN:
                case 'S':
                    player.dash();
                default:
                    break;
            }
        } else {
            switch (packet.key) {
                case KeyEvent.VK_UP:
                case 'W':
                    player.jump();
                    break;
                case KeyEvent.VK_LEFT:
                case 'A':
                    player.moveLeft();
                    break;
                case KeyEvent.VK_DOWN:
                case 'S':
                    break;
                case KeyEvent.VK_RIGHT:
                case 'D':
                    player.moveRight();
                    break;
                case KeyEvent.VK_ENTER:
                    break;
                default:
                    break;
            }
        }
        
    }

}
