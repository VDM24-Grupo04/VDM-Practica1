package com.grupo04.engine;

public abstract class Graphics {
    private float worldWidth;
    private float worldHeight;

    protected Graphics(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    protected Vector worldToScreenPoint(Vector position) {
        float screenX = position.x * this.getWindowWidth() / this.worldWidth;
        float screenY = position.y * this.getWindowHeight() / this.worldHeight;

        return new Vector(screenX, screenY);
    }

    protected abstract void prepareFrame();

    protected abstract boolean endFrame();

    public abstract void render(Scene currentScene);

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public float getWorldWidth() {
        return this.worldWidth;
    }

    public float getWorldHeight() {
        return this.worldHeight;
    }

    public abstract void clear(Color color);

    public abstract void setColor(Color color);

    public abstract void drawCircle(Vector position, float radius, float strokeWidth);

    public abstract void fillCircle(Vector position, float radius);

    public abstract void drawRectangle(Vector position, float w, float h, float strokeWidth);

    public abstract void fillRectangle(Vector position, float w, float h);

    public abstract void drawRoundRectangle(Vector position, float w, float h, float arc, float strokeWidth);

    public abstract void fillRoundRectangle(Vector position, float w, float h, float arc);

    public abstract void drawLine(Vector initPos, Vector endPos, float strokeWidth);

    public abstract void drawHexagon(Vector position, Vector sideSize, float strokeWidth);

    public boolean isWindowInitialized() {
        return this.getWindowWidth() != 0;
    }

    public abstract Image newImage(String name);

    public abstract void drawImage(Image img, Vector position);

    public abstract void drawImage(Image img, Vector position, int w, int h);
}
