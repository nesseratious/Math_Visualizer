package com.esie.core.eventDispatcher;
import com.esie.core.eventObserver.Event;

public class Dispatcher {

	private Event event;

	public Dispatcher(Event event) {
		this.event = event;
	}

	public void dispatch(Event.Type type, eventHandler handler) {
		if (event.handled) return;
		if (event.getType() == type) event.handled = handler.handle(event);
	}
}
