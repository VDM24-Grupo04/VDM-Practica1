package com.grupo04.androidengine;

import com.grupo04.engine.Sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import java.io.IOException;

public class AndroidSound extends Sound {
    private int soundId         = 0; // returned by the load function (0 if failed)
    private int streamId        = 0; // returned by the play function (0 if failed)
    private boolean isLoaded    = false; // Para reproducir cuando cargue la escena

    // Referencias
    private SoundPool soundPool = null;

    AndroidSound(AssetManager assetManager, SoundPool soundPool, String fileName, int priority,
                 float leftVolume, float rightVolume, int loop, float rate) {
        super(fileName, priority, leftVolume, rightVolume, loop, rate);

        this.soundPool = soundPool;

        try {
            AssetFileDescriptor audioFile = assetManager.openFd("sounds/" + fileName);
            this.soundId = this.soundPool.load(audioFile, this.priority);
        } catch (IOException e) {
            System.err.printf("Couldn't load sound (\"%s\")%n", fileName);
        }
    }

    @Override
    protected boolean play() {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        this.streamId = this.soundPool.play(this.soundId, this.leftVolume, this.rightVolume, this.priority, this.loop, this.rate);
        return true;
    }

    @Override
    protected boolean stop() {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.soundPool.stop(this.streamId);
        return true;
    }

    @Override
    protected boolean pause() {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.soundPool.pause(this.streamId);
        return true;
    }

    @Override
    protected boolean resume() {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        this.soundPool.resume(this.streamId);
        return true;
    }

    @Override
    public boolean setPriority(int priority) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played");
            return false;
        }

        super.setPriority(priority);
        this.soundPool.setPriority(this.streamId, this.priority);
        return true;
    }

    @Override
    public boolean setVolume(float leftVolume, float rightVolume) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized");
            return false;
        }

        super.setVolume(leftVolume, rightVolume);
        this.soundPool.setVolume(this.streamId, this.leftVolume, this.rightVolume);
        return true;
    }

    @Override
    public boolean setLoop(int loop) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        super.setLoop(loop);
        this.soundPool.setLoop(this.streamId, this.loop);
        return true;
    }

    @Override
    public boolean setRate(float rate) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        if (this.streamId == 0) {
            System.err.println("Sound has not been played.");
            return false;
        }

        super.setRate(rate);
        this.soundPool.setRate(this.streamId, this.rate);
        return true;
    }

    public int getSoundId() { return this.soundId; }

    public boolean getLoaded() { return this.isLoaded; }

    public void setLoaded(boolean isLoaded) { this.isLoaded = isLoaded; }
}
