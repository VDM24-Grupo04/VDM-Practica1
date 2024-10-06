package com.grupo04.engine;

public abstract class Font {
    private float size;
    private boolean bold;

    public Font(float size, boolean bold) {
        this.size = size;
        this.bold = bold;
    }

    public float getSize() {
        return size;
    }

    public boolean isBold() {
        return bold;
    }
}
