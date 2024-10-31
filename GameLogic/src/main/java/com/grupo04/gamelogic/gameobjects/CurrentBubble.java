package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Sound;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.BallColors;

import java.util.List;

public class CurrentBubble extends GameObject {
    final int lineLength = 100, lineThickness = 1;
    final Color lineColor = new Color(0, 0, 0, 255);

    final float spd = 300, minDirY = 0.01f;

    private boolean shot = false, dragging = false;
    int r, wallThickness, worldWidth;
    Vector initPos, pos, dir;
    int color;

    Grid grid;

    Sound throwSound = null;
    Sound bounceSound = null;

    public CurrentBubble(int w, int wallThickness, int headerOffset,  int r, int bubbleOffset, int rows) {
        super();
        dir = new Vector(0, 0);
        worldWidth = w;
        this.r = r;
        this.wallThickness = wallThickness;

        // La posicion inicial sera en el centro del mundo por debajo del limite vertical
        int initY = (int) ((this.r * 2 - bubbleOffset) * (rows + 2));
        initPos = new Vector(w / 2.0f, wallThickness + headerOffset + initY);

        reset();
    }

    @Override
    public void init() {
        grid = (Grid) scene.getHandler("grid");
        Engine engine = this.scene.getEngine();
        this.throwSound = engine.getAudio().newSound("ballThrow.wav");
        this.bounceSound = engine.getAudio().newSound("ballBounce.wav");
    }

    @Override
    public void dereference() {
        grid.dereference();
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // Se dibuja la bola
        if (color >= 0) {
            graphics.setColor(BallColors.getColor(color));
            graphics.fillCircle(pos, r);

            // Si se esta manteniendo pulsado, se dibuja la linea en direccion al lugar que se pulsa
            if (dragging) {
                graphics.setColor(lineColor);
                graphics.drawLine(pos, pos.plus(dir.getNormalized().times(lineLength)), lineThickness);
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Si se ha disparado, se normaliza la direccion y se mueve
        // la pelota en esa direccion a la velocidad establecida
        if (shot && color >= 0) {
            // Si choca con las paredes laterales, se coloca al limite y
            // se pone la dir horizontal hacia el sentido contrario
            // Pared derecha
            if (dir.x > 0 && (pos.x + r) >= worldWidth - wallThickness) {
                pos.x = worldWidth - wallThickness - r;
                dir.x *= -1;
                playBounceSound();
            }
            // Pared izquierda
            else if (dir.x < 0 && (pos.x - r) <= wallThickness) {
                pos.x = wallThickness + r;
                dir.x *= -1;
                playBounceSound();
            }

            // Aumenta la velocidad en vertical si es demasiado pequena
            // para que no se quede atascado rebotando de lado a lado
            if (dir.y < minDirY) {
                dir.y -= (float) deltaTime / 10.0f;
            }

            dir.normalize();
            pos = pos.plus(dir.times(spd * (float) deltaTime));

            // Comprobar colisiones. Si hay colision, se reinicia la bola
            if (grid.checkCollision(pos, dir, color)) {
                reset();
            }
        }
    }

    @Override
    public void handleInput(List<TouchEvent> touchEvents) {
        super.handleInput(touchEvents);

        // Si no se ha disparado, gestiona los eventos
        if (!shot && color >= 0) {
            for (TouchEvent event : touchEvents) {
                // Si no se esta manteniendo pulsado y se presiona, se empieza a mantener pulsado
                if (!dragging && event.getType() == TouchEvent.TouchEventType.PRESS) {
                    dragging = true;
                }

                // Si se esta manteniendo pulsado
                if (dragging) {
                    // Si se suelta, deja de mantener pulsado y si no se lanza la pelota hacia abajo, se dispara
                    if (event.getType() == TouchEvent.TouchEventType.RELEASE) {
                        dragging = false;
                        if (event.getPos().y < pos.y) {
                            playThrowSound();
                            shot = true;
                        }
                    }
                    // Si no, si se mantiene pulsado o se presiona, cambia la direccion
                    // de la bola al lugar en el que se produce la pulsacion
                    else if (event.getType() == TouchEvent.TouchEventType.DRAG || event.getType() == TouchEvent.TouchEventType.PRESS) {
                        dir = event.getPos().minus(pos);
                    }
                }
            }
        }
    }

    // Recoloca la bola en la posicion inicial, reinicia su direccion, y genera un nuevo color
    public void reset() {
        dir.x = 0;
        dir.y = 0;
        pos = initPos;
        color = BallColors.getRandomColor();
        dragging = false;
        shot = false;
    }

    private void playThrowSound() {
        if (this.throwSound != null) {
            this.throwSound.play();
        }
    }

    private void playBounceSound() {
        if (this.bounceSound != null) {
            this.bounceSound.play();
        }
    }
}
