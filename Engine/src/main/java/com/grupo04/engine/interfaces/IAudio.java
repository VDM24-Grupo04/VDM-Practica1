package com.grupo04.engine.interfaces;

public interface IAudio {
    ISound newSound(String soundName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad);
    ISound newSound(String soundName, int priority, boolean playOnLoad);
    ISound newSound(String soundName, boolean playOnLoad);
    ISound newSound(String soundName);
    boolean playSound(ISound sound);
    boolean stopSound(ISound sound);
    boolean pauseSound(ISound sound);
    boolean resumeSound(ISound sound);
}
