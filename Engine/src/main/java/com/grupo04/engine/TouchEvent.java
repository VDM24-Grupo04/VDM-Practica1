package com.grupo04.engine;

public class TouchEvent {
    public enum TouchEventType { NONE, PRESS, RELEASE, DRAG };
    private TouchEventType type = TouchEventType.NONE;
    private Vector pos;

    public TouchEvent(TouchEventType type, Vector pos) {
        this.type = type;
        this.pos = pos;
    }
    public TouchEventType getType() { return type; }
    public Vector getPos() { return pos; }
}
