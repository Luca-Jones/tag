package entities.tiles;

import graphics.SpriteStore;

public class DangerousObstacle extends Obstacle {

    public DangerousObstacle(int x, int y) {
        super(x, y, 0, 0);
        this.sprite = SpriteStore.getSpriteStore().getSprite("DANGEROUS_OBSTACLE");
    }

    public DangerousObstacle() {
        super(0,0, 0, 0);
        this.sprite = SpriteStore.getSpriteStore().getSprite("DANGEROUS_OBSTACLE");
    }

}
