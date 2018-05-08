package com.esie.core.ui;
import java.awt.Graphics;
import com.esie.core.eventObserver.Event;

public abstract class Element implements uiEventListener {
	public int deleteTime;
	abstract public void onEvent(Event e);
	abstract public void onRender(Graphics g);
	abstract public void onDestroy();
}

