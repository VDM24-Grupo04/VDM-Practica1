package com.grupo04.desktopengine;

import com.grupo04.engine.Sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DesktopSound extends Sound {
    String soundName                = "";
    int priority                    = 0;
    File audioFile                  = null;
    AudioInputStream audioStream    = null;
    Clip clip                       = null;

    DesktopSound(String fileName, int priority) {
        this.priority = priority;

        try {
            this.soundName = fileName;
            this.audioFile = new File(/*path+*/this.soundName);
            this.audioStream = AudioSystem.getAudioInputStream(this.audioFile);
            this.clip = AudioSystem.getClip();
        } catch (Exception e) {
            System.err.printf("Couldn't load audio file (%s)%n", fileName);
            e.printStackTrace();
        }
    }

    @Override
    public boolean play() { return perfomSoundAction(true); }

    @Override
    public boolean stop() { return perfomSoundAction(false); }

    private boolean perfomSoundAction(boolean play) {
        if (this.clip == null) {
            System.err.println("Clip not loaded or initialized.");
            return false;
        }

        try {
            if (play) clip.start();
            else clip.stop();
            return true;
        } catch (Exception e) {
            if (play) System.err.println("Failed to play the clip.");
            else System.err.println("Failed to stop the clip.");
            e.printStackTrace();
            return false;
        }
    }
}
