package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Sound;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.BallColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Set;

public class Grid extends GameObject {
    int[][] bubbles;          // Matriz de burbujas con los colores de las mismas
    int rows;                   // Numero de filas
    int bubblesPerRow;          // Numero de columnas
    int numBubbles;             // Numero total de burbujas

    int bubbleRadius;           // Radio de las burbujas
    int offsetX, offsetY;       // Offsets para las paredes y la cabecera

    // Linea del final del nivel
    final int lineThickness = 1;
    final Color lineColor = new Color(0, 0, 0, 255);
    Vector lineInit, lineEnd;

    List<Vector> dirs;
    int[] colorCount;

    Sound onAttachSound = null;

    @Override
    public void init() {
    }

    public Grid(int width, int initRows, int r, int wallThickness, int headerOffset, int limitY) {
        super();
        this.bubbleRadius = r;
        this.offsetX = wallThickness;
        this.offsetY = wallThickness + headerOffset;

        // Calculamos el numero de filas y columnas a partir del tamano de la zona de juego y el radio
        // (Se pone 1 fila mas que sera la que sobrepase el limte inferior)
        this.bubblesPerRow = (int) ((width - wallThickness * 2) / (this.bubbleRadius * 2));
        this.rows = 1 + (int) (limitY - headerOffset - wallThickness) / (this.bubbleRadius * 2);
        this.bubbles = new int[this.rows][this.bubblesPerRow];
        for(int[] row : this.bubbles) {
            Arrays.fill(row, -1);
        }

        // Se generan initRows filas iniciales
        this.numBubbles = 0;
        this.colorCount = new int[BallColors.getColorCount()];
        for (int i = 0; i < initRows; i++) {
            // En las filas impares hay una bola menos
            int bPerRow = (i % 2 == 0) ? this.bubblesPerRow : (this.bubblesPerRow - 1);
            this.numBubbles += bPerRow;

            // Se generan las burbujas de la fila
            for (int j = 0; j < bPerRow; ++j) {
                int color = BallColors.getRandomColor();
                this.bubbles[i][j] = color;
                this.colorCount[color]++;
            }
        }

        lineInit = new Vector(offsetX, limitY);
        lineEnd = new Vector(width - offsetX, limitY);

        // Creamos la lista de direcciones adyacentes
        dirs = new ArrayList<>();
        dirs.add(new Vector(-1, 0));    // Izquierda
        dirs.add(new Vector(1, 0));     // Derecha
        dirs.add(new Vector(0, -1));    // Arriba izquierda
        dirs.add(new Vector(0, 1));     // Abajo izquierda
        dirs.add(new Vector(1, -1));    // Arriba derecha
        dirs.add(new Vector(1, 1));     // Abajo derecha
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // Recorre la matriz y pinta las bolas si el color en la posicion i,j de la matriz no es null
        for (int i = 0; i < this.rows; i++) {
            int bPerRow = (i % 2 == 0) ? this.bubblesPerRow : (this.bubblesPerRow - 1);
            for (int j = 0; j < bPerRow; ++j) {
                if (this.bubbles[i][j] >= 0) {
                    graphics.setColor(BallColors.getColor(this.bubbles[i][j]));
                    graphics.fillCircle(gridToWorldPosition(i, j), this.bubbleRadius);
                }
            }
        }

        // Pintala linea del limite inferior
        graphics.setColor(lineColor);
        graphics.drawLine(lineInit, lineEnd, lineThickness);

        Engine engine = this.scene.getEngine();
        this.onAttachSound = engine.getAudio().newSound("ballAttach.wav");
    }

    // Convierte de coordenadas de la matriz a coordenadas de mundo
    private Vector gridToWorldPosition(int i, int j) {
        Vector pos = new Vector(0, 0);
        pos.x = this.offsetX + ((i % 2 == 0) ? 0 : this.bubbleRadius) + this.bubbleRadius + this.bubbleRadius * 2 * j;
        pos.y = this.offsetY + this.bubbleRadius + this.bubbleRadius * 2 * (i);
        return pos;
    }


    // public bool checkCollision(Vector2 pos, Color col) {
    //      int i,j = screenToMatrix(pos)
    //      chequear la celda actual y la sigiuente. Si la siguiente esta ocupada, me coloco en la actual
    //      cambiar el valor i,j de la matriz por color
    //
    //      if(hayColision) manageCollision(i, j, col);
    //      return true al colisionar o si la celda actual tiene y = 0
    // }

//    private Vector worldToGridPosition(Vector pos) {
//        Vector
//    }

