package com.grupo04.gamelogic;

import com.grupo04.engine.Engine;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;

public class TestScene extends Scene {
    private boolean attempt;

    Image img;
    public TestScene(Engine engine) {
        super(engine);
        img = engine.getGraphics().newImage("regirock.jpg");
        this.attempt = false;
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void fixedUpdate(double fixedDeltaTime) {

    }

    @Override
    public void render() {
        Graphics graphics = engine.getGraphics();

        graphics.setColor(0,0,255,255);
        graphics.fillRectangle(0,0, graphics.getWindowWidth(), graphics.getWindowHeight());

        img.render(0, 0 , graphics.getWindowWidth(), graphics.getWindowHeight());

        graphics.setColor(255, 0, 0, 255);
        graphics.fillCircle(graphics.getWindowWidth() / 2 - 25, graphics.getWindowHeight() / 2 - 25, 50);
        if (!attempt) {
            this.engine.pushScene(new TestScene2(this.engine));
            this.attempt = true;
        }
    }
}
