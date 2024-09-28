package com.grupo04.gamelogic;

import com.grupo04.engine.Engine;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Scene;

public class TestScene2 extends Scene {
    public TestScene2(Engine engine) {
        super(engine);
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
        graphics.setColor(0, 0, 255, 255);
        graphics.fillCircle(graphics.getWindowWidth() / 2, graphics.getWindowHeight() / 2, 50);
        this.engine.popScene();
    }
}
