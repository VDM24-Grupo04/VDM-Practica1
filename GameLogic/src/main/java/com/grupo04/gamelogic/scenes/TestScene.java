package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Font;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Sound;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;

public class TestScene extends Scene {
    private boolean attempt;
    private Image img;
    private Font font;
    private Font boldFont;

    public TestScene(Engine engine) {
        super(engine, 400, 600, new Color(0, 0, 255));
        img = engine.getGraphics().newImage("regirock.jpg");
        font = engine.getGraphics().newFont("kimberley.ttf", 300, false);
        //boldFont = engine.getGraphics().newFont("qties.ttf", 100, true);
    }

    @Override
    public void init() {
        super.init();
        int r = 20;
        addGameObject(new CurrentBubble(new Vector((engine.getGraphics().getWindowWidth() - r)/ 2 ,
                engine.getGraphics().getWindowHeight() * 0.85f), r, new Color(255, 0, 0)));
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // float radius = 100;
        graphics.setColor(new Color(255, 0, 0));
        //graphics.drawHexagon(new Vector(graphics.getWorldWidth() / 2, 200), 100, 10);

        String text = "Xola";
        graphics.setFont(this.font);
        Vector pos = new Vector((float) graphics.getWindowWidth() / 2,
                (float) graphics.getWindowHeight() / 2);
        graphics.drawText(text, new Vector(pos));

        graphics.drawRectangle(pos, graphics.getTextWidth(text), graphics.getTextHeight(text), 10);

        /*
        Vector pos1 = new Vector((float) graphics.getWindowWidth() / 2,
                (float) graphics.getWindowHeight() / 2 - graphics.getTextHeight(text));
        graphics.drawText(text, new Vector(pos1));
        graphics.drawRectangle(pos1, graphics.getTextWidth(text), graphics.getTextHeight(text), 2);
         */
        //graphics.drawImage(img, pos, 100, 100);

        //graphics.setFont(this.font);
        //graphics.drawText("Hola", new Vector(0, 300));

        /*
        if (!attempt) {
            this.engine.pushScene(new TestScene2(this.engine));
            this.attempt = true;
        }
        */
    }
}
