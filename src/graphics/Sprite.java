package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

// will this be too big to serialize eventually?
public class Sprite implements Serializable {
    
    private String name;
    private Color color;
    private int width;
    private int height;

    public Sprite(String name, Color color, int width, int height) {
        this.name = name;
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public String getName() {
        return name;
    }
}
