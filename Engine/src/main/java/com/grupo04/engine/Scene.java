package com.grupo04.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class Scene {
    private boolean alive;
    private HashSet<GameObject> gameObjects;
    private HashMap<String, GameObject> handlers;
    protected Engine engine;
    protected float worldWidth;
    protected float worldHeight;
    protected Color bgColor;

    protected Scene(Engine engine, int worldWidth, int worldHeight, Color bgColor) {
        this.alive = true;
        this.gameObjects = new HashSet<GameObject>();
        this.handlers = new HashMap<String, GameObject>();
        this.engine = engine;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.engine.setWorldSize(this.worldWidth, this.worldHeight);
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
        HashSet<GameObject> deadGameObjects = new HashSet<GameObject>();
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.isAlive()) {
                deadGameObjects.add(gameObject);
            }
        }
        for (GameObject deletedGameObject : deadGameObjects) {
            gameObjects.remove(deletedGameObject);
            // Elimina el objeto que corresponde con la clave y además,
            // devuelve true o false si existe o no, respectivamente
            handlers.remove(deletedGameObject.getId());
            deletedGameObject.dereference();
        }
        deadGameObjects.clear();
    }

    public void fixedUpdate(double fixedDeltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.fixedUpdate(fixedDeltaTime);
        }
    }

    public void render(Graphics graphics) {
        graphics.setBgColor(this.bgColor);
        for (GameObject gameObject : gameObjects) {
            gameObject.render(graphics);
        }
    }

    // El recolector de basura elimina un objeto cuando no hay mas punteros hacia ese objeto.
    // Una escena tiene gameobjects y cada gameobject tiene un puntero a la escena. De modo que
    // cuando se saque una escena de la pila, esta nunca se va a eliminar porque hay un "ciclo"
    // de referencias.
    // Este metodo funciona como una especie de delete y pone todas las referencias del objeto a null.
    // Si la escena o el gameobject hijos tienen mas referencias a otros objetos, debe hacerse override
    // de este metodo y poner esas referencias tambien a null
    public void dereference() {
        for (GameObject gameObject : gameObjects) {
            gameObject.dereference();
        }
        engine = null;
        gameObjects.clear();
        handlers.clear();
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return this.alive;
    }

    // Coger las diferentes referencias
    public void init() {
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
        }
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public Engine getEngine() {
        return this.engine;
    }
}
