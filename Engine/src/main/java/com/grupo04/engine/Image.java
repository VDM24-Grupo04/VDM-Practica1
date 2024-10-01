package com.grupo04.engine;

public abstract class Image {
    protected int x, y;

    public Image(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getWidth() { return 0; }
    public int getHeight() { return 0; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public void render(Graphics graphics) { }
}
