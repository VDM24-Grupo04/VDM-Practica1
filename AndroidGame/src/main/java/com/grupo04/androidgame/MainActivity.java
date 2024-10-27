package com.grupo04.androidgame;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceView;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

import com.grupo04.engine.Audio;
import com.grupo04.androidengine.AndroidAudio;
import com.grupo04.androidengine.AndroidEngine;
import com.grupo04.gamelogic.scenes.TitleScene;

public class MainActivity extends AppCompatActivity {
    private AndroidEngine androidEngine;

    private boolean audioLoaded = false;

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

        Audio audio = this.androidEngine.getAudio();
        if (audio instanceof AndroidAudio) {
            SoundPool soundPool = ((AndroidAudio) audio).getSoundPool();
            // Añadir un listener para saber si cargó la SoundPool
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    MainActivity.this.audioLoaded = true;
                }
            });
        }

        // Creacion de la escena
        TitleScene testScene = new TitleScene(this.androidEngine);
        this.androidEngine.pushScene(testScene);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndResumeEngine();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.androidEngine.onPause();
    }

    private void checkAndResumeEngine() {
        // Si ya cargó, realiza el onResume de las escenas
        if (this.audioLoaded) {
            this.androidEngine.onResume();
        } else {
            // Realizar un chequeo retrasado de 100ms hasta que cargue la SoundPool
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkAndResumeEngine();
                }
            }, 100);
        }
    }
}