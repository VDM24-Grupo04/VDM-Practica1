package com.grupo04.engine;

public class TouchEvent {
    public enum TouchEventType { NONE, PRESS, RELEASE, DRAG };
    private TouchEventType type = TouchEventType.NONE;
    private Vector pos;
    int finger;

    public TouchEvent(TouchEventType type, Vector pos, int finger) {
        this.type = type;
        this.pos = pos;
        this.finger = finger;
    }
    public TouchEventType getType() { return type; }
    public Vector getPos() { return pos; }
}
