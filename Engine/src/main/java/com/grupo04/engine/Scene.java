package com.grupo04.engine;

public abstract class Scene {
    protected Engine engine;

    protected Scene(Engine engine) {
        this.engine = engine;
    }

    public abstract void handleInput();

    public abstract void update(double deltaTime);

    public abstract void fixedUpdate(double fixedDeltaTime);

    public abstract void render();
}
