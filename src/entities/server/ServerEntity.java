package entities.server;

import java.awt.Graphics;
import java.awt.Rectangle;

import graphics.Camera;
import graphics.Depth;
import graphics.Sprite;
import utils.PriorityCollection;

public abstract class ServerEntity extends Entity {

    protected PriorityCollection<ServerEntity> collisionQueue;

    public ServerEntity(int x, int y, int width, int height, Sprite sprite, Depth depth) {
        super(new Rectangle(x, y, width, height), sprite, depth);
        collisionQueue = new PriorityCollection<>(new CollisionPriorityComparator(this));
    }

    public void draw(Graphics g, Camera camera) {
        sprite.draw(g, hitbox.x - camera.getX(), hitbox.y - camera.getY());
    }

    public abstract void update(double deltaTime);
    public abstract void handleCollision(ServerEntity entity);
    
    public boolean isTouching(ServerEntity entity) {
        return hitbox.intersects(entity.hitbox);
    }

    public void setLocation(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }
    
    public void addCollision(ServerEntity entity) {
        collisionQueue.add(entity);
    }

    public void handleQueuedCollisions() {
        for (ServerEntity entity : collisionQueue) {
            // need to check if nth entity is still valid after n-1 adjustments
            if (this.isTouching(entity)) {
                handleCollision(entity);
            }
        }
        collisionQueue.clear();
    }

}
