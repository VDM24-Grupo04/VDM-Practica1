package com.grupo04.engine;

public class TouchEvent {
    public enum TouchEventType { NONE, PRESS, RELEASE, DRAG };
    private TouchEventType type = TouchEventType.NONE;
    private float x, y;
    public TouchEvent(TouchEventType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
