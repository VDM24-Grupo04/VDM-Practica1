package com.grupo04.androidengine;

import com.grupo04.engine.Sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

public class AndroidSound extends Sound {
    String soundName                        = "";
    AssetFileDescriptor assetFileDescriptor = null;
    SoundPool soundPool                     = null;
    int soundId                             = -1;
    int priority                            = 0;

    AndroidSound(String fileName, int priority) {
//        soundPool = new SoundPool.Builder().setMaxStreams(5).build();
//        this.priority = priority;
//        try {
//            assetFileDescriptor = assetManager.openFd(/*path+*/fileName);
//            soundId = soundPool.load(assetFileDescriptor, this.priority);
//        } catch (IOException e) {
//            throw new RuntimeException("Couldn't load sound (" + soundName + ")");
//        }
    }

    @Override
    public void play() {
        //if (soundPool != null) soundPool.play(...);
    }

    @Override
    public void stop() {
        //if (soundPool != null) soundPool.stop(...);
    }
}
