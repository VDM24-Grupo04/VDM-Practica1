package com.grupo04.engine;

public abstract class Graphics {
    public abstract void render(Scene currentScene);

    public abstract int getWindowWidth();

    public boolean isWindowInitialized() {
        return this.getWindowWidth() != 0;
    }
}
