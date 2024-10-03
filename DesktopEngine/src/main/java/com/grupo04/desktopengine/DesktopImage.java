package com.grupo04.desktopengine;

import com.grupo04.engine.Graphics;
import com.grupo04.engine.Image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DesktopImage extends Image {
    private BufferedImage img;
    private DesktopGraphics graphics;

    DesktopImage(String fileName, DesktopGraphics graphics) {
        super();
        this.graphics = graphics;
        try {
            img = ImageIO.read(new File("./assets/" + fileName));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    public void render(int x, int y) {
        graphics.renderImage(this, x, y);
    }
    @Override
    public void render(int x, int y, int w, int h) {
        graphics.renderImage(this, x, y, w, h);
    }

    public BufferedImage getImg() { return img; }
    @Override
    public int getWidth() {
        return img.getWidth();
    }
    @Override
    public int getHeight() {
        return img.getHeight();
    }
}

