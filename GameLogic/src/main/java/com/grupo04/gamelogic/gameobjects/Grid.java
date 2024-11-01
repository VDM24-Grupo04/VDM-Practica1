package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Graphics;
import com.grupo04.engine.Pair;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Vector;
import com.grupo04.engine.interfaces.IAudio;
import com.grupo04.engine.interfaces.ISound;
import com.grupo04.gamelogic.BallColors;
import com.grupo04.gamelogic.scenes.GameOverScene;
import com.grupo04.gamelogic.scenes.VictoryScene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

public class Grid extends GameObject {
    // Linea del final del nivel
    final int lineThickness = 1;
    final Color lineColor = new Color(0, 0, 0, 255);

    // Matriz de burbujas con los colores de las mismas
    private int[][] bubbles;
    // Numero de filas y columnas
    private int rows, cols;

    // Numero total de burbujas
    // Cuando se ha llegado a 0, se gana la partida
    private int totalBubbles;
    // Indica cuantas bolas hay de cada color en el mapa
    // Se usa para saber de que color puede ser la bola que se lanza
    private int[] colorCount;
    // 5 puntos por cada burbuja en un grupo de 3 y 10 puntos por cada burbuja en un grupo de 10 o mas

    // Radio de las burbujas
    private int r;
    // Radio de los hexagonos de la cuadricula
    private float hexagonRadius;
    // Offsets para las paredes y la cabecera
    private int offsetX, offsetY;
    // Offsets para que no haya mucho espacio entre las bolas en y
    private int bubbleOffset;

    // Linea del final del nivel
    private Vector lineInit, lineEnd;

    // Posibles posiciones adyacentes a cada bola
    private List<Pair<Integer, Integer>> unevenAdjacentCells;
    private List<Pair<Integer, Integer>> evenAdjacentCells;

    // Bolas necesarias que esten juntas para que se produzca la explosion
    private int bubblesToExplode;
    // Puntuacion que se otorga si el grupo de bolas que se forma es del tamano bubblesToExplode + 1
    private int greatScore;
    // Puntuacion que se otorga si el grupo de bolas que se forma es del tamano bubblesToExplode
    private int smallScore;
    // Puntuacion actual
    private int score;

    private float fallingSpeed;
    private List<Pair<Vector, Integer>> fallingBubbles;
    private boolean won;

    private Engine engine;
    private IAudio audio;
    private ISound attachSound;
    private ISound explosionSound;

    // DEBUG DE LAS CELDAS
    private int currI = -1, currJ = -1;

    public Grid(int width, int wallThickness, int headerOffset, int r, int bubbleOffset, int rows, int cols, int initRows,
                int bubblesToExplode, int greatScore, int smallScore, float fallingSpeed) {
        super();
        this.cols = cols;
        this.rows = rows;
        this.bubbles = new int[this.rows][this.cols];
        for (int[] row : this.bubbles) {
            Arrays.fill(row, -1);
        }

        this.r = r;
        this.hexagonRadius = (float) Math.ceil((this.r / (Math.sqrt(3) / 2.0f)));
        this.offsetX = wallThickness;
        this.offsetY = wallThickness + headerOffset;
        this.bubbleOffset = bubbleOffset;

        // Se generan initRows filas iniciales
        this.totalBubbles = 0;
        this.colorCount = new int[BallColors.getColorCount()];
        BallColors.reset();
        for (int i = 0; i < initRows; i++) {
            // En las filas impares hay una bola menos
            int bPerRow = (i % 2 == 0) ? this.cols : (this.cols - 1);
            this.totalBubbles += bPerRow;

            // Se generan las burbujas de la fila
            for (int j = 0; j < bPerRow; ++j) {
                int color = BallColors.generateRandomColor();
                BallColors.addColor(color);
                this.bubbles[i][j] = color;
                this.colorCount[color]++;
            }
        }

        int lineY = (int) (this.r * 2 * (this.rows - 1)) - this.bubbleOffset * (this.rows - 2);
        this.lineInit = new Vector(this.offsetX, this.offsetY + lineY);
        this.lineEnd = new Vector(width - this.offsetX, this.offsetY + lineY);

        // Creamos la lista de celdas adyacentes a cada posicion
        this.unevenAdjacentCells = new ArrayList<>();
        this.unevenAdjacentCells.add(new Pair<Integer, Integer>(-1, 0));    // Arriba izquierda
        this.unevenAdjacentCells.add(new Pair<Integer, Integer>(-1, 1));    // Arriba derecha
        this.unevenAdjacentCells.add(new Pair<Integer, Integer>(0, -1));    // Izquierda
        this.unevenAdjacentCells.add(new Pair<Integer, Integer>(0, 1));     // Derecha
        this.unevenAdjacentCells.add(new Pair<Integer, Integer>(1, 0));     // Abajo izquierda
        this.unevenAdjacentCells.add(new Pair<Integer, Integer>(1, 1));     // Abajo derecha

        this.evenAdjacentCells = new ArrayList<>();
        this.evenAdjacentCells.add(new Pair<Integer, Integer>(-1, -1));     // Arriba izquierda
        this.evenAdjacentCells.add(new Pair<Integer, Integer>(-1, 0));      // Arriba derecha
        this.evenAdjacentCells.add(new Pair<Integer, Integer>(0, -1));      // Izquierda
        this.evenAdjacentCells.add(new Pair<Integer, Integer>(0, 1));       // Derecha
        this.evenAdjacentCells.add(new Pair<Integer, Integer>(1, -1));      // Abajo izquierda
        this.evenAdjacentCells.add(new Pair<Integer, Integer>(1, 0));       // Abajo derecha

        this.bubblesToExplode = bubblesToExplode;
        this.score = 0;
        this.greatScore = greatScore;
        this.smallScore = smallScore;

        this.fallingSpeed = fallingSpeed;
        this.fallingBubbles = new ArrayList<>();
        this.won = false;

        this.engine = null;
        this.audio = null;
        this.attachSound = null;
        this.explosionSound = null;
    }

