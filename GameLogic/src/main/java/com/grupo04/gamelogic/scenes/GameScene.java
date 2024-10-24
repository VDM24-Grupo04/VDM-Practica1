package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.gamelogic.BallColors;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;
import com.grupo04.gamelogic.gameobjects.Grid;
import com.grupo04.gamelogic.gameobjects.Walls;

public class GameScene extends Scene {
    // Nota: crear los objetos en la constructora
    public GameScene(Engine engine) {
        super(engine, 400, 600, new Color(255, 255, 255));

        BallColors.reset();

        int wallThickness = 20;
        float r = (((float) worldWidth - (wallThickness * 2)) / 10) / 2;
        int limitY = (int) (worldHeight * 0.85f);
        int headerWidth = 50;

        Walls walls = new Walls(wallThickness, headerWidth, worldWidth, worldHeight);
        addGameObject(walls);
        Grid grid = new Grid(worldWidth, 5, (int) r, wallThickness, headerWidth, limitY);
        addGameObject(grid, "grid");
        CurrentBubble currentBubble = new CurrentBubble(worldWidth, (int) r, wallThickness, limitY);
        addGameObject(currentBubble, "currentBubble");
    }
}
