package com.grupo04.androidengine;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.grupo04.engine.Input;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;

public class AndroidInput extends Input {
    SurfaceView window;
    AndroidInput(SurfaceView window) {
        super();
        this.window = window;

        // Se anade al SurfaceView un listener de tocar la pantalla
        window.setOnTouchListener(new View.OnTouchListener() {
            // Se sobreescribe el onTouch, que se llama cuando
            // se realiza una acción calificada como evento táctil,
            // como, presionar, soltar o cualquier gesto de movimiento
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                float x = event.getX();
                float y = event.getY();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.PRESS, new Vector(x, y)));
                        break;
                    case MotionEvent.ACTION_UP:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.RELEASE, new Vector(x, y)));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.DRAG, new Vector(x, y)));
                        break;
                }

                // Se devuelve true porque ya se ha gestionado el elemento.
                // Si el elemento tuviera un padre y se quisiera que el padre
                // tambien gestionara el elemento, habria que devolver false
                return true;
            }
        });
    }
}
