package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.gamelogic.gameobjects.CurrentBubble;
import com.grupo04.gamelogic.gameobjects.Spawner;
import com.grupo04.gamelogic.gameobjects.Walls;

public class GameScene extends Scene {
    // Nota: crear los objetos en la constructora
    public GameScene(Engine engine) {
        super(engine, 400, 600, new Color(255, 0, 255));
        
        float wallThickness = 20;
        float r = ((worldWidth - (wallThickness * 2)) / 10) / 2;
        addGameObject(new Walls(wallThickness, worldWidth, worldHeight));
        addGameObject(new CurrentBubble(worldWidth, worldHeight, r, wallThickness));
        addGameObject(new Spawner(worldWidth - wallThickness * 2, 5, r, wallThickness));
    }
}
