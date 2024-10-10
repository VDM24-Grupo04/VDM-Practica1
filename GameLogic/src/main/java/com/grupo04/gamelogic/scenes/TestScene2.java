package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Scene;

public class TestScene2 extends Scene {
    public TestScene2(Engine engine) {
        super(engine, 400, 600, new Color(0, 0, 255));
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // graphics.setColor(new Color(0, 0, 255));
        // graphics.fillCircle(graphics.getWindowWidth() / 2, graphics.getWindowHeight() / 2, 50);
        this.engine.popScene();
    }
}
