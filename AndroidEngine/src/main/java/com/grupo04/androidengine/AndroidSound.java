package com.grupo04.androidengine;

import com.grupo04.engine.Sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import java.io.IOException;

public class AndroidSound extends Sound {
    String soundName                = "";
    int priority                    = 0;
    AssetFileDescriptor audioFile   = null;
    int soundId                     = -1;

    // Referencias
    AssetManager assetManager       = null;
    SoundPool soundPool             = null;

    AndroidSound(AssetManager assetManager, SoundPool soundPool, String fileName, int priority) {
        this.assetManager = assetManager;
        this.soundPool = soundPool;
        this.priority = priority;

        try {
            this.audioFile = assetManager.openFd(/*path+*/fileName);
            this.soundId = soundPool.load(this.audioFile, this.priority);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound (" + soundName + ")");
        }
    }

    @Override
    public boolean play() { return performSoundAction(true); }

    @Override
    public boolean stop() { return performSoundAction(false); }

    private boolean performSoundAction(boolean play) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initializated.");
            return false;
        }

        try {
//            if (play) this.soundPool.play(this.soundId, ...);
            if (play) this.soundPool.stop(this.soundId);
            else this.soundPool.stop(this.soundId);
            return true;
        } catch (Exception e) {
            if (play) System.err.println("Failed to play the clip.");
            else System.err.println("Failed to stop the clip.");
            e.printStackTrace();
            return false;
        }
    }
}
