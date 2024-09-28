package com.grupo04.desktopengine;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Scene;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class DesktopGraphics extends Graphics {
    private JFrame window;
    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;

    public DesktopGraphics(JFrame window, Graphics2D graphics2D, BufferStrategy bufferStrategy) {
        this.window = window;
        this.graphics2D = graphics2D;
        this.bufferStrategy = bufferStrategy;
    }

    @Override
    public void render(Scene currentScene) {
        // Pintamos el frame
        do {
            do {
                this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
                try {
                    // Se pinta la escena actual
                    //currentScene.render();
                    this.test();
                } finally {
                    // Elimina el contexto grafico y libera recursos del sistema realacionado
                    this.graphics2D.dispose();
                }
            } while (this.bufferStrategy.contentsRestored());
            this.bufferStrategy.show();
        } while (this.bufferStrategy.contentsLost());
    }

    @Override
    public int getWindowWidth() {
        return this.window.getWidth();
    }

    private void test() {
        this.graphics2D.setColor(Color.BLACK);
        this.graphics2D.fillOval(50, 50, 20, 20);
        this.graphics2D.setPaintMode();
    }
}
