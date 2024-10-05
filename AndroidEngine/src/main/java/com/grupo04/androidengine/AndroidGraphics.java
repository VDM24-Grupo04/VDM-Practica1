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
import com.grupo04.engine.Vector;

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
    public void drawCircle(Vector position, float radius, float strokeWidth) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(radius, radius));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(strokeWidth);
        this.canvas.drawOval(screenPosition.x, screenPosition.y, screenDim.x, screenDim.y, this.paint);
    }

    @Override
    public void fillCircle(Vector position, float radius) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(radius, radius));
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawOval(screenPosition.x, screenPosition.y, screenDim.x, screenDim.y, this.paint);
    }

    @Override
    public void drawRectangle(Vector position, float w, float h, float strokeWidth) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(strokeWidth);
        this.canvas.drawRect(screenPosition.x, screenPosition.y, screenDim.x, screenDim.y, this.paint);
    }

    @Override
    public void fillRectangle(Vector position, float w, float h) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRect(screenPosition.x, screenPosition.y, screenDim.x, screenDim.y, this.paint);
    }

    @Override
    public void drawRoundRectangle(Vector position, float w, float h, float arc, float strokeWidth) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(strokeWidth);
        this.canvas.drawRoundRect(screenPosition.x, screenPosition.y, screenDim.x, screenDim.y, arc, arc, this.paint);
    }

    @Override
    public void fillRoundRectangle(Vector position, float w, float h, float arc) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.paint.setStyle(Paint.Style.FILL);
        this.canvas.drawRoundRect(screenPosition.x, screenPosition.y, screenDim.x, screenDim.y, arc, arc, this.paint);
    }

    @Override
    public void drawLine(Vector initPos, Vector endPos, float strokeWidth) {
        // No importa el estilo
        Vector initScreenPos = this.worldToScreenPoint(initPos);
        Vector endScreenPos = this.worldToScreenPoint(endPos);
        this.paint.setStrokeWidth(strokeWidth);
        this.canvas.drawLine(initScreenPos.x, initScreenPos.y, endScreenPos.x, endScreenPos.y, this.paint);
    }

    @Override
    public void drawHexagon(Vector position, Vector sideSize, float strokeWidth) {

    }

    @Override
    public Image newImage(String name) {
        return new AndroidImage(name, this.assetManager, this);
    }

    @Override
    public void drawImage(Image img, Vector position) {
        Vector screenPosition = this.worldToScreenPoint(position);
        this.canvas.drawBitmap(((AndroidImage) img).getImg(),
                screenPosition.x, screenPosition.y, this.paint);
    }

    @Override
    public void drawImage(Image img, Vector position, int w, int h) {
        // this.canvas.drawBitmap(img, x, y, paint);
    }
}
