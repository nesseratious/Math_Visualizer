package com.esie.core.eventObserver;

public class Event {

	private Type type;
	public boolean handled;

	public enum Type {
		MOUSE_MOVED,
		MOUSE_PRESSED,
		MOUSE_RELEASED,
		MOUSE_WHEEL,
		KEYBOARD_KEY,
	}

	protected Event(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
