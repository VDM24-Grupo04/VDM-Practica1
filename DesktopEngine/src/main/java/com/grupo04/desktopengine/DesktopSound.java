package com.grupo04.desktopengine;

import com.grupo04.engine.Sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.SourceDataLine;

public class DesktopSound extends Sound {
    File audioFile                  = null;
    AudioInputStream audioStream    = null;
    List<Clip> clips                = null;

    long currentFrame               = 0; // Para el resume()
    SourceDataLine dataLine         = null;
    AudioFormat audioFormat         = null;
    boolean isPlaying               = false;

    DesktopSound(String fileName, int priority) {
        this(fileName, priority, 1.0f, 1.0f, 0, 1.0f);
    }

    DesktopSound(String fileName, int priority, int loop, float rate) {
        this(fileName, priority, 1.0f, 1.0f, loop, rate);
    }

    DesktopSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        super(fileName, priority, leftVolume, rightVolume, loop, rate);

        this.clips = new ArrayList<>();

        try {
            this.audioFile = new File("./assets/" + this.soundName);
            this.audioStream = AudioSystem.getAudioInputStream(this.audioFile);
            this.audioFormat = this.audioStream.getFormat();
        } catch (Exception e) {
            System.err.printf("Couldn't load audio file (%s)%n", fileName);
            e.printStackTrace();
        }
    }

    @Override
    public boolean setVolume(float leftVolume, float rightVolume) {
        super.setVolume(leftVolume, rightVolume);

        for (Clip clip : this.clips) {
            setClipVolume(clip, leftVolume, rightVolume);
        }
        return true;
    }

    private void setClipVolume(Clip clip, float leftVolume, float rightVolume) {
        try {
            FloatControl balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
            float balance = (rightVolume - leftVolume); // Balance between -1.0 and 1.0
            balanceControl.setValue(balance);
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
        }
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
        super.setLoop(loop);

        for (Clip clip : clips) {
            if (clip.isRunning()) {
                clip.loop(this.loop);
            }
        }
        return true;
    }

    @Override
    public boolean setRate(float rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("New rate should be bigger than 0.0f.");
        }

        super.setRate(rate);
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

    @Override
    public boolean performSoundAction(int option) {
        try {
            switch (option) {
                case 0:
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(audioFile));
                    setClipVolume(clip, this.leftVolume, this.rightVolume);
                    clip.loop(this.loop);

                    clip.start();
                    this.isPlaying = true;

                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                            this.clips.remove(clip);
                        }
                    });
                    this.clips.add(clip);
                    break;
                case 1:
                    for (Clip clipIt : this.clips) {
                        if (clipIt.isRunning()) {
                            clipIt.stop();
                            clipIt.close();
                        }
                    }
                    this.clips.clear();
                    this.isPlaying = false;
                    break;
                case 2:
                    for (Clip clipIt : this.clips) {
                        if (!clipIt.isRunning()) {
                            clipIt.setMicrosecondPosition(this.currentFrame);
                            clipIt.start();
                        }
                    }
                    this.isPlaying = true;
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
}
