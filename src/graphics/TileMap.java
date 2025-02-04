package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entities.tiles.*;
import game.Main;

/**
 * This class is meant to make it easier to build a tile-based map.
 * The bottom left point and the square grid spacing must be defined,
 * and from then on, coordinates are relative to this grid in the 
 * traditional cartesian orientation. (multiples of the tile size 
 * wrt to the bottom left point with increasing y values going up)
 * The Tile at the bottom left box is (0,0)
 */
public class TileMap {
   
    public static int TILE_SIZE = 50;
    private static Point BOTTOM_LEFT = new Point(0, 0);
    public static final int GROUND = BOTTOM_LEFT.y;
    
    private Point spawnPoint;
    private Set<Tile> tiles;
    
    public TileMap(int spawnX, int spawnY) {
        this.spawnPoint = new Point(spawnX, spawnY);
        tiles = new HashSet<>();
    }
    
    private TileMap(int spawnX, int spawnY, Map<Point, Tile> tileMap) {
        this.tiles = new HashSet<>(tileMap.values());
        this.spawnPoint = new Point(spawnX, spawnY);
        tileMap.forEach((point, tile) -> add(point.x, point.y, tile));
    }
    
    /**
     * Snaps a coordinate to the nearest grid point
     * return max(TILE_SIZE * n) s.t. TILE_SIZE * n < coord
     */
    public int snapToGrid(int coord) {
        return coord - coord % TILE_SIZE - ((coord < 0) ? TILE_SIZE : 0);
    }
    
    public static Point MapToScreen(Point mapCoordinate) {
        return new Point(BOTTOM_LEFT.x + mapCoordinate.x * TILE_SIZE, BOTTOM_LEFT.y - (mapCoordinate.y+1) * TILE_SIZE);
    }
    
    /**
     * Draws the grid lines on the screen. This class is not responsible for drawing the Tile entities.
     */
    public static void drawGrid(Graphics g, Camera camera, int rows, int cols) {
        if (Main.DEBUG) {
            g.setColor(Color.LIGHT_GRAY);
            
            // vertical lines
            for (int col = 0; col <= cols; col++) {
                Point bottom = MapToScreen(new Point(col, -1));
                Point top = MapToScreen(new Point(col, rows));
                g.drawLine(bottom.x - camera.getX(), bottom.y - camera.getY(), top.x - camera.getX(), top.y - camera.getY());
            }
            
            // horizontal lines
            for (int row = -1; row <= rows; row++) {
                Point left = MapToScreen(new Point(0, row));
                Point right = MapToScreen(new Point(cols, row));
                g.drawLine(left.x - camera.getX(), left.y - camera.getY(), right.x - camera.getX(), right.y - camera.getY());
            }
        }
    }
    
    public void add(int x, int y, Tile tile) {
        Point location = MapToScreen(new Point(x, y));
        tile.setLocation(location.x, location.y);
        tiles.add(tile);
    }

    /**
     * @return the tile containing (x,y). If no tile exists at (x,y), then null is
     *         returned.
     */
    public Tile get(int x, int y) {
        x = snapToGrid(x);
        y = snapToGrid(y);
        for (Tile tile : tiles) {
            if (tile.getX() == x && tile.getY() == y) {
                return tile;
            }
        }
        return null;
    }
    
    public Collection<Tile> getTiles() {
        return tiles;
    }

    /**
     * @return the spawn point of the map in screen coordinates
     */
    public Point getSpawnPoint() {
        return MapToScreen(spawnPoint);
    }

