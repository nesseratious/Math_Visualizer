package com.esie.core.eventObserver.eventTypes;
import com.esie.core.eventObserver.Event;

public class KeyButtonEvent extends Event {

	private int keyCode;

	public KeyButtonEvent(int keyCode) {
		super(Event.Type.KEYBOARD_KEY);
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}

	
}
