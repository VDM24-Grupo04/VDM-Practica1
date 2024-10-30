package com.grupo04.engine;

public interface Audio {
    public Sound newSound(String soundName);

    public Sound newSound(String soundName, boolean playOnLoad);

    public Sound newSound(String soundName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad);

    public Sound newSound(String soundName, int priority, int loop, float rate, boolean playOnLoad);

    public Sound newSound(String soundName, int priority, boolean playOnLoad);

    public boolean playSound(String soundName);

    public boolean stopSound(String soundName);

    public boolean resumeSound(String soundName);
}
