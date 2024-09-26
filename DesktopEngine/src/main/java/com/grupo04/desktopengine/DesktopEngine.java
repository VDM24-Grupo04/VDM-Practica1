package com.grupo04.desktopengine;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class DesktopEngine implements Runnable {
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

    }

    public void onResume() {
        if (!this.isRunning) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.isRunning = true;

            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }
}