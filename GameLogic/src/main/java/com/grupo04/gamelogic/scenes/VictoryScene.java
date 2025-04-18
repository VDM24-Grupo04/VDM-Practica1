package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.interfaces.IEngine;
import com.grupo04.engine.utilities.Color;
import com.grupo04.gamelogic.Scene;
import com.grupo04.engine.utilities.Vector;
import com.grupo04.engine.interfaces.ISound;
import com.grupo04.gamelogic.gameobjects.TextButton;
import com.grupo04.gamelogic.gameobjects.Text;

public class VictoryScene extends Scene {
    final private ISound winSound;

    public VictoryScene(IEngine engine, int score) {
        super(engine, 400, 600, new Color(255, 255, 255));

        Color TEXT_COLOR = new Color(0, 0, 0);

        String TITLE_FONT = "TheMeshroomRegular.ttf";
        float TITLE_SIZE = 62;

        String SCORE_TEXT_FONT = "kimberley.ttf";
        float SCORE_TEXT_SIZE = 40;

        String BUTTON_SOUND = "button.wav";
        float BUTTON_WIDTH = 205f;
        float BUTTON_HEIGHT = 55f;
        float BUTTON_ARC = 25f;
        Color BUTTON_BASE_COLOR = new Color(44, 166, 28);
        Color BUTTON_OVER_COLOR = new Color(34, 138, 24);
        String BUTTON_FONT = "kimberley.ttf";
        float BUTTON_OFFSET_Y = 25f;

        Text title = new Text(new Vector(this.worldWidth / 2f, this.worldHeight / 6f), "Victory!",
                TITLE_FONT, TITLE_SIZE, false, false, TEXT_COLOR);
        addGameObject(title);

        Text scoreText = new Text(new Vector(this.worldWidth / 2f, 3f * this.worldHeight / 7f), Integer.toString(score),
                SCORE_TEXT_FONT, SCORE_TEXT_SIZE, false, false, TEXT_COLOR);
        addGameObject(scoreText);

        // Se reproduce una vez cargado el sonido
        this.winSound = engine.getAudio().newSound("win.wav", true);

        Vector tryAgainButtonPos = new Vector(this.worldWidth / 2f, 4f * this.worldHeight / 6f);
        TextButton tryAgainButton = new TextButton(tryAgainButtonPos,
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC, BUTTON_BASE_COLOR, BUTTON_OVER_COLOR,
                "Play again", BUTTON_FONT, BUTTON_SOUND,
                () -> {
                    // Al pulsar el boton se hace un fade in y cuando
                    // acaba la animacion se cambia a la escena de juego
                    this.setFade(Fade.IN, 0.25);
                    this.setFadeCallback(() -> {
                        this.engine.getAudio().stopSound(this.winSound);
                        if (this.sceneManager != null) {
                            this.sceneManager.changeScene(new GameScene(this.engine));
                        }
                    });
                });
        addGameObject(tryAgainButton);

        Vector menuButtonPos = new Vector(tryAgainButtonPos);
        menuButtonPos.y += BUTTON_HEIGHT + BUTTON_OFFSET_Y;
        TextButton menuButton = new TextButton(menuButtonPos,
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC, BUTTON_BASE_COLOR, BUTTON_OVER_COLOR,
                "Menu", BUTTON_FONT, BUTTON_SOUND,
                () -> {
                    // Al pulsar el boton se hace un fade in y cuando
                    // acaba la animacion se cambia al menu principal
                    // con animacion de fade out
                    this.setFade(Fade.IN, 0.25);
                    this.setFadeCallback(() -> {
                        TitleScene scene = new TitleScene(this.engine);
                        scene.setFade(Fade.OUT, 0.25);
                        this.engine.getAudio().stopSound(this.winSound);
                        if (this.sceneManager != null) {
                            this.sceneManager.changeScene(scene);
                        }
                    });
                });
        addGameObject(menuButton);

        setFade(Fade.OUT, 0.25);
    }
}
