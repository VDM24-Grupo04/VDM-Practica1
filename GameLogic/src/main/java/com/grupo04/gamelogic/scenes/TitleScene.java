package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.gameobjects.Button;
import com.grupo04.gamelogic.gameobjects.Text;

public class TitleScene extends Scene {
    final String TEXT_FONT = "TheMeshroomRegular.ttf";
    final Color TEXT_COLOR = new Color(0, 0, 0);
    final float TEXT_SIZE = 55;
    final float[] TEXT_INDENTING = new float[]{-15f, 15f};
    final float TEXT_LINE_SPACING = -15f;

    final float BUTTON_WIDTH = 205f;
    final float BUTTON_HEIGHT = 55f;
    final float BUTTON_ARC = 25f;
    final Color BUTTON_BASE_COLOR = new Color(252, 228, 5);
    final Color BUTTON_OVER_COLOR = new Color(226, 205, 5);
    final String BUTTON_FONT = "kimberley.ttf";

    public TitleScene(Engine engine) {
        super(engine, 400, 600, new Color(255, 255, 255));

        Text title = new Text(new Vector(worldWidth / 2f, 2f * worldHeight / 7f), new String[]{"Puzzle", "Booble"},
                TEXT_FONT, TEXT_SIZE, false, false, TEXT_COLOR,
                TEXT_INDENTING, TEXT_LINE_SPACING);
        addGameObject(title);

        Button playButton = new Button(new Vector(worldWidth / 2f, 3f * worldHeight / 5f),
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC, BUTTON_BASE_COLOR, BUTTON_OVER_COLOR,
                "Play", BUTTON_FONT,
                () -> engine.changeScene(new GameScene(engine)));
        addGameObject(playButton);
    }
}
