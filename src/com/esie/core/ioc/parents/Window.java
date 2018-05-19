package com.esie.core.ioc.parents;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import com.esie.core.configurationSingleton.ConfigurationSingleton;
import com.esie.core.eventObserver.eventTypes.KeyButtonEvent;
import com.esie.core.eventObserver.Event;
import com.esie.core.eventObserver.eventTypes.MouseMotionEvent;
import com.esie.core.eventObserver.eventTypes.MousePressedEvent;
import com.esie.core.eventObserver.eventTypes.MouseReleasedEvent;
import com.esie.core.ui.Element;
import static java.awt.EventQueue.*;
import static java.awt.event.WindowEvent.*;
import static java.lang.Boolean.*;
import static java.lang.Integer.*;
import static java.lang.Thread.*;
import static java.util.Objects.*;

public class Window extends Canvas implements WindowInterface {

	private ConfigurationSingleton configuration = ConfigurationSingleton.getInstance("src/resources/configuration.properties");
	public static volatile Map<String, Window> windowStack = new ConcurrentHashMap<>();
	private List<Element> elements = new ArrayList<>();
	private int tempRefreshRate = parseInt(requireNonNull(configuration).getValue("GRAPHICS_REFRESH_RATE"));
	private BufferStrategy gBufferStrategy;
	private JFrame frame;

	public Window(String name, int width, int height) {
		setPreferredSize(new Dimension(width, height));
		init(name);
		addListeners();
		if (gBufferStrategy == null)
			createBufferStrategy(parseInt(configuration.getValue("GRAPHICS_BUFFER")));
		render();
		windowStack.put(name, this);
	}


	private void addListeners() {
		addMouseListener(
				new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						onEvent(new MousePressedEvent(e.getButton(), e.getX(), e.getY()));
					}
					@Override
					public void mouseReleased(MouseEvent e) {
						onEvent(new MouseReleasedEvent(e.getButton(), e.getX(), e.getY()));
					}
				});
		addKeyListener(
				new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						onEvent(new KeyButtonEvent(e.getKeyCode()));
					}
				});
		addMouseMotionListener(
				new MouseMotionListener() {
					@Override
					public void mouseMoved(MouseEvent e) {
						onEvent(new MouseMotionEvent(e.getX(), e.getY(), false));
					}
					@Override
					public void mouseDragged(MouseEvent e) {
						onEvent(new MouseMotionEvent(e.getX(), e.getY(), true));
					}
				});
	}

	
	private void init(String name) {
		frame = new JFrame(name);
		if (name.equals("MainForm")) {
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setMinimumSize(new Dimension(1490,650));
		} else frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.add(this);
		frame.pack();
		if (parseBoolean(configuration.getValue("CENTERED"))) frame.setLocationRelativeTo(null);
		frame.setResizable(parseBoolean(configuration.getValue("RESIZABLE")));
		frame.setVisible(true);
	}


	private void render(){
		gBufferStrategy = getBufferStrategy();
		Graphics g = gBufferStrategy.getDrawGraphics();
		try{
			for (Element element : elements)
				element.onRender(g);
			g.dispose();
			gBufferStrategy.show();
			sleep(1000/tempRefreshRate);
		} catch (Exception ignored) {}
		invokeLater(this::render);
	}


	private void onEvent(Event event) {
		 for (int i = elements.size() - 1; i >= 0 ; i--)
		 	elements.get(i).onEvent(event);
	}


	@Override
	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WINDOW_CLOSING));
	}


	@Override
	public void construct(Element element) {
		elements.add(element);
	}


	@Override
	public void destruct(Element element) {
		element.onDestroy();
		try {
			sleep(element.deleteTime * 1000);
		} catch (Exception ignored) {}
		elements.remove(element);
	}

}
