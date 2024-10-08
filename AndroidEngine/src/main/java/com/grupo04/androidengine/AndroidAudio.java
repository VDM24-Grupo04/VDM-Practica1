package com.grupo04.androidengine;

import android.content.res.AssetManager;
import android.media.SoundPool;

import com.grupo04.engine.Audio;

import java.util.HashMap;

public class AndroidAudio extends Audio {
    public AssetManager assetManager     = null;
    SoundPool soundPool                  = null;
    int maxStreams                       = 0;
    HashMap<String, AndroidSound> sounds = null;

    public AndroidAudio(AssetManager assetManager, int maxStreams) {
        this.assetManager = assetManager;
        this.maxStreams = maxStreams;
        this.soundPool = new SoundPool.Builder().setMaxStreams(this.maxStreams).build();
    }

    @Override
    public AndroidSound newSound(String fileName, int priority) {
        AndroidSound sound = new AndroidSound(this.assetManager, this.soundPool, fileName, priority);
        sounds.put(fileName, sound);
        return sound;
    }

    @Override
    public boolean playSound(String soundName) { return perfomAudioAction(soundName, true); }

    @Override
    public boolean stopSound(String soundName) { return perfomAudioAction(soundName, false); }

    private boolean perfomAudioAction(String soundName, boolean play) {
        if (sounds.get(soundName) == null) return false;
        if (play) return sounds.get(soundName).play();
        else return sounds.get(soundName).stop();
    }
}
