package com.grupo04.engine;

public abstract class Audio {
    public Sound newSound(String soundName) { return null; }
    public Sound newSound(String soundName, int priority) { return null; }
    public Sound newSound(String soundName, int priority, int loop, float rate) { return null; }
    public Sound newSound(String soundName, int priority, float leftVolume, float rightVolume, int loop, float rate) { return null; }
    public boolean playSound(String soundName) { return false; }
    public boolean stopSound(String soundName) { return false; }
    public boolean resumeSound(String soundName) { return false; }
}
