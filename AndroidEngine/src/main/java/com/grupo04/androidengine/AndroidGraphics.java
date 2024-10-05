package com.grupo04.androidengine;

import android.content.res.AssetManager;
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
    private AssetManager assetManager;

    public AndroidGraphics(int worldWidth, int worldHeight, SurfaceView window, AssetManager assetManager) {
        super(worldWidth, worldHeight);
        this.window = window;
        this.holder = window.getHolder();
        this.paint = new Paint();
        this.canvas = null;
        this.assetManager = assetManager;
    }

    @Override
    protected void prepareFrame() {
        // Pintamos el frame
        while (!this.holder.getSurface().isValid()) ;
        // Se permite editar el canvas
        this.canvas = this.holder.lockCanvas();
    }

    @Override
    protected boolean endFrame() {
        this.holder.unlockCanvasAndPost(canvas);
        return true;
    }

    @Override
    public void render(Scene currentScene) {
        /*
        // Pintamos el frame
        while (!this.holder.getSurface().isValid()) ;
        // Se permite editar el canvas
        this.canvas = this.holder.lockCanvas();
        // Se pinta la escena actual si existe
        currentScene.render(this);

        this.holder.unlockCanvasAndPost(canvas);
        */
        // Se indica al gestor de renderizado que prepare el frame
        this.prepareFrame();
        // Se pinta la escena
        currentScene.render(this);
        // Se indica al gestor de renderizado que lo muestre
        this.endFrame();
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
    public void clear(com.grupo04.engine.Color color) {
        int colorInt = Color.argb(color.alpha, color.red, color.green, color.blue);
        this.canvas.drawColor(colorInt);
    }

    @Override
    public void setColor(com.grupo04.engine.Color color) {
        int colorInt = Color.argb(color.alpha, color.red, color.green, color.blue);
        this.paint.setColor(colorInt);
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        this.canvas.drawCircle(screenPosition[0], screenPosition[1], radius, this.paint);
    }

    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        this.canvas.drawRect(new Rect(screenPosition[0], screenPosition[1], w, h), this.paint);
    }

    @Override
    public Image newImage(String name) {
        return new AndroidImage(name, this.assetManager, this);
    }

    @Override
    public void renderImage(Image img, int x, int y) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        this.canvas.drawBitmap(((AndroidImage) img).getImg(),
                screenPosition[0], screenPosition[1], this.paint);
    }

    @Override
    public void renderImage(Image img, int x, int y, int w, int h) {
        // this.canvas.drawBitmap(img, x, y, paint);
    }
}