     public boolean checkCollision(Vector pos, int color) {
         int i = -1, j = -1;

        //          int i,j = screenToMatrix(pos)
//          chequear la celda actual y la sigiuente. Si la siguiente esta ocupada, me coloco en la actual
//          cambiar el valor i,j de la matriz por color
//
//          if(hayColision) manageCollision(i, j, col);
//          return true al colisionar o si la celda actual tiene y = 0
         if (colorCount[color] <= 0) {
             BallColors.removeColor(color);
         }

         return false;
     }

     private void manageCollision(int i, int j, Color col) {
        playAttachSound();
    //      recorro dfs con grafo implicito y me guarda las bolas del mismo color por coordenadas
    //      tb me guardo las coordenadas adyacentes a partir del borde del conjunto de bolas
    //      si hay igual o mas de 3 bolas, significa que se eliminara el conjunto de bolas
    //      por lo que despegar las bolas que se queden sueltsa
//        List<Vector> bubblesToErase = new ArrayList<>();
//        Set<Vector> pBubblesToFall = new Set<Vector>() ... // Implementar los conjuntos con vectores
//        boolean[][] visited = new boolean[this.rows][this.bubblesPerRow];
//        dfs(..., bubblesToErase, pBubblesToFall)
//        if (bubblesToErase.length > 3) {
//            eliminar bubblesToErase
//            pasar a laura pBubblesToFall
//
//        }
    }

    // A partir de la lista de coordenadas adyacentes tras la eliminacion del conjunto de
    // bolas, sacar conjuntos con dfs y comprobar si hay al menos una bola en el conjunto
    // que este pegada al techo, en ese caso, no se caen.
     private void manageFall(Set<Vector> pBubblesToFall) {
        boolean[][] visited = new boolean[this.rows][this.bubblesPerRow];
        for (Vector v : pBubblesToFall) {
            // Se comprueba que sea distinto de nulo porque puede dos bolas pueden ser del
            // mismo conjunto y se puede haber eliminado ya la coordenada
            if (this.bubbles[(int)v.x][(int)v.y] != -1) {
                List<Vector> bubbles = new ArrayList<>();
                // Si no hay ninguna bola del conjunto que toque el techo, se eliminan
                if (!dfs(visited, (int)v.x, (int)v.y, bubbles)) {
                    for (Vector w : bubbles) {
                        // Por ahora hacemos que se "eliminen" en vez de recrear la caida
                        this.bubbles[(int)w.x][(int)w.y] = -1;
                    }
                }
            }
        }
    }

    // En los recorridos en profundidad (DFS), igual es posible crear solo una funcion general
    // condicionada a los parametros para obtener unas listas u otras dependiendo de si manejamos
    // la eliminacion de bolas del mismo color o de la caida de bolas

    private void dfs(boolean[][] visited, int i, int j, int color, List<Vector> bubblesToErase, Set<Vector> pBubblesToFall) {
        visited[i][j] = true;
        for (Vector dir : this.dirs) {
            int ni = i + (int)dir.x, nj = j + (int)dir.y; // Cambiar Vector para que sea con template
            // Si es una posicion correcta dentro del array bidimensional, hay bola y no esta visitado
            if (isCorrect(ni, nj) && !visited[ni][nj] && this.bubbles[ni][nj] != -1) {
                // Si son del mismo color que la bola lanzada, se añade a la lista de a eliminar
                if (color == this.bubbles[ni][nj]) {
                    bubblesToErase.add(new Vector(ni, nj));
                }
                // Si no, se tendra en cuenta para la caida
                else {
                    pBubblesToFall.add(new Vector(ni, nj));
                }
                dfs(visited, ni, nj, color, bubblesToErase, pBubblesToFall);
            }
        }
    }

    private boolean dfs(boolean[][] visited, int i, int j, List<Vector> bubbles) {
        visited[i][j] = true;
        boolean isRoof = isRoof(i, j);
        for (Vector dir : this.dirs) {
            int ni = i + (int)dir.x, nj = j + (int)dir.y; // Cambiar Vector para que sea con template
            // Si es una posicion correcta dentro del array bidimensional, hay bola y no esta visitado
            if (isCorrect(ni, nj) && this.bubbles[ni][nj] != -1 && !visited[ni][nj]) {
                bubbles.add(new Vector(ni, nj));
                // Si alguna devuelve true, es que el conjunto calculado tras la recursion
                // se caera
                isRoof = dfs(visited, ni, nj, bubbles) || isRoof;
            }
        }
        return isRoof;
    }

    private boolean isCorrect(int i, int j) {
        return i >= 0 && i < this.rows && j >= 0 && j < this.bubblesPerRow;
    }

    private boolean isRoof(int i, int j) {
        return i == 0 && j >= 0 && j < this.bubblesPerRow;
    }

    private void playAttachSound() {
        if (onAttachSound != null) {
            onAttachSound.play();
        }
    }
}
