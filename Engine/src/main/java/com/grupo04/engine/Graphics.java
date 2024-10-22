package com.grupo04.engine;

public abstract class Graphics {
    protected float worldWidth;
    protected float worldHeight;
    protected float scale = 1.0f;
    protected float offsetX = 0.0f;
    protected float offsetY = 0.0f;
    protected Color bgColor = new Color(0,0,0,255);

    protected abstract void prepareFrame();

    protected abstract boolean endFrame();

    public abstract void render(Scene currentScene);

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    protected abstract void calculateTransform();

    public abstract void scale(float scale);
    public float getScale() { return this.scale; }

    public abstract void translate(float offsetX, float offsetY);
    public float getOffsetX() { return this.offsetX; }
    public float getOffsetY() { return this.offsetY; }

    public float getWorldWidth() { return this.worldWidth; }
    public float getWorldHeight() { return this.worldHeight; }

    protected void setWorldSize(float worldWidth, float worldHeight) {
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

    public Vector worldToScreenPoint(Vector position) {
        float screenX = position.x * this.getWindowWidth() / this.worldWidth;
        float screenY = position.y * this.getWindowHeight() / this.worldHeight;
        System.out.println(this.getWindowWidth() + " " + this.worldWidth +
                " " + this.getWindowHeight() + " " + this.worldHeight);
        return new Vector(screenX, screenY);
    }
}