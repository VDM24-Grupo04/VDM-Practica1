package com.grupo04.androidgame;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.grupo04.androidengine.AndroidEngine;
import com.grupo04.gamelogic.TestScene;

public class MainActivity extends AppCompatActivity {
    private SurfaceView window;
    private AndroidEngine androidEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creamos el SurfaceView que "contendrá" nuestra escena
        this.window = new SurfaceView(this);
        setContentView(this.window);

        // Creacion del motor
        this.androidEngine = new AndroidEngine(this.window);

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