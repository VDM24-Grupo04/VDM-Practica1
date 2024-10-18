package com.grupo04.gamelogic.scenes;

import com.grupo04.engine.Color;
import com.grupo04.engine.Engine;
import com.grupo04.engine.Scene;
import com.grupo04.engine.Sound;
import com.grupo04.gamelogic.gameobjects.TestGameObject3;

public class TestScene3 extends Scene {
    public TestScene3(Engine engine) { super(engine, 400, 600, new Color(0, 0, 255)); }

    @Override
    public void init() {
        TestGameObject3 testGameObject3 = new TestGameObject3();
        addGameObject(testGameObject3);
        testGameObject3.init();
    }
}
