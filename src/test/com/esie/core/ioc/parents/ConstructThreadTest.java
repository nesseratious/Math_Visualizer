package com.esie.core.ioc.parents;
import com.esie.math.Threads.Thread_0;
import org.junit.jupiter.api.Test;
import static com.esie.core.ioc.parents.ConstructThread.ThreadStack;
import static org.junit.jupiter.api.Assertions.*;

class ConstructThreadTest {

    @Test
    void pause() {
        new ConstructThread("thread0", new Thread_0(), 1).pause();
        assertFalse(ThreadStack.get("thread0").isRunning());
    }


    @Test
    void stop() {
        new ConstructThread("thread0", new Thread_0(), 1).stop();
        assertTrue(ThreadStack.get("thread0")==null);
    }


    @Test
    void resume() {
        new ConstructThread("thread0", new Thread_0(), 1).pause();
        ThreadStack.get("thread0").resume();
        assertTrue(ThreadStack.get("thread0").isRunning());
    }


    @Test ()
    void setRefreshRate() {
        new ConstructThread("thread0", new Thread_0(), 1).setRefreshRate(2);
        assertEquals(2,ThreadStack.get("thread0").getREFRESH_RATE());
    }
}