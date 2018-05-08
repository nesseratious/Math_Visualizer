package com.esie.math.Threads;
import com.esie.core.ioc.MasterThread;
import com.esie.math.Forms.SettingsForm;
import com.esie.math.UI.uiMainLayer;
import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiListItemHorizontal.ListItemHorizontalStack;
import static com.esie.core.ui.elements.uiListItem.ListItemStack;

public class Thread_1 extends MasterThread {

    private static volatile boolean isSettingsMenuOpened = false;

    @Override
    public void main(){

        int tempListAnswer = ListItemHorizontalStack.get("Settings").answer;

        if (tempListAnswer == 1) {
            ListItemHorizontalStack.get("Settings").answer = 0;
            if (!isSettingsMenuOpened) {
                new SettingsForm("Settings Items");
                isSettingsMenuOpened = true;
            } else {
                windowStack.get("MainForm").destruct(ListItemStack.get("Settings Items"));
                isSettingsMenuOpened = false;
            }
        }

        if (isSettingsMenuOpened) {

            if (tempListAnswer == 1) {
                ListItemStack.get("Settings Items").answer = 0;
                uiMainLayer.uiLayer.Clear();
            }

            if (tempListAnswer == 2) {
                ListItemStack.get("Settings Items").answer = 0;
                uiMainLayer.uiLayer.ClearALL();
            }
        }
    }
}
