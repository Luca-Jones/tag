package entities.tiles;

import entities.server.ServerEntity;
import graphics.Depth;
import graphics.Sprite;
import graphics.TileMap;

public abstract class Tile extends ServerEntity {

    private int xOffset;
    private int yOffset;

    // Regular Tiles
    public Tile(int x, int y, int xOffset, int yOffset, Sprite sprite) {
        super(x, y, TileMap.TILE_SIZE, TileMap.TILE_SIZE, sprite, Depth.TILE);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    // Custom tiles, ex. platforms
    public Tile(int x, int y, int width, int height, int xOffset, int yOffset, Sprite sprite) {
        super(x, y, width, height, sprite, Depth.TILE);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getX() {
        return hitbox.x;
    }

    public int getY() {
        return hitbox.y;
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x + xOffset, y + yOffset);
    }
}
