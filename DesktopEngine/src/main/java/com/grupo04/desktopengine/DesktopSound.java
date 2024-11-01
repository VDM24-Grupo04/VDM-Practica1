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
    private File audioFile          = null;
    private long currentFrame       = -1;   // Para el resume()
    private SourceDataLine dataLine = null;
    private AudioFormat audioFormat = null;

    // Referencias
    private List<Clip> clips        = null;

    DesktopSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate) {
        super(fileName, priority, leftVolume, rightVolume, loop, rate);

        this.clips = new ArrayList<>();

        try {
            this.audioFile = new File("./assets/sounds/" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(this.audioFile);
            this.audioFormat = audioStream.getFormat();
        } catch (Exception e) {
            System.err.println("Could not load audio file (\"" + fileName + "\"): " + e.getMessage());
        }
    }

    public void addClip(Clip clip) { this.clips.add(clip); }

    @Override
    protected boolean play() {
        try {
            for (int i = 0; i < this.clips.size(); ++i) {
                Clip clip = this.clips.get(i);
                if (!clip.isOpen()) {
                    clip.open(AudioSystem.getAudioInputStream(this.audioFile));
                }
                setClipVolume(clip);
                clip.loop(this.loop);
                clip.start();

                // Si el clip terminó de reproducirse, se cierra y
                // se elimina de la lista de clips
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.stop();
                        clip.close();
                        this.clips.remove(clip);
                    }
                });
            }
            return true;
        } catch (Exception e) {
            System.err.println("Failed to play the clip: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean stop() {
        try {
            if (this.clips.isEmpty()) {
                System.err.println("Sound " + this.soundName + " did not stop because it was not played.");
                return true;
            }
            for (int i = 0; i < this.clips.size(); ++i) {
                Clip clip = this.clips.get(i);
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
            }
            this.clips.clear();
            return true;
        } catch (Exception e) {
            System.err.println("Failed to stop the clip: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean pause() {
        try {
            if (this.clips.isEmpty()) {
                System.err.println("Sound " + this.soundName + " did not pause because it was not played.");
                return true;
            }
            Clip lastClip = this.clips.get(this.clips.size() - 1);
            if (lastClip.isRunning()) {
                this.currentFrame = lastClip.getMicrosecondPosition();
            }
            return stop();
        } catch (Exception e) {
            System.err.println("Failed to pause the clip: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean resume() {
        try {
            if (this.clips.isEmpty()) {
                System.err.println("Sound " + this.soundName + " did not resume because it was not pause.");
                return true;
            }
            for (int i = 0; i < this.clips.size(); ++i) {
                // Resumimos el sonido desde donde lo dejamos
                // en el pause
                Clip clip = this.clips.get(i);
                clip.setMicrosecondPosition(this.currentFrame);
            }
            return play();
        } catch (Exception e) {
            System.err.println("Failed to resume the clip: " + e.getMessage());
            return false;
        }
    }

    private boolean setClipVolume(Clip clip) {
        try {
            // Ajuste de balance
            FloatControl balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
            float balance = this.rightVolume - this.leftVolume;
            balanceControl.setValue(balance);

            // Ajuste del volumen general
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float averageVolume = (this.leftVolume + this.rightVolume) / 2;
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = gainControl.getMinimum() + range * averageVolume;
            gainControl.setValue(gain);
            return true;
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setVolume(float leftVolume, float rightVolume) {
        super.setVolume(leftVolume, rightVolume);
        for (int i = 0; i < this.clips.size(); ++i) {
            if (!setClipVolume(this.clips.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setLoop(int loop) {
        super.setLoop(loop);
        for (int i = 0; i < this.clips.size(); ++i) {
            this.clips.get(i).loop(this.loop);
        }
        return true;
    }

    @Override
    public boolean setRate(float rate) {
        if (rate <= 0) {
            System.err.println("New rate should be bigger than 0.0f");
            return false;
        }

        super.setRate(rate);

        // Calcular la nueva tasa de muestreo
        float newRate = this.audioFormat.getSampleRate() * rate;
        // Crear un nuevo formato de audio modificando la tasa de muestreo
        AudioFormat newFormat = new AudioFormat(
                newRate,
                this.audioFormat.getSampleSizeInBits(),
                this.audioFormat.getChannels(),
                this.audioFormat.isBigEndian(),
                this.audioFormat.isBigEndian()
        );

        // Paramos y cerramos la linea de datos si esta abierta
        if (this.dataLine != null && this.dataLine.isOpen()) {
            this.dataLine.stop();
            this.dataLine.close();
        }

        try {
            // Creamos una nueva linea de datos con el nuevo formato
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, newFormat);
            this.dataLine = (SourceDataLine) AudioSystem.getLine(info);
            this.dataLine.open(newFormat);
            return true;
        } catch (Exception e) {
            System.err.println("Error setting rate: " + e.getMessage());
            return false;
        }
    }
}
