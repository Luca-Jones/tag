package utils;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Trajectory {

    private List<Point> points;

    public Trajectory(double dt, int iterations, int x, int y, int vx, int vy, int ax, int ay) {
        points = new ArrayList<>();
        points.add(new Point(x, y));
        for (int i = 0; i < iterations; i++) {
            vx += ax / 2 * dt;
            vy += ay / 2 * dt;
            x += vx * dt;
            y += vy * dt;
            vx += ax / 2 * dt;
            vy += ay / 2 * dt;

            points.add(new Point(x, y));
        }
    }

    public void draw(Graphics g) {
        for (int i = 1; i < points.size(); i++) {
            g.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y);
        }
    }

    public Point getLastPoint() {
        return points.getLast();
    }

    /**
     * @param y
     * @return The point with the largest y coordinate less than y. If no such point
     *         exists, the last point in the trajectory is returned.
     */
    public Point getLastPoint(int y) {
        Point point;
        for (int i = points.size() - 1; i >= 0; i--) {
            point = points.get(i);
            if (point.y < y) {
                return point;
            }
        }
        return getLastPoint();
    }
}
