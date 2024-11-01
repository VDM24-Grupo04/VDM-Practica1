package com.grupo04.engine;

import com.grupo04.engine.interfaces.IAudio;

public abstract class Audio implements IAudio {
    public abstract Sound newSound(String soundName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad);
    public Sound newSound(String soundName, int priority, boolean playOnLoad) { return newSound(soundName, priority, 1.0f, 1.0f, 0, 1.0f, playOnLoad); }
    public Sound newSound(String soundName, boolean playOnLoad) { return newSound(soundName, 0, playOnLoad); }
    public Sound newSound(String soundName) { return newSound(soundName, false); }
    public abstract boolean playSound(Sound sound);
    public abstract boolean stopSound(Sound sound);
    public abstract boolean pauseSound(Sound sound);
    public abstract boolean resumeSound(Sound sound);
}
