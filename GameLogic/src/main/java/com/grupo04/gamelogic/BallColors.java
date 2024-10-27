package com.grupo04.gamelogic;

import com.grupo04.engine.Color;

import java.util.HashMap;
import java.util.Random;

public class BallColors {
    private static final Color[] colors = {
            new Color(245, 75, 100),
            new Color(105, 245, 85),
            new Color(135, 150, 235),
            new Color(245, 225, 85)
    };
    private static final Random randomNumbers = new Random();
    private static final HashMap<Integer, Color> availableColors = new HashMap<Integer, Color>();

    public static int getColorCount() {
        return colors.length;
    }

    public static int getRandomColor() {
        return randomNumbers.nextInt(colors.length);
    }

    public static Color getColor(int i) {
        return colors[i];
    }

    public static void reset() {
        for (int i = 0; i < colors.length; i++) {
            availableColors.put(i, colors[i]);
        }
    }

    public static void removeColor(int i) {
        availableColors.remove(i);
    }
}