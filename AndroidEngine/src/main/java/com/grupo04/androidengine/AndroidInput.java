package com.grupo04.androidengine;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.grupo04.engine.Engine;
import com.grupo04.engine.Input;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.Vector;

public class AndroidInput extends Input {
    SurfaceView window;
    AndroidInput(SurfaceView window, Engine engine) {
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
                int i = event.getActionIndex();
                Vector pos = new Vector(x, y);
                //Vector pos = engine.worldToScreenPoint(new Vector(x, y));

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.PRESS, pos, i));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.RELEASE, pos, i));
                        break;
                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_HOVER_MOVE:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.DRAG, pos, i));
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
