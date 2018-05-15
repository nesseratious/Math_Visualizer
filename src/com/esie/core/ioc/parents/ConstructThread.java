package com.esie.core.ioc.parents;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.ioc.MasterThread;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static java.lang.Boolean.*;
import static java.lang.Integer.*;
import static java.lang.Thread.*;

public class ConstructThread implements ConstructThreadInterface {

    private ConfigurationSingleton config = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
    volatile public static Map<String, ConstructThreadInterface> ThreadStack = new ConcurrentHashMap<>();
    private boolean running = true;
    private int REFRESH_RATE;
    private String name;
    private MasterThread mst;

    public ConstructThread(String name, MasterThread mst, int REFRESH_RATE){
        this.name = name;
        this.mst = mst;
        var tempLogicMaxRefreshRate = parseInt(config.getValue("LOGIC_MAX_REFRESH_RATE"));

        if (REFRESH_RATE > 0 && REFRESH_RATE < tempLogicMaxRefreshRate)
            this.REFRESH_RATE = REFRESH_RATE;

        if (REFRESH_RATE > tempLogicMaxRefreshRate)
            this.REFRESH_RATE = tempLogicMaxRefreshRate;

        if (parseBoolean(config.getValue("OVERWRITE_THREAD_LIMIT")))
            this.REFRESH_RATE = tempLogicMaxRefreshRate;

        ThreadStack.put(name, this);
        threadStart();
    }


    synchronized private void threadStart() {
        var tempThreadLimit = parseBoolean(config.getValue("THREAD_LIMIT"));
        new Thread(() -> {
            while(running) {
                mst.main();
                try {
                    if (tempThreadLimit)
                        sleep(1000/REFRESH_RATE);
                } catch (Exception ignored) { }
            }
        }).start();
    }


    @Override
    public void pause() {
        running = false;
    }


    @Override
    public boolean isRunning() {
        return running;
    }


    @Override
    public int getREFRESH_RATE() {
        return REFRESH_RATE;
    }


    @Override
    public void stop() {
        ThreadStack.remove(name);
        mst = null;
    }


    @Override
    public void resume() {
        running = true;
    }


    @Override
    public void setRefreshRate(int delay) {
        var tempLogicMaxRefreshRate = parseInt(config.getValue("LOGIC_MAX_REFRESH_RATE"));
        if (delay > 0 && delay < tempLogicMaxRefreshRate)
            this.REFRESH_RATE = delay;
        if (delay > tempLogicMaxRefreshRate)
            this.REFRESH_RATE = tempLogicMaxRefreshRate;
    }
}
