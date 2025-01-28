package entities.server;

import java.awt.Graphics;
import java.awt.Rectangle;

import graphics.Camera;
import graphics.Depth;
import graphics.Sprite;
import graphics.SpriteStore;

/**
 * Minimal data to draw a game entity on the screen
 */
public abstract class Entity implements Comparable<Entity> {
    
    protected Rectangle hitbox;
    protected Sprite sprite;
    protected Depth depth;

    public Entity(Rectangle hitbox, String spriteName, Depth depth) {
        this.hitbox = hitbox;
        sprite = SpriteStore.getSpriteStore().getSprite(spriteName);
        this.depth = depth;
    }

    public Entity(Rectangle hitbox, Sprite sprite, Depth depth) {
        this.hitbox = hitbox;
        this.sprite = sprite;
        this.depth = depth;
    }

    @Override
    public int compareTo(Entity e) {
        return depth.compareTo(e.depth);
    }

    public void draw(Graphics g, Camera camera) {
        sprite.draw(g, hitbox.x - camera.getX(), hitbox.y - camera.getY());
    }

    public boolean isWithin(Rectangle r) {
        return r.contains(hitbox) || r.intersects(hitbox);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
    
    public Sprite getSprite() {
        return sprite;
    }

    public Depth getDepth() {
        return depth;
    }
}
