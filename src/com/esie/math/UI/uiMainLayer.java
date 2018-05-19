package com.esie.math.UI;
import com.esie.core.eventObserver.OnEvent;
import com.esie.core.ui.Element;
import com.esie.core.eventDispatcher.Dispatcher;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.eventTypes.KeyButtonEvent;
import com.esie.core.eventObserver.eventTypes.MouseMotionEvent;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import static com.esie.core.eventObserver.Event.Type.*;
import static com.esie.core.ioc.parents.Window.windowStack;
import static com.esie.core.ui.elements.uiSwitch.SwitchStack;
import static com.esie.core.ui.elements.uiTouchController.uiTouchControllerStack;
import static java.awt.event.KeyEvent.*;

public class uiMainLayer extends Element {

    volatile public static uiMainLayer uiLayer;
    private Map <Double, Double> data = new HashMap<>();
    private Map <Color, HashMap <Double, Double> > pinnedData = new HashMap<>();
    private int GraphX;
    private int GraphY;
    private double sin;
    private Rectangle GraphCollision, TFuncCollision;
    private RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private boolean once  = true;
    private boolean isGraphHovered = false;
    private boolean isTFuncHovered = false;
    private double GraphScaleX = 300;
    private double GraphScaleY = 303;
    private double scale_to_y = 0;
    private double scale_to_x = 0;
    private double a = 1;
    private int GraphSize = 700;
    private int MouseX = 0;
    private int MouseY = 0;
    private int TFuncSize = 500;


    public uiMainLayer() {
        uiLayer = this;
    }


    @Override
    public void onDestroy(){
        ClearALL();
        uiLayer = null;
    }


    @Override
    public void onEvent(Event event) {
        Dispatcher d = new Dispatcher(event);
        d.dispatch(MOUSE_MOVED, this::OnMovedAdapter);
        d.dispatch(KEYBOARD_KEY, this::OnPressedAdaptor);
    }


    @OnEvent
    private boolean onKey(KeyButtonEvent event){
        if(this.isGraphHovered && SwitchStack.get("Auto Scale").returnAnswer != 1) {
            if (event.getKeyCode() == VK_UP) this.GraphScaleY += 8;
            if (event.getKeyCode() == VK_DOWN) this.GraphScaleY -= 8;
            if (event.getKeyCode() == VK_LEFT && scale_to_x > 1) this.GraphScaleX += 8;
            if (event.getKeyCode() == VK_RIGHT) this.GraphScaleX -= 8;
        }
        if(this.isTFuncHovered) {
            if (event.getKeyCode() == VK_LEFT ) this.a += 0.1;
            if (event.getKeyCode() == VK_RIGHT) this.a -= 0.1;
        }
        return true;
    }


    @OnEvent
    private boolean onMoved(MouseMotionEvent event) {
        isGraphHovered = GraphCollision.contains(new Point(event.getX( ), event.getY( )));
        isTFuncHovered = TFuncCollision.contains(new Point(event.getX( ), event.getY( )));
        MouseX = event.getX();
        MouseY = event.getY();
        return false;
    }



    @Override
    public void onRender(Graphics g2) {

        Adaptation();
        if (SwitchStack.get("Sync").returnAnswer >= 1) {
            if (once) {
                uiLayer.Clear();
                once = false;
            }
            double x = -10+  uiLayer.getA()/0.0628*0.2;
            uiLayer.Set(x, sin/100);
        }

        //GRAPH
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHints(rh);

        if (isGraphHovered)
            g.setColor(new Color(50,50,50,200));

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.LIGHT_GRAY);
        g.drawRoundRect(GraphX, GraphY, GraphSize, GraphSize,15,15);
        g.drawLine(GraphX +(GraphSize /2), GraphY, GraphX +(GraphSize /2), GraphY + GraphSize);
        g.drawLine(GraphX, GraphY +(GraphSize /2), GraphX + GraphSize, GraphY +(GraphSize /2));
        g.setStroke(new BasicStroke(1));
        g.drawLine(GraphX, GraphY +(GraphSize /4), GraphX + GraphSize, GraphY +(GraphSize /4));
        g.drawLine(GraphX, GraphY +(GraphSize /4)+(GraphSize /2), GraphX + GraphSize, GraphY +(GraphSize /4)+(GraphSize /2));
        g.drawLine(GraphX +(GraphSize /4), GraphY, GraphX +(GraphSize /4), GraphY +(GraphSize));
        g.drawLine(GraphX +(GraphSize /4)+(GraphSize /2), GraphY, GraphX +(GraphSize /4)+(GraphSize /2), GraphY +(GraphSize));
        g.setColor(Color.red);

