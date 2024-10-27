package com.grupo04.desktopengine;

import com.grupo04.engine.Font;

import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DesktopFont extends Font {
    private java.awt.Font derivedFont;

    public DesktopFont(String name, float size, boolean bold, boolean italic) {
        super(size);
        try {
            FileInputStream stream = new FileInputStream("./assets/fonts/" + name);
            // El primer parametro especifica el tipo de la fuente (TTF)
            java.awt.Font baseFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, stream);
            if (bold && italic) {
                this.derivedFont = baseFont.deriveFont(java.awt.Font.BOLD | java.awt.Font.ITALIC, size);
            } else if (bold) {
                // Se crea una nueva fuente partiendo de la original y aplicandole transformaciones
                this.derivedFont = baseFont.deriveFont(java.awt.Font.BOLD, size);
            } else if (italic) {
                this.derivedFont = baseFont.deriveFont(java.awt.Font.ITALIC, size);
            } else {
                this.derivedFont = baseFont.deriveFont(size);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Font with name " + name + " not found: " + ex.getMessage());
        } catch (FontFormatException ex) {
            System.err.println("Error in the formate of the font with name " + name + ": " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Error in the font with name " + name + ": " + ex.getMessage());
        }
    }

    java.awt.Font getFont() {
        return derivedFont;
    }
}
