package entities.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import graphics.Camera;
import graphics.Depth;

public class ClientPlayer extends ClientEntity {
    
    private String username;
    private boolean it;

    public ClientPlayer(Rectangle hitbox, String spriteName, Depth depth, String username, boolean it) {
        super(hitbox, spriteName, depth);
        this.username = username;
        this.it = it;
    }

    public void draw(Graphics g, Camera camera) {
        super.draw(g, camera);
        g.setColor(Color.BLACK);
        g.drawString(username, hitbox.x - camera.getX(), hitbox.y - camera.getY() - 10);
        if (it) {
            g.drawString("IT", hitbox.x - camera.getX(), hitbox.y - camera.getY() - 20);
        }
    }
}
