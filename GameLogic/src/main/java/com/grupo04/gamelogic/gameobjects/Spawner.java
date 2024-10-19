package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.BallColors;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends GameObject {
    int totalWidth;
    int numRows;
    int bubbleRadius;
    int offset;
    float firstHeight;
    int numBubbles;
    int bubblesPerRow;
    Color[][] bubbles;

    @Override
    public void init() {}

    // width = ancho del conjunto de bolas, rows = "alto" del conjunto de bolas,
    // r = radio de las bolas, wallThickness = offset a partir del cual renderiza
    public Spawner(int width, int rows, int r, int wallThickness) {
        super();
        this.totalWidth = width;
        this.numRows = rows;
        this.bubbleRadius = r;
        this.offset = wallThickness;
        this.firstHeight = ((float)(Math.sqrt(3) / 2) * this.bubbleRadius);

        // Calculamos el numero total de bolas que caben
        this.numBubbles = 0;
        this.bubblesPerRow = this.totalWidth / (this.bubbleRadius * 2);
        this.bubbles = new Color[this.numRows][this.bubblesPerRow];
        for (int row = 1; row <= this.numRows; row++) {
            // Para filas pares, hay una bola menos
            int bPerRow = (row % 2 == 0) ? (this.bubblesPerRow - 1) : this.bubblesPerRow;
            this.numBubbles += bPerRow;
            for (int j = 0; j < bPerRow; ++j) {
                this.bubbles[row-1][j] = BallColors.getRandomColor();
            }
        }
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        for (int row = 1; row <= this.numRows; row++) {
            int bPerRow = (row % 2 == 0) ? (this.bubblesPerRow - 1) : this.bubblesPerRow;
            for (int j = 0; j < bPerRow; ++j) {
                graphics.setColor(this.bubbles[row-1][j]);
                graphics.fillCircle(new Vector(this.offset + ((row % 2 == 0) ? this.bubbleRadius : 0) + this.bubbleRadius + this.bubbleRadius * 2 * j,
                        this.offset + (33) /*Cuando se solucione lo del redimensionado, quitarlo*/ + firstHeight  + firstHeight * 2 * (row - 1)), this.bubbleRadius);
            }
        }
    }

    public Color[][] getBubbles() { return this.bubbles; }
}
