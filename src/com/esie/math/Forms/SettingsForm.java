package com.esie.math.Forms;
import com.esie.core.ui.elements.uiListItem;
import java.util.ArrayList;
import static com.esie.core.ioc.parents.Window.windowStack;

final public class SettingsForm {

    public SettingsForm(String name) {
        String name1 = name;
        try {
            source();
        } catch (Exception ignored) { }
    }


    private void source() {
        ArrayList<String> settings_items = new ArrayList<>();
        settings_items.add("Clear this");
        settings_items.add("Clear pinned ");
        settings_items.add(" ");
        settings_items.add(" ");
        settings_items.add(" ");
        int tempWeigh = windowStack.get("MainForm").getWidth();
        windowStack.get("MainForm").construct(
                new uiListItem("Settings Items",tempWeigh - 305,45,5, settings_items,false,300,true,30));
    }
}
