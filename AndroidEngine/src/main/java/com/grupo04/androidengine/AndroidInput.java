package com.grupo04.androidengine;

import android.annotation.SuppressLint;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.grupo04.engine.Engine;
import com.grupo04.engine.Input;
import com.grupo04.engine.TouchEvent;
import com.grupo04.engine.utilities.Vector;

// Al sobrescribir el metodo setOnTouchListener, se pide sobrescribir tambien el metodo performClick()
// ya que en el caso de que no se pueda usar el input convencion, hay servicios de accesibilidad que invocan
// a dicho metodo, por lo tanto, la logica deberia estar en el. En caso de no sobrescribirlo, se produce un warning
// Sin embargo, como no se van a ser necesarios dichos servicios, se utiliza esta instruccion para eliminar el warning
@SuppressLint("ClickableViewAccessibility")

public class AndroidInput extends Input {
    AndroidInput(SurfaceView window, Engine engine) {
        super();

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
                    // Se produce este evento cuando el primer dedo toca la pantalla
                    case MotionEvent.ACTION_DOWN:
                        // Se produce este evento cuando los siguientes dedos despues del primero
                        // tocan la pantalla
                        // case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.PRESS, pos, i));
                        break;
                    // Se produce este evento cuando un dedo deja de tocar la pantalla,
                    // pero todavia quedan dedos tocandola
                    case MotionEvent.ACTION_UP:
                        // Se produce este evento cuando el ultimo dedo que habia en la pantalla
                        // deja de tocarla
                        // case MotionEvent.ACTION_POINTER_UP:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.RELEASE, pos, i));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // case MotionEvent.ACTION_HOVER_MOVE:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.DRAG, pos, i));
                        break;
                }

                // Se devuelve true porque ya se ha gestionado el elemento.
                // Si el elemento tuviera un padre y se quisiera que el padre
                // tambien gestionara el elemento, habria que devolver false
                return true;
            }
        });

        // A diferencia de los touch events, los generic motion events describen eventos de
        // desplazamiento producidos en Android que no implican como tal tocar la pantall, como
        // el desplazamiento de un joystick, de un raton o de un stylus; el movimiento de la
        // rueda del raton...
        window.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent event) {
                int action = event.getActionMasked();
                float x = event.getX();
                float y = event.getY();
                int i = event.getActionIndex();
                Vector pos = new Vector(x, y);

                switch (action) {
                    // El puntero ha entrado en el componente, en este caso en la ventana
                    case MotionEvent.ACTION_HOVER_ENTER:
                        // El puntero se ha movido
                    case MotionEvent.ACTION_HOVER_MOVE:
                        touchEvents.add(new TouchEvent(TouchEvent.TouchEventType.MOTION, pos, i));
                }
                return true;
            }
        });
    }
}
