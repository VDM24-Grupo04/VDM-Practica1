package com.grupo04.engine;

import java.util.ArrayList;
import java.util.List;

public class Input {
    protected List<TouchEvent> touchEvents;             // Todos los TouchEvents que se reciben en el tick
    protected List<TouchEvent> sceneTouchEvents;        // TouchEvents que se van a mandar a la escena

    // Se usan 2 listas porque la deteccion del input y la ejecucion del juego se hacen en hilos
    // distintos, por lo que se puede estar recibiendo input a la vez que se esta gestionando,

    public Input() {
        touchEvents = new ArrayList<TouchEvent>();
        sceneTouchEvents = new ArrayList<TouchEvent>();
    }

    // Obtiene los TouchEvents que le va a mandar a la escena
    public List<TouchEvent> getTouchEvents() {
        // Se reinicia la lista de elementos que enviar a la escena
        sceneTouchEvents.clear();

        // Si hay eventos de input
        if (!touchEvents.isEmpty()) {
            // addAll clona en aux los elementos de touchEvents
            sceneTouchEvents.addAll(touchEvents);
        }

        // Devuelve los TouchEvents de la escena
        return sceneTouchEvents;
    }

    // Tienen que ser synchronized porque se puede estar modificando la lista
    // en el hilo que recoge el input y a la vez en el bucle principal. Con synchronized
    // se asegura que la lista no pueda ser modificada por 2 hilos al mismo tiempo
    public synchronized void clearEvents() {
        touchEvents.clear();
    }

    public synchronized void addEvent(TouchEvent e) {
        touchEvents.add(e);
    }


}
