package com.grupo04.engine;

import java.util.Stack;

// La interfaz runnable se trata de una interfaz que cuenta con un solo metodo a implementar (run)
// Cuando se pasa una instancia de esta clase a un nuevo hilo, el hilo ejecuta el metodo run
public abstract class Engine implements Runnable {
    private static final int MAX_NUM_FIXED_UDPATES = 150;
    private static final double FIXED_DELTA_TIME = 1000.0 / 60.0;

    // Se necesita un hilo para correr el renderizado a la par que la ejecucion de android
    private Thread mainLoopThread;
    private volatile boolean isRunning;

    // Modulos
    private Graphics graphics;

    // Escenas
    private Stack<Scene> scenes;

    protected Engine() {
        this.mainLoopThread = null;
        this.isRunning = false;
        this.graphics = null;
        scenes = new Stack<Scene>();
    }

    protected void init(Graphics graphics) {
        this.graphics = graphics;
    }

    public void popScene() {
        if (!scenes.empty()) {
            scenes.pop();
        }
    }

    public void pushScene(Scene newScene) {
        scenes.push(newScene);
    }

    public void changeScene(Scene newScene) {
        if (!scenes.empty()) {
            // Si la escena que se quiere insertar no es la misma que la activa...
            if (scenes.peek() != newScene) {
                // Se saca la escena activa
                scenes.pop();
                // Se inserta la nueva escena
                scenes.push(newScene);
            }
        }
    }

    @Override
    public void run() {
        // Evita que se pueda llamar al run desde cualquier sitio.
        // (No se puede hacer que run sea protected o private por Runnable)
        if (this.mainLoopThread != Thread.currentThread()) {
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha muy rápido, la vista podría todavía no estar inicializada,
        // por lo que se inicia un bucle que no hace nada pero que bloquea la ejecucion del codigo
        // posterior hasta que se haya inicializado la vista
        while (this.isRunning && !this.graphics.isWindowInitialized()) ;

        double deltaTime = 0.0;
        double lag = 0.0;

        long lastFrameTime = System.nanoTime();
        // Informe de FPS
        long previousReport = lastFrameTime;
        int frames = 0;

        while (this.isRunning) {
            // Calcular deltatime
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            deltaTime = (double) nanoElapsedTime / 1.0E9;

            handleInput();

            lag += deltaTime;

            int numFixedUpdates = 0;
            // Se realiza el fixedUpdate cada cierto tiempo determinado
            // Al acumularse el tiempo sobrante (algo) puede haber varios fixedUpdates en el mismo frame
            while (lag >= FIXED_DELTA_TIME) {
                fixedUpdate();
                lag -= FIXED_DELTA_TIME;

                // Evitar problema de Spiral of Death
                ++numFixedUpdates;
                if (numFixedUpdates > MAX_NUM_FIXED_UDPATES) {
                    lag = 0;
                    break;
                }
            }

            update(deltaTime);
            // Informar sobre los FPS a los que corre el juego
            if (currentTime - previousReport > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - previousReport);
                System.out.println(fps + " fps");
                frames = 0;
                previousReport = currentTime;
            }
            ++frames;

            render();
        }
    }

    private void handleInput() {
        if (!scenes.empty()) {
            scenes.peek().handleInput();
        }
    }

    private void fixedUpdate() {
        if (!scenes.empty()) {
            scenes.peek().fixedUpdate(FIXED_DELTA_TIME);
        }
    }

    private void update(double deltaTime) {
        if (!scenes.empty()) {
            scenes.peek().update(deltaTime);
        }
    }

    private void render() {
        if (!scenes.empty()) {
            // Si se quedan escenas sin renderizar y se ejecuta el renderizado de un frame,
            // se produce un flickering
            this.graphics.render(scenes.peek());
        }
    }

    public void onResume() {
        if (!this.isRunning) {
            this.isRunning = true;

            // Se crea un nuevo hilo y se inicia
            this.mainLoopThread = new Thread(this);
            // El hilo tiene que estar sincronizado con el de android studio
            this.mainLoopThread.start();
        }
    }

    public void onPause() {
        if (this.isRunning) {
            // Se pone isRunning a false y se intenta esperar a que termine el hilo
            this.isRunning = false;
            while (true) {
                try {
                    // El hilo tiene que estar sincronizado con el de android studio
                    this.mainLoopThread.join();
                    this.mainLoopThread = null;
                    break;
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
