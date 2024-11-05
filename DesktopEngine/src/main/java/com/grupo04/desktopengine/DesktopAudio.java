package com.grupo04.desktopengine;

import com.grupo04.engine.Audio;
import com.grupo04.engine.interfaces.ISound;

import java.util.HashSet;
import java.util.PriorityQueue;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DesktopAudio extends Audio {
    // Para gestionar la prioridad
    // como el SoundPool de Android
    public static class ClipEntry {
        private Clip clip;
        private int priority;

        ClipEntry(Clip clip, int priority) {
            this.clip = clip;
            this.priority = priority;
        }
        public Clip getClip() { return this.clip; }
        public int getPriority() { return this.priority; }
        public void setClip(Clip clip) { this.clip = clip; }
        public void setPriority(int priority) { this.priority = priority; }
    }

    final private PriorityQueue<ClipEntry> playingPool;
    final private HashSet<DesktopSound> pausedPool;

    public DesktopAudio(int maxStreams) {
        // La condición de prioridad es el valor
        // del entero de priority en ClipEntry
        this.playingPool = new PriorityQueue<>(maxStreams, (c1, c2) -> Integer.compare(c2.getPriority(), c1.getPriority()));
        for (int i = 0; i < maxStreams; i++) {
            try {
                Clip clip = AudioSystem.getClip();
                this.playingPool.add(new ClipEntry(clip, 0));
            } catch (Exception e) {
                System.err.println("Error getting clip: " + e.getMessage());
            }
        }
        this.pausedPool = new HashSet<>();
    }

    @Override
    public DesktopSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        if (!fileName.isEmpty() && !fileName.isBlank()) {
            DesktopSound newSound = new DesktopSound(this.playingPool, fileName, priority, leftVolume, rightVolume, loop, rate);
            if (playOnLoad) {
                ClipEntry clipEntry = getAvailableClip();
                if (clipEntry == null) {
                    System.out.printf("Maximum number of streams exceeded, not playing sound: %s%n", fileName);
                }
                else {
                    newSound.addClip(clipEntry);
                    newSound.play();
                }
            }
            return newSound;
        }
        return null;
    }

    private ClipEntry getAvailableClip() {
        for (ClipEntry clipEntry : this.playingPool) {
            Clip clip = clipEntry.getClip();
            if (!clip.isOpen()) {
                return clipEntry;
            }
            else if (!clip.isRunning()) {
                clip.close();
                return clipEntry;
            }
        }
        System.out.println("No clips available.");
        return null;
    }

    @Override
    public void playSound(ISound sound) {
        ClipEntry clipEntry = getAvailableClip();
        if (clipEntry == null) {
            System.out.printf("Maximum number of streams exceeded, not playing sound: %s%n", sound.getSoundName());
            return;
        }

        DesktopSound s = (DesktopSound) sound;
        s.addClip(clipEntry);
        s.play();
    }

    @Override
    public void stopSound(ISound sound) {
        DesktopSound s = (DesktopSound) sound;
        s.stop();
    }

    @Override
    public void pauseSound(ISound sound) {
        DesktopSound s = (DesktopSound) sound;
        this.pausedPool.add(s);
        s.pause();
    }

    @Override
    public void resumeSound(ISound sound) {
        DesktopSound s = (DesktopSound) sound;
        if (!this.pausedPool.remove(s)) {
            System.out.println("Sound " + sound.getSoundName() + " did not resume because it has not been played.");
            return;
        }
        ClipEntry clipEntry = getAvailableClip();
        if (clipEntry == null) {
            System.out.printf("Maximum number of streams exceeded, not playing sound: %s%n", sound.getSoundName());
            return;
        }
        s.addClip(clipEntry);
        s.resume();
    }
}
