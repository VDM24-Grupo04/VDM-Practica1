package com.grupo04.engine;

public abstract class Graphics {
    public abstract void render(Scene currentScene);

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public abstract void setColor(int red, int green, int blue, int alpha);

    public abstract void fillCircle(int x, int y, int radius);
    public abstract void fillRectangle(int x, int y, int w, int h);

    public boolean isWindowInitialized() {
        return this.getWindowWidth() != 0;
    }
}
