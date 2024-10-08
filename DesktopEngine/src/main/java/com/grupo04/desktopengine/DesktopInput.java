package com.grupo04.desktopengine;

import com.grupo04.engine.Input;
import com.grupo04.engine.TouchEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class DesktopInput extends Input {
    DesktopInput(JFrame window) {
        super();

        // Se anade al JFrame un listener de raton
        window.addMouseListener(new MouseAdapter() {

            // Se sobreescribe el evento de pulsar el raton
            @Override
            public void mousePressed(MouseEvent e) {
                touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.PRESS, e.getX(), e.getY()));
            }

            // Se sobreescribe el evento de soltar el raton
            @Override
            public void mouseReleased(MouseEvent e) {
                touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.RELEASE, e.getX(), e.getY()));
            }
        });

        // Se anade al JFrame un listener de movimiento del raton
        window.addMouseMotionListener(new MouseAdapter() {
            // Se sobreescribe el evento de arrastrar el raton
            @Override
            public void mouseDragged(MouseEvent e) {
                touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.DRAG, e.getX(), e.getY()));
            }
        });
    }
}
