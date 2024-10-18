package com.grupo04.desktopgame;

import com.grupo04.desktopengine.DesktopEngine;
import com.grupo04.gamelogic.scenes.GameScene;
import com.grupo04.gamelogic.scenes.TestScene;
import com.grupo04.gamelogic.scenes.TestScene2;
import com.grupo04.gamelogic.scenes.TestScene3;

import javax.swing.JFrame;

public class DesktopGame {
    public static void main(String[] args) {
        // Creacion de la ventana desktop
        JFrame window = new JFrame("Puzzle Bobble");
        window.setSize(400, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setIgnoreRepaint(true);
        window.setVisible(true);

        // Creacion del motor
        DesktopEngine engine = new DesktopEngine(window);

        // Creacion de la escena
//        TestScene testScene = new TestScene(engine);
//        TestScene2 testScene = new TestScene2(engine);
//        TestScene3 testScene = new TestScene3(engine);
        GameScene testScene = new GameScene(engine);
        engine.pushScene(testScene);

        engine.onResume();
    }
}