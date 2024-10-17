package com.grupo04.androidengine;

import com.grupo04.engine.Sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import java.io.IOException;

public class AndroidSound extends Sound {
    String soundName                = "";
    AssetFileDescriptor audioFile   = null;
    int soundId                     = 0; // returned by the load function (0 if failed)
    int streamId                    = 0; // returned by the play function (0 if failed)
    int priority                    = 0; // 0 - lowest priority
    float leftVolume                = 1; // [0, 1]
    float rightVolume               = 1; // [0, 1]
    int loop                        = 0; // 0 - no loop, 1 - loop forever
    float rate                      = 1f;// 1.0 - normal playback, [0.5, 2.0]

    // Referencias
    AssetManager assetManager       = null;
    SoundPool soundPool             = null;

    AndroidSound(AssetManager assetManager, SoundPool soundPool, String fileName, int priority) {
        this(assetManager, soundPool, fileName, priority, 1, 1, 0, 1.0f);
    }

    AndroidSound(AssetManager assetManager, SoundPool soundPool, String fileName, int priority, int loop, float rate) {
        this(assetManager, soundPool, fileName, priority, 1, 1, loop, rate);
    }

    AndroidSound(AssetManager assetManager, SoundPool soundPool, String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        this.assetManager = assetManager;
        this.soundPool = soundPool;
        this.priority = priority;
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        this.loop = loop;
        this.rate = rate;

        try {
            this.audioFile = assetManager.openFd(fileName);
            this.soundId = soundPool.load(this.audioFile, this.priority);
        } catch (IOException e) {
            System.err.printf("Couldn't load sound (%s)", soundName);
        }
    }

    @Override
    public boolean play() { return performSoundAction(0); }

    @Override
    public boolean stop() { return performSoundAction(1); }

    @Override
    public boolean resume() { return performSoundAction(2); }

    @Override
    public boolean setPriority(int priority) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initializated.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.priority = priority;
        this.soundPool.setPriority(this.streamId, this.priority);
        return true;
    }

    @Override
    public boolean setVolume(float leftVolume, float rightVolume) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initializated.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        this.soundPool.setVolume(this.streamId, this.leftVolume, this.rightVolume);
        return true;
    }

    @Override
    public boolean setLeftVolume(float leftVolume) {
        return setVolume(leftVolume, this.rightVolume);
    }

    @Override
    public boolean setRightVolume(float rightVolume) {
        return setVolume(this.leftVolume, rightVolume);
    }

    @Override
    public boolean setLoop(int loop) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initializated.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.loop = loop;
        this.soundPool.setLoop(this.streamId, this.loop);
        return true;
    }

    @Override
    public boolean setRate(float rate) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initializated.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.rate = rate;
        this.soundPool.setRate(this.streamId, this.rate);
        return true;
    }

    private boolean performSoundAction(int option) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initializated.");
            return false;
        }

        if (option != 0 && this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        try {
            switch (option) {
                case 0:
                    this.streamId = this.soundPool.play(this.soundId, this.leftVolume, this.rightVolume, this.priority, this.loop, this.rate);
                    break;
                case 1:
                    this.soundPool.stop(this.streamId);
                    break;
                case 2:
                    this.soundPool.resume(this.streamId);
                    break;
                default:
                    System.err.println("No action was taken with the sound");
                    break;
            }
            return true;
        } catch (Exception e) {
            switch (option) {
                case 0:
                    System.err.println("Failed to play the clip.");
                    break;
                case 1:
                    System.err.println("Failed to stop the clip.");
                    break;
                case 2:
                    System.err.println("Failed to resume the clip.");
                    break;
            }
            e.printStackTrace();
            return false;
        }
    }
}
