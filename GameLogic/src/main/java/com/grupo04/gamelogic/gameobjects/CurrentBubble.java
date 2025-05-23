package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.interfaces.IGraphics;
import com.grupo04.engine.utilities.Color;
import com.grupo04.gamelogic.GameObject;
import com.grupo04.engine.utilities.Vector;
import com.grupo04.engine.interfaces.IAudio;
import com.grupo04.engine.interfaces.ISound;
import com.grupo04.engine.interfaces.ITouchEvent;
import com.grupo04.gamelogic.BubbleColors;

import java.lang.ref.WeakReference;
import java.util.List;

public class CurrentBubble extends GameObject {
    private final int lineLength;
    private final int lineThickness;
    private final Color lineColor;

    private final float spd;
    private final float minDirY;

    private boolean shot;
    private boolean dragging;
    private final int worldWidth;
    private final int wallThickness;
    private final int headerOffset;
    private final int r;
    private final Vector initPos;
    private Vector pos;
    private Vector dir;
    private int color;

    private WeakReference<Grid> grid;
    private IAudio audio;
    private ISound throwSound;
    private ISound bounceSound;

    private BubbleColors bubbleColors;

    public CurrentBubble(int w, int wallThickness, int headerOffset, int r, int bubbleOffset, int rows, BubbleColors bubbleColors) {
        super();

        this.lineLength = 100;
        this.lineThickness = 1;
        this.lineColor = new Color(0, 0, 0, 255);

        this.spd = 300;
        this.minDirY = 0.01f;

        this.shot = false;
        this.dragging = false;

        this.worldWidth = w;
        this.wallThickness = wallThickness;
        this.headerOffset = headerOffset;
        this.r = r;
        // La posicion inicial sera en el centro del mundo por debajo del limite vertical
        int initY = (this.r * 2 - bubbleOffset) * (rows + 2);
        this.initPos = new Vector(w / 2.0f, wallThickness + headerOffset + initY);
        this.pos = this.initPos;
        this.dir = new Vector(0, 0);

        this.bubbleColors = bubbleColors;
        reset();
    }

    @Override
    public void init() {
        this.grid = new WeakReference<>((Grid) this.scene.getHandler("grid"));
        this.audio = this.scene.getEngine().getAudio();
        this.throwSound = this.audio.newSound("ballThrow.wav");
        this.bounceSound = this.audio.newSound("ballBounce.wav");
    }

    @Override
    public void render(IGraphics graphics) {
        super.render(graphics);

        // Se dibuja la bola
        if (this.color >= 0) {
            graphics.setColor(this.bubbleColors.getColor(this.color));
            graphics.fillCircle(this.pos, this.r);

            // Si se esta manteniendo pulsado, se dibuja la linea en direccion al lugar que se pulsa
            if (this.dragging) {
                graphics.setColor(this.lineColor);
                graphics.drawLine(this.pos, this.pos.plus(this.dir.getNormalized().times(this.lineLength)), this.lineThickness);
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Si se ha disparado, se normaliza la direccion y se mueve
        // la pelota en esa direccion a la velocidad establecida
        if (this.shot && this.color >= 0) {
            // Si choca con las paredes laterales, se coloca al limite y
            // se pone la dir horizontal hacia el sentido contrario
            // Pared derecha
            if (this.dir.x > 0 && (this.pos.x + this.r) >= this.worldWidth - this.wallThickness) {
                this.pos.x = this.worldWidth - this.wallThickness - this.r;
                this.dir.x *= -1;
                this.audio.playSound(this.bounceSound);
            }
            // Pared izquierda
            else if (this.dir.x < 0 && (this.pos.x - this.r) <= this.wallThickness) {
                this.pos.x = this.wallThickness + this.r;
                this.dir.x *= -1;
                this.audio.playSound(this.bounceSound);
            }

            // Aumenta la velocidad en vertical si es demasiado pequena
            // para que no se quede atascado rebotando de lado a lado
            if (this.dir.y < this.minDirY) {
                this.dir.y -= (float) deltaTime / 10.0f;
            }

            this.dir.normalize();
            this.pos = this.pos.plus(this.dir.times(this.spd * (float) deltaTime));

            // Comprobar colisiones. Si hay colision, se reinicia la bola
            Grid gridPointer = this.grid.get();
            if (gridPointer != null) {
                if (gridPointer.checkCollision(this.pos, this.color)) {
                    reset();
                }
            }
        }
    }

    @Override
    public void handleInput(List<ITouchEvent> touchEvents) {
        super.handleInput(touchEvents);

        // Si no se ha disparado, gestiona los eventos
        if (!this.shot && this.color >= 0) {
            for (ITouchEvent event : touchEvents) {
                boolean notOnHeader = event.getPos().y > this.headerOffset + this.wallThickness;
                // Si no se esta manteniendo pulsado y se presiona y la pulsacion no se
                // hace en la zona de la cabecera, se empieza a mantener pulsado
                if (!this.dragging && event.getType() == ITouchEvent.TouchEventType.PRESS
                        && notOnHeader) {
                    this.dragging = true;
                }

                // Si se esta manteniendo pulsado
                if (this.dragging) {
                    // Si se suelta, deja de mantener pulsado
                    if (event.getType() == ITouchEvent.TouchEventType.RELEASE) {
                        this.dragging = false;

                        // Si no se lanza la pelota hacia abajo ni se ha soltado en la zona de la cabecera, se dispara
                        if (event.getPos().y < this.pos.y && notOnHeader) {
                            this.audio.playSound(this.throwSound);
                            this.shot = true;
                        }
                    }
                    // Si no, si se mantiene pulsado o se presiona, cambia la direccion
                    // de la bola al lugar en el que se produce la pulsacion
                    else if (event.getType() == ITouchEvent.TouchEventType.DRAG || event.getType() == ITouchEvent.TouchEventType.PRESS) {
                        this.dir = event.getPos().minus(this.pos);
                    }
                }
            }

        }
    }

    // Recoloca la bola en la posicion inicial, reinicia su direccion, y genera un nuevo color
    public void reset() {
        this.dir.x = 0;
        this.dir.y = 0;
        this.pos = initPos;
        this.color = this.bubbleColors.getRandomColor();
        this.dragging = false;
        this.shot = false;
    }

    @Override
    public void dereference() {
        super.dereference();

        this.bubbleColors = null;
        this.audio = null;
        this.throwSound = null;
        this.bounceSound = null;
    }
}
