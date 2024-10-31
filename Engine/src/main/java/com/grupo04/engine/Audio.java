package com.grupo04.engine;

public interface Audio {
    Sound newSound(String soundName);

    Sound newSound(String soundName, boolean playOnLoad);

    Sound newSound(String soundName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad);

    Sound newSound(String soundName, int priority, int loop, float rate, boolean playOnLoad);

    Sound newSound(String soundName, int priority, boolean playOnLoad);

    boolean performAudioAction(String soundName, int option);

    private boolean playSound(String soundName) { return performAudioAction(soundName, 0); }

    private boolean stopSound(String soundName) { return performAudioAction(soundName, 1); }

    private boolean pauseSound(String soundName) { return performAudioAction(soundName, 2); }

    private boolean resumeSound(String soundName) { return performAudioAction(soundName, 3); }
}
