package com.grupo04.androidengine;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;

import java.io.IOException;
import java.io.InputStream;

public class AndroidImage extends Image {
    Bitmap img;
    AndroidGraphics graphics;

    AndroidImage(String fileName, AssetManager assetManager, AndroidGraphics graphics) {
        super();
        this.graphics = graphics;

        try {
            InputStream is = assetManager.open(fileName);
            img = BitmapFactory.decodeStream(is);
        } catch (IOException ex) { }
    }

    @Override
    public void render(int x, int y) {
        graphics.renderImage(this, x, y);
    }
    @Override
    public void render(int x, int y, int w, int h) {
        graphics.renderImage(this, x, y, w, h);
    }

    public Bitmap getImg() { return img; }
    @Override
    public int getWidth() {
        return img.getWidth();
    }
    @Override
    public int getHeight() { return img.getHeight(); }
}
