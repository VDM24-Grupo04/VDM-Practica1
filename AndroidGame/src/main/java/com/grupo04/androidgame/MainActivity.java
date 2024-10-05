package com.grupo04.androidgame;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo04.androidengine.AndroidEngine;
import com.grupo04.gamelogic.TestScene;

public class MainActivity extends AppCompatActivity {
    private AndroidEngine androidEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se encarga de cargar los assets
        AssetManager assetManager = this.getAssets();

        // Creamos el SurfaceView que "contendrá" nuestra escena
        SurfaceView window = new SurfaceView(this);
        setContentView(window);

        // Creacion del motor
        this.androidEngine = new AndroidEngine(window, assetManager);

        // Creacion de la escena
        TestScene testScene = new TestScene(this.androidEngine);
        this.androidEngine.pushScene(testScene);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.androidEngine.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.androidEngine.onPause();
    }
}