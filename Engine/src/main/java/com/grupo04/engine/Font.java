package com.grupo04.engine;

import com.grupo04.engine.interfaces.IFont;

public abstract class Font implements IFont {
    private float size;

    public Font(float size) {
        this.size = size;
    }

    @Override
    public float getSize() {
        return size;
    }
}
