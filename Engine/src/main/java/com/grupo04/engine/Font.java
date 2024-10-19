package com.grupo04.engine;

public abstract class Font {
    private float size;

    public Font(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }
}
