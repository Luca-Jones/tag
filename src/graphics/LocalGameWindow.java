package graphics;

import javax.swing.JFrame;

import game.Game;

public class LocalGameWindow extends JFrame {
    
    private static final String TITLE = "Platformer Game";

    public LocalGameWindow(Game game) {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        GamePanel gamePanel = game.getGamePanel();
        add(gamePanel);
        pack();
        gamePanel.requestFocus();
    }
}
