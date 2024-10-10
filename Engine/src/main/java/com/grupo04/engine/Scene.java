package com.grupo04.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class Scene {
    private HashSet<GameObject> gameObjects;
    private HashMap<String, GameObject> handlers;
    protected Engine engine;
    protected Color bgColor;

    protected Scene(Engine engine, int worldWidth, int worldHeigth, Color bgColor) {
        this.gameObjects = new HashSet<GameObject>();
        this.handlers = new HashMap<String, GameObject>();
        this.engine = engine;
        this.engine.setWorldSize(worldWidth, worldHeigth);
        this.bgColor = bgColor;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.setScene(this);
    }

    public void addGameObject(GameObject gameObject, String handler) {
        gameObjects.add(gameObject);
        if (!handlers.containsKey(handler)) {
            handlers.put(handler, gameObject);
            gameObject.setId(handler);
        }
        gameObject.setScene(this);
    }

    public GameObject getHandler(String handler) {
        return handlers.get(handler);
    }

    public void handleInput(List<TouchEvent> touchEvent) {
        for (GameObject gameObject : gameObjects) {
            gameObject.handleInput(touchEvent);
        }
    }

    public void update(double deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
    }

    public void refresh() {
        HashSet<GameObject> deletedGameObjects = new HashSet<GameObject>();
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isAlive()) {
                deletedGameObjects.add(gameObject);
            }
        }
        for (GameObject deletedGameObject : deletedGameObjects) {
            gameObjects.remove(deletedGameObject);
            // Elimina el objeto que corresponde con la clave y además,
            // devuelve true o false si existe o no, respectivamente
            handlers.remove(deletedGameObject.getId());
            deletedGameObject.dereference();
        }
        deletedGameObjects.clear();
    }

    public void fixedUpdate(double fixedDeltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.fixedUpdate(fixedDeltaTime);
        }
    }

    public void render(Graphics graphics) {
        graphics.clear(this.bgColor);
        for (GameObject gameObject : gameObjects) {
            gameObject.render(graphics);
        }
    }

    public void dereference() {
        for (GameObject gameObject : gameObjects) {
            gameObject.dereference();
        }
        engine = null;
        gameObjects.clear();
        handlers.clear();
    }

    // Coger las diferentes refrenciar
    public void init() {
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
        }
    }

    public Engine getEngine() {
        return this.engine;
    }
}
