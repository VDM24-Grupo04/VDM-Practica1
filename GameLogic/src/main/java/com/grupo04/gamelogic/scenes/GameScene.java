package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;
import com.grupo04.gamelogic.gameobjects.Walls;

public class GameScene extends Scene {
    private final static int worldWidth = 400, worldHeight = 600;

    public GameScene(Engine engine) {
        super(engine, worldWidth, worldHeight, new Color(255, 255, 255));
    }

    @Override
    public void init() {
        super.init();

        int wallThickness = 20;
        int r = ((worldWidth - (wallThickness * 2)) / 10) / 2 ;
        addGameObject(new Walls(wallThickness, worldWidth, worldHeight));
        addGameObject(new CurrentBubble(worldWidth, worldHeight, r, wallThickness));
    }
}
