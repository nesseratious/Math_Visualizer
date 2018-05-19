package com.esie.core.ui.elements;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventObserver.Event;
import com.esie.core.ui.Element;
import java.awt.*;

import static java.lang.Integer.*;

final public class uiRectangle extends Element {

    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    private boolean renderOnce = true;
    private int[]  colorIdle = new int[3];
    private double animation = 0;
    private int gX;
    private int gY;
    private int h;
    private int w;
    private int ax;
    private int ay;
    private int aw;
    private int ah;
    private int aa = 0;

    public uiRectangle(int gX, int gY, int w, int h, int r, int g, int b) {
        this.gX = gX;
        this.gY = gY;
        this.h = h;
        this.w = w;
        this.colorIdle[0] = r;
        this.colorIdle[1] = g;
        this.colorIdle[2] = b;
    }


    @Override
    public void onEvent(Event e) {

    }


    @Override
    public void onRender(Graphics g) {
            if (renderOnce) {
                animation += parseInt(config.getValue("ANIMATION_SPEED")) * 0.01;
                ah = (int) (animation * h);
                aw = (int) (animation * w);
                ax = (w / 2) + gX - (int)(animation * (w / 2));
                ay = (h / 2) + gY - (int)(animation * (h / 2));
                aa = (int) (animation * 255);
                if (animation > 0.99)
                    renderOnce = false;
            }
        g.setColor(new Color(colorIdle[0],colorIdle[1],colorIdle[2],aa));
        g.fillRect(ax,ay,aw,ah);
    }


    @Override
    public void onDestroy() {

    }
}
