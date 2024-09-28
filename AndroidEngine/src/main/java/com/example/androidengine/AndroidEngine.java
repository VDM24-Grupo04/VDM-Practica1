package com.example.androidengine;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.grupo04.engine.Engine;

public class AndroidEngine extends Engine {
    private SurfaceHolder holder;

    public AndroidEngine(SurfaceView window) {
        super();
        this.holder = window.getHolder();

        AndroidGraphics androidGraphics = new AndroidGraphics(window, holder);
        this.init(androidGraphics);
    }
}
