package com.grupo04.engine.interfaces;

public interface ISound {
    String getSoundName();
    int getPriority();
    float getLeftVolume();
    float getRightVolume();
    int getLoop();
    float getRate();
    boolean setPriority(int priority);
    boolean setVolume(float leftVolume, float rightVolume);
    boolean setLoop(int loop);
    boolean setRate(float rate);
}
