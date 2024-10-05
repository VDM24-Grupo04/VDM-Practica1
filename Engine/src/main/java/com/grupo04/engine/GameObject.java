package com.grupo04.engine;

public abstract class GameObject {
    private String id;
    private boolean alive;
    protected Scene scene;

    protected GameObject() {
        this.id = null;
        this.alive = true;
        this.scene = null;
    }

    public void handleInput() {
    }

    public void render(Graphics graphics) {
    }

    public void update(double deltaTime) {
    }

    public void fixedUpdate(double fixedDeltaTime) {
    }

    public void dereference() {
        scene = null;
    }

    public void initGameObject() {

    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
