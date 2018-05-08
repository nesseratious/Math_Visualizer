package com.esie.math.Forms;
import com.esie.core.ioc.parents.Window;
import com.esie.core.ui.elements.uiListItemHorizontal;
import com.esie.core.ui.elements.uiRectangle;
import com.esie.core.ui.elements.uiColorSelector;
import java.util.ArrayList;
import java.util.List;
import static com.esie.core.ioc.parents.Window.windowStack;

final public class ColorSelectForm {

    private List<String> colorOK;

    public ColorSelectForm(String name) {
        try {
            source();
        } catch (Exception ignored) { }
    }

    private void source() {
        colorOK = new ArrayList<>();
        colorOK.add("           Ok");
        colorOK.add("       Cancel");
        new Window("ColorSelect", 255, 286);
        windowStack.get("ColorSelect").construct(new uiRectangle(0,0,500,500,60,60,60));
        windowStack.get("ColorSelect").construct(new uiColorSelector("plt.png",0,0));
        windowStack.get("ColorSelect").construct(new uiListItemHorizontal("colorOK", 1,254, 2, colorOK, 125, true, 30 ));
    }
}
