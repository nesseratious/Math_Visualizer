package com.esie.core.ui.elements;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventDispatcher.Dispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.OnEvent;
import com.esie.core.eventObserver.eventTypes.MouseMotionEvent;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import com.esie.core.ui.Element;

final public class uiListItem extends Element {

    volatile public static Map<String,uiListItem> ListItemStack = new ConcurrentHashMap<>();
    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    private Font font = new Font("Helvetica", Font.PLAIN, 12);
    private List<Integer> animationY = new ArrayList<>();
    private List<Rectangle> list = new ArrayList<>();
    private List<Color> listColor = new ArrayList<>();
    private List<Integer> animation = new ArrayList<>();
    private List<Integer> animated_buttons = new ArrayList<>();
    private List<String> arr;
    private String name;
    private int[] colorHovered = {255,255,255};
    private int[] colorIdle = {60,60,60};
    private boolean [] hover;
    private boolean bound;
    private boolean invert;
    private boolean renderOnce = true;
    private int list_x;
    private int list_y;
    private int buttons_height;
    private int buttons_weigh;
    private int num;
    private int alpha = 0;
    private int line = 0;
    public  int answer = 0;

    public uiListItem(String name, int list_x, int list_y, int num, List<String> arr, boolean bound, int buttons_weigh, boolean invert, int buttons_space) {

        this.name = name;
        this.list_x = list_x;
        this.list_y = list_y;
        this.bound = bound;
        this.num = num;
        this.invert = invert;
        this.buttons_weigh = buttons_weigh;
        this.buttons_height = buttons_space;
        this.arr = new ArrayList<>(arr);
        ListItemStack.put(name, this);

        for(var i = 0; i < num; i++) {
            var item = new Rectangle(list_x, list_y, 200, buttons_height);
            list_y = list_y + buttons_height + 5;
            list.add(item);
            listColor.add(new Color(colorIdle[0],colorIdle[1],colorIdle[2]));
            hover = new boolean [num];
            animationY.add(0);
            animated_buttons.add(buttons_weigh);
            animation.add(colorIdle[0]);
        }
        deleteTime = 0;
    }


    @Override
    public void onDestroy(){
        alpha = 0;
        line = 0;
        renderOnce = true;
        ListItemStack.remove(name);
    }


    @Override
    public void onEvent(Event event) {
        var d = new Dispatcher(event);
        d.dispatch(Event.Type.MOUSE_PRESSED, this::eventOnPressAdapter);
        d.dispatch(Event.Type.MOUSE_MOVED, this::eventOnMoveAdapter);
    }


    private boolean eventOnPressAdapter(Event e) {
        return onPressed((MousePressedEvent) e);
    }


    private boolean eventOnMoveAdapter(Event e) {
        return onMoved((MouseMotionEvent) e);
    }


    @Override
    public void onRender(Graphics g) {
        var localAnimationSpeed = Integer.parseInt(config.getValue("ANIMATION_SPEED"));
        g.setFont(font);
        if(renderOnce){
            alpha += localAnimationSpeed * 8;
            if (alpha < 255)
                animationY.set(line, alpha);
            else {
                alpha = 0;
                line++;
            } if (line == num)
                renderOnce = false;
        }
        if(bound) {
            g.setColor(new Color(60, 60, 60, animationY.get(num - 1)));
            g.fillRect(list_x - 5, list_y - 5, 2, ((buttons_height + 5) * num) + 4);
            g.fillRect(list_x + buttons_weigh + 3, list_y - 5, 2, (35 * num) + 4);
            g.fillRect(list_x - 5, list_y - 5, buttons_weigh + 10, 2);
            g.fillRect(list_x - 5, list_y + ((buttons_height + 5) * num) - 2, 210, 2);
            g.drawString(name, list_x, list_y - 10);
        }

        for(var i = 0; i < num; i++) {
            if (hover[i]){
                if(line > i) {
                    listColor.set(i, new Color(colorHovered[0],colorHovered[1],colorHovered[2]));
                    animation.set(i, colorHovered[0]);
                }
            } else listColor.set(i, new Color(animation.get(i), animation.get(i), animation.get(i), animationY.get(i)));

            if (!hover[i] && animation.get(i) > colorIdle[0])
                animation.set(i, (animation.get(i)-localAnimationSpeed));

            g.setColor(listColor.get(i));
            g.fillRect(list_x, list.get(i).y, animated_buttons.get(i), buttons_height);

            if (!invert)
                g.setColor(new Color(listColor.get(i).getRed(), listColor.get(i).getGreen(),listColor.get(i).getBlue()));
            else
                g.setColor(new Color(255-listColor.get(i).getRed(), 255-listColor.get(i).getGreen(),255-listColor.get(i).getBlue()));
            if (line >= i)
                g.drawString(arr.get(i), list_x + 15, list.get(i).y + (buttons_height)-10);
        }
    }


    @OnEvent
    private boolean onPressed(MousePressedEvent event) {
        for (var i = 0; i < num; i++)
            if (list.get(i).contains(new Point(event.getX(),event.getY())))
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
