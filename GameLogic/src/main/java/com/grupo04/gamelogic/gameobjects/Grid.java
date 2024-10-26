package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Pair;
import com.grupo04.engine.Sound;
import com.grupo04.engine.Vector;
import com.grupo04.gamelogic.BallColors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Set;

public class Grid extends GameObject {
    int[][] bubbles;            // Matriz de burbujas con los colores de las mismas
    int rows, cols;             // Numero de filas y columnas
    int totalBubbles;           // Numero total de burbujas

    int r;                      // Radio de las burbujas
    float hexagonRadius;        // Radio de los hexagonos de la c
    int offsetX, offsetY;       // Offsets para las paredes y la cabecera
    int bubbleOffset;           // Offsets para que las bolas

    // Linea del final del nivel
    final int lineThickness = 1;
    final Color lineColor = new Color(0, 0, 0, 255);
    Vector lineInit, lineEnd;

    List<Vector> dirs;
    int[] colorCount;
    int currI = -1, currJ = -1;
    float fallingSpeed = 10.0f;
    List<Pair<Vector, Integer>> fallingBubbles;

    Sound onAttachSound = null;

    public Grid(int width, int wallThickness, int headerOffset, int r, int rows, int cols, int initRows, float fallingSpeed) {
        super();
        this.r = r;
        this.hexagonRadius = (float) Math.ceil((this.r / (Math.sqrt(3) / 2.0f)));

        this.offsetX = wallThickness;
        this.offsetY = wallThickness + headerOffset;
        this.bubbleOffset = (int) (this.r * 0.3);

        this.cols = cols;
        this.rows = rows;
        this.bubbles = new int[this.rows][this.cols];
        for (int[] row : this.bubbles) {
            Arrays.fill(row, -1);
        }

        // Se generan initRows filas iniciales
        this.totalBubbles = 0;
        this.colorCount = new int[BallColors.getColorCount()];
        for (int i = 0; i < initRows; i++) {
            // En las filas impares hay una bola menos
            int bPerRow = (i % 2 == 0) ? this.cols : (this.cols - 1);
            this.totalBubbles += bPerRow;

            // Se generan las burbujas de la fila
            for (int j = 0; j < bPerRow; ++j) {
                int color = BallColors.getRandomColor();
                this.bubbles[i][j] = color;
                this.colorCount[color]++;
            }
        }

        int lineY = (int) (this.r * 2 * (this.rows - 1)) - this.bubbleOffset * (this.rows - 1) + this.bubbleOffset;
        this.lineInit = new Vector(this.offsetX, this.offsetY + lineY);
        this.lineEnd = new Vector(width - this.offsetX, this.offsetY + lineY);

        // Creamos la lista de direcciones adyacentes
        this.dirs = new ArrayList<>();
        this.dirs.add(new Vector(-1, 0));    // Izquierda
        this.dirs.add(new Vector(1, 0));     // Derecha
        this.dirs.add(new Vector(0, -1));    // Arriba izquierda
        this.dirs.add(new Vector(0, 1));     // Abajo izquierda
        this.dirs.add(new Vector(1, -1));    // Arriba derecha
        this.dirs.add(new Vector(1, 1));     // Abajo derecha

        this.fallingSpeed = fallingSpeed;
        this.fallingBubbles = new ArrayList<>();
    }

    public Grid(int width, int wallThickness, int headerOffset, int r, int rows, int cols, int initRows) {
        this(width, wallThickness, headerOffset, r, rows, cols, initRows, 10.0f);
    }

    @Override
    public void init() {
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // Recorre la matriz y pinta las bolas si el color en la posicion i,j de la matriz no es null
        for (int i = 0; i < this.rows; i++) {
            int bPerRow = (i % 2 == 0) ? this.cols : (this.cols - 1);
            for (int j = 0; j < bPerRow; ++j) {
                if (this.bubbles[i][j] >= 0) {
                    graphics.setColor(BallColors.getColor(this.bubbles[i][j]));
                    graphics.fillCircle(gridToWorldPosition(i, j), this.r);
                }

            }
        }

        // Recorre la matriz pintando los hexagonos (hay que recorrerla de nuevo
        // para que los hexagonos se pinten por encima de todas las bolas)
        for (int i = 0; i < this.rows; i++) {
            int bPerRow = (i % 2 == 0) ? this.cols : (this.cols - 1);
            for (int j = 0; j < bPerRow; ++j) {
                Vector pos = gridToWorldPosition(i, j);
                pos.x += 0.5f;
                graphics.setColor(lineColor);
                graphics.drawHexagon(pos, hexagonRadius, 90, lineThickness);
            }
        }

        // DEBUG DE LAS CELDAS
        if( currI >= 0 && currJ >= 0) {
            Vector pos = gridToWorldPosition(currI, currJ);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(0));
            graphics.drawHexagon(pos, hexagonRadius, 90, lineThickness * 2);

            pos = gridToWorldPosition(currI, currJ - 1);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, hexagonRadius, 90, lineThickness * 2);