    public static TileMap MAP_0 = new TileMap(0, 0, Map.of());
    public static TileMap MAP_1 = new TileMap(3, 0, Map.of(
        new Point(4, 0), new Wall(),
        new Point(4, 1), new Wall(),
        new Point(4, 2), new Wall(),
        new Point(4, 3), new Wall()
    ));
    private static TileMap MAP_2() {
        TileMap tileMap = new TileMap(3,0);
        tileMap.add(10, 0, new DangerousObstacle());
        return tileMap;
    }
    public static TileMap MAP_2 = MAP_2();
    private static TileMap MAP_3() {
        TileMap tileMap = new TileMap(0, 3);
        tileMap.add(1, -1, new DangerousObstacle());
        tileMap.add(2, -1, new DangerousObstacle());
        tileMap.add(3, -1, new DangerousObstacle());
        tileMap.add(4, -1, new DangerousObstacle());
        tileMap.add(5, -1, new DangerousObstacle());
        tileMap.add(6, -1, new DangerousObstacle());
        tileMap.add(7, -1, new DangerousObstacle());
        tileMap.add(8, -1, new DangerousObstacle());
        tileMap.add(9, -1, new DangerousObstacle());
        tileMap.add(10, -1, new DangerousObstacle());
        tileMap.add(11, -1, new DangerousObstacle());
        tileMap.add(12, -1, new DangerousObstacle());
        tileMap.add(0, 0, new Platform());
        tileMap.add(1, 0, new Platform());
        tileMap.add(2, 0, new Platform());
        tileMap.add(3, 0, new Platform());
        tileMap.add(4, 0, new Platform());
        tileMap.add(5, 0, new Platform());
        tileMap.add(6, 0, new Platform());
        tileMap.add(7, 0, new Platform());
        tileMap.add(8, 0, new Platform());
        tileMap.add(9, 0, new Platform());
        tileMap.add(10, 0, new Platform());
        tileMap.add(11, 0, new Platform());
        tileMap.add(12, 0, new Platform());
        tileMap.add(6, 1, new Wall());
        tileMap.add(6, 2, new Wall());
        tileMap.add(6, 3, new Wall());
        tileMap.add(6, 4, new Wall());
        tileMap.add(6, 5, new Wall());
        tileMap.add(8, 1, new End());
        return tileMap;
    };
    public static TileMap MAP_3 = MAP_3();

    private static TileMap MAP_4() {
        TileMap tilemap = new TileMap(1, 3);

        // border
        for (int i = 0; i <= 50; i ++) {
            tilemap.add(i, 0, new Obstacle());
            tilemap.add(i, 10, new Obstacle());
        }
        for (int i = 1; i < 10; i ++) {
            tilemap.add(0, i, new Obstacle());
            tilemap.add(50, i, new Obstacle());
        }
        
        tilemap.add(4, 1, new Obstacle());
        tilemap.add(4, 2, new Obstacle());
        tilemap.add(4, 3, new Obstacle());
        tilemap.add(4, 4, new Obstacle());
        tilemap.add(3, 2, new Platform());
        tilemap.add(3, 1, new Obstacle());
        tilemap.add(3, 2, new Obstacle());
        tilemap.add(3, 3, new Obstacle());
        tilemap.add(3, 4, new Obstacle());
        tilemap.add(2, 2, new Platform());

        tilemap.add(5, 1, new DangerousObstacle());
        tilemap.add(6, 1, new DangerousObstacle());
        tilemap.add(7, 1, new DangerousObstacle());
        tilemap.add(8, 1, new DangerousObstacle());
        tilemap.add(9, 1, new DangerousObstacle());
        tilemap.add(10, 1, new DangerousObstacle());
        tilemap.add(11, 1, new DangerousObstacle());
        tilemap.add(12, 1, new DangerousObstacle());
        tilemap.add(13, 1, new DangerousObstacle());
        tilemap.add(14, 1, new DangerousObstacle());
        tilemap.add(15, 1, new DangerousObstacle());
        tilemap.add(16, 1, new DangerousObstacle());
        tilemap.add(17, 1, new DangerousObstacle());
        
        tilemap.add(18, 1, new Wall());
        tilemap.add(18, 2, new Wall());
        tilemap.add(18, 3, new Wall());
        tilemap.add(18, 4, new Wall());
        tilemap.add(18, 5, new Wall());
        tilemap.add(18, 6, new Wall());

        tilemap.add(23, 7, new Platform());
        tilemap.add(28, 9, new Wall());
        tilemap.add(28, 8, new Wall());
        tilemap.add(28, 7, new Wall());
        tilemap.add(28, 6, new Wall());

        tilemap.add(49, 1, new End());
        
        return tilemap;
    }

    public static TileMap MAP_4 = MAP_4();
   
}
