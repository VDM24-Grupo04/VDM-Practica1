package com.grupo04.androidengine;

import android.content.Context;
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
    Canvas canvas;
    Paint paint;

    AndroidImage(String fileName, Context context, Canvas canvas, Paint paint) {
        super();
        this.canvas = canvas;
        this.paint = paint;

        try {
            InputStream is = context.getAssets().open(fileName);
            img = BitmapFactory.decodeStream(is);
        } catch (IOException ex) { }
    }

    @Override
    public void render(int x, int y) {
        canvas.drawBitmap(img, x, y, paint);
    }
    @Override
    public void render(int x, int y, int w, int h) {
//        canvas.drawBitmap(img, x, y, paint);
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }
    @Override
    public int getHeight() { return img.getHeight(); }
}
