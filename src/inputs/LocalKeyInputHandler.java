package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import entities.server.Player;

public class LocalKeyInputHandler implements KeyListener {

    private Player controlledPlayer;
    private Set<Integer> keys;

    public LocalKeyInputHandler(Player controlledCharacter) {
        this.controlledPlayer = controlledCharacter;
        this.keys = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keys.contains(e.getKeyCode())) {
            keys.add(e.getKeyCode());
            handleQuickKey(e.getKeyCode());
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    public void handleKeys() {
        for (int key : keys) {
            handleKey(key);
        }
    }

    private void handleKey(int key) {

        switch (key) {
            case KeyEvent.VK_UP:
            case 'W':
                controlledPlayer.jump();
                break;
            case KeyEvent.VK_LEFT:
            case 'A':
                controlledPlayer.moveLeft();
                break;
            case KeyEvent.VK_DOWN:
            case 'S':
                break;
            case KeyEvent.VK_RIGHT:
            case 'D':
                controlledPlayer.moveRight();
                break;
            case KeyEvent.VK_ENTER:
                break;
            default:
                break;
        }
        
    }

    private void handleQuickKey(int key) {
        switch (key) {
            case KeyEvent.VK_UP:
            case 'W':
                controlledPlayer.doubleJump();
                break;
            case 'S':
                controlledPlayer.dash();
                // controlledPlayer.groundPound();
            default:
                break;
        }
    }
    
}
