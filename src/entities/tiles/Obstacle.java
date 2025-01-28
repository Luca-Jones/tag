package entities.tiles;

import entities.server.ServerEntity;
import graphics.SpriteStore;

public class Obstacle extends Tile {

    public Obstacle(int x, int y, int xOffset, int yOffset) {
        super(x, y, xOffset, yOffset, SpriteStore.getSpriteStore().getSprite("OBSTACLE"));
    }

    public Obstacle(int x, int y) {
        super(x, y, 0, 0, SpriteStore.getSpriteStore().getSprite("OBSTACLE"));
    }

    public Obstacle() {
        super(0,0, 0, 0, SpriteStore.getSpriteStore().getSprite("OBSTACLE"));
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void handleCollision(ServerEntity entity) {}

}
