package com.grupo04.desktopengine;

import com.grupo04.engine.Engine;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class DesktopEngine extends Engine {
    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;

    public DesktopEngine(JFrame window) {
        super();

        // Intentamos crear el buffer strategy con 2 buffers.
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

        this.bufferStrategy = window.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();

        DesktopGraphics desktopGraphics = new DesktopGraphics(window, this.graphics2D, this.bufferStrategy);
        DesktopAudio desktopAudio = new DesktopAudio(5);
        DesktopInput desktopInput = new DesktopInput(window, this);
        this.initModules(desktopGraphics, desktopAudio, desktopInput);
    }
}
