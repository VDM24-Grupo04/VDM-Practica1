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
        super();
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
                    // Se pinta la escena actual si existe
                    currentScene.render();
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

    @Override
    public int getWindowHeight() {
        return this.window.getHeight();
    }

    @Override
    public void setColor(int red, int green, int blue, int alpha) {
        Color color = new Color(red, green, blue, alpha);
        this.graphics2D.setColor(color);
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        this.graphics2D.fillOval(x, y, radius, radius);
    }
    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        this.graphics2D.fillRect(x, y, w, h);
    }
}
