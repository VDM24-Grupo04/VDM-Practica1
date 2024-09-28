package com.grupo04.engine;

// La interfaz runnable se trata de una interfaz que cuenta con un solo metodo a implementar (run)
// Cuando se pasa una instancia de esta clase a un nuevo hilo, el hilo ejecuta el metodo run
public abstract class Engine implements Runnable {
    private static final int MAX_NUM_FIXED_UDPATES = 150;
    private static final double FIXED_DELTA_TIME = 1000.0 / 60.0;

    private Thread mainLoopThread;
    private volatile boolean isRunning;

    private Graphics graphics;

    protected Engine() {
        this.mainLoopThread = null;
        this.isRunning = false;
        this.graphics = null;
    }

    protected void init(Graphics graphics) {
        this.graphics = graphics;
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
                System.out.println("" + fps + " fps");
                frames = 0;
                previousReport = currentTime;
            }
            ++frames;

            render();
        }
    }

    private void handleInput() {

    }

    private void fixedUpdate() {

    }

    private void update(double deltaTime) {

    }

    private void render() {
        this.graphics.render(null);
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
}
