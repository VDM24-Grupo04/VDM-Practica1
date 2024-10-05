package com.grupo04.desktopengine;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;

import javax.swing.JFrame;

import java.awt.BasicStroke;
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
        // Establece que si ya hay algo dibujado en graphics2D y se vuelve a dibujar en el,
        // se pinta por encima de lo que habia
        // Es el modo por defecto. Hay otros modos como setXORMode()...
        this.graphics2D.setPaintMode();
    }

    @Override
    public void setColor(com.grupo04.engine.Color color) {
        Color colorInt = new Color(color.red, color.green, color.blue, color.alpha);
        this.graphics2D.setColor(colorInt);
    }

    @Override
    public void drawCircle(Vector position, float radius, float strokeWidth) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(radius, radius));
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawOval((int) screenPosition.x, (int) screenPosition.y,
                (int) screenDim.x, (int) screenDim.y);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillCircle(Vector position, float radius) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(radius, radius));
        this.graphics2D.fillOval((int) screenPosition.x, (int) screenPosition.y,
                (int) screenDim.x, (int) screenDim.y);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawRectangle(Vector position, float w, float h, float strokeWidth) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawRect((int) screenPosition.x, (int) screenPosition.y,
                (int) screenDim.x, (int) screenDim.y);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillRectangle(Vector position, float w, float h) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.graphics2D.fillRect((int) screenPosition.x, (int) screenPosition.y,
                (int) screenDim.x, (int) screenDim.y);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawRoundRectangle(Vector position, float w, float h, float arc, float strokeWidth) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawRoundRect((int) screenPosition.x, (int) screenPosition.y,
                (int) screenDim.x, (int) screenDim.y, (int) arc, (int) arc);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillRoundRectangle(Vector position, float w, float h, float arc) {
        Vector screenPosition = this.worldToScreenPoint(position);
        Vector screenDim = this.worldToScreenPoint(new Vector(w, h));
        this.graphics2D.drawRoundRect((int) screenPosition.x, (int) screenPosition.y,
                (int) screenDim.x, (int) screenDim.y, (int) arc, (int) arc);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawLine(Vector initPos, Vector endPos, float strokeWidth) {
        Vector initScreenPos = this.worldToScreenPoint(initPos);
        Vector endScreenPos = this.worldToScreenPoint(endPos);
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawLine((int) initScreenPos.x, (int) initScreenPos.y, (int) endScreenPos.x, (int) endScreenPos.y);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawHexagon(Vector position, Vector sideSize, float strokeWidth) {

    }

    @Override
    public Image newImage(String name) {
        return new DesktopImage(name, this);
    }

    @Override
    public void drawImage(Image img, Vector position) {
        Vector screenPosition = this.worldToScreenPoint(position);
        graphics2D.drawImage(((DesktopImage) img).getImg(),
                (int) screenPosition.x, (int) screenPosition.y, null);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawImage(Image img, Vector position, int w, int h) {
        Vector screenPosition = this.worldToScreenPoint(position);
        graphics2D.drawImage(((DesktopImage) img).getImg(),
                (int) screenPosition.x, (int) screenPosition.y, w, h, null);
        this.graphics2D.setPaintMode();
    }
}
