package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

import entities.server.Entity;
import utils.ConcurrentSortedList;

public class GamePanel extends JPanel {

    private static final Dimension PREFERRED_SIZE = new Dimension(1000, 1000);
    private ConcurrentSortedList<? extends Entity> entities;
    private Camera camera;
    private long lastTime;
    private int frames;
    private int fps;
    
    public GamePanel(ConcurrentSortedList<? extends Entity> entities, Entity cameraFocus) {
        setPreferredSize(PREFERRED_SIZE);
        setLayout(null);
        this.entities = entities;
        this.camera = new Camera(PREFERRED_SIZE, cameraFocus);
        this.lastTime = System.nanoTime();
        frames = 0;
        fps = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Toolkit.getDefaultToolkit().sync(); // prevents tarring for single user
        Graphics2D g2D = (Graphics2D) g;

        camera.draw(g2D);
        g.setColor(Color.LIGHT_GRAY);
        TileMap.drawGrid(g, camera, 10, 50);

        for (Entity e : entities) {
            if (camera.inView(e)) {
                e.draw(g2D, camera);
            }
        }

        long currentTime = System.nanoTime();
        frames++;
        if (currentTime - lastTime > 1000000000) {
            fps = frames;
            lastTime = currentTime;
            frames = 0;
        }
        g.setColor(Color.BLACK);
        g2D.drawString("FPS: " + fps, 100, 100);

    }

    public void updateCamera() {
        camera.update();
    }

    public void setFocus(Entity camerafocus) {
        camera.setFocus(camerafocus);
    }

}
