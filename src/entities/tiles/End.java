package entities.tiles;

import entities.server.ServerEntity;
import graphics.SpriteStore;

public class End extends Tile {
    
    public End(int x, int y) {
        super(x, y, 25, 50, 25, 0, SpriteStore.getSpriteStore().getSprite("END"));
    }

    public End() {
        super(0, 0, 25, 50, 25, 0, SpriteStore.getSpriteStore().getSprite("END"));
    }

    @Override
    public void update(double deltaTime) {}

    @Override
    public void handleCollision(ServerEntity entity) {}

}
