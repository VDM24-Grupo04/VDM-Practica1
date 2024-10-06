package com.grupo04.gamelogic;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;

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

        //graphics.drawImage(img, new Vector());

        graphics.setColor(new Color(255, 0, 0));
        // graphics.fillCircle(new Vector(graphics.getWorldWidth() / 2, graphics.getWorldHeight() / 2), 50);

        float radius = 100;
        graphics.setColor(new Color(0, 255, 0));
        graphics.drawHexagon(new Vector(200, 200), 200, 10);

        /*
        if (!attempt) {
            this.engine.pushScene(new TestScene2(this.engine));
            this.attempt = true;
        }
        */
    }
}
