package com.example.androidengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Scene;

public class AndroidGraphics extends Graphics {
    private SurfaceView window;
    private SurfaceHolder holder;
    private Paint paint;
    private Canvas canvas;

    public AndroidGraphics(SurfaceView window, SurfaceHolder holder) {
        this.window = window;
        this.holder = holder;
        this.paint = new Paint();
        // Se establece por si acaso un color inicial
        this.paint.setColor(0xFFFFFFFF);
        this.canvas = null;
    }

    @Override
    public void render(Scene currentScene) {
        // Pintamos el frame
        while (!this.holder.getSurface().isValid()) ;
        // Se permite editar el canvas
        this.canvas = this.holder.lockCanvas();
        // Se pinta la escena actual
        // currentScene.render();
        this.test();

        this.holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public int getWindowWidth() {
        return this.window.getWidth();
    }

    private void test() {
        this.paint.setColor(0xFFFFFFFF);
        canvas.drawCircle(50, 50, 20, this.paint);
    }
}