        for (double x : data.keySet()){
            double y = data.get(x);
            double draw_x, draw_y;
            scale_to_y = this.GraphSize / GraphScaleY;
            scale_to_x = this.GraphSize / GraphScaleX;
            draw_x = (x * (GraphSize * 0.5 * 0.1) * scale_to_x);
            draw_y = (y * (GraphSize * 0.5 * 0.1) * scale_to_y);
            if (GraphCollision.contains(new Point((int)(draw_x + GraphX +(GraphSize /2)), (int)(draw_y + GraphY +(GraphSize /2)))))
                g.fillRect((int)(draw_x + GraphX +(GraphSize /2)), (int)(draw_y + GraphY +(GraphSize /2)), 4,4);
        }

        for (Color c : pinnedData.keySet()){
            g.setColor(c);
            for (double x : pinnedData.get(c).keySet()){
                double y = pinnedData.get(c).get(x);
                double draw_x, draw_y;
                scale_to_y = this.GraphSize / GraphScaleY;
                scale_to_x = this.GraphSize / GraphScaleX;
                draw_x = (x * (GraphSize * 0.5 * 0.1) * scale_to_x);
                draw_y = (y * (GraphSize * 0.5 * 0.1) * scale_to_y);
                if (GraphCollision.contains(new Point((int)(draw_x + GraphX +(GraphSize /2)), (int)(draw_y + GraphY +(GraphSize /2)))))
                    g.fillRect((int)(draw_x + GraphX +(GraphSize /2)), (int)(draw_y + GraphY +(GraphSize /2)), 4,4);
            }
        }

        g.setColor(Color.LIGHT_GRAY);
        String scale = "Scale " + scale_to_x + " : " + scale_to_y;
        g.drawString(scale, GraphX, GraphY -15);

        if(isGraphHovered) {
            String s = "X=" + ((MouseX - GraphX -(GraphSize /2))/scale_to_x) +
                       "Y=" + ((MouseY- GraphY -(GraphSize /2))/scale_to_y);
            g.drawString(s, MouseX +2, MouseY+2);
            g.setColor(new Color(255,255,255,25));
            g.setFont(new Font("Helvetica", Font.BOLD, 28));

            if (SwitchStack.get("Auto Scale").returnAnswer != 1)
                g.drawString("Use ⬅︎⬆︎⬇︎➡ to scale graph", GraphX + 7, GraphY + GraphSize - 15);
            if (SwitchStack.get("Auto Scale").returnAnswer == 1)
                g.drawString("Disable AutoScale to scale the graph", GraphX + 7, GraphY + GraphSize - 15);
        }

        //T_FUNC

        int gX = 40;
        int gY = 100;
        if (isTFuncHovered){
            g.setFont (new Font("Helvetica", Font.BOLD, 28));
            g.setColor(new Color(255,255,255,25));
            g.drawString("Grag the circle to change A", gX + 7, gY + TFuncSize - 15);
        }

        int r = TFuncSize /2;
        TFuncCollision = new Rectangle(gX, gY, gX + TFuncSize, gY + TFuncSize);
        g.setStroke(new BasicStroke(1));

