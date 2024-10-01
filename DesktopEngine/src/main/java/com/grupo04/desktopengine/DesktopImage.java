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

    DesktopImage(String fileName) {
        super(0, 0);
        try {
            img = ImageIO.read(new File("./assets/" + fileName));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    @Override
    public void render(Graphics graphics) {
        ((DesktopGraphics)graphics).getGraphics2D().drawImage(img, x, y, null);
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