            pos = gridToWorldPosition(currI, currJ + 1);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, hexagonRadius, 90, lineThickness * 2);

            pos = gridToWorldPosition(currI - 1, currJ);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, hexagonRadius, 90, lineThickness * 2);

            pos = gridToWorldPosition(currI - 1, (currI % 2 == 0) ? currJ - 1 : currJ + 1);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, hexagonRadius, 90, lineThickness * 2);
        }

        // Pinta la linea del limite inferior
        graphics.setColor(lineColor);
        graphics.drawLine(lineInit, lineEnd, lineThickness);

        // Recorre las bolas caidas y las pinta
        if (!this.fallingBubbles.isEmpty()) {
            for (Pair<Vector, Integer> bubble : this.fallingBubbles) {
                graphics.setColor(BallColors.getColor(bubble.getSecond()));
                Vector bPos = bubble.getFirst();
                graphics.fillCircle(gridToWorldPosition((int) bPos.x, (int) bPos.y), this.r);
            }
        }

        Engine engine = this.scene.getEngine();
        this.onAttachSound = engine.getAudio().newSound("ballAttach.wav");
    }

    @Override
    public void update(double deltaTime) {
        if (!this.fallingBubbles.isEmpty()) {
            // Para tener la posibilidad de eliminar elementos mientras se recorre,
            // realizamos la actualizacion con iteradores
            Iterator<Pair<Vector, Integer>> iterator = this.fallingBubbles.iterator();
            while (iterator.hasNext()) {
                Pair<Vector, Integer> bubble = iterator.next();
                bubble.getFirst().y += this.fallingSpeed * (float) deltaTime;

                // Si la posición de la bola ya se salió de la pantalla, eliminarla
                if (bubble.getFirst().y + (float) this.r > this.scene.getWorldHeight()) {
                    iterator.remove();
                }
            }
        }
    }


    // Convierte posiciones i,j de la matriz a coordenadas de mundo
    private Vector gridToWorldPosition(int i, int j) {
        Vector pos = new Vector(0, 0);
        pos.x = this.offsetX + ((i % 2 == 0) ? 0 : this.r) + this.r + this.r * 2 * j;
        pos.y = this.offsetY + this.r + (this.r * 2 * i) - this.bubbleOffset * i;

        pos.x = Math.round(pos.x);
        pos.y = Math.round(pos.y);
        return pos;
    }

    // Convierte coordenadas de mundo a posiciones i,j de la matriz
    private Vector worldToGridPosition(Vector pos) {
        float y = (pos.y - this.offsetY) / (this.r * 2);
        y += (this.bubbleOffset * y) / (this.r * 2);

        float x = (pos.x - this.offsetX - ((y % 2 == 0) ? 0 : this.r)) / (this.r * 2);

        return new Vector(x, y);
    }

    public boolean checkCollision(Vector pos, Vector dir, int color) {
        boolean hasCollided = false;
        Vector rowCol = worldToGridPosition(pos);
        System.out.println(rowCol.x + " " + rowCol.y);

        // Se redondea x por si la bola esta mas hacia un lado o hacia el otro,
        // pero y no se redondea porque se se necesita la posicion de la parte superior
        int i = (int) rowCol.y;
        int j = (i % 2 == 0) ? Math.round(rowCol.x) : (int) rowCol.x;

        // Se comprueban las casillas adyacentes y si hay alguna ocupada, se marca que ha colisionado
        hasCollided |= occupied(pos, i - 1, (i % 2 == 0) ? j - 1 : j + 1);
        hasCollided |= occupied(pos, i - 1, j);
        hasCollided |= occupied(pos,  i, j - 1);
        hasCollided |= occupied(pos, i, j + 1);

        // DEBUG DE LAS CELDAS
        currI = i;
        currJ = j;

        // Si no ha colisionado tras comprobar las casillas contiguas, habra
        // colisionado con la pared superior si llega a la primera fila
        if (!hasCollided) {
            hasCollided = i <= 0;
        }

        // Si ha colisionado
        if (hasCollided && i >= 0 && j >= 0 && i < this.rows && j < ((i % 2 == 0) ? this.cols : this.cols - 1)) {
            // DEBUG DE LAS CELDAS
            currI = -1;
            currJ = -1;

            this.bubbles[i][j] = color;
            // manageCollision
            // hay que hacer que tanto manageCollision como manageFall
            // vayan quitando las burbujas que borren de colorCount

            // gestonar tambien que pasa si despues de explotar las burbujas alguna queda por debajo del limite (i == cols)
        }

        return hasCollided;
    }

    private boolean occupied(Vector pos, int i, int j) {
        if (i >= 0 && j >= 0 && i < this.rows && j < ((i % 2 == 0) ? this.cols : this.cols - 1)) {
            if (this.bubbles[i][j] >= 0) {
                Vector laterals = gridToWorldPosition(i, j);
                return (laterals.distance(pos) < this.r * 2);
            }
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
        boolean[][] visited = new boolean[this.rows][this.cols];
        for (Vector v : pBubblesToFall) {
            // Se comprueba que sea distinto de nulo porque puede dos bolas pueden ser del
            // mismo conjunto y se puede haber eliminado ya la coordenada
            if (this.bubbles[(int) v.x][(int) v.y] != -1) {
                List<Vector> bubbles = new ArrayList<>();
                // Si no hay ninguna bola del conjunto que toque el techo, se eliminan
                if (!dfs(visited, (int) v.x, (int) v.y, bubbles)) {
                    for (Vector w : bubbles) {
                        // Se guardan en una lista de bolas en movimiento simulando la caida
                        this.fallingBubbles.add(new Pair<>(new Vector((int) w.x, (int) w.y), this.bubbles[(int) w.x][(int) w.y]));
                        // Se quitan del grid
                        this.bubbles[(int) w.x][(int) w.y] = -1;
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
            int ni = i + (int) dir.x, nj = j + (int) dir.y; // Cambiar Vector para que sea con template
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
            int ni = i + (int) dir.x, nj = j + (int) dir.y; // Cambiar Vector para que sea con template
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
        return i >= 0 && i < this.rows && j >= 0 && j < this.cols;
    }

    private boolean isRoof(int i, int j) {
        return i == 0 && j >= 0 && j < this.cols;
    }

    private void playAttachSound() {
        if (onAttachSound != null) {
            onAttachSound.play();
        }
    }
}
