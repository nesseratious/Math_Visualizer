package com.esie.core.ui.elements;
import com.esie.math.Forms.MainForm;
import org.junit.jupiter.api.Test;
import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiColorSelector.ImageStack;
import static org.junit.jupiter.api.Assertions.*;

class uiColorSelectorTest {

    @Test
    void testGetColor() {
        new MainForm("MainForm");
        windowStack.get("MainForm").construct(new uiColorSelector("plt.png",0,0));
        int clr = ImageStack.get("plt.png").getWallpaper().getRGB(1,1);
        int red   = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue  = (clr & 0x000000ff);
        int finalColor = red + green + blue;
        assertEquals(759,finalColor);
    }
}