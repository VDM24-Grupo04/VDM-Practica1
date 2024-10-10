package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Vector;

public class TestGameObject2 extends GameObject {
    private Image img;

    TestGameObject2() {
        this.img = null;
    }

    @Override
    public void init() {
        Engine engine = scene.getEngine();
        this.img = engine.getGraphics().newImage("regirock.jpg");
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(img, new Vector(graphics.getWorldWidth() / 2, graphics.getWorldHeight() / 2), 200, 200);
    }
}
