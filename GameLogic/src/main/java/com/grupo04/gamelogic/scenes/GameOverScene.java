package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.gameobjects.Button;
import com.grupo04.gamelogic.gameobjects.Text;

public class GameOverScene extends Scene {
    final String TEXT_FONT = "TheMeshroomRegular.ttf";
    final Color TEXT_COLOR = new Color(0, 0, 0);
    final float TEXT_SIZE = 70;
    final float[] TEXT_INDENTING = new float[]{3f, -10f};
    final float TEXT_LINE_SPACING = -30f;

    final float BUTTON_WIDTH = 205f;
    final float BUTTON_HEIGHT = 55f;
    final float BUTTON_ARC = 25f;
    final Color BUTTON_BASE_COLOR = new Color(237, 12, 46);
    final Color BUTTON_OVER_COLOR = new Color(203, 10, 38);
    final String BUTTON_FONT = "kimberley.ttf";
    final float BUTTON_OFFSET_Y = 25f;

    public GameOverScene(Engine engine) {
        super(engine, 400, 600, new Color(255, 255, 255));

        Text title = new Text(new Vector(worldWidth / 2f, worldHeight / 4f), new String[]{"Game", "Over!"},
                TEXT_FONT, TEXT_SIZE, false, false, TEXT_COLOR,
                TEXT_INDENTING, TEXT_LINE_SPACING);
        addGameObject(title);

        Vector tryAgainButtonPos = new Vector(worldWidth / 2f, 4f * worldHeight / 6f);
        Button tryAgainButton = new Button(tryAgainButtonPos,
                BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_ARC, BUTTON_BASE_COLOR, BUTTON_OVER_COLOR,
                "Try again", BUTTON_FONT,
                () -> {
                    // Al pulsar el boton se hace un fade in y cuando
                    // acaba la animacion se cambia a la escena de juego
                    this.setFade(FADE.IN, 0.25);
                    this.setFadeCallback(() -> {
                        engine.changeScene(new GameScene(engine));
                    });
                }
        );
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
        );
        addGameObject(menuButton);

        // Al iniciar la escena se hace un fade out y al terminar la animacion se reproduce el sonido de derrota
        super.setFade(FADE.OUT, 0.25);
        /*
        super.setFadeCallback(() -> {
            if (winSound != null) {
                winSound.play();
            }
        });
        */
    }
}
