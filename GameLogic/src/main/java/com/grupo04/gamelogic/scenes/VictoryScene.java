package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Sound;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.gameobjects.Button;
import com.grupo04.gamelogic.gameobjects.Text;

public class VictoryScene extends Scene {
    final Color TEXT_COLOR = new Color(0, 0, 0);

    final String TITLE_FONT = "TheMeshroomRegular.ttf";
    final float TITLE_SIZE = 62;

    final String SCORE_TEXT_FONT = "kimberley.ttf";
    final float SCORE_TEXT_SIZE = 40;

    final float BUTTON_WIDTH = 205f;
    final float BUTTON_HEIGHT = 55f;
    final float BUTTON_ARC = 25f;
    final Color BUTTON_BASE_COLOR = new Color(44, 166, 28);
    final Color BUTTON_OVER_COLOR = new Color(34, 138, 24);
    final String BUTTON_FONT = "kimberley.ttf";
    final float BUTTON_OFFSET_Y = 25f;

    Sound winSound;

    public VictoryScene(Engine engine, int score) {
        super(engine, 400, 600, new Color(255, 255, 255));

        Text title = new Text(new Vector(worldWidth / 2f, worldHeight / 6f), "Victory!",
                TITLE_FONT, TITLE_SIZE, false, false, TEXT_COLOR);
        addGameObject(title);

        Text scoreText = new Text(new Vector(worldWidth / 2f, 3f * worldHeight / 7f), Integer.toString(score),
                SCORE_TEXT_FONT, SCORE_TEXT_SIZE, false, false, TEXT_COLOR);
        addGameObject(scoreText);

        Vector tryAgainButtonPos = new Vector(worldWidth / 2f, 4f * worldHeight / 6f);
        Button tryAgainButton = new Button(tryAgainButtonPos,
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC, BUTTON_BASE_COLOR, BUTTON_OVER_COLOR,
                "Play again", BUTTON_FONT,
                () -> {
                    // Al pulsar el boton se hace un fade in y cuando
                    // acaba la animacion se cambia a la escena de juego
                    this.setFade(FADE.IN, 0.25);
                    this.setFadeCallback(() -> {
                        engine.changeScene(new GameScene(engine));
                    });
                }
        , "button.wav");
        addGameObject(tryAgainButton);

        Vector menuButtonPos = new Vector(tryAgainButtonPos);
        menuButtonPos.y += BUTTON_HEIGHT + BUTTON_OFFSET_Y;
        Button menuButton = new Button(menuButtonPos,
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC, BUTTON_BASE_COLOR, BUTTON_OVER_COLOR,
                "Menu", BUTTON_FONT,
                () -> {
                    // Al pulsar el boton se hace un fade in y cuando
                    // acaba la animacion se cambia al menu principal
                    // con animacion de fade out
                    this.setFade(FADE.IN, 0.25);
                    this.setFadeCallback(() -> {
                        TitleScene scene = new TitleScene(engine);
                        scene.setFade(FADE.OUT, 0.25);
                        engine.changeScene(scene);
                    });
                }
        , "button.wav");
        addGameObject(menuButton);

        super.setFade(FADE.OUT, 0.25);

        // Se reproduce una vez cargado el sonido
        winSound = engine.getAudio().newSound("win.wav", true);
    }
}
