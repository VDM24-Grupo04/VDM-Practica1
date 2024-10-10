package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Font;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;

public class TestScene extends Scene {
    private boolean attempt;
    private Image img;
    private Font font;
    private Font boldFont;

    public TestScene(Engine engine) {
        super(engine, 400, 600, new Color(0, 0, 255));
//        img = engine.getGraphics().newImage("regirock.jpg");
//        this.attempt = false;
//        font = engine.getGraphics().newFont("qties.ttf", 100, false);
//        boldFont = engine.getGraphics().newFont("qties.ttf", 100, true);
    }

    @Override
    public void init() {
        super.init();
        addGameObject(new CurrentBubble(new Vector(100, 100), 20, new Color(255, 0, 0)));
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        //
//        graphics.drawImage(img, new Vector(graphics.getWorldWidth() / 2 - 100, graphics.getWorldHeight() / 2 - 100), 200, 200);
//
//        graphics.setColor(new Color(255, 0, 0));
//        graphics.fillCircle(new Vector(graphics.getWorldWidth() / 2, graphics.getWorldHeight() / 2), 50);


        // float radius = 100;
        //graphics.setColor(new Color(0, 255, 0));
        //graphics.drawHexagon(new Vector(graphics.getWorldWidth() / 2, 200), 100, 10);

//        graphics.setFont(this.boldFont);
//        graphics.drawText("Hola", new Vector(0, 200));
//        graphics.setFont(this.font);
//        graphics.drawText("Hola", new Vector(0, 300));

        /*
        if (!attempt) {
            this.engine.pushScene(new TestScene2(this.engine));
            this.attempt = true;
        }
        */
    }
}
