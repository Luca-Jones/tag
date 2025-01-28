package entities.server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import entities.tiles.Obstacle;
import entities.tiles.Platform;
import game.Main;
import graphics.Camera;
import graphics.Depth;
import graphics.Sprite;

public abstract class MoveableEntity extends ServerEntity {

    public static final double ENERGY_LOSS = 0.3;

    protected Rectangle previousHitbox;
    protected int velocityX;
    protected int velocityY;
    protected int accelerationX;
    protected int accelerationY;
    protected double frictionCoefficient;

    public MoveableEntity(int x, int y, int width, int height, Sprite sprite, Depth depth, double frictionCoefficient, int gravity) {
        super(x, y, width, height, sprite, depth);
        previousHitbox = new Rectangle(hitbox);
        velocityX = 0;
        velocityY = 0;
        accelerationX = 0;
        accelerationY = gravity;
        this.frictionCoefficient = frictionCoefficient;
    }

    @Override
    public void update(double deltaTime) {

        previousHitbox = new Rectangle(hitbox);

        if ((int) (Math.abs(velocityX) * deltaTime) < 1) {
            velocityX = 0;
        }

        accelerationX = (int) (-frictionCoefficient * velocityX);

        velocityX += accelerationX / 2 * deltaTime;
        velocityY += accelerationY / 2 * deltaTime;
        hitbox.x += velocityX * deltaTime;
        hitbox.y += velocityY * deltaTime;
        velocityX += accelerationX / 2 * deltaTime;
        velocityY += accelerationY / 2 * deltaTime;

    }

    @Override
    public void draw(Graphics g, Camera camera) {
        super.draw(g, camera);
        if (Main.DEBUG) {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(previousHitbox.x - camera.getX(), previousHitbox.y - camera.getY(), previousHitbox.width, previousHitbox.height);
        }
    }

    @Override
    public void handleCollision(ServerEntity entity) {
        
        if (entity instanceof Obstacle) {

            // this above entity
            if (previousHitbox.y + previousHitbox.height <= entity.hitbox.y) {
                velocityY = 0;
                hitbox.y = entity.hitbox.y - hitbox.height;
            }

            // this below entity
            else if (previousHitbox.y >= entity.hitbox.y + entity.hitbox.height) {
                velocityY = 0;
                hitbox.y = entity.hitbox.y + entity.hitbox.height;
            }

            // this left of entity
            else if (previousHitbox.x + previousHitbox.width <= entity.hitbox.x) {
                velocityX = 0;
                hitbox.x = entity.hitbox.x - hitbox.width;
            }

            // this right of entity
            else if (previousHitbox.x >= entity.hitbox.x + entity.hitbox.width) {
                velocityX = 0;
                hitbox.x = entity.hitbox.x + entity.hitbox.width;
            }

        } else if (entity instanceof Platform) {
            
            // this left of entity
            if (previousHitbox.x + previousHitbox.width <= entity.hitbox.x) {
                
            }

            // this right of entity
            else if (previousHitbox.x >= entity.hitbox.x + entity.hitbox.width) {
                
            }

            // this above entity
            if (previousHitbox.y + previousHitbox.height <= entity.hitbox.y) {
                velocityY = 0;
                hitbox.y = entity.hitbox.y - hitbox.height;
            }

            // this below entity
            else if (previousHitbox.y >= entity.hitbox.y + entity.hitbox.height) {
                
            }
        }
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        previousHitbox.setLocation(new Point(x, y));
    }

}
