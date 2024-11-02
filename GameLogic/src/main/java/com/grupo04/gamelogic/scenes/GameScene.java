package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Scene;
import com.grupo04.engine.interfaces.IEngine;
import com.grupo04.engine.utilities.Color;
import com.grupo04.engine.utilities.Vector;
import com.grupo04.gamelogic.BallColors;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;
import com.grupo04.gamelogic.gameobjects.Grid;
import com.grupo04.gamelogic.gameobjects.ImageButton;
import com.grupo04.gamelogic.gameobjects.ImageToggleButton;
import com.grupo04.gamelogic.gameobjects.Text;
import com.grupo04.gamelogic.gameobjects.TextButton;
import com.grupo04.gamelogic.gameobjects.Walls;

public class GameScene extends Scene {
    private final int N_COLS = 10;
    private final int INIT_ROWS = 5;
    private final int HEADER_WIDTH = 50;
    private final int WALL_THICKNESS = 20;

    private final int BUBBLES_TO_EXPLODE = 3;
    private final int GREAT_SCORE = 10;
    private final int SMALL_SCORE = 5;

    private final String BUTTON_SOUND = "button.wav";
    private final int SIDE_BUTTONS_PADDING = 10;
    private final int SIDE_BUTTONS_SIZE = 40;

    private final String MENU_BUTTON_IMG = "close.png";

    private final String SHOW_GRID_BUTTON_UNCHECKED_IMG = "hex_empty.png";
    private final String SHOW_GRID_BUTTON_CHECKED_IMG = "hex_full.png";

    final Color TEXT_COLOR = new Color(0, 0, 0);
    final String SCORE_TEXT_FONT = "kimberley.ttf";
    final float SCORE_TEXT_SIZE = 35;

    public GameScene(IEngine engine) {
        // En caso de dejar un color de fondo
        //super(engine, 400, 600, new Color(255, 255, 255));
        // En caso de dejar una imagen de fondo
        super(engine, 400, 600, "background.jpg");

        // Al iniciar la escena se hace un fade out
        setFade(FADE.OUT, 0.25);

        // Radio de las burbujas en el mapa
        float r = (((float) worldWidth - (WALL_THICKNESS * 2)) / N_COLS) / 2;
        int bubbleOffset = (int) (r * 0.3);
        int rows = ((int) ((worldHeight - HEADER_WIDTH - WALL_THICKNESS) / (r * 2)));

        // UI de la parte superior
        ImageButton menuButton = new ImageButton(new Vector(SIDE_BUTTONS_SIZE / 2f + SIDE_BUTTONS_PADDING, HEADER_WIDTH / 2f),
                SIDE_BUTTONS_SIZE, SIDE_BUTTONS_SIZE, MENU_BUTTON_IMG, BUTTON_SOUND,
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
                });
        addGameObject(menuButton);

        Text scoreText = new Text(new Vector(worldWidth / 2f, HEADER_WIDTH / 2f), "Score: 0",
                SCORE_TEXT_FONT, SCORE_TEXT_SIZE, false, false, TEXT_COLOR);
        addGameObject(scoreText, "scoreText");

        ImageToggleButton showGridButton = new ImageToggleButton(
                new Vector(worldWidth - SIDE_BUTTONS_SIZE / 2f - SIDE_BUTTONS_PADDING, HEADER_WIDTH / 2f),
                SIDE_BUTTONS_SIZE, SIDE_BUTTONS_SIZE,
                SHOW_GRID_BUTTON_UNCHECKED_IMG, SHOW_GRID_BUTTON_CHECKED_IMG, BUTTON_SOUND);
        addGameObject(showGridButton, "showGridButton");

        // Posibles colores de las bolas
        BallColors ballColors = new BallColors();

        // Elementos de juego
        Walls walls = new Walls(WALL_THICKNESS, HEADER_WIDTH, worldWidth, worldHeight);
        addGameObject(walls);

        Grid grid = new Grid(worldWidth, WALL_THICKNESS, HEADER_WIDTH, (int) r, bubbleOffset, rows, N_COLS,
                INIT_ROWS, BUBBLES_TO_EXPLODE, GREAT_SCORE, SMALL_SCORE, ballColors);
        addGameObject(grid, "grid");

        CurrentBubble currentBubble = new CurrentBubble(worldWidth, WALL_THICKNESS, HEADER_WIDTH,
                (int) r, bubbleOffset, rows, ballColors);
        addGameObject(currentBubble);
    }
}
