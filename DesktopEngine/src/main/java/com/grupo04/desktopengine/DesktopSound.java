package com.grupo04.desktopengine;

import com.grupo04.engine.Sound;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class DesktopSound extends Sound {
    String soundName                = "";
    File audioFile                  = null;
    AudioInputStream audioStream    = null;
    Clip clip                       = null;
    int priority                    = 0;
    float leftVolume                = 1;
    float rightVolume               = 1;
    int loop                        = 0; // Clip.LOOP_CONTINUOUSLY = -1
    float rate                      = 1f;//

    long currentFrame               = 0; // Para el resume()
    SourceDataLine dataLine         = null;
    AudioFormat audioFormat         = null;

    DesktopSound(String fileName, int priority) {
        this(fileName, priority, 1, 1, 0, 1.0f);
    }

    DesktopSound(String fileName, int priority, int loop, float rate) {
        this(fileName, priority, 1, 1, loop, rate);
    }

    DesktopSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        this.priority = priority;
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        this.loop = loop;
        this.rate = rate;

        try {
            this.soundName = fileName;
            this.audioFile = new File("./assets/" + this.soundName);
            this.audioStream = AudioSystem.getAudioInputStream(this.audioFile);
            this.audioFormat = this.audioStream.getFormat();
            this.clip = AudioSystem.getClip();
            this.clip.open(audioStream);
            this.clip.loop(this.loop);
        } catch (Exception e) {
            System.err.printf("Couldn't load audio file (%s)%n", fileName);
            e.printStackTrace();
        }
    }

    @Override
    public boolean play() { return perfomSoundAction(0); }

    @Override
    public boolean stop() { return perfomSoundAction(1); }

    @Override
    public boolean resume() { return perfomSoundAction(2); }

    @Override
    public boolean setPriority(int priority) {
        this.priority = priority;
        return true;
    }

    @Override
    public boolean setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;

        try {
            FloatControl balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
            // Calculate the balance (range -1.0 to 1.0, where -1 is fully left, 1 is fully right)
            float balance = (rightVolume - leftVolume);
            balanceControl.setValue(balance);
//            // Set master volume (if needed, for overall volume control)
//            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//            float maxVolume = volumeControl.getMaximum();
//            float minVolume = volumeControl.getMinimum();
//            float targetVolume = (this.leftVolume + this.rightVolume) / 2;
//            // Set master volume
//            volumeControl.setValue(Math.min(maxVolume, Math.max(minVolume, targetVolume)));
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
        }

        return true;
    }

    @Override
    public boolean setLeftVolume(float leftVolume) {
        return setVolume(leftVolume, this.rightVolume);
    }

    @Override
    public boolean setRightVolume(float rightVolume) {
        return setVolume(this.leftVolume, rightVolume);
    }

    @Override
    public boolean setLoop(int loop) {
        this.loop = loop;
        this.clip.loop(this.loop);
        return true;
    }

    @Override
    public boolean setRate(float rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("New rate should be bigger than 0.0f.");
        }

        this.rate = rate;
        float newRate = audioFormat.getSampleRate() * rate;
        AudioFormat newFormat = new AudioFormat(
                newRate,
                audioFormat.getSampleSizeInBits(),
                audioFormat.getChannels(),
                audioFormat.isBigEndian(),
                audioFormat.isBigEndian()
        );

        if (dataLine != null && dataLine.isOpen()) {
            dataLine.stop();
            dataLine.close();
        }

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, newFormat);
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(newFormat);
            return true;
        } catch (Exception e) {
            System.err.println("Could not set new rate in clip.");
            return false;
        }
    }

    private boolean perfomSoundAction(int option) {
        if (this.clip == null) {
            System.err.println("Clip not loaded or initialized.");
            return false;
        }

        try {
            switch (option) {
                case 0: this.clip.start(); break;
                case 1:
                    this.currentFrame = this.clip.getMicrosecondPosition();
                    this.clip.stop();
                    break;
                case 2:
                    this.clip.close();
                    resetAudioStream();
                    this.clip.setMicrosecondPosition(currentFrame);
                    this.play();
                    break;
                default:
                    System.err.println("No action was taken.");

            }
            return true;
        } catch (Exception e) {
            if (option == 0) System.err.println("Failed to play the clip.");
            else System.err.println("Failed to stop the clip.");
            e.printStackTrace();
            return false;
        }
    }

    private void resetAudioStream() {
        try {
            this.clip.open(audioStream);
            this.clip.loop(this.loop);
        } catch (Exception e) {
            System.err.printf("Couldn't load audio file (%s)%n", this.soundName);
            e.printStackTrace();
        }
    }
}
