package com.grupo04.engine.interfaces;

import com.grupo04.engine.Vector;

public interface ITouchEvent  {
    public enum TouchEventType { NONE, PRESS, RELEASE, DRAG, MOTION }

    public TouchEventType getType();
    public Vector getPos();
    public void setPos(Vector newPos);
}
