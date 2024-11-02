package com.grupo04.engine.interfaces;

import com.grupo04.engine.Scene;

public interface IEngine {
    void popScene();
    void pushScene(Scene newScene);
    void changeScene(Scene newScene);
    void setWorldSize(int worldWidth, int worldHeight);
    IGraphics getGraphics();
    IAudio getAudio();
}
