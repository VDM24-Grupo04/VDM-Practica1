package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.gamelogic.BallColors;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;
import com.grupo04.gamelogic.gameobjects.Grid;
import com.grupo04.gamelogic.gameobjects.Walls;

public class GameScene extends Scene {
    private final int N_COLS = 10;
    private final int N_ROWS = 5;
    private final int BUBBLES_TO_EXPLODE = 3;
    private final int GREAT_SCORE = 10;
    private final int SMALL_SCORE = 5;

    public GameScene(Engine engine) {
        super(engine, 400, 600, new Color(255, 255, 255));

        BallColors.reset();

        int headerWidth = 50;
        int wallThickness = 20;
        // Radio de las burbujas en el mapa
        float r = (((float) worldWidth - (wallThickness * 2)) / N_COLS) / 2;
        int rows = ((int) ((worldHeight - headerWidth - wallThickness) / (r * 2)));

        Walls walls = new Walls(wallThickness, headerWidth, worldWidth, worldHeight);
        addGameObject(walls);

        Grid grid = new Grid(worldWidth, wallThickness, headerWidth, (int) r, rows, N_COLS,
                N_ROWS, BUBBLES_TO_EXPLODE, GREAT_SCORE, SMALL_SCORE);
        addGameObject(grid, "grid");

        CurrentBubble currentBubble = new CurrentBubble(worldWidth, (int) r, wallThickness, rows);
        addGameObject(currentBubble, "currentBubble");

    }
}
