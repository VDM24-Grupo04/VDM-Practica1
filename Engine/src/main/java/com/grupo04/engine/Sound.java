package com.grupo04.engine;

public abstract class Sound {
    public boolean play() { return false; }
    public boolean stop() { return false; }
    public boolean resume() { return false; }
    public boolean setPriority(int priority) { return false; }
    public boolean setVolume(float leftVolume, float rightVolume) { return false; }
    public boolean setLeftVolume(float leftVolume) { return false; }
    public boolean setRightVolume(float rightVolume) { return false; }
    public boolean setLoop(int loop) { return false; }
    public boolean setRate(float rate) { return false; }
}