        g.setColor(Color.WHITE);
        g.drawOval(gX, gY, TFuncSize, TFuncSize);
        g.drawLine(gX +r, gY, gX +r, gY + TFuncSize);
        g.drawLine(gX, gY +r, gX + TFuncSize, gY +r);

        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLUE);
        g.drawLine(gX +r, gY +r, gX +r+(int)(Math.cos(a)*r), gY +r);
        g.drawLine(gX +r, gY +r, gX +r+(int)(Math.cos(a)*r), gY +r-(int)(Math.sin(a)*r));
        g.drawLine(gX +r+(int)(Math.cos(a)*r), gY +r, gX +r+(int)(Math.cos(a)*r), gY +r-(int)(Math.sin(a)*r));
        sin = ((gY +r)-(gY +r-(int)(Math.sin(a)*r)));

        g.setColor(new Color(255,255,255,100));
        g.setFont(new Font("Helvetica", Font.BOLD, 33));
        String tempA = "a = "; {
            if ((a * 57.2957914) >= 0)   tempA = tempA + String.valueOf((a * 57.2957914)).charAt(0);
            if ((a * 57.2957914) >= 10)  tempA = tempA + String.valueOf((a * 57.2957914)).charAt(1);
            if ((a * 57.2957914) >= 100) tempA = tempA + String.valueOf((a * 57.2957914)).charAt(2);
        }
        g.drawString(tempA, gX + r + 5, gY + r - 5);
        int uiTouchSize = uiTouchControllerStack.get("MoveF").getController_size();

        if (!uiTouchControllerStack.get("MoveF").dragging)
             uiTouchControllerStack.get("MoveF").updatePosition(gX +r+(int)(Math.cos(a)*r)-(uiTouchSize/2),
                                                                gY +r-(int)(Math.sin(a)*r)-(uiTouchSize/2));
        else {
            int shift = MouseX - gX;
            int inverse_shift = TFuncSize - shift;
            int percentage_shift = (inverse_shift*100)/TFuncSize;
            a = percentage_shift*Math.PI/100;
        }
    }


    public void Set(double x, double y){
        this.data.put(x,y);
    }


    public double getA() {
        return a;
    }


    public void Clear(){
       this.data.clear();
    }


    public void ClearALL(){
        this.pinnedData.clear();

    }


    public void Pin(Color c){
        pinnedData.put(c, new HashMap<>(data));
    }


    private void Adaptation() {

        GraphCollision = new Rectangle(GraphX, GraphY, GraphSize, GraphSize);
        String parentWindowID = "MainForm";
        int parentWindowWight = windowStack.get(parentWindowID).getWidth();
        int parentWindowHeigt = windowStack.get(parentWindowID).getHeight();

        int sw = (int)(parentWindowWight * 0.75);
        int sh = (int)(parentWindowHeigt * 0.75);
        GraphSize = getMin(sw,sh);
        GraphY = 100;
        GraphX = parentWindowWight - GraphSize - 50;

        SwitchStack.get("Auto Scale").SwitchX = GraphX + 15;
        SwitchStack.get("Auto Scale").SwitchY = GraphY + GraphSize + 25;
        SwitchStack.get("Sync").SwitchX = GraphX + 215;
        SwitchStack.get("Sync").SwitchY = GraphY + GraphSize + 25;

        if (SwitchStack.get("Auto Scale").returnAnswer == 1) {
            GraphScaleY = this.GraphSize/2;
            GraphScaleX = this.GraphSize;
        }

        int sw2 = (int)(parentWindowWight * 0.75);
        int sh2 = (int)(parentWindowHeigt * 0.75);
        TFuncSize = getMin(sw2,sh2);
        SwitchStack.get("Automatic").SwitchX = 40;
        SwitchStack.get("Automatic").SwitchY = 100 + TFuncSize;

        if (SwitchStack.get("Automatic").returnAnswer == 1) {
            if (a > (Math.PI * 2)) {
                a = 0;
                if (SwitchStack.get("Sync").returnAnswer >= 0) {
                    uiLayer.Clear();
                }
            }
            this.a += 0.005;
        }
    }


    private int getMin(int a, int b) {
        return (a < b ? a : b);
    }


    private boolean OnMovedAdapter(Event e) {
        return onMoved((MouseMotionEvent) e);
    }


    private boolean OnPressedAdaptor(Event e) {
        return onKey((KeyButtonEvent) e);
    }

}
