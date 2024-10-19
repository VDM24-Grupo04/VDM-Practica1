package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.BallColors;

import java.util.List;

public class CurrentBubble extends GameObject {
    final float lineLength = 100, lineThickness = 3;
    final Color lineColor = new Color(0, 0, 0, 255);

    final float spd = 300;
    final float minDirY = 0.01f;

    private boolean shot = false, dragging = false;
    int r, wallThickness, worldWidth, worldHeight;
    Vector initPos, pos, dir;
    Color color;

    @Override
    public void init() {
    }

    public CurrentBubble(int w, int h, int r, int wallThickness) {
        super();
        dir = new Vector(0, 0);
        worldWidth = w;
        worldHeight = h;
        initPos = new Vector((w - r) / 2.0f, h * 0.85f);
        this.r = r;
        this.wallThickness = wallThickness;
        reset();
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

        // Si se ha disparado, se normaliza la direccion y se mueve
        // la pelota en esa direccion a la velocidad establecida
        if (shot) {
            dir.normalize();
            pos = pos.plus(dir.times(spd * (float) deltaTime));
        }

        // Si choca con las paredes laterales, se coloca al limite y
        // se pone la dir horizontal hacia el sentido contrario

        // Pared derecha
        if (dir.x > 0 && (pos.x + r) >= worldWidth - wallThickness) {
            pos.x = worldWidth - wallThickness - r;
            dir.x *= -1;
        }
        // Pared izquierda
        else if (dir.x < 0 && (pos.x - r) <= wallThickness) {
            pos.x = wallThickness + r;
            dir.x *= -1;
        }

        // Aumenta la velocidad en vertical si es demasiado pequena
        // para que no se quede atascado rebotando de lado a lado
        if (shot && dir.y < minDirY) {
            dir.y -= (float) deltaTime / 10.0f;
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
                    } else if (event.getType() == TouchEvent.TouchEventType.DRAG || event.getType() == TouchEvent.TouchEventType.PRESS) {
                        dir = event.getPos().minus(pos);
                    }

                }
            }
        }
    }

    public void reset() {
        dir.x = 0;
        dir.y = 0;
        pos = initPos;
        color = BallColors.getRandomColor();
        dragging = false;
        shot = false;
    }
}
