package com.esie.core.eventDispatcher;
import com.esie.core.eventObserver.Event;

public interface eventHandler {
	boolean handle(Event e);
}

