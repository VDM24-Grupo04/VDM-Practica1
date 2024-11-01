package com.grupo04.engine.interfaces;

import com.grupo04.engine.Sound;

public interface IAudio {
    Sound newSound(String soundName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad);
    Sound newSound(String soundName, int priority, boolean playOnLoad);
    Sound newSound(String soundName, boolean playOnLoad);
    Sound newSound(String soundName);
    boolean playSound(Sound sound);
    boolean stopSound(Sound sound);
    boolean pauseSound(Sound sound);
    boolean resumeSound(Sound sound);
}
