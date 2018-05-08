package com.esie.core.ui.elements;
import com.esie.math.Forms.MainForm;
import org.junit.jupiter.api.Test;
import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiSwitch.SwitchStack;
import static org.junit.jupiter.api.Assertions.assertEquals;

class uiSwitchTest {

    @Test
    void testSwitch() {
        new MainForm("MainForm");
        windowStack.get("MainForm").construct(new uiSwitch("test",0,0));
        windowStack.get("MainForm").construct(new uiSwitch("test1",0,0));
        windowStack.get("MainForm").construct(new uiSwitch("test2",0,0));
        SwitchStack.get("test") .setState();
        SwitchStack.get("test1").setState();
        SwitchStack.get("test2").setState();
        assertEquals(3,
                 SwitchStack.get("test").returnAnswer +
                        SwitchStack.get("test1").returnAnswer +
                        SwitchStack.get("test2").returnAnswer);
    }
}