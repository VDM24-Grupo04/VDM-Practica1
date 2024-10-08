package com.grupo04.engine;

import java.util.ArrayList;
import java.util.List;

public class Input {
    protected List<TouchEvent> touchEvents;

    public Input() {
        touchEvents = new ArrayList<TouchEvent>();
    }

    // Devuelve la lista de eventos recibidos desde la última invocación
    // Esto implica que la lista de eventos se limpia para que en la siguiente llamada
    // no se devuelvan tambien los eventos que se devuelven en la llamada anterior
    public List<TouchEvent> getTouchEvents() {
        // Crea una lista auxiliar
        List<TouchEvent> aux = new ArrayList<TouchEvent>();

        // Si hay eventos de input, se pasan todos los eventos a una
        // lista auxiliar y se limpia la lista de eventos
        if(!touchEvents.isEmpty()) {
            // addAll clona en aux los elementos de touchEvents
            aux.addAll(touchEvents);
            touchEvents.clear();
//            System.out.println(aux.size());
        }
        return aux;
    }
}
