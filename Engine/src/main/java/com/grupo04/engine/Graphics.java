package com.grupo04.engine;

public abstract class Graphics {
    private int worldWidth;
    private int worldHeight;

    protected Graphics(int worldWidth, int worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    protected int[] convertToScreenPosition(int x, int y) {
        // Se saca la proporcion
        float scaleX = (float) x / this.worldWidth;
        // Se hace el clamp de la proporcion para que siempre este entre 0 y 1
        scaleX = Math.max(0, Math.min(1, scaleX));
        // Se convierte a coordenadas de panatalla
        // De este modo, la resolucion del dispositivo no afecta a la posicion de los objetos
        int screenX = (int) (scaleX * this.getWindowWidth());

        float scaleY = (float) y / this.worldHeight;
        scaleY = Math.max(0, Math.min(1, scaleY));
        int screenY = (int) (scaleY * this.getWindowHeight());
        return new int[]{screenX, screenY};
    }

    protected abstract void prepareFrame();

    protected abstract boolean endFrame();

    public abstract void render(Scene currentScene);

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public int getWorldWidth() {
        return this.worldWidth;
    }

    public int getWorldHeight() {
        return this.worldHeight;
    }

    public abstract void clear(Color color);

    public abstract void setColor(Color color);

    public abstract void fillCircle(int x, int y, int radius);

    public abstract void fillRectangle(int x, int y, int w, int h);

    public boolean isWindowInitialized() {
        return this.getWindowWidth() != 0;
    }

    public abstract Image newImage(String name);

    public abstract void renderImage(Image img, int x, int y);

    public abstract void renderImage(Image img, int x, int y, int w, int h);
}
