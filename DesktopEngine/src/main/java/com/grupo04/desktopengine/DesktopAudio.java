package com.grupo04.desktopengine;

import com.grupo04.engine.Audio;

import java.util.HashMap;

public class DesktopAudio implements Audio {
    private int maxStreams = 0;
    final private HashMap<String, DesktopSound> soundPool;

    public DesktopAudio(int maxStreams) {
        this.maxStreams = maxStreams;
        this.soundPool = new HashMap<>();
    }

    @Override
    public DesktopSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        if (this.soundPool.size() >= this.maxStreams) {
            throw new IllegalStateException("Maximum number of streams exceeded.");
        }

        if (!fileName.isEmpty() && !fileName.isBlank()) {
            DesktopSound newSound = new DesktopSound(fileName, priority, leftVolume, rightVolume, loop, rate, playOnLoad);
            if (newSound != null) {
                this.soundPool.put(fileName, newSound);
                ++this.maxStreams;
                return newSound;
            }
        }
        return null;
    }

    @Override
    public DesktopSound newSound(String fileName, int priority, int loop, float rate, boolean playOnLoad) {
        return newSound(fileName, priority, 1.0f, 1.0f, loop, rate, playOnLoad);
    }

    @Override
    public DesktopSound newSound(String fileName, int priority, boolean playOnLoad) {
        return newSound(fileName, priority, 1.0f, 1.0f, 0, 0.0f, playOnLoad);
    }

    @Override
    public DesktopSound newSound(String fileName, boolean playOnLoad) {
        return newSound(fileName, 0, playOnLoad);
    }

    @Override
    public DesktopSound newSound(String fileName) {
        return newSound(fileName, false);
    }

    @Override
    public boolean playSound(String soundName) {
        return performAudioAction(soundName, 0);
    }

    @Override
    public boolean stopSound(String soundName) {
        return performAudioAction(soundName, 1);
    }

    @Override
    public boolean resumeSound(String soundName) {
        return performAudioAction(soundName, 2);
    }

    private boolean performAudioAction(String soundName, int option) {
        DesktopSound sound = this.soundPool.get(soundName);
        if (sound == null) {
            System.err.printf("Cannot find %s in soundPool.%n", soundName);
            return false;
        }

        switch (option) {
            case 0:
                return sound.play();
            case 1:
                return sound.stop();
            case 2:
                return sound.resume();
            default:
                System.err.println("No action was taken.");
                break;
        }
        return true;
    }
}
