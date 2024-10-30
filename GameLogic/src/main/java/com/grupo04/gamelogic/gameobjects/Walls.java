package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Vector;

public class Walls extends GameObject {
    final Color color = new Color(60, 60, 60);
    Vector[] pos;
    Vector[] sizes;

    public Walls(int thickness, int headerOffset, int width, int height) {
        super();
        pos = new Vector[]{
                new Vector(thickness / 2.0f, headerOffset + (height - headerOffset) / 2.0f),
                new Vector(width / 2.0f, headerOffset + thickness / 2.0f),
                new Vector(width - (thickness / 2.0f), headerOffset + (height - headerOffset) / 2.0f)
        };
        sizes = new Vector[]{
                new Vector(thickness, height - headerOffset),
                new Vector(width, thickness),
        };
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        graphics.setColor(color);
        graphics.fillRectangle(pos[0], sizes[0].x, sizes[0].y);
        graphics.fillRectangle(pos[1], sizes[1].x, sizes[1].y);
        graphics.fillRectangle(pos[2], sizes[0].x, sizes[0].y);
    }
}
