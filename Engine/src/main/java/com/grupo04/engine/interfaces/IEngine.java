package com.grupo04.engine.interfaces;

import com.grupo04.engine.Scene;

public interface IEngine {
    public void popScene();

    public void pushScene(Scene newScene);

    public void changeScene(Scene newScene);

    public void setWorldSize(int worldWidth, int worldHeight);

    public IGraphics getGraphics();

    public IAudio getAudio();
}
