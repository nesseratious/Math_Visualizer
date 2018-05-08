package com.esie.core.eventObserver.eventTypes;

import com.esie.core.eventObserver.Event;

public class MouseMotionEvent extends Event {
	
	private int x, y;

	public MouseMotionEvent(int x, int y, boolean dragged) {
		super(Event.Type.MOUSE_MOVED);
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
