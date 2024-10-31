package com.grupo04.desktopengine;

import com.grupo04.engine.Sound;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
    final private List<Clip> clips;

    private long currentFrame       = -1;    // Para el resume()
    private SourceDataLine dataLine = null;
    private AudioFormat audioFormat = null;

    // Referencias
    private DesktopAudio audio= null;

    DesktopSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        super(fileName, priority, leftVolume, rightVolume, loop, rate, playOnLoad);

        this.clips = new ArrayList<>();

        try {
            this.audioFile = new File("./assets/sounds/" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(this.audioFile);
            this.audioFormat = audioStream.getFormat();
        } catch (Exception e) {
            System.err.printf("Couldn't load audio file (\"%s\")%n", fileName);
        }
    }

    DesktopSound(String fileName, int priority, int loop, float rate, boolean playOnLoad) {
        this(fileName, priority, 1.0f, 1.0f, loop, rate, playOnLoad);
    }

    DesktopSound(String fileName, int priority, int loop, float rate) {
        this(fileName, priority, 1.0f, 1.0f, loop, rate, false);
    }

    DesktopSound(String fileName, int priority, boolean playOnLoad) {
        this(fileName, priority, 1.0f, 1.0f, 0, 1.0f, playOnLoad);
    }

    DesktopSound(String fileName, int priority) {
        this(fileName, priority, 1.0f, 1.0f, 0, 1.0f, false);
    }

    @Override
    public boolean performSoundAction(int option) {
        try {
            switch (option) {
                case 0:
                    // Comprobamos que no sobrepasemos del maximo de streams
                    if (this.audio != null && this.audio.canPlaySound()) {
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(this.audioFile));
                        setClipVolume(clip, this.leftVolume, this.rightVolume);
                        clip.loop(this.loop);

                        // Reproducimos
                        clip.start();

                        // Si el clip terminó de reproducirse, se cierra y
                        // se elimina de la lista de clips decrementando
                        // el numero de streams en reproducción
                        clip.addLineListener(event -> {
                            if (event.getType() == LineEvent.Type.STOP) {
                                clip.stop();
                                clip.close();
                                this.clips.remove(clip);
                                this.audio.decreaseCurrentStreams();
                            }
                        });
                        this.clips.add(clip);
                        this.audio.increaseCurrentStreams();
                    } else {
                        System.err.println("Maximum streams exceeded, not playing.");
                    }
                    break;
                case 1:
                    // Recorremos los clips asegurando que si un evento asincrono
                    // cierra alguno de los clips, no salte excepcion
                    Iterator<Clip> iterator = this.clips.iterator();
                    while (iterator.hasNext()) {
                        Clip clip = iterator.next();
                        if (clip.isRunning()) {
                            clip.stop();
                        }
                        clip.close();
                        iterator.remove();
                    }
                    break;
                case 2:
                    // Pausamos el clip guardando el frame actual
                    // para reusarlo en el resume (3) y decrementamos
                    // el numero de streams en reproduccion
                    for (Clip clip : this.clips) {
                        if (clip.isRunning()) {
                            this.currentFrame = clip.getMicrosecondPosition();
                            clip.stop();
                            this.audio.decreaseCurrentStreams();
                        }
                    }
                    break;
                case 3:
                    // Resumimos el sonido desde donde lo dejamos
                    // en el pause incrementando el numero de streams
                    // en reproduccion
                    for (Clip clip : this.clips) {
                        if (!clip.isRunning() && this.currentFrame != -1 && this.audio.canPlaySound()) {
                            clip.setMicrosecondPosition(this.currentFrame);
                            clip.start();
                            this.audio.increaseCurrentStreams();
                        }
                    }
                    break;
                default:
                    System.err.println("No action was taken.");

            }
            return true;
        } catch (Exception e) {
            switch (option) {
                case 0: System.err.println("Failed to play the clip."); break;
                case 1: System.err.println("Failed to stop the clip."); break;
                case 2: System.err.println("Failed to pause the clip."); break;
                case 3: System.err.println("Failed to resume the clip."); break;
            }
            return false;
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
            // Ajuste de balance
            FloatControl balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
            float balance = rightVolume - leftVolume;
            balanceControl.setValue(balance);

            // Ajuste del volumen general
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float averageVolume = (leftVolume + rightVolume) / 2;
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = gainControl.getMinimum() + range * averageVolume;
            gainControl.setValue(gain);
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
        }
    }

    @Override
    public boolean setLoop(int loop) {
        super.setLoop(loop);

        for (Clip clip : this.clips) {
            if (clip.isRunning()) {
                clip.loop(this.loop);
            }
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
            System.err.println("Could not set new rate in clip");
            return false;
        }
    }

    public void setAudio(DesktopAudio audio) { this.audio = audio; }

    public void playOnLoad() {
        if (this.playOnLoad) {
            play();
        }
    }
}
