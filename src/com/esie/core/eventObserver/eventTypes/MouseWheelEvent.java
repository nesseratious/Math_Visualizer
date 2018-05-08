package com.esie.core.eventObserver.eventTypes;
import com.esie.core.eventObserver.Event;

public class MouseWheelEvent extends MouseButtonEvent {

    public MouseWheelEvent(int keyCode, int x, int y) {
        super(Event.Type.MOUSE_WHEEL, keyCode, x, y);
    }

}
