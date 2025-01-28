package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;

import entities.server.ServerEntity;
import entities.tiles.Obstacle;
import graphics.Camera;
import graphics.TileMap;

public class MouseInputHandler implements MouseListener, MouseMotionListener{

    private static final int BLOCK_SIZE = TileMap.TILE_SIZE;
    private static final Obstacle cursor = new Obstacle(0, 0);

    private Collection<ServerEntity> entities;
    private Camera camera;

    public MouseInputHandler(Collection<ServerEntity> entities, Camera camera) {
        this.entities = entities;
        this.camera = camera;
    }

    /**
     * if x - Bn < B
     */
    public int snapToGrid(int coord) {
        if (coord > 0) {
            return coord - coord % BLOCK_SIZE;
        } else {
            return coord - coord % BLOCK_SIZE - BLOCK_SIZE;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        entities.add(new Obstacle(snapToGrid(e.getX() + camera.getX()), snapToGrid(e.getY() + camera.getY())));
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        cursor.setLocation(e.getX() + camera.getX() - BLOCK_SIZE / 2, e.getY() + camera.getY() - BLOCK_SIZE / 2);
    }
    
}
