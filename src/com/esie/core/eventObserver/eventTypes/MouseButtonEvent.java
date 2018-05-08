package com.esie.core.eventObserver.eventTypes;
import com.esie.core.eventObserver.Event;

public class MouseButtonEvent extends Event {

	private int x;
	private int y;
	
	MouseButtonEvent(Type type, int keyCode, int x, int y) {
		super(type);
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
