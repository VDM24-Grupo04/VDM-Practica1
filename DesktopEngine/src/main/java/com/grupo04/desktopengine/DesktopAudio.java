package com.grupo04.desktopengine;

import com.grupo04.engine.Sound;
import com.grupo04.engine.Audio;

import java.util.HashSet;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DesktopAudio extends Audio {
    final private HashSet<Clip> playingPool;
    final private HashSet<DesktopSound> pausedPool;

    public DesktopAudio(int maxStreams) {
        this.playingPool = new HashSet<>(maxStreams);
        for (int i = 0; i < maxStreams; i++) {
            try {
                Clip clip = AudioSystem.getClip();
                this.playingPool.add(clip);
            } catch (Exception e) {
                System.err.println("Error getting clips: " + e.getMessage());
            }
        }
        this.pausedPool = new HashSet<>();
    }

    @Override
    public DesktopSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        if (!fileName.isEmpty() && !fileName.isBlank()) {
            DesktopSound newSound = new DesktopSound(fileName, priority, leftVolume, rightVolume, loop, rate);
            if (playOnLoad) {
                Clip clip = getAvailableClip();
                if (clip == null) {
                    System.err.printf("Maximum number of streams exceeded, not playing sound: %s%n", fileName);
                }
                else {
                    newSound.addClip(clip);
                    newSound.play();
                }
            }
            return newSound;
        }
        return null;
    }

    private Clip getAvailableClip() {
        for (Clip clip : this.playingPool) {
            if (!clip.isRunning()) {
                return clip;
            }
        }
        System.err.println("No clips available.");
        return null;
    }

    @Override
    public boolean playSound(Sound sound) {
        Clip clip = getAvailableClip();
        if (clip == null) {
            System.err.printf("Maximum number of streams exceeded, not playing sound: %s%n", sound.getSoundName());
            return false;
        }

        DesktopSound s = (DesktopSound) sound;
        s.addClip(clip);
        return s.play();
    }

    @Override
    public boolean stopSound(Sound sound) {
        DesktopSound s = (DesktopSound) sound;
        return s.stop();
    }

    @Override
    public boolean pauseSound(Sound sound) {
        DesktopSound s = (DesktopSound) sound;
        this.pausedPool.add(s);
        return s.pause();
    }

    @Override
    public boolean resumeSound(Sound sound) {
        DesktopSound s = (DesktopSound) sound;
        this.pausedPool.remove(s);
        Clip clip = getAvailableClip();
        if (clip == null) {
            System.err.printf("Maximum number of streams exceeded, not playing sound: %s%n", sound.getSoundName());
            return false;
        }
        s.addClip(clip);
        return s.resume();
    }
}
