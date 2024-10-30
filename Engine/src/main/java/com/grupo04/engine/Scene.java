package com.grupo04.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class Scene {
    public enum FADE {NONE, IN, OUT}
    private FADE fade;
    private double fadeDuration;
    private double fadeTimer;
    private Color fadeColor;
    private Vector fadePos;
    private Callback onFadeEnd;

    private boolean alive;
    private HashSet<GameObject> gameObjects;
    private HashMap<String, GameObject> handlers;
    protected Engine engine;
    protected int worldWidth;
    protected int worldHeight;
    protected Color bgColor;
    protected Image bgImage;

    protected Scene(Engine engine, int worldWidth, int worldHeight, Color bgColor) {
        this(engine, worldWidth, worldHeight);
        this.bgColor = bgColor;
    }

    protected Scene(Engine engine, int worldWidth, int worldHeight, Image bgImage) {
        this(engine, worldWidth, worldHeight);
        this.bgImage = bgImage;
    }

    protected Scene(Engine engine, int worldWidth, int worldHeight, String bgImageFileName) {
        this(engine, worldWidth, worldHeight);
        this.bgImage = this.engine.getGraphics().newImage(bgImageFileName);
    }

    protected Scene(Engine engine, int worldWidth, int worldHeight) {
        this.alive = true;
        this.gameObjects = new HashSet<GameObject>();
        this.handlers = new HashMap<String, GameObject>();
        this.engine = engine;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.engine.setWorldSize(this.worldWidth, this.worldHeight);
        this.bgColor = new Color(255, 255, 255);

        this.fade = FADE.NONE;
        this.fadeDuration = 0;
        this.fadeTimer = 0;
        this.fadeColor = new Color(0, 0, 0, 255);
        fadePos = new Vector(worldWidth / 2, worldHeight / 2);
        this.onFadeEnd = null;
    }


    // Hacer que la escena comience con un fade. Se le puede pasar solo el tipo de fade,
    // solo el tipo y la duracion, o el tipo, la duracion y el color del fade.
    // ** fadeDuration tiene que ir en segundos
    public void setFade(FADE fade) {
        this.fade = fade;
        this.fadeDuration = 0.2;
        this.fadeTimer = 0;
    }
    public void setFade(FADE fade, double fadeDuration) {
        this.fade = fade;
        this.fadeDuration = fadeDuration;
        this.fadeTimer = 0;
    }
    public void setFade(FADE fade, double fadeDuration, Color fadeColor) {
        setFade(fade, fadeDuration);
        this.fadeColor = fadeColor;

        if (this.fade == FADE.IN) {
            fadeColor.alpha = 0;
        } 
        else if (this.fade == FADE.OUT) {
            fadeColor.alpha = 0;
        }
    }
    public void setFade(FADE fade, Color fadeColor) {
        setFade(fade);
        this.fadeColor = fadeColor;
    }

    public void setFadeCallback(Callback onFadeEnd) {
        this.onFadeEnd = onFadeEnd;
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
        if (fadeTimer >= fadeDuration) {
            for (GameObject gameObject : gameObjects) {
                gameObject.handleInput(touchEvent);
            }
        }

    }

    public void update(double deltaTime) {
        // Si ya se ha terminado de reproducir el fade, permite que se actualicen los objetos
        if (fadeTimer >= fadeDuration) {
            for (GameObject gameObject : gameObjects) {
                gameObject.update(deltaTime);
            }
        }
        // Si no, actualiza el contador del fade
        else {
            fadeTimer += deltaTime;

            // Si el fade justo se ha acabado y hay un callback al acabar, se llama
            if (fadeTimer >= fadeDuration && onFadeEnd != null) {
                onFadeEnd.call();
            }
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
        if (fadeTimer >= fadeDuration) {
            for (GameObject gameObject : gameObjects) {
                gameObject.fixedUpdate(fixedDeltaTime);
            }
        }

    }

    public void render(Graphics graphics) {
        graphics.setBgColor(this.bgColor);
        if (this.bgImage != null) {
            float width = getWorldWidth();
            float height = getWorldHeight();
            float posX = width / 2;
            float posY = height / 2;
            graphics.drawImage(this.bgImage, new Vector(posX, posY), (int)(width), (int)(height));
        }
        for (GameObject gameObject : gameObjects) {
            gameObject.render(graphics);
        }

        // Si no se ha terminado de reproducir el fade
        if (this.fadeTimer < this.fadeDuration) {
            // Se calcula el alpha del fondo en base al tiempo que dura la animacion y al tiempo que lleva reproducido
            int alpha = (int) ((255.0f * this.fadeTimer) / this.fadeDuration);

            // Si es un fade out, se hace a la inversa
            if (this.fade == FADE.OUT) {
                alpha = 255 - alpha;
            }

            // Si el alpha es mayor que 0, se cambia el color del fondo
            // y se pinta un rectangulo que ocupa toda la pantalla
            if (alpha > 0) {
                this.fadeColor.alpha = alpha;
                graphics.setColor(this.fadeColor);
                graphics.fillRectangle(this.fadePos, worldWidth, worldHeight);
            }
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

    public int getWorldWidth() { return worldWidth; }

    public int getWorldHeight() {
        return worldHeight;
    }

    public Engine getEngine() {
        return this.engine;
    }
}
