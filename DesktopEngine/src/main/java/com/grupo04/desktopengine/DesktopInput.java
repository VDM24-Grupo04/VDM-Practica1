package com.grupo04.desktopengine;

import com.grupo04.engine.Engine;
import com.grupo04.engine.Input;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class DesktopInput extends Input {
    DesktopInput(JFrame window, Engine engine) {
        super();

        // Se anade al JFrame un listener de raton
        window.addMouseListener(new MouseAdapter() {
            // Se sobrescribe el evento de pulsar el raton
            @Override
            public void mousePressed(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                //Vector pos = engine.worldToScreenPoint(new Vector(e.getX(), e.getY()));
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.PRESS, pos, 0);
                touchEvents.add(evt);
            }

            // Se sobrescribe el evento de soltar el raton
            @Override
            public void mouseReleased(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                //Vector pos = engine.worldToScreenPoint(new Vector(e.getX(), e.getY()));
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.RELEASE, pos, 0);
                touchEvents.add(evt);
            }
        });

        // Se anade al JFrame un listener de movimiento del raton
        window.addMouseMotionListener(new MouseAdapter() {
            // Se sobrescribe el evento de arrastrar el raton
            @Override
            public void mouseDragged(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                //Vector pos = engine.worldToScreenPoint(new Vector(e.getX(), e.getY()));
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.DRAG, pos, 0);
                touchEvents.add(evt);
            }

            // Se sobrescribe el evento de que ha entrado el raton en el componente, en este caso en la ventana
            @Override
            public void mouseEntered(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                //Vector pos = engine.worldToScreenPoint(new Vector(e.getX(), e.getY()));
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.MOTION, pos, 0);
                touchEvents.add(evt);
            }

            // Se sobrescribe el evento de que se ha movido el raton
            @Override
            public void mouseMoved(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                //Vector pos = engine.worldToScreenPoint(new Vector(e.getX(), e.getY()));
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.MOTION, pos, 0);
                touchEvents.add(evt);
            }
        });

        // Al cerrar la ventana se realiza una salida adecuada del sistema
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
}