    public Grid(int width, int wallThickness, int headerOffset, int r, int bubbleOffset, int rows, int cols, int initRows,
                int bubblesToExplode, int greatScore, int smallScore) {
        this(width, wallThickness, headerOffset, r, bubbleOffset, rows, cols, initRows,
                bubblesToExplode, greatScore, smallScore, 300.0f);
    }

    @Override
    public void init() {
        this.engine = this.scene.getEngine();
        this.audio = this.engine.getAudio();
        this.attachSound = audio.newSound("ballAttach.wav");
        this.explosionSound = audio.newSound("ballExplosion.wav");
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        // Recorre la matriz y pinta las bolas si el color en la posicion i,j de la matriz es >= 0
        for (int i = 0; i < this.rows; i++) {
            int bPerRow = (i % 2 == 0) ? this.cols : (this.cols - 1);
            for (int j = 0; j < bPerRow; ++j) {
                if (this.bubbles[i][j] >= 0) {
                    Vector pos = gridToWorldPosition(i, j);
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

        debugCollisions(graphics);


        // Pinta la linea del limite inferior
        graphics.setColor(lineColor);
        graphics.drawLine(lineInit, lineEnd, lineThickness);

        // Recorre las bolas caidas y las pinta
        if (!this.fallingBubbles.isEmpty()) {
            for (Pair<Vector, Integer> bubble : this.fallingBubbles) {
                graphics.setColor(BallColors.getColor(bubble.getSecond()));
                Vector bPos = bubble.getFirst();
                graphics.fillCircle(bPos, this.r);
            }
        }
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
        else if (this.won) {
            this.won = false;

            // Se hace un fade in y cuando acaba la animacion se cambia a la escena de victoria
            this.scene.setFade(Scene.FADE.IN, 0.25);
            this.scene.setFadeCallback(() -> {
                // Se paran los sonidos por si acaso
                this.audio.stopSound(this.attachSound);
                this.audio.stopSound(this.explosionSound);
                this.engine.changeScene(new VictoryScene(this.engine, this.score));
            });
        }
    }

    private void debugCollisions(Graphics graphics) {
        if (this.currI >= 0 && this.currJ >= 0) {
            Vector pos = gridToWorldPosition(this.currI, this.currJ);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(0));
            graphics.drawHexagon(pos, this.hexagonRadius, 90, this.lineThickness * 2);

            pos = gridToWorldPosition(this.currI, this.currJ - 1);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, this.hexagonRadius, 90, this.lineThickness * 2);

            pos = gridToWorldPosition(this.currI, this.currJ + 1);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, this.hexagonRadius, 90, this.lineThickness * 2);

            pos = gridToWorldPosition(this.currI - 1, this.currJ);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, this.hexagonRadius, 90, this.lineThickness * 2);

            pos = gridToWorldPosition(this.currI - 1, (this.currI % 2 == 0) ? this.currJ - 1 : this.currJ + 1);
            pos.x += 0.5f;
            graphics.setColor(BallColors.getColor(1));
            graphics.drawHexagon(pos, this.hexagonRadius, 90, this.lineThickness * 2);
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
    private Pair<Integer, Integer> worldToGridPosition(Vector pos) {
        float y = (pos.y - this.offsetY) / (this.r * 2);
        y += (this.bubbleOffset * y) / (this.r * 2);

        float x = (pos.x - this.offsetX - ((y % 2 == 0) ? 0 : this.r)) / (this.r * 2);

        // Se redondea x por si la bola esta mas hacia un lado o hacia el otro,
        // pero y no se redondea porque se se necesita la posicion de la parte superior
        int i = (int) y;
        int j = (i % 2 == 0) ? Math.round(x) : (int) x;

        return new Pair<>(i, j);
    }

    public boolean checkCollision(Vector pos, Vector dir, int color) {
        boolean hasCollided = false;
        Pair<Integer, Integer> rowCol = worldToGridPosition(pos);

        int i = rowCol.getFirst();
        int j = rowCol.getSecond();

        // Se comprueban las casillas adyacentes y si hay alguna ocupada, se marca que ha colisionado
        hasCollided |= cellOccupied(pos, i - 1, (i % 2 == 0) ? j - 1 : j + 1);
        hasCollided |= cellOccupied(pos, i - 1, j);
        hasCollided |= cellOccupied(pos, i, j - 1);
        hasCollided |= cellOccupied(pos, i, j + 1);

        // DEBUG DE LAS CELDAS
        this.currI = i;
        this.currJ = j;

        // Si no ha colisionado tras comprobar las casillas contiguas, habra
        // colisionado con la pared superior si llega a la primera fila
        if (!hasCollided) {
            hasCollided = i <= 0;
        }

        // Si ha colisionado
        if (hasCollided && i >= 0 && j >= 0 && i < this.rows && j < ((i % 2 == 0) ? this.cols : this.cols - 1)) {
            // DEBUG DE LAS CELDAS
            this.currI = -1;
            this.currJ = -1;

            this.totalBubbles += 1;
            ++this.colorCount[color];
            this.bubbles[i][j] = color;

            if (manageCollision(i, j)) {
                this.won = true;
            }
            // Condicion de derrota
            else if (this.bubbles[i][j] != -1 && gridToWorldPosition(i, j).y + this.r > this.lineEnd.y) {
                // Se hace un fade in y cuando acaba la animacion se cambia a la escena de game over
                this.scene.setFade(Scene.FADE.IN, 0.25);
                this.scene.setFadeCallback(() -> {
                    this.engine.changeScene(new GameOverScene(this.engine));
                });
            }
        }

        return hasCollided;
    }

    private boolean cellWithinGrid(int i, int j) {
        return i >= 0 && j >= 0 && i < this.rows && j < ((i % 2 == 0) ? this.cols : this.cols - 1);
    }

    private boolean cellOccupied(Vector pos, int i, int j) {
        if (cellWithinGrid(i, j)) {
            if (this.bubbles[i][j] >= 0) {
                Vector laterals = gridToWorldPosition(i, j);
                return (laterals.distance(pos) < this.r * 2);
            }
        }
        return false;
    }

    private boolean roofCell(int i, int j) {
        return i == 0 && cellWithinGrid(i, j);
    }

    private boolean manageCollision(int i, int j) {
        // El valor por defecto de un booleano es false
        boolean[][] visited = new boolean[this.rows][this.cols];
        int color = this.bubbles[i][j];
        List<Pair<Integer, Integer>> bubblesToErase = new ArrayList<>();
        List<Pair<Integer, Integer>> bubblesToFall = new ArrayList<>();

        dfs(visited, i, j, color, bubblesToErase, bubblesToFall);

        // Numero de bolas del grupo
        int bubblesToEraseSize = bubblesToErase.size();
        // Si se supera el limite establecido, se eliminan
        if (bubblesToEraseSize >= this.bubblesToExplode) {
            this.audio.playSound(this.explosionSound);

            // Si el grupo es mayor que el limite establecido, la puntuacion es mayor
            if (bubblesToEraseSize >= this.bubblesToExplode + 1) {
                this.score += bubblesToEraseSize * this.greatScore;
            } else {
                this.score += bubblesToEraseSize * this.smallScore;
            }
            // Se actualiza el numero de bolas totales
            this.totalBubbles -= bubblesToEraseSize;
            // Se actualiza el numero de bolas que hay de cada color
            this.colorCount[color] -= bubblesToEraseSize;
            if (this.colorCount[color] <= 0) {
                BallColors.removeColor(color);
            }
            // Se actualiza el mapa
            for (Pair<Integer, Integer> bubble : bubblesToErase) {
                int bubbleI = bubble.getFirst();
                int bubbleJ = bubble.getSecond();
                this.bubbles[bubbleI][bubbleJ] = -1;
            }
            // Si no quedan bolas, se ha ganado
            if (this.totalBubbles <= 0) {
                return true;
            }
            // Si siguen quedando bolas, se comprueba si hay bolas que se pueden caer
            return manageFall(bubblesToFall);
        } else {
            this.audio.playSound(this.attachSound);
        }
        return false;
    }

    // A partir de la lista de coordenadas adyacentes, obtenida tras la eliminacion de la bolas del mismo color
    // bolas, se sacan los diferentes conjuntos con dfs y se comprueba si en cada conjunto hay al menos una bola
    // pegada al techo. En ese caso, todas las bolas de ese conjunto no se caen
    private boolean manageFall(List<Pair<Integer, Integer>> bubblesToFall) {
        int numBubblesToFall = 0;
        for (Pair<Integer, Integer> v : bubblesToFall) {
            int vX = v.getFirst();
            int vY = v.getSecond();
            // Se comprueba que sea distinto de nulo porque dos bolas pueden ser del
            // mismo conjunto y se puede haber eliminado ya la coordenada
            if (this.bubbles[vX][vY] >= 0) {
                List<Pair<Integer, Integer>> bubbles = new ArrayList<>();
                boolean[][] visited = new boolean[this.rows][this.cols];
                // Si no hay ninguna bola del conjunto que toque el techo, se eliminan
                if (!dfs(visited, vX, vY, bubbles) && !bubbles.isEmpty()) {
                    for (Pair<Integer, Integer> w : bubbles) {
                        // Se guardan en una lista de bolas en movimiento simulando la caida
                        int wX = w.getFirst();
                        int wY = w.getSecond();
                        Vector worldPos = gridToWorldPosition(wX, wY);
                        int color = this.bubbles[wX][wY];
                        if (--this.colorCount[color] <= 0) {
                            BallColors.removeColor(color);
                        }
                        this.fallingBubbles.add(new Pair<>(worldPos, color));
                        // Se quitan del grid
                        this.bubbles[wX][wY] = -1;
                    }
                    numBubblesToFall += bubbles.size();
                }
            }
        }
        this.totalBubbles -= numBubblesToFall;
        // Devolver indicando si todavia quedan bolas o no
        return this.totalBubbles <= 0;
    }

    private void dfs(boolean[][] visited, int i, int j, int color, List<Pair<Integer, Integer>> bubblesToErase,
                     List<Pair<Integer, Integer>> bubblesToFall) {
        visited[i][j] = true;

        int currBubbleCol = this.bubbles[i][j];
        // Si son del mismo color que la bola lanzada, se anade para eliminar
        Pair<Integer, Integer> bubblePos = new Pair<>(i, j);

        if (color == currBubbleCol) {
            bubblesToErase.add(bubblePos);
        }
        // Si no, se trata de una bola adyacente y se tendra en cuenta para la caida...
        else {
            bubblesToFall.add(bubblePos);
        }

        if (color == currBubbleCol) {
            List<Pair<Integer, Integer>> adjacentCells = (i % 2 == 0) ?
                    this.evenAdjacentCells : this.unevenAdjacentCells;
            for (Pair<Integer, Integer> dir : adjacentCells) {
                int ni = i + dir.getFirst();
                int nj = j + dir.getSecond();
                // Si es una posicion correcta dentro del mapa...
                if (cellWithinGrid(ni, nj)) {
                    // Si hay bola y no esta visitada...
                    if (this.bubbles[ni][nj] >= 0 && !visited[ni][nj]) {
                        dfs(visited, ni, nj, color, bubblesToErase, bubblesToFall);
                    }
                }
            }
        }
    }

    private boolean dfs(boolean[][] visited, int i, int j, List<Pair<Integer, Integer>> bubbles) {
        visited[i][j] = true;
        boolean isRoof = roofCell(i, j);
        if (isRoof) return true;
        bubbles.add(new Pair<>(i, j));
        List<Pair<Integer, Integer>> adjacentCells = (i % 2 == 0) ?
                this.evenAdjacentCells : this.unevenAdjacentCells;
        for (Pair<Integer, Integer> dir : adjacentCells) {
            int ni = i + dir.getFirst();
            int nj = j + dir.getSecond();
            // Si es una posicion correcta dentro del mapa...
            if (cellWithinGrid(ni, nj)) {
                if (this.bubbles[ni][nj] >= 0 && !visited[ni][nj]) {
                    // Si se devuelve true, es que el conjunto calculado tras la recursion esta pegado al techo
                    if (dfs(visited, ni, nj, bubbles))
                        return true;
                }
            }
        }
        return false;
    }
}
