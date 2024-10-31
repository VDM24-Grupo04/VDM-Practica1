package com.grupo04.desktopengine;

import com.grupo04.engine.Audio;

import java.util.HashMap;

public class DesktopAudio implements Audio {
    final private int maxStreams;
    private int currentStreams = 0;
    final private HashMap<String, DesktopSound> sounds;

    public DesktopAudio(int maxStreams) {
        this.maxStreams = maxStreams;
        this.sounds = new HashMap<>();
    }

    @Override
    public DesktopSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        if (!fileName.isEmpty() && !fileName.isBlank()) {
            DesktopSound newSound = new DesktopSound(fileName, priority, leftVolume, rightVolume, loop, rate, playOnLoad);
            newSound.setAudio(this);
            newSound.playOnLoad();
            if (newSound != null) {
                this.sounds.put(fileName, newSound);
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
    public boolean performAudioAction(String soundName, int option) {
        DesktopSound sound = this.sounds.get(soundName);
        if (sound == null) {
            System.err.printf("Cannot find %s in sounds.%n", soundName);
            return false;
        }

        switch (option) {
            case 0: return sound.play();
            case 1: return sound.stop();
            case 2: return sound.pause();
            case 3: return sound.resume();
            default:
                System.err.println("No action was taken.");
                break;
        }
        return true;
    }

    public void decreaseCurrentStreams() {
        if (this.currentStreams > 0) {
            --this.currentStreams;
        }
    }

    public void increaseCurrentStreams() {
        ++this.currentStreams;
    }

    public boolean canPlaySound() {
        return this.currentStreams < this.maxStreams;
    }
}
