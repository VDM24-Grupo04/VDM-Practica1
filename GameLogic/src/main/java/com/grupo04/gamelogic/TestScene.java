package com.grupo04.gamelogic;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;

public class TestScene extends Scene {
    private boolean attempt;

    Image img;

    public TestScene(Engine engine) {
        super(engine, new Color(0, 0, 255));
        img = engine.getGraphics().newImage("regirock.jpg");
        this.attempt = false;
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        graphics.renderImage(img, 0, 0);

        graphics.setColor(new Color(255, 0, 0));
        graphics.fillCircle(graphics.getWorldWidth() / 2, graphics.getWorldHeight() / 2, 50);
        /*
        if (!attempt) {
            this.engine.pushScene(new TestScene2(this.engine));
            this.attempt = true;
        }
        */
    }
}
