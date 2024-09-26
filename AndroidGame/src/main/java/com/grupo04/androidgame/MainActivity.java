package com.grupo04.androidgame;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private SurfaceView surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creamos el SurfaceView que "contendrá" nuestra escena
        this.surface = new SurfaceView(this);
        setContentView(this.surface);

        // Creacion de la escena

        // Creacion del motor
    }
}