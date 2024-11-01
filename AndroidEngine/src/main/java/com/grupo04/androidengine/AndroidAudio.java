package com.grupo04.androidengine;

import android.content.res.AssetManager;
import android.media.SoundPool;

import com.grupo04.engine.Sound;
import com.grupo04.engine.Audio;

public class AndroidAudio extends Audio {
    private AssetManager assetManager               = null;
    private SoundPool soundPool                     = null;

    public AndroidAudio(AssetManager assetManager, int maxStreams) {
        this.assetManager = assetManager;
        this.soundPool = new SoundPool.Builder().setMaxStreams(maxStreams).build();
    }

    @Override
    public AndroidSound newSound(String fileName, int priority, float leftVolume, float rightVolume, int loop, float rate, boolean playOnLoad) {
        if (!fileName.isBlank() && !fileName.isEmpty()) {
            AndroidSound sound = new AndroidSound(this.assetManager, this.soundPool, fileName, priority, leftVolume, rightVolume, loop, rate);
            if (playOnLoad) {
                this.soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
                    if (sound.getSoundId() == sampleId && status == 0) {
                        if (!sound.getLoaded()) {
                            sound.play();
                            sound.setLoaded(true);
                        }
                    }
                });
            }
            return sound;
        }
        return null;
    }

    @Override
    public boolean playSound(Sound sound) {
        AndroidSound s = (AndroidSound) sound;
        return s.play();
    }

    @Override
    public boolean stopSound(Sound sound) {
        AndroidSound s = (AndroidSound) sound;
        return s.stop();
    }

    @Override
    public boolean pauseSound(Sound sound) {
        AndroidSound s = (AndroidSound) sound;
        return s.pause();
    }

    @Override
    public boolean resumeSound(Sound sound) {
        AndroidSound s = (AndroidSound) sound;
        return s.resume();
    }
}
