package com.grupo04.desktopengine;

import com.grupo04.engine.Sound;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;

public class DesktopSound extends Sound {
    protected String soundName = "";
    protected File audioFile = null;
    protected AudioInputStream audioStream = null;
    protected Clip clip = null;

    DesktopSound(String fileName) {
        try {
            this.soundName = fileName;
            this.audioFile = new File(/*path+*/this.soundName);
            this.audioStream = AudioSystem.getAudioInputStream(audioFile);
            this.clip = AudioSystem.getClip();
        } catch (Exception e) {
            System.err.printf("Couldn't load audio file (%s)%n", fileName);
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        if (this.clip != null) this.clip.start();
    }

    @Override
    public void stop() {
        if (this.clip != null) this.clip.stop();
    }
}
