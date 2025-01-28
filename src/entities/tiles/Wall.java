package entities.tiles;

import graphics.SpriteStore;

public class Wall extends Obstacle {
    
    public Wall(int x, int y) {
        super(x, y, 0, 0);
        sprite = SpriteStore.getSpriteStore().getSprite("WALL");
    }

    public Wall() {
        super(0, 0, 0, 0);
        sprite = SpriteStore.getSpriteStore().getSprite("WALL");
    }
    
}
