package com.esie.core.eventObserver.eventTypes;

import com.esie.core.eventObserver.Event;

public class MousePressedEvent extends MouseButtonEvent {

	public MousePressedEvent(int keyCode, int x, int y) {
		super(Event.Type.MOUSE_PRESSED, keyCode, x, y);
	}

}
