package com.grupo04.engine;

public abstract class Image {
    public Image() {}
    public int getWidth() { return 0; }
    public int getHeight() { return 0; }

    public void render(int x, int y) { }
    public void render(int x, int y, int w, int h) { }
}

