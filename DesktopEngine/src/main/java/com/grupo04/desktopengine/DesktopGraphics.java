package com.grupo04.desktopengine;

import com.grupo04.engine.Font;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;

import javax.swing.JFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
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
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawOval((int) (position.x - radius / 2), (int) (position.y - radius / 2),
                (int) radius, (int) radius);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillCircle(Vector position, float radius) {
        this.graphics2D.fillOval((int) (position.x), (int) (position.y),
                (int) radius * 2, (int) radius * 2);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawRectangle(Vector position, float w, float h, float strokeWidth) {
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawRect((int) (position.x - w / 2), (int) (position.y - h / 2),
                (int) w, (int) h);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillRectangle(Vector position, float w, float h) {
        this.graphics2D.fillRect((int) (position.x - w / 2), (int) (position.y - h / 2),
                (int) w, (int) h);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawRoundRectangle(Vector position, float w, float h, float arc, float strokeWidth) {
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawRoundRect((int) (position.x - w / 2), (int) (position.y - h / 2),
                (int) w, (int) h, (int) arc, (int) arc);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void fillRoundRectangle(Vector position, float w, float h, float arc) {
        this.graphics2D.drawRoundRect((int) (position.x - w / 2), (int) (position.y - h / 2),
                (int) w, (int) h, (int) arc, (int) arc);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawLine(Vector initPos, Vector endPos, float strokeWidth) {
        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawLine((int) initPos.x, (int) initPos.y,
                (int) endPos.x, (int) endPos.y);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawHexagon(Vector center, float radius, float rotInDegrees, float strokeWidth) {
        // Numero de lados del poligono
        int nSides = 6;
        Polygon hexagon = new Polygon();

        // Rotacion del hexagano en radianes y en sentido antihorario
        double rotInRadians = rotInDegrees * Math.PI / 180;

        for (int i = 0; i < nSides; i++) {
            // PI son 180 grados
            // Para dibujar un hexagano hay que dividir una circunferencia en 6 lados
            // Por lo tanto, 360 grados / 6 = 2 * PI / 6
            double pointRot = i * 2 * Math.PI / nSides;
            // Rotar el hexagono respecto a su posicion inicial
            pointRot += rotInRadians;

            int x = (int) (center.x + radius * Math.cos(pointRot));
            int y = (int) (center.y + radius * Math.sin(pointRot));

            hexagon.addPoint((int) x, (int) y);
        }

        this.graphics2D.setStroke(new BasicStroke(strokeWidth));
        this.graphics2D.drawPolygon(hexagon);
        this.graphics2D.setPaintMode();
    }

    @Override
    public Image newImage(String name) {
        return new DesktopImage(name);
    }

    @Override
    public void drawImage(Image img, Vector position) {
        DesktopImage desktopImage = (DesktopImage) img;
        this.graphics2D.drawImage(desktopImage.getImg(),
                (int) (position.x - desktopImage.getWidth() / 2f),
                (int) (position.y - desktopImage.getHeight() / 2f), null);
        this.graphics2D.setPaintMode();
    }

    @Override
    public void drawImage(Image img, Vector position, int w, int h) {
        DesktopImage desktopImage = (DesktopImage) img;
        graphics2D.drawImage(desktopImage.getImg(),
                (int) (position.x - w / 2f),
                (int) (position.y - h / 2f), w, h, null);
        this.graphics2D.setPaintMode();
    }

    @Override
    public Font newFont(String name, float size, boolean isBold) {
        return new DesktopFont(name, size, isBold);
    }

    @Override
    public void setFont(Font font) {
        DesktopFont desktopFont = (DesktopFont) font;
        this.graphics2D.setFont(desktopFont.getFont());
    }

    // Los textos no se dibujan como el resto de objetos desde una esquina del cuadro que los delimita,
    // sino que se pintan desde el principio de una linea (baseline), como cuando se escribe en papel
    // Obtener el ancho del texto es sencillo, pero obtener el alto del texto no ya que se solo se puede
    // obtener las metricas de la tipografia:
    // - Ascent -> altura desde la baseline hasta el caracter mas alto de la tipografia
    // - Descent -> altrua dessde la baseline hasta el caracter mas bajo de la tipografia
    // - Leading -> separacion entre el texto si hubiera varias lineas
    // - Height -> ascent + descent + leading
    @Override
    public void drawText(String text, Vector position) {
        FontMetrics fontMetrics = this.graphics2D.getFontMetrics();

        float h = fontMetrics.getHeight();
        float ascent = fontMetrics.getAscent();

        float x = position.x - getTextWidth(text) / 2f;
        float y = position.y - h / 2 + ascent;
        this.graphics2D.drawString(text, x, y);
    }

    public int getTextWidth(String text) {
        FontMetrics fontMetrics = this.graphics2D.getFontMetrics();
        return fontMetrics.stringWidth(text);
    }

    public int getTextHeight(String text) {
        FontRenderContext fontRenderContext = this.graphics2D.getFontRenderContext();
        GlyphVector glyphVector = this.graphics2D.getFont().createGlyphVector(fontRenderContext, text);
        return glyphVector.getPixelBounds(null, 0, 0).height;
    }
}
