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
    DesktopGraphics graphics;
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
        graphics.getGraphics2D().drawImage(img, x, y, null);
    }
    @Override
    public void render(int x, int y, int w, int h) {
        graphics.getGraphics2D().drawImage(img, x, y, w, h, null);
    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }
    @Override
    public int getHeight() {
        return img.getHeight();
    }
}

