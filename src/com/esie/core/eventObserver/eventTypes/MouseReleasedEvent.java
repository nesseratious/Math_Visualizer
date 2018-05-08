package com.esie.core.eventObserver.eventTypes;
import com.esie.core.eventObserver.Event;

public class MouseReleasedEvent extends MouseButtonEvent {

	public MouseReleasedEvent(int keyCode, int x, int y) {
		super(Event.Type.MOUSE_RELEASED, keyCode, x, y);
	}

}
