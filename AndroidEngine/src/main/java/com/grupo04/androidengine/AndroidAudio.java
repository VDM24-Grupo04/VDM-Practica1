package com.grupo04.androidengine;

import android.content.res.AssetManager;
import android.media.SoundPool;

import com.grupo04.engine.Audio;
import com.grupo04.engine.Sound;

import java.util.HashMap;

public class AndroidAudio implements Audio {
    private AssetManager assetManager               = null;
    private SoundPool soundPool                     = null;
    private HashMap<String, AndroidSound> sounds    = null;

    public AndroidAudio(AssetManager assetManager, int maxStreams) {
        this.assetManager = assetManager;
        this.soundPool = new SoundPool.Builder().setMaxStreams(maxStreams).build();
        this.sounds = new HashMap<>();
    }

    @Override
    public AndroidSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        if (!fileName.isBlank() && !fileName.isEmpty()) {
            AndroidSound newSound = new AndroidSound(this.assetManager, this.soundPool, fileName, priority, leftVolume, rightVolume, loop, rate, playOnLoad);
            if (newSound != null) {
                sounds.put(fileName, newSound);
                return newSound;
            }
        }
        return null;
    }

    @Override
    public AndroidSound newSound(String fileName, int priority, int loop, float rate, boolean playOnLoad) {
        return newSound(fileName, priority, 1.0f, 1.0f, loop, rate, playOnLoad);
    }

    @Override
    public AndroidSound newSound(String fileName, int priority, boolean playOnLoad) {
        return newSound(fileName, priority, 1.0f, 1.0f, 0, 1.0f, playOnLoad);
    }

    @Override
    public AndroidSound newSound(String fileName, boolean playOnLoad) {
        return newSound(fileName, 0, playOnLoad);
    }

    @Override
    public AndroidSound newSound(String fileName) {
        return newSound(fileName, false);
    }

    private boolean perfomAudioAction(String soundName, int option) {
        AndroidSound sound = this.sounds.get(soundName);
        if (sound == null) {
            System.err.printf("Cannot find %s in sounds.%n", soundName);
            return false;
        }

        switch (option) {
            case 0:
                return sound.play();
            case 1:
                return sound.stop();
            case 2:
                return sound.resume();
            default:
                System.err.println("No action was taken with the sound");
                return false;
        }
    }

    @Override
    public boolean playSound(String soundName) {
        return perfomAudioAction(soundName, 0);
    }

    @Override
    public boolean stopSound(String soundName) {
        return perfomAudioAction(soundName, 1);
    }

    @Override
    public boolean resumeSound(String soundName) {
        return perfomAudioAction(soundName, 2);
    }
}
