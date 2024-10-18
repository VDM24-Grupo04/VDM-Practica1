package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.Engine;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.Sound;
import com.grupo04.engine.TouchEvent;

import java.util.List;

public class TestGameObject3 extends GameObject {
    private Sound sound;

    public TestGameObject3() {
        this.sound = null;
    }

    @Override
    public void init() {
        Engine engine = scene.getEngine();
        this.sound = engine.getAudio().newSound("ballAttach.wav", 0);
    }

    @Override
    public void handleInput(List<TouchEvent> touchEvents) {
        super.handleInput(touchEvents);

        for (TouchEvent event : touchEvents) {
            if (event.getType() == TouchEvent.TouchEventType.PRESS) {
                System.out.println("Pressing...");
                this.sound.play();
            } else if (event.getType() == TouchEvent.TouchEventType.DRAG) {
                System.out.println("Dragging");
            } else if (event.getType() == TouchEvent.TouchEventType.RELEASE) {
                System.out.println("Releasing...");
                this.sound.stop();
            }
        }
    }

}
