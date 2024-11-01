package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.interfaces.Callback;
import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Font;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Vector;
import com.grupo04.engine.interfaces.IAudio;
import com.grupo04.engine.interfaces.ISound;
import com.grupo04.engine.interfaces.ITouchEvent;

import java.util.List;

public class Button extends GameObject {
    private Vector pos;

    private float width, height;
    private float arc;
    private Color baseCol;
    private Color pointerOverCol;
    private Color bgCol;

    private Font font;
    private String text;
    private String fontName;
    private Color fontColor;
    private boolean bold;

    private Callback onClick;

    private Vector topLeft;

    private IAudio audio;
    private String onClickSoundPath;
    private ISound onClickSound;

    private boolean withinArea(Vector pos) {
        return pos.x > this.topLeft.x && pos.x < this.topLeft.x + width &&
                pos.y > this.topLeft.y && pos.y < this.topLeft.y + height;
    }

    public Button(Vector pos,
                  float width, float height, float arc, Color baseCol, Color pointerOverCol,
                  String text, String fontName, Color fontColor, boolean bold, Callback onClick, String onClickSoundPath) {
        this.pos = pos;

        this.width = width;
        this.height = height;
        this.arc = arc;
        this.baseCol = baseCol;
        this.pointerOverCol = pointerOverCol;
        this.bgCol = this.baseCol;

        this.font = null;
        this.text = text;
        this.fontName = fontName;
        this.fontColor = fontColor;
        this.bold = bold;

        this.onClick = onClick;

        this.topLeft = new Vector(pos.x - (float) width / 2, pos.y - (float) height / 2);

        this.onClickSoundPath = onClickSoundPath;
    }

    public Button(Vector pos,
                  float width, float height, float arc, Color baseCol, Color pointerOverCol,
                  String text, String fontName, Callback onClick, String onClickSoundPathName) {
        this(pos, width, height, arc, baseCol, pointerOverCol, text, fontName,
                new Color(0,0,0), false, onClick, onClickSoundPathName);
    }

    public Button(Vector pos,
                  float width, float height, float arc, Color baseCol, Color pointerOverCol,
                  String text, String fontName, Color fontColor, boolean bold, Callback onClick) {
        this(pos, width, height, arc, baseCol, pointerOverCol, text, fontName, fontColor, bold, onClick, "");
    }

    public Button(Vector pos,
                  float width, float height, float arc, Color baseCol, Color pointerOverCol,
                  String text, String fontName, Callback onClick) {
        this(pos, width, height, arc, baseCol, pointerOverCol, text, fontName, onClick, "");
    }

    @Override
    public void init() {
        Engine engine = this.scene.getEngine();
        Graphics graphics = engine.getGraphics();
        this.audio = engine.getAudio();
        this.font = graphics.newFont(fontName, this.height / 1.7f, false, false);
        this.onClickSound = this.audio.newSound(this.onClickSoundPath);
    }

    @Override
    public void handleInput(List<ITouchEvent> touchEvents) {
        for (ITouchEvent touchEvent : touchEvents) {
            if (touchEvent.getType() == ITouchEvent.TouchEventType.PRESS) {
                if (withinArea(touchEvent.getPos())) {
                    this.audio.playSound(this.onClickSound);
                    this.onClick.call();
                }
            } else if (touchEvent.getType() == ITouchEvent.TouchEventType.MOTION) {
                if (withinArea(touchEvent.getPos())) {
                    // Se podria añadir un sonido cuando este encima
                    this.bgCol = this.pointerOverCol;
                } else {
                    // Se podria añadir un sonido cuando no este encima
                    this.bgCol = this.baseCol;
                }
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(this.bgCol);
        graphics.fillRoundRectangle(this.pos, this.width, this.height, this.arc);

        graphics.setColor(this.fontColor);
        graphics.setFont(this.font);
        graphics.drawText(this.text, this.pos);
    }
}
