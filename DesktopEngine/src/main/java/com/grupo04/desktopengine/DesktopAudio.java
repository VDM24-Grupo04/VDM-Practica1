package com.grupo04.desktopengine;

import com.grupo04.engine.Audio;

public class DesktopAudio extends Audio {

    // Pool de DesktopSounds
    DesktopSound[] sounds = new DesktopSound[5];

    @Override
    public DesktopSound newSound(String fileName) { return new DesktopSound(fileName); }

    @Override
    public boolean playSound(String soundName) {
        return performSoundAction(soundName, true);
    }

    @Override
    public boolean stopSound(String soundName) {
        return performSoundAction(soundName, false);
    }

    private boolean performSoundAction(String soundName, boolean play) {
        int i = 0;
        while (i < sounds.length) {
            DesktopSound sound = sounds[i];
            if (sound.soundName.equals(soundName)) {
                if (play)
                    sound.play();
                else
                    sound.stop();
                return true;
            }
            ++i;
        }
        return false;
    }
}
