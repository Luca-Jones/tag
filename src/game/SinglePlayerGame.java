package game;

import graphics.LocalGameWindow;

public class SinglePlayerGame {
    public static void main(String[] args) {
        Game game = new Game();
        LocalGameWindow gameWindow = new LocalGameWindow(game);
        gameWindow.setVisible(true);
        game.start();
    }
}
