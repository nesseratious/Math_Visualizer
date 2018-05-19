package com.esie.core.ui.elements;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventDispatcher.Dispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.OnEvent;
import com.esie.core.eventObserver.eventTypes.MouseMotionEvent;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import com.esie.core.eventObserver.eventTypes.MouseReleasedEvent;
import com.esie.core.ui.Element;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.esie.core.eventObserver.Event.Type.*;
import static java.lang.Integer.*;

final public class uiTouchController extends Element {

    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    volatile public static Map<String,uiTouchController> uiTouchControllerStack = new ConcurrentHashMap<>();
    private Rectangle collision;
    private double animation = 0;
    public  boolean dragging = false;
    private int controller_x;
    private int controller_y;
    private int controller_size;
    private int px;
    private int py;

    public uiTouchController(String name, int controller_size) {
        this.controller_size = controller_size;
        uiTouchControllerStack.put(name, this);
    }


    @Override
    public void onEvent(Event event) {
        Dispatcher d = new Dispatcher(event);
        d.dispatch(MOUSE_PRESSED, this::eventOnPressedAdapter);
        d.dispatch(MOUSE_RELEASED, this::eventOnReleasedAdapter);
        d.dispatch(MOUSE_MOVED, this::eventOnMovedAdapter);
    }


    private boolean eventOnPressedAdapter(Event e) {
        return onPressed((MousePressedEvent) e);
    }


    private boolean eventOnReleasedAdapter(Event e) {
        return onReleased((MouseReleasedEvent) e);
    }


    private boolean eventOnMovedAdapter(Event e) {
        return onMoved((MouseMotionEvent) e);
    }


    @Override
    public void onRender(Graphics g) {
        collision =  new Rectangle(controller_x, controller_y, controller_size, controller_size);
        if(animation <= 1)
            animation += (parseInt(config.getValue("ANIMATION_SPEED")) * 0.002);
        else
            animation = 0;
        g.setColor(new Color(255,255,255,(int)(animation*100)));
        if (!dragging)
            g.fillOval(controller_x, controller_y, controller_size, controller_size);
    }


    @Override
    public void onDestroy() {

    }


    public void updatePosition(int x, int y){
        this.controller_x = x;
        this.controller_y = y;
    }


    public int getController_size() {
        return controller_size;
    }


    @OnEvent
    private boolean onPressed(MousePressedEvent event) {
        if (collision.contains(new Point(event.getX(), event.getY())))
            dragging = true;
        return false;
    }


    @OnEvent
    private boolean onReleased(MouseReleasedEvent event) {
        dragging = false;
        return false;
    }


    @OnEvent
    private boolean onMoved(MouseMotionEvent event) {
        if (dragging) {
            controller_x += event.getX() - px;
            controller_y += event.getY() - py;
        }
        px = event.getX();
        py = event.getY();
        return false;
    }

}
