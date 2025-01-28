package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import networking.Client;
import networking.packets.PlayerUpdatePacket;

public class KeyInputHandler implements KeyListener {

    private Client client;
    private Set<Integer> keys;

    public KeyInputHandler(Client client) {
        this.client = client;
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
            case KeyEvent.VK_LEFT:
            case 'A':
            case KeyEvent.VK_DOWN:
            case 'S':
            case KeyEvent.VK_RIGHT:
            case 'D':
                PlayerUpdatePacket playerUpdatePacket = new PlayerUpdatePacket(client.getUsername(), key, false);
                client.sendData(playerUpdatePacket.serialize());
                break;
            default:
                break;
        }
        
    }

    private void handleQuickKey(int key) {
        switch (key) {
            case KeyEvent.VK_UP:
            case 'W':
            case KeyEvent.VK_DOWN:
            case 'S':
                PlayerUpdatePacket playerUpdatePacket = new PlayerUpdatePacket(client.getUsername(), key, true);
                client.sendData(playerUpdatePacket.serialize());
            default:
                break;
        }
    }
    
}
