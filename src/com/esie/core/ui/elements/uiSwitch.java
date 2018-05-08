package com.esie.core.ui.elements;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventDispatcher.Dispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.OnEvent;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import com.esie.core.ui.Element;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class uiSwitch extends Element {

    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    volatile public static Map<String, uiSwitch> SwitchStack = new ConcurrentHashMap<>();
    private Font font = new Font("Helvetica", Font.PLAIN, 16);
    private String name;
    private Rectangle collision;
    private RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private boolean renderOnce = true;
    private boolean answer = false;
    private int[] colorHovered = {160,22,0};
    private int[] colorIdle = {60,60,60};
    public  int SwitchX, SwitchY;
    public  int returnAnswer = 0;
    private int switch_y = 0;
    private int animation = 0;
    private int animationColor = 0;

    public uiSwitch(String name, int SwitchX, int SwitchY) {
        this.name = name;
        this.SwitchX = SwitchX;
        this.SwitchY = SwitchY;
        SwitchStack.put(name, this);
    }


    @Override
    public void onEvent(Event event) {
        Dispatcher d = new Dispatcher(event);
        d.dispatch(Event.Type.MOUSE_PRESSED, this::eventOnPressedAdapter);
    }


    private boolean eventOnPressedAdapter(Event e) {
        return onPressed((MousePressedEvent) e);
    }


    @Override
    public void onRender(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHints(rh);
        g.setFont(font);
        collision = new Rectangle(SwitchX, SwitchY, 50, 30);

        if(renderOnce){
           animationColor += Integer.parseInt(config.getValue("ANIMATION_SPEED"));
           if (animationColor >= 140) {
               animationColor = 140;
               renderOnce = false;
           }
        }
        if (animation == 1) {
            switch_y++;
            if (switch_y == 20) {
                animation = 0;
            }
        }
        if (animation == -1) {
            switch_y--;
            if (switch_y == 0) {
                animation = 0;
            }
        }

        if (!answer)
            g.setColor(new Color(colorIdle[0], colorIdle[1], colorIdle[2],255));
        else
            g.setColor(new Color(colorHovered[0], colorHovered[1], colorHovered[2],255));

        g.fillOval(SwitchX, SwitchY, 30, 30);
        g.fillOval(SwitchX + 20, SwitchY, 30, 30);
        g.fillRect(SwitchX + 15, SwitchY, 20, 30);
        g.setColor(new Color(200,200,200, 255));
        g.fillOval(SwitchX +2+switch_y, SwitchY +2, 26, 26);
        g.setColor(new Color(255,255,255,animationColor));
        g.drawString(name, SwitchX + 62, SwitchY + 22);
    }


    @Override
    public void onDestroy() {
        SwitchStack.remove(name);
    }

    public void setState(){
        if (this.answer) {
            this.answer = false;
            returnAnswer = -1;
            animation = -1;
        } else {
            this.answer = true;
            returnAnswer = 1;
            animation = 1;
        }
    }


    @OnEvent
    private boolean onPressed(MousePressedEvent event) {
            if(collision.contains(new Point(event.getX(), event.getY())))
                setState();
        return false;
    }

}
