package com.grupo04.androidengine;

import com.grupo04.engine.Audio;

public class AndroidAudio extends Audio {


    @Override
    public AndroidSound newSound(String fileName) { return null; }

    @Override
    public boolean playSound(String soundName) { return false; }

    @Override
    public boolean stopSound(String soundName) { return false; }
}
