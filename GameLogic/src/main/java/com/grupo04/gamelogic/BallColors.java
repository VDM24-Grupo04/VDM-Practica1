package com.grupo04.gamelogic;

import com.grupo04.engine.Color;

import java.util.HashMap;
import java.util.Random;

public class BallColors {
    private static final Color[] colors = {
            new Color(245, 75, 100),    // Rojo
            new Color(105, 245, 85),    // Verde
            new Color(135, 150, 235),   // Azul
            new Color(245, 225, 85)     // Amarillo
    };
    private static final Random randomNumbers = new Random();
    private static final HashMap<Integer, Color> availableColors = new HashMap<Integer, Color>();

    public static int getColorCount() {
        return colors.length;
    }

    // Obtiene un color aleatorio de entre los que hay actualmente en el mapa
    public static int getRandomColor() {
        // Si esta vacio (no hay mas bolas) devuelve "sin color"
        if (availableColors.isEmpty()) {
            return -1;
        }
        // Coge las claves disponibles del hashmap
        Integer[] keys = availableColors.keySet().toArray(new Integer[0]);
        return keys[randomNumbers.nextInt(keys.length)];
    }

    // Genera un color aleatorio de entre todos los colores posibles
    public static int generateRandomColor() {
        return randomNumbers.nextInt(colors.length);
    }

    public static void reset() { availableColors.clear(); }

    public static Color getColor(int i) { return colors[i]; }
    public static void removeColor(int i) {
        availableColors.remove(i);
    }
    public static void addColor(int i) {
        availableColors.put(i, colors[i]);
    }
}
