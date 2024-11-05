package com.grupo04.engine.interfaces;

import com.grupo04.engine.GameObject;
import com.grupo04.engine.utilities.Callback;
import com.grupo04.engine.utilities.Color;

public interface IScene {
    enum Fade {NONE, IN, OUT}

    // Hacer que la escena comience con un fade. Se le puede pasar solo el tipo de fade,
    // solo el tipo y la duracion, o el tipo, la duracion y el color del fade.
    // ** fadeDuration tiene que ir en segundos
    void setFade(Fade fade);
    void setFade(Fade fade, double fadeDuration);
    void setFade(Fade fade, double fadeDuration, Color fadeColor);
    void setFade(Fade fade, Color fadeColor);
    void setFadeCallback(Callback onFadeEnd);

    void addGameObject(GameObject gameObject);
    void addGameObject(GameObject gameObject, String handler);

    GameObject getHandler(String handler);

    int getWorldWidth();
    int getWorldHeight();

    IEngine getEngine();
}
