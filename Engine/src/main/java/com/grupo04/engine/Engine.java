package com.grupo04.engine;

public interface Engine {
    public void update(double deltaTime);
    public void render();

    public void onResume();
    public void onPause();
}

