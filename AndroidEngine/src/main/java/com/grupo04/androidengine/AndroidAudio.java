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
        return newSound(fileName, priority, 1.0f, 1.0f, 0, 1.0f);
    }

    @Override
    public AndroidSound newSound(String fileName, int priority, int loop, float rate) {
        return newSound(fileName, priority, 1.0f, 1.0f, loop, rate);
    }

    @Override
    public AndroidSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        AndroidSound sound = new AndroidSound(this.assetManager, this.soundPool, fileName, priority, leftVolume, rightVolume, loop, rate);
        sounds.put(fileName, sound);
        return sound;
    }

    @Override
    public boolean playSound(String soundName) { return perfomAudioAction(soundName, 0); }

    @Override
    public boolean stopSound(String soundName) { return perfomAudioAction(soundName, 1); }

    @Override
    public boolean resumeSound(String soundName) { return perfomAudioAction(soundName, 2); }

    private boolean perfomAudioAction(String soundName, int option) {
        AndroidSound sound = sounds.get(soundName);
        if (sound == null) {
            System.err.printf("Cannot find %s in sounds.%n", soundName);
            return false;
        }

        switch (option) {
            case 0: return sound.play();
            case 1: return sound.stop();
            case 2: return sound.resume();
            default:
                System.err.println("No action was taken with the sound");
                return false;
        }
    }
}
