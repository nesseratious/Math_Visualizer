package com.esie.core.ui.elements;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventObserver.Event;
import com.esie.core.ui.Element;
import java.awt.*;
import static java.lang.Integer.*;

final public class uiText extends Element {

    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    private String name;
    private Font font;
    private Color c;
    private int gX;
    private int gY;
    private double animation = 0;

    public uiText(String name, int gX, int gY, String f, int z, Color c) {
        this.name = name;
        this.gX = gX;
        this.gY = gY;
        this.c = c;
        font = new Font(f, Font.BOLD, z);
    }


    @Override
    public void onEvent(Event e) {

    }


    @Override
    public void onRender(Graphics g) {
        if(animation < 1)
            animation += (parseInt(config.getValue("ANIMATION_SPEED")) * 0.01);
        g.setFont(font);
        g.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(), (int)(animation * c.getAlpha())));
        g.drawString(name,gX,gY);
    }


    @Override
    public void onDestroy() {

    }
}
