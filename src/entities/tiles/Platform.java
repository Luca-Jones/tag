package entities.tiles;

import entities.server.ServerEntity;
import graphics.SpriteStore;

public class Platform extends Tile {

    public Platform(int x, int y) {
        super(x, y, 50, 10, 0, 0, SpriteStore.getSpriteStore().getSprite("PLATFORM"));
    }

    public Platform() {
        super(0, 0, 50, 10, 0, 0, SpriteStore.getSpriteStore().getSprite("PLATFORM"));
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void handleCollision(ServerEntity entity) {}
    
}
