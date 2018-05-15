package com.esie.core.ui.elements;
import com.esie.core.eventDispatcher.Dispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.OnEvent;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import com.esie.core.ui.Element;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiListItemHorizontal.ListItemHorizontalStack;
import static com.esie.math.UI.uiMainLayer.uiLayer;

final public class uiColorSelector extends Element {

    volatile public static Map<String, uiColorSelector> ImageStack = new ConcurrentHashMap<>();
    private int imageCoordX;
    private int imageCoordY;
    private int red;
    private int green;
    private int blue;
    private String name;
    private BufferedImage wallpaper;
    private ImageObserver imageObserver;

    public uiColorSelector(String name, int imageCoordX, int imageCoorY) {
        this.imageCoordX = imageCoordX;
        this.imageCoordY = imageCoorY;
        this.name = name;
        try {
            wallpaper = ImageIO.read(new File("src/resources/" + name));
        } catch (IOException ignored) {
            try {
                wallpaper = ImageIO.read(new File("resources/" + name));
            } catch (IOException e) { e.printStackTrace(); }
        }
        ImageStack.put(name, this);
    }


    @Override
    public void onEvent(Event event) {
        var d = new Dispatcher(event);
        d.dispatch(Event.Type.MOUSE_PRESSED, this::eventOnPressAdapter);
    }


    private boolean eventOnPressAdapter(Event e) {
        return onPressed((MousePressedEvent) e);
    }


    public BufferedImage getWallpaper() {
        return wallpaper;
    }


    @OnEvent
    private boolean onPressed(MousePressedEvent event) {
        var clr = wallpaper.getRGB(event.getX(), event.getY());
        red   = (clr & 0x00ff0000) >> 16;
        green = (clr & 0x0000ff00) >> 8;
        blue  = (clr & 0x000000ff);
        uiLayer.Pin(new Color(red,green,blue));
        return false;
    }


    @Override
    public void onRender(Graphics g) {

        g.drawImage(wallpaper, this.imageCoordX, this.imageCoordY, imageObserver);
        if (ListItemHorizontalStack.get("colorOK").answer == 1) {
            ListItemHorizontalStack.get("colorOK").answer = 0;
            uiLayer.Pin(new Color(red,green,blue));
            windowStack.get("ColorSelect").close();
        }

        if (ListItemHorizontalStack.get("colorOK").answer == 2) {
            ListItemHorizontalStack.get("colorOK").answer = 0;
            windowStack.get("ColorSelect").close();
        }
    }


    @Override
    public void onDestroy() {
        ImageStack.remove(name);
    }

}
