package com.grupo04.androidengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;

public class AndroidGraphics extends Graphics {
    private SurfaceView window;
    private SurfaceHolder holder;
    private Paint paint;
    private Canvas canvas;

    public AndroidGraphics(SurfaceView window, SurfaceHolder holder) {
        super();
        this.window = window;
        this.holder = holder;
        this.paint = new Paint();
        // Se establece un color inicial por si acaso
        // this.paint.setColor(0xFFFFFFFF);
        this.canvas = null;
    }

    @Override
    public void render(Scene currentScene) {
        // Pintamos el frame
        while (!this.holder.getSurface().isValid()) ;
        // Se permite editar el canvas
        this.canvas = this.holder.lockCanvas();
        // Se pinta la escena actual si existe
        currentScene.render();

        this.holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public int getWindowWidth() {
        return this.window.getWidth();
    }

    @Override
    public int getWindowHeight() {
        return this.window.getHeight();
    }

    @Override
    public void setColor(int red, int green, int blue, int alpha) {
        int color = Color.argb(alpha, red, green, blue);
        this.paint.setColor(color);
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        canvas.drawCircle(x, y, radius, this.paint);
    }

    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        canvas.drawRect(new Rect(x, y, w, h), paint);
    }

    public Image newImage(String name) {
        return new AndroidImage(name, window.getContext().getAssets(), this);
    }
    @Override
    public void renderImage(Image img, int x, int y) {
        canvas.drawBitmap(((AndroidImage)img).getImg(), x, y, paint);
    }
    @Override
    public void renderImage(Image img, int x, int y, int w, int h) {
//        canvas.drawBitmap(img, x, y, paint);
    }
}
