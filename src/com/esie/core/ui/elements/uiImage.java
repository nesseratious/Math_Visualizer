package com.esie.core.ui.elements;
import com.esie.core.eventObserver.Event;
import com.esie.core.ui.Element;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class uiImage extends Element {

    volatile public static Map<String, uiImage> ImageStack = new ConcurrentHashMap<>();
    private BufferedImage wallpaper [] = new BufferedImage[100];
    private ImageObserver imageObserver;
    private float[] data = new float[25];
    private int imageCoordX;
    private int imageCoordY;
    private int radius = 0;
    private int img = 0;
    private boolean blur;

    public uiImage(String name, int imageCoordX, int imageCoordY, boolean blur) {
        this.imageCoordX = imageCoordX;
        this.imageCoordY = imageCoordY;
        this.blur = blur;
        try {
            wallpaper [0] = ImageIO.read(new File("src/resources/" + name));
        } catch (IOException ignored) {
            try {
                wallpaper [0] = ImageIO.read(new File("resources/" + name));
            } catch (IOException e) { e.printStackTrace(); }
        }
        ImageStack.put(name, this);
        for (int i = 0; i < data.length; i++) {
            float weight = 1.0f / 25;
            data[i] = weight;
        }
    }


    public void setPosition(int x, int y){
        this.imageCoordX = x;
        this.imageCoordY = y;
    }


    @Override
    public void onEvent(Event e) {

    }


    @Override
    public void onRender(Graphics g) {
         if (radius < 50 && blur) {
            radius++;
            img++;
            wallpaper[img] = new ConvolveOp(
                    new Kernel(5, 5, data), ConvolveOp.EDGE_NO_OP, null).filter(wallpaper[img - 1], null);
         }
        g.drawImage(wallpaper[img], this.imageCoordX, this.imageCoordY, imageObserver);
    }


    @Override
    public void onDestroy() {

    }
}
