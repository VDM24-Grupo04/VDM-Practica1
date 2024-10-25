package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Callback;
import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Font;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Sound;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;

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

    private String onClickSoundPath;
    private Sound onClickSound;

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
        this.font = graphics.newFont(fontName, this.height / 1.7f, false, false);
        this.onClickSound = engine.getAudio().newSound(this.onClickSoundPath);
    }

    @Override
    public void handleInput(List<TouchEvent> touchEvents) {
        for (TouchEvent touchEvent : touchEvents) {
            if (touchEvent.getType() == TouchEvent.TouchEventType.PRESS) {
                if (withinArea(touchEvent.getPos())) {
                    if (this.onClickSound != null) {
                        this.onClickSound.play();
                    }
                    this.onClick.call();
                }
            } else if (touchEvent.getType() == TouchEvent.TouchEventType.MOTION) {
                if (withinArea(touchEvent.getPos())) {
                    this.bgCol = this.pointerOverCol;
                } else {
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
