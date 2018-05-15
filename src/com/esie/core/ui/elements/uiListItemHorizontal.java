package com.esie.core.ui.elements;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventDispatcher.Dispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.OnEvent;
import com.esie.core.eventObserver.eventTypes.MouseMotionEvent;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import com.esie.core.ui.Element;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.esie.core.ioc.parents.Window.windowStack;

final public class uiListItemHorizontal extends Element {

    volatile public static Map<String,uiListItemHorizontal> ListItemHorizontalStack = new HashMap<>();
    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    private Font font = new Font("Helvetica", Font.PLAIN, 16);
    private RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private List<Integer> animationY = new ArrayList<>();
    private List<Rectangle> list = new ArrayList<>();
    private List<Color> listColor = new ArrayList<>();
    private List<Integer> animation = new ArrayList<>();
    private List<Integer> animated_buttons = new ArrayList<>();
    private List<String> arr;
    private String name;
    private int gY;
    private int button_space;
    private int num;
    public  int answer = 0;
    private int alpha = 0;
    private int line = 0;
    private int[] colorHovered = {255,255,255};
    private int[] colorIdle    = {60,60,60};
    private boolean [] hover;
    private boolean renderOnce = true;
    private boolean invert;

    public uiListItemHorizontal(String name, int gX, int gY, int num, List<String> arr, int buttonsY, boolean invert, int buttons_space) {
        this.name = name;
        this.gY = gY;
        this.num = num;
        this.invert = invert;
        this.button_space = buttons_space;
        this.arr = new ArrayList<>(arr);
        ListItemHorizontalStack.put(name, this);

        for(var i = 0; i < num; i++) {
            Rectangle item = new Rectangle(gX, gY, buttonsY, button_space);
            gX = gX + buttonsY + 5;
            list.add(item);
            listColor.add(new Color(colorIdle[0],colorIdle[1],colorIdle[2]));
            hover = new boolean [num];
            animationY.add(0);
            animated_buttons.add(buttonsY);
            animation.add(colorIdle[0]);
        }
        deleteTime = 0;
    }


    @Override
    public void onDestroy(){
        ListItemHorizontalStack.remove(name);
    }


    @Override
    public void onEvent(Event event) {
        var d = new Dispatcher(event);
        d.dispatch(Event.Type.MOUSE_PRESSED, this::eventOnPressedAdapter);
        d.dispatch(Event.Type.MOUSE_MOVED, this::eventOnMovedAdapter);
    }


    private boolean eventOnPressedAdapter(Event e) {
        return onPressed((MousePressedEvent) e);
    }


    private boolean eventOnMovedAdapter(Event e) {
        return onMoved((MouseMotionEvent) e);
    }


    @Override
    public void onRender(Graphics g2) {
        int tempAnimationSpeed = Integer.parseInt(config.getValue("ANIMATION_SPEED"));
        if (this.name.equals("Settings"))
            list.set(0,new Rectangle(windowStack.get("MainForm").getWidth()-305,5,200,30));

        var g = (Graphics2D) g2;
        g.setRenderingHints(rh);
        g.setFont(font);
        if(renderOnce){
            alpha += (tempAnimationSpeed * 8);
            if(alpha < 255)
                animationY.set(line, alpha);
            else {
                alpha = 0;
                line++;
            }
            if(line == num)
                renderOnce = false;
        }

        for(int i = 0; i < num; i++) {
            if (hover[i]){
                if(line > i) {
                    listColor.set(i, new Color(colorHovered[0],colorHovered[1],colorHovered[2]));
                    animation.set(i, colorHovered[0]);
                }
            } else {
                listColor.set(i, new Color(animation.get(i), animation.get(i), animation.get(i), animationY.get(i)));
            }
            if (!hover[i] && animation.get(i) > colorIdle[0])
                animation.set(i, (animation.get(i)-tempAnimationSpeed));

            g.setColor(listColor.get(i));
            g.fillRect(list.get(i).x, gY, animated_buttons.get(i), button_space);

            if (!invert)
                g.setColor(new Color(listColor.get(i).getRed(), listColor.get(i).getGreen(),listColor.get(i).getBlue()));
            else
                g.setColor(new Color(255-listColor.get(i).getRed(), 255-listColor.get(i).getGreen(),255-listColor.get(i).getBlue()));
            if (line >= i)
                g.drawString(arr.get(i), list.get(i).x + 7 , gY + 20);
        }
    }


    @OnEvent
    private boolean onPressed(MousePressedEvent event) {
        for (var i = 0; i < num; i++)
            if(list.get(i).contains(new Point(event.getX(),event.getY())))
                answer = i+1;
        return false;
    }


    @OnEvent
    private boolean onMoved(MouseMotionEvent event) {
        for (var i = 0; i < num; i++)
            hover[i] = list.get(i).contains(new Point(event.getX(), event.getY()));
        return false;
    }
}
