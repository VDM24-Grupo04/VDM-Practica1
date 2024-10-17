package com.grupo04.desktopengine;

import com.grupo04.engine.Audio;

import java.util.HashMap;

public class DesktopAudio extends Audio {
    int maxStreams = 0;
    HashMap<String, DesktopSound> soundPool;

    public DesktopAudio(int maxStreams) {
        this.maxStreams = maxStreams;
        this.soundPool = new HashMap<>();
    }

    @Override
    public DesktopSound newSound(String fileName, int priority) {
        if (soundPool.size() >= this.maxStreams) {
            throw new IllegalStateException("Maximum number of streams exceeded.");
        }

        DesktopSound newSound = new DesktopSound(fileName, priority);
        soundPool.put(fileName, newSound);
        ++this.maxStreams;
        return newSound;
    }

    @Override
    public boolean playSound(String soundName) { return performAudioAction(soundName, 0); }

    @Override
    public boolean stopSound(String soundName) { return performAudioAction(soundName, 1); }

    @Override
    public boolean resumeSound(String soundName) { return performAudioAction(soundName, 2); }

    private boolean performAudioAction(String soundName, int option) {
        if (this.soundPool == null) {
            System.err.println("SoundPool not initialized.");
            return false;
        }

        DesktopSound sound = soundPool.get(soundName);
        if (sound == null) {
            System.err.printf("Cannot find %s in soundPool.%n", soundName);
            return false;
        }

        switch (option) {
            case 0: return sound.play();
            case 1: return sound.stop();
            case 2: return sound.resume();
            default:
                System.err.println("No action was taken.");
                break;
        }
        return true;
    }
}
