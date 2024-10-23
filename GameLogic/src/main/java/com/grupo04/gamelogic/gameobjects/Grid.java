package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.BallColors;

public class Grid extends GameObject {
    Color[][] bubbles;          // Matriz de burbujas con los colores de las mismas
    int rows;                   // Numero de filas
    int bubblesPerRow;          // Numero de columnas
    int numBubbles;             // Numero total de burbujas

    int bubbleRadius;           // Radio de las burbujas
    int offsetX, offsetY;       // Offsets para las paredes y la cabecera

    // Linea del final del nivel
    final int lineThickness = 1;
    final Color lineColor = new Color(0, 0, 0, 255);
    Vector lineInit, lineEnd;

    @Override
    public void init() { }

    public Grid(int width, int initRows, int r, int wallThickness, int headerOffset, int limitY) {
        super();
        this.bubbleRadius = r;
        this.offsetX = wallThickness;
        this.offsetY = wallThickness + headerOffset;

        // Calculamos el numero de filas y columnas a partir del tamano de la zona de juego y el radio
        // (Se pone 1 fila mas que sera la que sobrepase el limte inferior)
        this.bubblesPerRow = (int) ((width - wallThickness * 2) / (this.bubbleRadius * 2));
        this.rows =  1 + (int)(limitY - headerOffset - wallThickness) / (this.bubbleRadius * 2);
        this.bubbles = new Color[this.rows][this.bubblesPerRow];

        // Se generan initRows filas iniciales
        this.numBubbles = 0;
        for (int i = 0; i < initRows; i++) {
            // En las filas impares hay una bola menos
            int bPerRow = (i % 2 == 0) ? this.bubblesPerRow : (this.bubblesPerRow - 1);
            this.numBubbles += bPerRow;

            // Se generan las burbujas de la fila
            for (int j = 0; j < bPerRow; ++j) {
                this.bubbles[i][j] = BallColors.getRandomColor();
            }
        }

        lineInit = new Vector(offsetX, limitY);
        lineEnd = new Vector(width - offsetX, limitY);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // Recorre la matriz y pinta las bolas si el color en la posicion i,j de la matriz no es null
        for (int i = 0; i < this.rows; i++) {
            int bPerRow = (i % 2 == 0) ? this.bubblesPerRow : (this.bubblesPerRow - 1);
            for (int j = 0; j < bPerRow; ++j) {
                if (this.bubbles[i][j] != null) {
                    graphics.setColor(this.bubbles[i][j]);
                    graphics.fillCircle(gridToWorldPosition(i, j), this.bubbleRadius);
                }
            }
        }

        // Pintala linea del limite inferior
        graphics.setColor(lineColor);
        graphics.drawLine(lineInit, lineEnd, lineThickness);
    }

    public Color[][] getBubbles() { return this.bubbles; }

    // Convierte de coordenadas de la matriz a coordenadas de mundo
    private Vector gridToWorldPosition(int i, int j) {
        Vector pos = new Vector(0, 0);
        pos.x = this.offsetX + ((i % 2 == 0) ? 0 : this.bubbleRadius) + this.bubbleRadius + this.bubbleRadius * 2 * j;
        pos.y = this.offsetY + this.bubbleRadius + this.bubbleRadius * 2 * (i);
        return pos;
    }


}
