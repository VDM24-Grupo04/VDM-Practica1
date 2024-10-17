package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;

import java.util.List;

public class CurrentBubble extends GameObject {
    final float spd = 300;
    final float lineLength = 100;
    final Color lineColor = new Color(0, 0, 0, 255);
    private boolean shot = false;
    private boolean dragging = false;
    Vector pos, dir;
    int r;
    Color color;

    @Override
    public void init() {
    }

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

        graphics.setColor(lineColor);
        if (dragging) {
            graphics.drawLine(pos, pos.plus(dir.getNormalized().times(lineLength)), 1);
        }
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        if (this.shot) {
            this.dir.normalize();
            this.pos = this.pos.plus(this.dir.times(spd * (float) deltaTime));
        }

    }

    @Override
    public void handleInput(List<TouchEvent> touchEvents) {
        super.handleInput(touchEvents);

        if (!shot) {
            for (TouchEvent event : touchEvents) {
//            System.out.println(event.getPos().x + " " + event.getPos().y);

                if (!dragging && event.getType() == TouchEvent.TouchEventType.PRESS) {
                    dragging = true;
                }
                if (dragging) {
                    if (event.getType() == TouchEvent.TouchEventType.RELEASE) {
                        dragging = false;
                        if (event.getPos().y < pos.y) {
                            shot = true;
                        }
                    }
                    else if (event.getType() == TouchEvent.TouchEventType.DRAG || event.getType() == TouchEvent.TouchEventType.PRESS) {
                        dir = event.getPos().minus(pos);
                    }

                }
            }
        }
    }
}
