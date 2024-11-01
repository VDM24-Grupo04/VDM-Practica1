package com.grupo04.engine;

public abstract class Sound {
    protected String soundName  = "";
    protected int priority      = 0;
    protected float leftVolume  = 1.0f;
    protected float rightVolume = 1.0f;
    protected int loop          = 0;
    protected float rate        = 0.0f;

    public Sound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        this.soundName = fileName;
        this.priority = priority;
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        this.loop = loop;
        this.rate = rate;
    }

    protected abstract boolean play();
    protected abstract boolean stop();
    protected abstract boolean pause();
    protected abstract boolean resume();

    public String getSoundName() { return this.soundName; }
    public int getPriority() { return this.priority; }
    public float getLeftVolume() { return this.leftVolume; }
    public float getRightVolume() { return this.rightVolume; }
    public int getLoop() { return this.loop; }
    public float getRate() { return this.rate; }

    public boolean setPriority(int priority) {
        this.priority = priority;
        return true;
    }

    public boolean setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        return true;
    }

    public boolean setLoop(int loop) {
        this.loop = loop;
        return true;
    }

    public boolean setRate(float rate) {
        this.rate = rate;
        return true;
    }
}
