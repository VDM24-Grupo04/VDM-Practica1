package com.grupo04.desktopengine;

import com.grupo04.engine.Engine;
import com.grupo04.engine.Input;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.utilities.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class DesktopInput extends Input {
    DesktopInput(JFrame window, Engine engine) {
        super();

        // Se añade al JFrame un listener de raton
        window.addMouseListener(new MouseAdapter() {
            // Se sobrescribe el evento de pulsar el raton
            @Override
            public void mousePressed(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.PRESS, pos);
                addEvent(evt);
            }

            // Se sobrescribe el evento de soltar el raton
            @Override
            public void mouseReleased(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.RELEASE, pos);
                addEvent(evt);
            }
        });

        // Se anade al JFrame un listener de movimiento del raton
        window.addMouseMotionListener(new MouseAdapter() {
            // Se sobrescribe el evento de arrastrar el raton
            @Override
            public void mouseDragged(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.DRAG, pos);
                addEvent(evt);
            }

            // Se sobrescribe el evento de que ha entrado el raton en el componente, en este caso en la ventana
            @Override
            public void mouseEntered(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.MOTION, pos);
                addEvent(evt);
            }

            // Se sobrescribe el evento de que se ha movido el raton
            @Override
            public void mouseMoved(MouseEvent e) {
                Vector pos = new Vector(e.getX(), e.getY());
                TouchEvent evt = new TouchEvent(TouchEvent.TouchEventType.MOTION, pos);
                addEvent(evt);
            }
        });

        // Al cerrar la ventana se realiza una salida adecuada del sistema
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        // Se anade al JFrame un listener de eventos de teclado
        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) { }

            // Pulsar tecla
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                // Si se pulsa el escape
                if (keyEvent.getKeyCode() == 27) {
                    System.exit(0);
                }
            }

            // Soltar tecla
            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        // Se anade al JFrame un listener de foco de la ventana
        window.addWindowFocusListener(new WindowFocusListener() {
            // Si se pierde el foco, se pausa el hilo que ejecuta el juego
            @Override
            public void windowLostFocus(WindowEvent e) {
                engine.onPause();
            }
            // Si se recupera el foco, se reanuda el hilo que ejecuta el juego
            @Override
            public void windowGainedFocus(WindowEvent e) {
                engine.onResume();
            }
        });
    }
}
