package com.grupo04.engine;

import com.grupo04.engine.interfaces.Image;

public abstract class Graphics {
    protected int worldWidth;
    protected int worldHeight;
    protected float scale;
    protected float offsetX;
    protected float offsetY;
    protected Color bgColor;

    protected Graphics() {
        this.worldWidth = 0;
        this.worldHeight = 0;
        this.scale = 1.0f;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.bgColor = new Color(0, 0, 0, 255);
    }

    protected abstract void prepareFrame();

    protected abstract boolean endFrame();

    public abstract void render(Scene currentScene);

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    protected abstract void calculateTransform();

    protected abstract void scale(float scale);

    protected abstract void translate(float offsetX, float offsetY);

    protected void setWorldSize(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void setBgColor(Color bgColor) { this.bgColor = bgColor; }

    public abstract void clear(com.grupo04.engine.Color color);

    public abstract void setColor(Color color);

    public abstract void drawCircle(Vector position, float radius, float strokeWidth);

    public abstract void fillCircle(Vector position, float radius);

    public abstract void drawRectangle(Vector position, float w, float h, float strokeWidth);

    public abstract void fillRectangle(Vector position, float w, float h);

    public abstract void drawRoundRectangle(Vector position, float w, float h, float arc, float strokeWidth);

    public abstract void fillRoundRectangle(Vector position, float w, float h, float arc);

    public abstract void drawLine(Vector initPos, Vector endPos, float strokeWidth);

    public abstract void drawHexagon(Vector center, float radius, float rotInDegrees, float strokeWidth);

    public void drawHexagon(Vector center, float radius, float strokeWidth) {
        drawHexagon(center, radius, 0, strokeWidth);
    }

    public boolean isWindowInitialized() {
        return this.getWindowWidth() != 0;
    }

    public abstract Image newImage(String name);

    public abstract void drawImage(Image img, Vector position);

    public abstract void drawImage(Image img, Vector position, int w, int h);

    public abstract Font newFont(String name, float size, boolean bold, boolean italic);

    public abstract void setFont(Font font);

    public abstract void drawText(String text, Vector position);

    public abstract int getTextWidth(String text);

    public abstract int getTextHeight(String text);

    public abstract Vector screenToWorldPoint(Vector point);
}
