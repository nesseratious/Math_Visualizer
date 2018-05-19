package com.esie.math.Forms;
import com.esie.core.ioc.parents.Window;
import com.esie.core.ioc.parents.WindowInterface;
import com.esie.core.ui.elements.uiImage;
import com.esie.core.ui.elements.uiListItemHorizontal;
import com.esie.core.ui.elements.uiSwitch;
import com.esie.core.ui.elements.uiTouchController;
import com.esie.math.UI.uiBackGround;
import com.esie.math.UI.uiMainLayer;
import java.util.ArrayList;
import java.util.List;
import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiSwitch.SwitchStack;
import static com.esie.core.ui.elements.uiTouchController.uiTouchControllerStack;

final public class MainForm {

    public MainForm(String name) {
        try {
            source();
        } catch (Exception ignored) { }
    }

    private void source()  {
        List<String> open = new ArrayList<>();
        open.add("Open functions");
        List<String> settings = new ArrayList<>();
        settings.add("Settings");
        new Window("MainForm", 1600, 900);
        WindowInterface window =  windowStack.get("MainForm");

        window.construct(new uiImage("bg.jpg",0,0,false));
        window.construct(new uiBackGround());
        window.construct(new uiListItemHorizontal("Open", 5, 5, 1, open,160, true, 30));
        window.construct(new uiListItemHorizontal("Settings", 5, 5, 1, settings,300, true, 30));
        window.construct(new uiImage("Unit_circle_angles.png",0,0,false));
        window.construct(new uiMainLayer());
        window.construct(new uiSwitch("Auto Scale",0,0));
        window.construct(new uiSwitch("Automatic",0,0));
        window.construct(new uiSwitch("Sync",0,0));
        window.construct(new uiTouchController("MoveF", 55));

        uiTouchControllerStack.get("MoveF").updatePosition(50,50);
        changeSwichesState();
    }


    private void changeSwichesState(){
        SwitchStack.get("Auto Scale").setState();
        SwitchStack.get("Automatic").setState();
        SwitchStack.get("Sync").setState();
    }

}
