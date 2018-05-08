package com.esie.core.ioc.parents;

public interface ConstructThreadInterface {
    void pause();
    void stop();
    void resume();
    void setRefreshRate(int delay);
    boolean isRunning();
    int getREFRESH_RATE();
}
