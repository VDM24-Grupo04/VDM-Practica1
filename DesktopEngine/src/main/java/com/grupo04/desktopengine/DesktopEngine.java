package com.grupo04.desktopengine;

import com.grupo04.engine.Engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class DesktopEngine extends Engine {
    private DesktopAudio desktopAudio;

    public DesktopEngine(JFrame window, int maxStreams) {
        super();

        // Intentamos crear el buffer strategy con 2 buffers
        int attempts = 100;
        while (attempts-- > 0) {
            try {
                window.createBufferStrategy(2);
                break;
            } catch (Exception e) {
                System.out.println("Error creating double buffer: " + e.getMessage());
            }
        }
        if (attempts == 0) {
            System.err.println("BufferStrategy could not be created");
            return;
        } else {
            System.out.println("BufferStrategy after " + (100 - attempts) + " attempts");
        }

        BufferStrategy bufferStrategy = window.getBufferStrategy();
        Graphics2D graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

        DesktopGraphics desktopGraphics = new DesktopGraphics(window, graphics2D, bufferStrategy);
        this.desktopAudio = new DesktopAudio(maxStreams);
        DesktopInput desktopInput = new DesktopInput(window);
        this.initModules(desktopGraphics, this.desktopAudio, desktopInput);

        // Al cerrar la ventana se realiza una salida adecuada del sistema
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                shutdown();
                System.exit(0);
            }
        });

        // Se anade al JFrame un listener de eventos de teclado
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) { }

            // Pulsar tecla
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                // Si se pulsa el escape
                if (keyEvent.getKeyCode() == 27) {
                    shutdown();
                    System.exit(0);
                }
            }

            // Soltar tecla
            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        // Se anade al JFrame un listener de foco de la ventana
        window.addWindowFocusListener(new WindowFocusListener() {
            // Si se pierde el foco, se pausa el hilo que ejecuta el juego
            @Override
            public void windowLostFocus(WindowEvent e) {
                onPause();
            }
            // Si se recupera el foco, se reanuda el hilo que ejecuta el juego
            @Override
            public void windowGainedFocus(WindowEvent e) {
                onResume();
            }
        });
    }

    // Cierra todos los clips que estuvieran abiertos cuando se cierra el juego en Desktop
    @Override
    public void shutdown() {
        if (this.desktopAudio != null) {
            this.desktopAudio.closeAllClips();
        }
    }
}
