package com.grupo04.desktopgame;

import com.grupo04.desktopengine.DesktopEngine;
import com.grupo04.gamelogic.scenes.TestScene;

import javax.swing.JFrame;

public class DesktopGame {
    public static void main(String[] args) {
        // Creacion de la ventana desktop
        JFrame window = new JFrame("Puzzle Bobble");
        window.setSize(480, 720);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setIgnoreRepaint(true);
        window.setVisible(true);

        // Creacion del motor
        DesktopEngine engine = new DesktopEngine(window);

        // Creacion de la escena
        TestScene testScene = new TestScene(engine);
        engine.pushScene(testScene);

        engine.onResume();
    }
}