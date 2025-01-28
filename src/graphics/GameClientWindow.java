package graphics;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import game.Main;
import networking.Client;
import networking.packets.DisconnectPacket;

public class GameClientWindow extends JFrame implements WindowListener {

    private static final String TITLE = "Game Client";
    Client gameClient;

    public GameClientWindow(Client gameClient) {
        this.gameClient = gameClient;
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(Main.DEBUG);
        addWindowListener(this);
        GamePanel gamePanel = gameClient.getGamePanel();
        add(gamePanel);
        pack();
        gamePanel.requestFocus();
    }
    
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        DisconnectPacket packet = new DisconnectPacket(gameClient.getUsername());
        gameClient.sendData(packet.serialize());
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
