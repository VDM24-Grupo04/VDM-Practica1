package com.grupo04.engine;

import com.grupo04.engine.interfaces.ITouchEvent;
import com.grupo04.engine.utilities.Vector;

public class TouchEvent implements ITouchEvent {
    private final TouchEventType type;  // Tipo del evento producido
    private Vector pos;                 // Posicion (sin escalar) en la que se ha producido el evento
    // private int finger;

    public TouchEvent(TouchEventType type, Vector pos, int finger) {
        this.type = type;
        this.pos = pos;
        // this.finger = finger;
    }

    public TouchEventType getType() { return type; }
    public Vector getPos() { return pos; }
    public void setPos(Vector newPos) { this.pos = newPos; }
}
