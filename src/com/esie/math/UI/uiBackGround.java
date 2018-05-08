package com.esie.math.UI;
import com.esie.core.eventObserver.Event;
import com.esie.core.ui.Element;
import java.awt.*;
import static com.esie.core.ioc.parents.Window.windowStack;

public class uiBackGround extends Element {

    private int ParentWindowWeigh;
    private int ParentWindowHeigh;

    @Override
    public void onEvent(Event e) {

    }


    @Override
    public void onRender(Graphics g) {
        Adaptation();
        g.setColor(new Color(100,100,100,200));
        g.fillRect(0,0, ParentWindowWeigh,40);
        g.setColor(new Color(50,50,50,200));
        g.fillRect(0,40, ParentWindowWeigh, ParentWindowHeigh -40);
    }


    @Override
    public void onDestroy() {

    }


    private void Adaptation() {
        String parentWindowID = "MainForm";
        ParentWindowWeigh = windowStack.get(parentWindowID).getWidth();
        ParentWindowHeigh = windowStack.get(parentWindowID).getHeight();
    }
}


