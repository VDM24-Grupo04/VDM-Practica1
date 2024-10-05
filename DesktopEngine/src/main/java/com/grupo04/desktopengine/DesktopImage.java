package com.grupo04.desktopengine;

import com.grupo04.engine.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DesktopImage implements Image {
    private BufferedImage img;
    private DesktopGraphics graphics;

    DesktopImage(String fileName, DesktopGraphics graphics) {
        this.graphics = graphics;
        try {
            img = ImageIO.read(new File("./assets/" + fileName));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public BufferedImage getImg() {
        return img;
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

