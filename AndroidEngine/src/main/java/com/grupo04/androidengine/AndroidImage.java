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
    AndroidImage(String fileName, Context context) {
        super(0, 0);
        try {
            InputStream is = context.getAssets().open(fileName);
            img = BitmapFactory.decodeStream(is);
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    public void render(Graphics graphics) {
        Canvas canvas = ((AndroidGraphics)graphics).getCanvas();
        Paint paint = ((AndroidGraphics)graphics).getPaint();
        canvas.drawBitmap(img, x, y, paint);
    }
    @Override
    public int getWidth() {
        return img.getWidth();
    }
    @Override
    public int getHeight() { return img.getHeight(); }
}
