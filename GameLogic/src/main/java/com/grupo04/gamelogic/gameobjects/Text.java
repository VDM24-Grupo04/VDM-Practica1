package com.grupo04.gamelogic.gameobjects;

import com.grupo04.engine.interfaces.IEngine;
import com.grupo04.engine.interfaces.IFont;
import com.grupo04.engine.interfaces.IGraphics;
import com.grupo04.engine.utilities.Color;
import com.grupo04.engine.GameObject;
import com.grupo04.engine.utilities.Vector;

public class Text extends GameObject {
    private Vector pos;
    private String[] texts;
    private String fontName;
    private float size;
    private boolean bold;
    private boolean italic;
    private Color color;
    private IFont font;
    private float[] indentation;
    private float lineSpacing;

    private float firstTextWidth;
    private float[] textsWidths;
    private float[] textsHeights;
    private float firstTextHeight;
    private float fullHeight;

    private void initCommonParams(Vector pos, String fontName, float size, boolean bold, boolean italic,
                                  Color color) {
        this.pos = pos;
        this.fontName = fontName;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
        this.color = color;

        this.firstTextWidth = 0f;
        this.textsWidths = new float[texts.length];
        this.textsHeights = new float[texts.length];
        this.fullHeight = 0f;
    }

    // Texto de multiples lineas con indentacion (respecto a la posicion original del primer texto)
    public Text(Vector pos, String[] texts, String fontName, float size, boolean bold, boolean italic,
                Color color, float[] indentation, float lineSpacing) {
        super();
        this.texts = texts;
        this.indentation = indentation;
        this.lineSpacing = lineSpacing;

        initCommonParams(pos, fontName, size, bold, italic, color);
    }

    // Texto de multiples lineas sin indentacion
    public Text(Vector pos, String[] texts, String fontName, float size, boolean bold, boolean italic,
                Color color, float lineSpacing) {
        super();
        this.texts = texts;
        this.indentation = new float[0];
        this.lineSpacing = lineSpacing;

        initCommonParams(pos, fontName, size, bold, italic, color);
    }

    // Texto de una sola linea
    public Text(Vector pos, String text, String fontName, float size, boolean bold, boolean italic,
                Color color) {
        super();
        this.texts = new String[]{text};
        this.indentation = new float[0];
        this.lineSpacing = 0;

        initCommonParams(pos, fontName, size, bold, italic, color);
    }

    @Override
    public void init() {
        IEngine engine = scene.getEngine();
        IGraphics graphics = engine.getGraphics();
        this.font = graphics.newFont(fontName, size, bold, italic);

        // Se establece la fuente para poder calcular los tamanos de los textos
        graphics.setFont(font);
        // Guardar en arrays ancho y alto de los textos para no tener que calcularlos todo el rato
        for (int i = 0; i < texts.length; ++i) {
            String text = texts[i];
            this.textsWidths[i] = graphics.getTextWidth(text);
            this.textsHeights[i] = graphics.getTextHeight(text);

            this.fullHeight += this.textsHeights[i];
            if (i > 0) {
                this.fullHeight += this.lineSpacing;
            } else {
                this.firstTextWidth = this.textsWidths[i];
                this.firstTextHeight = this.textsHeights[i];
            }
        }
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setColor(color);
        graphics.setFont(font);
        Vector textPos = new Vector();
        textPos.y = pos.y + firstTextHeight / 2f - fullHeight / 2f;
        for (int i = 0; i < texts.length; ++i) {
            String currentText = texts[i];
            textPos.x = pos.x;
            if (i > 0) {
                float widthDiff = textsWidths[i] - firstTextWidth;
                textPos.x += widthDiff / 2f;

                float textHeight = textsHeights[i - 1];
                textPos.y += textHeight + lineSpacing;
            }
            if (i < indentation.length) {
                textPos.x += indentation[i];
            }

            graphics.drawText(currentText, textPos);
            //graphics.drawRectangle(textPos, textsWidths[i], textsHeights[i], 2f);
        }
    }
}
