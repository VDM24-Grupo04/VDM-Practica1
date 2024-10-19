package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Font;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.scenes.TestScene2;

public class TestGameObject extends GameObject {
    private Engine engine;

    private Font font;
    private Font boldFont;

    private boolean attempt = false;

    TestGameObject() {
        this.engine = null;
        this.font = null;
        this.boldFont = null;
    }

    @Override
    public void init() {
        this.engine = scene.getEngine();
        this.font = engine.getGraphics().newFont("kimberley.ttf", 200, false, false);
        this.boldFont = engine.getGraphics().newFont("kimberley.ttf", 200, true, false);
    }

    @Override
    public void update(double deltaTime) {
        if (!attempt) {
            this.attempt = true;
            this.engine.pushScene(new TestScene2(this.engine));
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(this.boldFont);
        graphics.drawText("Hola", new Vector(200, 200));
        graphics.setFont(this.font);
        graphics.drawText("Hola", new Vector(200, 250));
        graphics.setColor(new Color(255, 0, 0));
        graphics.fillCircle(new Vector(100), 20);
    }
}
