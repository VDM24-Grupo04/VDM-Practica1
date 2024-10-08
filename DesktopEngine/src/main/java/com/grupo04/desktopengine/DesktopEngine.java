package com.grupo04.desktopengine;

import com.grupo04.engine.Engine;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class DesktopEngine extends Engine {
    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;
    private JFrame window;

    public DesktopEngine(JFrame window) {
        super();
        this.window = window;

        // Intentamos crear el buffer strategy con 2 buffers.
        int attempts = 100;
        while (attempts-- > 0) {
            try {
                window.createBufferStrategy(2);
                break;
            } catch (Exception e) {
            }
        }
        if (attempts == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        } else {
            System.out.println("BufferStrategy tras " + (100 - attempts) + " intentos.");
        }

        this.bufferStrategy = window.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

        // Para poder hacer resize de la ventana
        /*
        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                //Component c = (Component)evt.getSource();
                System.out.println("componentResized: " + evt.getSource());
                graphics2D.dispose();

                bufferStrategy.show();
                graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
            }
        });
        */

        DesktopGraphics desktopGraphics = new DesktopGraphics(400, 600, window, this.graphics2D, this.bufferStrategy);
        DesktopAudio desktopAudio = new DesktopAudio(5);
        DesktopInput desktopInput = new DesktopInput(window);
        this.initModules(desktopGraphics, desktopAudio, desktopInput);
    }


}