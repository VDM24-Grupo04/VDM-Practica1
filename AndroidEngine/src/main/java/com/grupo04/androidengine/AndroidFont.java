package com.grupo04.androidengine;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.grupo04.engine.Font;

public class AndroidFont extends Font {
    private Typeface typeface;

    public AndroidFont(String name, float size, boolean bold, AssetManager assetManager) {
        super(size, bold);
        try {
            this.typeface = Typeface.createFromAsset(assetManager, name);
        } catch (RuntimeException ex) {
            System.err.println("Error en la fuente con nombre " + name + ": " + ex.getMessage());
        }
    }

    public Typeface getFont() {
        return this.typeface;
    }
}
