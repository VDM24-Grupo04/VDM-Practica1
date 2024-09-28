package com.grupo04.engine;

public abstract class Scene {
    public abstract void handleInput();

    public abstract void update(double deltaTime);

    public abstract void fixedUpdate(double fixedDeltaTime);

    public abstract void render();
}
