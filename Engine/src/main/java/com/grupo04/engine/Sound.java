package com.grupo04.engine;

public abstract class Sound {
    protected String soundName    = "";
    protected int priority        = 0;
    protected float leftVolume    = 1.0f;
    protected float rightVolume   = 1.0f;
    protected int loop            = 0;
    protected float rate          = 0.0f;

    public Sound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        this.soundName = fileName;
        this.priority = priority;
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        this.loop = loop;
        this.rate = rate;
    }
    public abstract boolean performSoundAction(int option);
    public boolean play() { return performSoundAction(0); }
    public boolean stop() { return performSoundAction(1); }
    public boolean resume() { return performSoundAction(2); }
    public void setSoundName(String fileName) { this.soundName = fileName; }
    public boolean setPriority(int priority) { this.priority = priority; return true; }
    public boolean setVolume(float leftVolume, float rightVolume) { this.leftVolume = leftVolume; this.rightVolume = rightVolume; return true; }
    public boolean setLeftVolume(float leftVolume) { setVolume(leftVolume, this.rightVolume); return true; }
    public boolean setRightVolume(float rightVolume) { setVolume(this.leftVolume, rightVolume); return true; }
    public boolean setLoop(int loop) { this.loop = loop; return true; }
    public boolean setRate(float rate) { this.rate = rate; return true; }
    public int getPriority() { return this.priority; }
    public float getLeftVolume() { return this.leftVolume; }
    public float getRightVolume() { return this.rightVolume; }
    public int getLoop() { return this.loop; }
    public float getRate() { return this.rate; }
}
