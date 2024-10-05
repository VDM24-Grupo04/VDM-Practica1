package com.grupo04.desktopengine;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

public class DesktopGraphics extends Graphics {
    private JFrame window;
    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;

    public DesktopGraphics(int worldWidth, int worldHeight, JFrame window, Graphics2D graphics2D, BufferStrategy bufferStrategy) {
        super(worldWidth, worldHeight);
        this.window = window;
        this.graphics2D = graphics2D;
        this.bufferStrategy = bufferStrategy;
    }

    @Override
    protected void prepareFrame() {
        this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
    }

    @Override
    protected boolean endFrame() {
        this.graphics2D.dispose();
        this.graphics2D = null;
        if (bufferStrategy.contentsRestored()) {
            return true;
        }
        this.bufferStrategy.show();
        return this.bufferStrategy.contentsLost();
    }

    @Override
    public void render(Scene currentScene) {
        // Pintamos el frame
        /*
        do {
            do {
                this.graphics2D = (Graphics2D) this.bufferStrategy.getDrawGraphics();
                try {
                    // Se pinta la escena actual si existe
                    currentScene.render(this);
                } finally {
                    // Elimina el contexto grafico y libera recursos del sistema realacionado
                    this.graphics2D.dispose();
                }
            } while (this.bufferStrategy.contentsRestored());
            this.bufferStrategy.show();
        } while (this.bufferStrategy.contentsLost());
        */
        do {
            // Se indica al gestor de renderizado que prepare el frame
            this.prepareFrame();
            // Se pinta la escena
            currentScene.render(this);
            // Se indica al gestor de renderizado que lo muestre
        } while (this.endFrame());
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
    public void clear(com.grupo04.engine.Color color) {
        Color colorInt = new Color(color.red, color.green, color.blue, color.alpha);
        this.graphics2D.setColor(colorInt);
        this.graphics2D.fillRect(0, 0, this.getWindowWidth(), this.getWindowHeight());
    }

    @Override
    public void setColor(com.grupo04.engine.Color color) {
        Color colorInt = new Color(color.red, color.green, color.blue, color.alpha);
        this.graphics2D.setColor(colorInt);
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        this.graphics2D.fillOval(screenPosition[0], screenPosition[1], radius, radius);
    }

    @Override
    public void fillRectangle(int x, int y, int w, int h) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        this.graphics2D.fillRect(screenPosition[0], screenPosition[1], w, h);
    }

    @Override
    public Image newImage(String name) {
        return new DesktopImage(name, this);
    }

    @Override
    public void renderImage(Image img, int x, int y) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        graphics2D.drawImage(((DesktopImage) img).getImg(),
                screenPosition[0], screenPosition[1], null);
    }

    @Override
    public void renderImage(Image img, int x, int y, int w, int h) {
        int[] screenPosition = this.convertToScreenPosition(x, y);
        graphics2D.drawImage(((DesktopImage) img).getImg(),
                screenPosition[0], screenPosition[1], w, h, null);
    }
}
