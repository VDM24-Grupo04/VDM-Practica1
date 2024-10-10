package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;

import java.util.List;

public class CurrentBubble extends GameObject {
    private boolean shot = false, dragging = false;
    Vector pos, dir;
    int r;
    Color color;

    @Override
    public void init() { }

    public CurrentBubble(Vector pos, int r, Color color) {
        super();
        this.dir = new Vector(0, 0);
        this.pos = pos;
        this.color = color;
        this.r = r;
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        graphics.setColor(color);
        graphics.fillCircle(pos, r);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        if (this.shot) {
            this.pos = this.pos.plus(this.dir);
        }

        System.out.println(dragging);
    }

    @Override
    public void handleInput(List<TouchEvent> touchEvent) {
        super.handleInput(touchEvent);
        for (TouchEvent event : touchEvent) {
            if (!dragging) {
                float dist = event.getPos().distance(this.pos);
                if (event.getType() == TouchEvent.TouchEventType.PRESS && dist < r) {
                    dragging = true;
                }
            }
        }
    }
}
