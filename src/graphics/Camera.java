package graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import entities.server.Entity;

public class Camera {

    private Rectangle view;
    private Entity focus;

    public Camera(Dimension dimensions, Entity focus) {
        view = new Rectangle(new Point(0, 0), dimensions);
        this.focus = focus;
        update();
    }

    public boolean inView(Entity e) {
        return e.isWithin(view);
    }

    public void update() {
        view.x = focus.getHitbox().x + focus.getHitbox().width / 2 - view.width / 2;
        view.y = focus.getHitbox().y + focus.getHitbox().height / 2 - view.height / 2;
    }

    public int getX() {
        return view.x;
    }

    public int getY() {
        return view.y;
    }

    public void draw(Graphics g) {
        g.drawRect(0, 0, view.width, view.height);
    }

    public void setFocus(Entity focus) {
        this.focus = focus;
    }
}
