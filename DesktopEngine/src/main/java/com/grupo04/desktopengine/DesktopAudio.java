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
        if (this.soundPool == null) {
            throw new IllegalStateException("DesktopAudio sounds not initializated");
        }

        if (soundPool.size() >= this.maxStreams) {
            throw new IllegalStateException("Maximum number of streams exceeded.");
        }

        DesktopSound newSound = new DesktopSound(fileName, priority);
        soundPool.put(fileName, newSound);
        ++this.maxStreams;
        return newSound;
    }

    @Override
    public boolean playSound(String soundName) { return performAudioAction(soundName, true); }

    @Override
    public boolean stopSound(String soundName) { return performAudioAction(soundName, false); }

    private boolean performAudioAction(String soundName, boolean play) {
        if (this.soundPool == null) return false;
        DesktopSound sound = soundPool.get(soundName);
        if (play) return sound.play();
        else return sound.stop();
    }
}
