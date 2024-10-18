package com.grupo04.gamelogic;
import com.grupo04.engine.Color;

import java.util.Random;

public enum BallColors {
    RED, GREEN, BLUE, YELLOW;

    private static final Color[] colors = {
            new Color(255, 77, 104),
            new Color(2, 247, 68),
            new Color(127, 155, 240),
            new Color(251, 230, 70)
    };

    private static final Random randomNumbers = new Random();

    public static Color getRandomColor() {
        return colors[randomNumbers.nextInt(colors.length)];
    }
}