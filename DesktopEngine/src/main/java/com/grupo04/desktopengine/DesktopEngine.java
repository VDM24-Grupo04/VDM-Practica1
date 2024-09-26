package com.grupo04.desktopengine;

import com.grupo04.engine.Engine;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class DesktopEngine implements Runnable, Engine {
    private JFrame window;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;
    private Paint paint;
    private volatile boolean isRunning;

    Thread renderThread;

    public DesktopEngine(JFrame wd) {
        window = wd;
        // Para poder hacer resize de la ventana
        this.window.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                //Component c = (Component)evt.getSource();
                System.out.println("componentResized: "+evt.getSource());
                graphics2D.dispose();

                bufferStrategy.show();
                graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
            }
        });

        // Intentamos crear el buffer strategy con 2 buffers.
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                window.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        }
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }
        else {
            System.out.println("BufferStrategy tras " + (100 - intentos) + " intentos.");
        }


        bufferStrategy = window.getBufferStrategy();
        graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
    }

    @Override
    public void run() {
        // Evita que se pueda llamar al run desde cualquier sitio.
        // (No se puede hacer que run sea protected o private por Runnable)
        if (renderThread != Thread.currentThread()) {
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha muy rápido, la vista podría todavía no estar inicializada,
        // por lo que se inicia un bucle que no hace nada pero que bloquea la ejecucion del codigo
        // posterior hasta que se haya inicializado la vista
        while (this.isRunning && this.window.getWidth() == 0);

        long lastFrameTime = System.nanoTime();
        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        // Bucle de juego principal.
        while(isRunning) {
            // Calcular deltatime
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            this.update(elapsedTime);
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            // Pintamos el frame
            do {
                do {
                    this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
                    try {
                        this.render();
                    }
                    finally {
                        this.graphics2D.dispose(); //Elimina el contexto gráfico y libera recursos del sistema realacionado
                    }
                } while(this.bufferStrategy.contentsRestored());
                this.bufferStrategy.show();
            } while(this.bufferStrategy.contentsLost());

            /*
            // Posibilidad: cedemos algo de tiempo. Es una medida conflictiva...
            try { Thread.sleep(1); } catch(Exception e) {}
            */
        }
    }

    @Override
    public void update(double deltaTime) {

    }
    @Override
    public void render() {
        // Limpiar fondo y renderizar
    }

    @Override
    public void onResume() {
        if (!this.isRunning) {
            this.isRunning = true;

            // Se crea un nuevo hilo y se inicia
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    @Override
    public void onPause() {
        if (this.isRunning) {
            // Se pone isRunning a false y se intenta esperar a que termine el hilo
            this.isRunning = false;
            while (true) {
                try {
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                }
                catch (InterruptedException ie) { }
            }
        }
    }
}