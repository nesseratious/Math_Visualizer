package com.esie.core.ioc.parents;
import com.esie.core.ui.elements.uiSwitch;
import com.esie.math.Forms.MainForm;
import org.junit.jupiter.api.Test;

import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiSwitch.SwitchStack;
import static org.junit.jupiter.api.Assertions.*;

class WindowTest {

    @Test
    void construct() {
        new MainForm("MainForm");
        windowStack.get("MainForm").construct(new uiSwitch("test",0,0));
        assertTrue(SwitchStack.get("test")!=null);
    }

    @Test
    void destruct() {
        new MainForm("MainForm");
        windowStack.get("MainForm").construct(new uiSwitch("test",0,0));
        windowStack.get("MainForm").destruct(SwitchStack.get("test"));
        assertTrue(SwitchStack.get("test")==null);
    }
}