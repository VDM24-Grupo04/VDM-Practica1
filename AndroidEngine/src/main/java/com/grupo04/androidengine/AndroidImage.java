package com.grupo04.androidengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.grupo04.engine.Image;

import java.io.IOException;
import java.io.InputStream;

public class AndroidImage implements Image {
    Bitmap img;

    AndroidImage(String fileName, AssetManager assetManager) {
        try {
            InputStream is = assetManager.open(fileName);
            this.img = BitmapFactory.decodeStream(is);
        } catch (IOException ex) {
            System.err.println("Error en la fuente con nombre " + fileName + ": " + ex.getMessage());
        }
    }

    public Bitmap getImg() {
        return this.img;
    }

    @Override
    public int getWidth() {
        return this.img.getWidth();
    }

    @Override
    public int getHeight() {
        return this.img.getHeight();
    }
}
