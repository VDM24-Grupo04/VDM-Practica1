package com.grupo04.gamelogic;
import com.grupo04.engine.Color;

import java.util.Random;

public enum BallColors {
    RED, GREEN, BLUE, YELLOW, NONE;

    private static final Color[] colors = {
            new Color(245, 75, 100),
            new Color(105, 245, 85),
            new Color(135, 150, 235),
            new Color(245, 225, 85)
    };

    private static final Random randomNumbers = new Random();

    public static Color getRandomColor() {
        return colors[randomNumbers.nextInt(colors.length)];
    }
}