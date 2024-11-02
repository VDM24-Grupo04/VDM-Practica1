package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Scene;
import com.grupo04.engine.interfaces.IEngine;
import com.grupo04.gamelogic.BallColors;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;
import com.grupo04.gamelogic.gameobjects.Grid;
import com.grupo04.gamelogic.gameobjects.Walls;

public class GameScene extends Scene {
    private final int N_COLS = 10;
    private final int INIT_ROWS = 5;
    private final int BUBBLES_TO_EXPLODE = 3;
    private final int GREAT_SCORE = 10;
    private final int SMALL_SCORE = 5;
    private final int HEADER_WIDTH = 50;
    private final int WALL_THICKNESS = 20;

    public GameScene(IEngine engine) {
        // En caso de dejar un color de fondo
        //super(engine, 400, 600, new Color(255, 255, 255));
        // En caso de dejar una imagen de fondo
        super(engine, 400, 600, "background.jpg");

        // Al iniciar la escena se hace un fade out
        super.setFade(FADE.OUT, 0.25);

        // Radio de las burbujas en el mapa
        float r = (((float) worldWidth - (WALL_THICKNESS * 2)) / N_COLS) / 2;
        int bubbleOffset = (int)(r * 0.3);
        int rows = ((int) ((worldHeight - HEADER_WIDTH - WALL_THICKNESS) / (r * 2)));

        Walls walls = new Walls(WALL_THICKNESS, HEADER_WIDTH, worldWidth, worldHeight);
        addGameObject(walls);

        BallColors ballColors = new BallColors();

        Grid grid = new Grid(worldWidth, WALL_THICKNESS, HEADER_WIDTH, (int) r, bubbleOffset, rows, N_COLS,
                INIT_ROWS, BUBBLES_TO_EXPLODE, GREAT_SCORE, SMALL_SCORE, ballColors);
        addGameObject(grid, "grid");

        CurrentBubble currentBubble = new CurrentBubble(worldWidth, WALL_THICKNESS, HEADER_WIDTH,
                (int) r, bubbleOffset, rows, ballColors);
        addGameObject(currentBubble, "currentBubble");
    }
}
