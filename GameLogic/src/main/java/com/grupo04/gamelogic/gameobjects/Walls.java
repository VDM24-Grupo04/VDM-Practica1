package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Vector;

public class Walls extends GameObject {
    private float thickness, width, height;

    final Color color = new Color(60, 60, 60);
    Vector[] pos;

    public Walls(float thickness, float width, float height) {
        super();
        this.thickness = thickness;
        this.width = width;
        this.height = height;

        pos = new Vector[] {
                new Vector(thickness / 2.0f,height / 2.0f),
                new Vector(width / 2.0f, thickness * 2),
                new Vector(width - (thickness / 2.0f),height / 2.0f)
        };
    }

    @Override
    public void init() {

    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        graphics.setColor(color);
        graphics.fillRectangle(pos[0],thickness, height);
        graphics.fillRectangle(pos[1], width, thickness);
        graphics.fillRectangle(pos[2],thickness, height);
    }
}
