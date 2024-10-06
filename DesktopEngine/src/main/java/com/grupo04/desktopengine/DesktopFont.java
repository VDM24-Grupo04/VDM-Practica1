package com.grupo04.desktopengine;

import com.grupo04.engine.Font;

import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DesktopFont extends Font {
    private java.awt.Font derivedFont;

    public DesktopFont(String name, float size, boolean bold) {
        super(size, bold);
        try {
            FileInputStream stream = new FileInputStream("./assets/" + name);
            // El primer parametro especifica el tipo de la fuente (TTF)
            java.awt.Font baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, stream);
            if (this.isBold()) {
                // Se crea una nueva fuente partiendo de la original y aplicandole transformaciones
                this.derivedFont = baseFont.deriveFont(java.awt.Font.BOLD, size);
            } else {
                this.derivedFont = baseFont.deriveFont(size);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Fuente con nombre " + name + " no encontrada: " + ex.getMessage());
        } catch (FontFormatException ex) {
            System.err.println("Error en el formato de la fuente con nombre" + name + ": " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error en la fuente con nombre" + name + ": " + ex.getMessage());
        }
    }

    java.awt.Font getFont() {
        return derivedFont;
    }
}
