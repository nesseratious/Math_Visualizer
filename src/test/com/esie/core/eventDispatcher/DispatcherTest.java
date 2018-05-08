package com.esie.core.eventDispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DispatcherTest {

    @Test
    void dispatch() {
        Dispatcher d = new Dispatcher(new MousePressedEvent(0,0,0));
        d.dispatch(Event.Type.MOUSE_PRESSED, e -> {
            Event.Type type = e.getType();
            assertEquals(Event.Type.MOUSE_PRESSED,type);
            return true;
        });
    }


}