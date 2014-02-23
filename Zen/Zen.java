package Zen;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;               
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Easy graphics for Java students and programmers interested in creating Java
 * graphical programs quickly. The more astute programmer may notice some odd
 * design choices - The design decisions (such as using class methods) is there
 * to simplify common use cases. This code is supplied 'as is' and no warranty
 * is provided.
 * 
 * This code is copyright but free to use. It is provided under the scripts are
 * provided under the "Attribution Creative Commons License"
 * http://creativecommons.org/licenses/by/3.0/ You may use it in any way
 * provided you preserve the following copyright statement and included it in
 * copyright information about your program. "Zen Graphics" Copyright Lawrence
 * Angrave 2010.
 * 
 * For an introduction to Zen Graphics Programming see the included examples.
 * 
 * Notes for multi-threaded programmers (advanced). Zen Graphics uses
 * thread-local storage to tie a particular applet instance to a thread. Thus
 * beginning programmers can use static class methods Zen.drawText etc yet the
 * Zen Graphics will still function correctly in multi-threaded environments
 * such as web browser running multiple Zen applets. If you create a multi-threaded
 * programmer you must either always call Zen methods from the original main() thread
 * or, use the Zen.Master object returned by Zen.create().
 * 
 * Startup notes (advanced): If started as an applet the applet container
 * automatically calls init() and typically not in the GUI thread. The init
 * method notices that 'instance' is null and creates a new background thread to
 * call the 'main' method.
 * 
 * If started from a main method. The user's program will at some point try to
 * draw something. This causes getInstance() to be invoked which lazily creates
 * an object instance and calls init. If the applet is stopped we should close
 * the main thread gracefully.
 * 
 * @author angrave
 * 
 */
@SuppressWarnings("serial")
public class Zen extends JApplet {
	
	/**
	 * Firebase connectivity.
	 */
	
	private static Firebase fb;
	private static boolean fb_connected = false;
	private static Map <String, String> data;
	
	public static void connect(String firebase) {
		Zen.connect(firebase, true);
	}
	
	public static void connect(String firebase, boolean block) {
		fb = new Firebase("https://" + firebase + ".firebaseio.com");
		data = new HashMap <String, String> ();
		fb.addValueEventListener(new ValueEventListener() {
			@SuppressWarnings("unchecked")
			@Override
		    public void onDataChange(DataSnapshot snap) {
				fb_connected = true;
		    	if (snap.getValue() != null) {
		    		try {
		    			synchronized (data) {
			    			Map <String, Object> snapData = (Map <String, Object>) snap.getValue();
			    			for (Map.Entry<String, Object> entry : snapData.entrySet())
				    	        data.put(entry.getKey().toString(), entry.getValue().toString());
		    			}
		    		}
		    		catch (ClassCastException e) { }
		    	}
		    }

			@Override
			public void onCancelled(FirebaseError arg) {
			}
		});
		
		while (! Zen.connected())
			Zen.sleep(100);
	}
	
	public static boolean connected() {
		return fb_connected;
	}
	
	public static void write(String key, boolean value) {
		data.put(key, "" + value);
		fb.child(key).setValue(value);
	}
	
	public static void write(String key, int value) {
		data.put(key, "" + value);
		fb.child(key).setValue(value);
	}
	
	public static void write(String key, double value) {
		data.put(key, "" + value);
		fb.child(key).setValue(value);
	}
	
	public static void write(String key, long value) {
		data.put(key, "" + value);
		fb.child(key).setValue(value);
	}
	
	public static void write(String key, String value) {
		data.put(key, value);
		fb.child(key).setValue(value);
	}
	
	public static boolean readBoolean(String key) {
		String str = Zen.read(key);
		if (str != null) {
			return Boolean.parseBoolean(str);
		}
		return false;
	}
	
	public static int readInt(String key) {
		String str = Zen.read(key);
		if (str != null)
			return Integer.parseInt(str);
		return 0;
	}
	
	public static long readLong(String key) {
		String str = Zen.read(key);
		if (str != null)
			return Long.parseLong(str);
		return 0;
	}
	
	public static double readDouble(String key) {
		String str = Zen.read(key);
		if (str != null)
			return Double.parseDouble(str);
		return 0;
	}
	
	public static String read(String key) {
		synchronized (data) {
			if (data.containsKey(key))
				return data.get(key);
			else return null;
		}
	}
	
	public static final Dimension DEFAULT_SIZE = new Dimension(640, 480);
	public static final String DEFAULT_OPTIONS = "";
	protected static final boolean VERBOSE = false;

	private static boolean initialized = false, menuInitialized = false;
	private static Map <String, Integer> keyMap;
	private static Map <String, Integer> colorMap;
	private static Map <String, Map <String, String>> actionMap;
	private static ArrayList <String> actionQueue;
	private static int width, height;
	private static String windowName;
	
	public static ZenInstance create(int width, int height) {
		return create(width, height, "");
	}
	
	public static ZenInstance create(int width, int height, String options) {
		if (! initialized) {
			initialize();
			Zen.width = width;
			Zen.height = height;
		}
		if (!mustBeAnWebApplet)
			mustBeAnApplication = true;
		synchronized (Zen.class) {
			ZenInstance instance = instanceMap.get();
			if (instance == null) { // no instance set for this thread
				JFrame frame = new JFrame((Zen.windowName != null) ? Zen.windowName : "Zen");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (Zen.menuInitialized)
					frame.setJMenuBar(Zen.generateMenuBar());
				// System.err.println("Creating Instance");
				Zen zen = new Zen();
				zen.bufferSize = new Dimension(width, height);
				zen.bufferOptions = options;
				instanceMap.set(zen.master);
				Container pane = frame.getContentPane();
				pane.add(zen);
				pane.setSize(zen.getSize());
				pane.setMinimumSize(zen.getSize());
				// frame.getContentPane().setIgnoreRepaint(true);
				zen.init();
				frame.pack();
				frame.setVisible(true);

				zen.start();
				return zen.master;
			}
			return instance;
		}
	}
	
	private static JMenuBar generateMenuBar() {
		JMenuBar bar = new JMenuBar();
		for (Map.Entry<String, Map<String, String>> actionMenu : actionMap.entrySet()) {
			JMenu menu = new JMenu(actionMenu.getKey());
			for (Map.Entry<String, String> actionItem : actionMenu.getValue().entrySet()) {
				JMenuItem item = new JMenuItem(actionItem.getKey());
				item.setActionCommand(actionItem.getValue());
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Zen.pushAction(e.getActionCommand());
					}
				});
				menu.add(item);
			}
			bar.add(menu);
		}
		return bar;
	}
	
	public static boolean hasAction() {
		return Zen.actionQueue.size() > 0;
	}
	
	public static String getAction() {
		if (Zen.actionQueue.size() > 0)
			return Zen.actionQueue.remove(0);
		else return null;
	}
	
	public static void pushAction(String action) {
		Zen.actionQueue.add(action);
	}
	
	public static void menu(String menu, String name, String action) {
		if (! menuInitialized) {
			actionMap = new HashMap <String, Map <String, String>> ();
			actionQueue = new ArrayList <String> ();
			menuInitialized = true;
		}
		if (menu != null && name != null && action != null) {
			if (! actionMap.containsKey(menu))
				actionMap.put(menu, new HashMap <String, String> ());
			actionMap.get(menu).put(name, action);
		}
	}
	
	private static void initialize() {
		keyMap = new HashMap <String, Integer> ();
		keyMap.put("space", KeyEvent.VK_SPACE);
		keyMap.put("left", KeyEvent.VK_LEFT);
		keyMap.put("right", KeyEvent.VK_RIGHT);
		keyMap.put("up", KeyEvent.VK_UP);
		keyMap.put("down", KeyEvent.VK_DOWN);
		keyMap.put("escape", KeyEvent.VK_ESCAPE);
		keyMap.put("tab", KeyEvent.VK_TAB);
		keyMap.put("shift", KeyEvent.VK_SHIFT);
		keyMap.put("control", KeyEvent.VK_CONTROL);
		keyMap.put("alt", KeyEvent.VK_ALT);
		keyMap.put("delete", KeyEvent.VK_DELETE);
		keyMap.put("home", KeyEvent.VK_HOME);
		colorMap = new HashMap <String, Integer> ();
		initializeColor("alice blue", 240, 248, 255);
		initializeColor("antique white", 250, 235, 215);
		initializeColor("aqua", 0, 255, 255);
		initializeColor("aquamarine", 127, 255, 212);
		initializeColor("azure", 240, 255, 255);
		initializeColor("beige", 245, 245, 220);
		initializeColor("bisque", 255, 228, 196);
		initializeColor("black", 0, 0, 0);
		initializeColor("blanched almond", 255, 235, 205);
		initializeColor("blue", 0, 0, 255);
		initializeColor("blue violet", 138, 43, 226);
		initializeColor("brown", 165, 42, 42);
		initializeColor("burlywood", 222, 184, 135);
		initializeColor("cadet blue", 95, 158, 160);
		initializeColor("chartreuse", 127, 255, 0);
		initializeColor("chocolate", 210, 105, 30);
		initializeColor("coral", 255, 127, 80);
		initializeColor("cornflower blue", 100, 149, 237);
		initializeColor("cornsilk", 255, 248, 220);
		initializeColor("cyan", 0, 255, 255);
		initializeColor("dark blue", 0, 0, 139);
		initializeColor("dark cyan", 0, 139, 139);
		initializeColor("dark goldenrod", 184, 134, 11);
		initializeColor("dark gray", 169, 169, 169);
		initializeColor("dark green", 0, 100, 0);
		initializeColor("dark khaki", 189, 183, 107);
		initializeColor("dark magenta", 139, 0, 139);
		initializeColor("dark olive green", 85, 107, 47);
		initializeColor("dark orange", 255, 140, 0);
		initializeColor("dark orchid", 153, 50, 204);
		initializeColor("dark red", 139, 0, 0);
		initializeColor("dark salmon", 233, 150, 122);
		initializeColor("dark sea green", 143, 188, 143);
		initializeColor("dark slate blue", 72, 61, 139);
		initializeColor("dark slate gray", 47, 79, 79);
		initializeColor("dark turquoise", 0, 206, 209);
		initializeColor("dark violet", 148, 0, 211);
		initializeColor("deep pink", 255, 20, 147);
		initializeColor("deep sky blue", 0, 191, 255);
		initializeColor("dim gray", 105, 105, 105);
		initializeColor("dodger blue", 30, 144, 255);
		initializeColor("firebrick", 178, 34, 34);
		initializeColor("floral white", 255, 250, 240);
		initializeColor("forest green", 34, 139, 34);
		initializeColor("fuschia", 255, 0, 255);
		initializeColor("gainsboro", 220, 220, 220);
		initializeColor("ghost white", 255, 250, 250);
		initializeColor("gold", 255, 215, 0);
		initializeColor("goldenrod", 218, 165, 32);
		initializeColor("gray", 128, 128, 128);
		initializeColor("green", 0, 128, 0);
		initializeColor("green yellow", 173, 255, 47);
		initializeColor("honeydew", 240, 255, 240);
		initializeColor("hot pink", 255, 105, 180);
		initializeColor("indian red", 205, 92, 92);
		initializeColor("ivory", 255, 255, 240);
		initializeColor("khaki", 240, 230, 140);
		initializeColor("lavender", 230, 230, 250);
		initializeColor("lavender blush", 255, 240, 245);
		initializeColor("lawn green", 124, 252, 0);
		initializeColor("lemon chiffon", 255, 250, 205);
		initializeColor("light blue", 173, 216, 230);
		initializeColor("light coral", 240, 128, 128);
		initializeColor("light cyan", 224, 255, 255);
		initializeColor("light goldenrod", 238, 221, 130);
		initializeColor("light goldenrod yellow", 250, 250, 210);
		initializeColor("light gray", 211, 211, 211);
		initializeColor("light green", 144, 238, 144);
		initializeColor("light pink", 255, 182, 193);
		initializeColor("light salmon", 255, 160, 122);
		initializeColor("light sea green", 32, 178, 170);
		initializeColor("light sky blue", 135, 206, 250);
		initializeColor("light slate blue", 132, 112, 255);
		initializeColor("light slate gray", 119, 136, 153);
		initializeColor("light steel blue", 176, 196, 222);
		initializeColor("light yellow", 255, 255, 224);
		initializeColor("lime", 0, 255, 0);
		initializeColor("lime green", 50, 205, 50);
		initializeColor("linen", 250, 240, 230);
		initializeColor("magenta", 255, 0, 255);
		initializeColor("maroon", 128, 0, 0);
		initializeColor("medium aquamarine", 102, 205, 170);
		initializeColor("medium blue", 0, 0, 205);
		initializeColor("medium orchid", 186, 85, 211);
		initializeColor("medium purple", 147, 112, 219);
		initializeColor("medium sea green", 60, 179, 113);
		initializeColor("medium slate blue", 123, 104, 238);
		initializeColor("medium spring green", 0, 250, 154);
		initializeColor("medium turquoise", 72, 209, 204);
		initializeColor("medium violet red", 199, 21, 133);
		initializeColor("midnight blue", 25, 25, 112);
		initializeColor("mint cream", 245, 255, 250);
		initializeColor("misty rose", 255, 228, 225);
		initializeColor("moccasin", 255, 228, 181);
		initializeColor("navajo white", 255, 222, 173);
		initializeColor("navy", 0, 0, 128);
		initializeColor("old lace", 253, 245, 230);
		initializeColor("olive", 128, 128, 0);
		initializeColor("olive drab", 107, 142, 35);
		initializeColor("orange", 255, 165, 0);
		initializeColor("orange red", 255, 69, 0);
		initializeColor("orchid", 218, 112, 214);
		initializeColor("pale goldenrod", 238, 232, 170);
		initializeColor("pale green", 152, 251, 152);
		initializeColor("pale turquoise", 175, 238, 238);
		initializeColor("pale violet red", 219, 112, 147);
		initializeColor("papaya whip", 255, 239, 213);
		initializeColor("peach puff", 255, 218, 185);
		initializeColor("peru", 205, 133, 63);
		initializeColor("pink", 255, 192, 203);
		initializeColor("plum", 221, 160, 221);
		initializeColor("powder blue", 176, 224, 230);
		initializeColor("purple", 128, 0, 128);
		initializeColor("red", 255, 0, 0);
		initializeColor("rosy brown", 188, 143, 143);
		initializeColor("royal blue", 65, 105, 225);
		initializeColor("saddle brown", 139, 69, 19);
		initializeColor("salmon", 250, 128, 114);
		initializeColor("sandy brown", 244, 164, 96);
		initializeColor("sea green", 46, 139, 87);
		initializeColor("seashell", 255, 245, 238);
		initializeColor("sienna", 160, 82, 45);
		initializeColor("silver", 192, 192, 192);
		initializeColor("sky blue", 135, 206, 235);
		initializeColor("slate blue", 106, 90, 205);
		initializeColor("slate gray", 112, 128, 144);
		initializeColor("snow", 255, 250, 250);
		initializeColor("spring green", 0, 255, 127);
		initializeColor("steel blue", 70, 130, 180);
		initializeColor("tan", 210, 180, 140);
		initializeColor("teal", 0, 128, 128);
		initializeColor("thistle", 216, 191, 216);
		initializeColor("tomato", 255, 99, 71);
		initializeColor("turquoise", 64, 224, 208);
		initializeColor("violet", 238, 130, 238);
		initializeColor("violet red", 208, 32, 144);
		initializeColor("wheat", 245, 222, 179);
		initializeColor("white", 255, 255, 255);
		initializeColor("white smoke", 245, 245, 245);
		initializeColor("yellow", 255, 255, 0);
		initializeColor("yellow green", 154, 205, 50);
	}
	
	private static void initializeColor(String name, int red, int green, int blue) {
		colorMap.put(name, ((red & 0x0ff) << 16) | ((green & 0x0ff) << 8) | (blue & 0x0ff));
	}
	
	public static void setWindowName(String name) {
		Zen.windowName = name;
	}

	public static String getAboutMessage() {
		return "Zen Graphics (version 0.1) Copyright Lawrence Angrave, 2010";
	}

	public static void waitForClick() {
		getInstanceFromThread().waitForClick();
	}

	public static int getZenWidth() {
		return getInstanceFromThread().getZenWidth();
	}

	public static int getZenHeight() {
		return getInstanceFromThread().getZenHeight();
	}

	public static int getMouseClickX() {
		return getInstanceFromThread().getMouseClickX();
	}

	public static int getMouseClickY() {
		return getInstanceFromThread().getMouseClickY();
	}

	public static long getMouseClickTime() {
		return getInstanceFromThread().getMouseClickTime();
	}

	public static void setEditText(String s) {
		getInstanceFromThread().setEditText(s);
	}

	public static String getEditText() {
		return getInstanceFromThread().getEditText();
	}

	public static int getMouseButtonsAndModifierKeys() {
		return getInstanceFromThread().getMouseButtonsAndModifierKeys();
	}

	public static int getMouseX() {
		return getInstanceFromThread().getMouseX();
	}

	public static int getMouseY() {
		return getInstanceFromThread().getMouseY();
	}

	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception ignored) {
		}
	}
	
	public static boolean isKeyPressed(String key) {
		if (keyMap.containsKey(key))
			return Zen.isVirtualKeyPressed(keyMap.get(key));
		else if (key.length() > 0)
			return Zen.isKeyPressed(key.charAt(0));
		return false;
	}
	
	public static boolean isKeyPressed(char key) {
		return getInstanceFromThread().isKeyPressed(key);
	}

	public static boolean isVirtualKeyPressed(int keyCode) {
		return getInstanceFromThread().isVirtualKeyPressed(keyCode);
	}

	public static boolean isRunning() {
		return getInstanceFromThread().isRunning();
	}

	public static Graphics2D getBufferGraphics() {
		return getInstanceFromThread().getBufferGraphics();
	}

	public static void draw(ZenShape shape) {
		shape.colorAndDraw();
	}
	
	public static void drawImage(String filename, int x, int y) {
		getInstanceFromThread().drawImage(filename, x, y);
	}

	public static void drawLine(int x1, int y1, int x2, int y2) {
		getInstanceFromThread().drawLine(x1, y1, x2, y2);
	}
	
	public static void drawPolygon(int[] x, int[] y) {
		getInstanceFromThread().drawPolygon(x, y);
	}

	public static void drawText(String text, int x, int y) {
		getInstanceFromThread().drawText(text, x, y);

	}

	public static void drawArc(int x, int y, int width, int height,
			int startAngle, int arcAngle) {
		getInstanceFromThread().drawArc(x, y, width, height, startAngle,
				arcAngle);
	}

	public static void fillOval(int minX, int minY, int width, int height) {
		getInstanceFromThread().fillOval(minX, minY, width, height);
	}

	public static void fillRect(int x1, int y1, int width, int height) {
		getInstanceFromThread().fillRect(x1, y1, width, height);
	}
	
	public static void fillPolygon(int[] x, int[] y) {
		getInstanceFromThread().fillPolygon(x, y);
	}
	
	public static void setBackground(String color) {
		if (color != null) {
			Zen.setColor(color);
			Zen.fillRect(0, 0, Zen.width, Zen.height);
		}
	}
	
	public static void setColor(String color) {
		if (color != null && colorMap.containsKey(color.toLowerCase())) {
			int c = colorMap.get(color.toLowerCase());
			Zen.setColor((c >> 16) & 0xff, (c >> 8) & 0xff, c & 0xff);
		}
		else Zen.setColor(0, 0, 0);
	}

	public static void setColor(int red, int green, int blue) {
		getInstanceFromThread().setColor(red, green, blue);
	}

	public static int bound(int value, int min, int max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}
	
	public static int getRandomNumber(int max) {
		return (int) (Math.random() * max + 1);
	}
	
	public static int getRandomNumber(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	public static Font setFont(String fontname, int size) {
		return Zen.setFont(fontname + "-" + size);
	}
	
	public static Font setFont(String fontname) {
		return getInstanceFromThread().setFont(fontname);
	}

	public static Image getCachedImage(String filename) {
		return getInstanceFromThread().getCachedImage(filename);
	}

	public static void buffer(int ms) {
		Zen.flipBuffer();
		Zen.sleep(ms);
	}
	
	public static void flipBuffer() {
		getInstanceFromThread().flipBuffer();
	}

	/*
	 * ----------------- Instance Methods -------------------------
	 */
	class ZenInstance {
		public void waitForClick() {
			long t = mouseClickTime;
			// Todo: remove polling and use synchronization wait lock
			while (isRunning && t == mouseClickTime)
				sleep(250);
		}

		public int getZenWidth() {
			return bufferSize.width;
		}

		public int getZenHeight() {
			return bufferSize.height;
		}

		public int getMouseClickX() {
			return mouseClickX;
		}

		public int getMouseClickY() {
			return mouseClickY;
		}

		public long getMouseClickTime() {
			return mouseClickTime;
		}

		public void setEditText(String s) {
			editText = new StringBuilder(s);
		}

		public String getEditText() {
			return editText.toString();
		}

		public int getMouseButtonsAndModifierKeys() {
			return mouseButtonsAndModifierKeys;
		}

		public int getMouseX() {
			return mouseX;
		}

		public int getMouseY() {
			return mouseY;
		}
		
		public boolean isMouseClicked() {
			return isMouseClicked;
		}

		public boolean isKeyPressed(char key) {
			return key >= 0 && key < keyPressed.length ? keyPressed[key]
					: false;
		}

		public boolean isVirtualKeyPressed(int keyCode) {
			return keyCode >= 0 && keyCode < virtualKeyPressed.length ? virtualKeyPressed[keyCode]
					: false;
		}

		public boolean isRunning() {
			return isRunning;
		}

		public Graphics2D getBufferGraphics() {
			// getSingleton(); // ensure instance created
			while (g == null) {
				System.err
						.println("Odd... graphics not yet ready! Sleeping...");
				sleep(1000); // race-condition hack ; should never happen if the
				// container is correctly implemented
			}
			return g;
		}

		public void drawImage(String filename, int x, int y) {
			Graphics2D g = getBufferGraphics();
			Image img = getCachedImage(filename);
			if (img != null)
				g.drawImage(img, x, y, null);
			else
				g.drawString(filename + "?", x, y);
			if (paintImmediately)
				paintWindowImmediately();
		}

		public void drawLine(int x1, int y1, int x2, int y2) {
			getBufferGraphics().drawLine(x1, y1, x2, y2);
			if (paintImmediately)
				paintWindowImmediately();
		}

		public void drawText(String text, int x, int y) {
			getBufferGraphics().drawString(text, x, y);
			if (paintImmediately)
				paintWindowImmediately();
		}
		
		public void drawPolygon(int[] x, int[] y) {
			getBufferGraphics().drawPolygon(x, y, x.length);
			if (paintImmediately)
				paintWindowImmediately();
		}

		public void drawArc(int x, int y, int width, int height,
				int startAngle, int arcAngle) {
			getBufferGraphics().drawArc(x, y, width, height, startAngle, arcAngle);
			if (paintImmediately)
				paintWindowImmediately();
		}

		public void fillOval(int minX, int minY, int width, int height) {
			getBufferGraphics().fillOval(minX, minY, width, height);
			if (paintImmediately)
				paintWindowImmediately();
		}

		public void fillRect(int x1, int y1, int width, int height) {
			getBufferGraphics().fillRect(x1, y1, width, height);
			if (paintImmediately)
				paintWindowImmediately();
		}
		
		public void fillPolygon(int[] x, int[] y) {
			getBufferGraphics().fillPolygon(x, y, x.length);
			if (paintImmediately)
				paintWindowImmediately();
		}

		public void setColor(int red, int green, int blue) {
			currentColor = new Color(bound(red, 0, 255), bound(green, 0, 255),
					bound(blue, 0, 255));
			getBufferGraphics().setColor(currentColor);
		}

		public Font setFont(String font) {
			currentFont = Font.decode(font);
			getBufferGraphics().setFont(currentFont);
			return currentFont;
		}

		public Image getCachedImage(String filename) {
			Image img = nameToImage.get(filename);
			if (img != null)
				return img;
			try {
				InputStream is = Zen.class.getResourceAsStream(filename);
				img = ImageIO.read(is);
				is.close();
				nameToImage.put(filename, img);
				return img;
			} catch (Exception ex) {
				// System.err.println("Can't load '" + filename + "' : "
				// + ex.getMessage());
				return null;
			}
		}

		public void flipBuffer() {
			// Both flipBuffer and portions of paint() are synchronized
			// on the class object to ensure
			// that both cannot execute at the same time.
			paintImmediately = false; // user has called flipBuffer at least
			// once
			// getSingleton();
			synchronized (Zen.this) {
				Image temp = backImageBuffer;
				backImageBuffer = frontImageBuffer;
				frontImageBuffer = temp;

				if (g != null)
					g.dispose();
				paintWindowImmediately(); // paint to Video

				g = (Graphics2D) backImageBuffer.getGraphics();
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, backImageBuffer.getWidth(null),
						backImageBuffer.getHeight(null));
				g.setColor(currentColor);
				g.setFont(currentFont);
			}
		}

		void createBuffers(int width, int height, String options) {
			if (g != null)
				g.dispose();
			if (frontImageBuffer != null)
				frontImageBuffer.flush();
			if (backImageBuffer != null)
				backImageBuffer.flush();
			options = options != null ? options.toLowerCase() : "";
			bufferSize = new Dimension(width, height);
			stretchToFit = options.contains("stretch");

			// if buffers are requested _after_ the window has been realized
			// then faster volatile images are possible
			// BUT volatile images silently fail when tested Vista IE8 and
			// JRE1.6
			boolean useVolatileImages = false;
			if (useVolatileImages) {
				try {
					// Paint silently fails when tested in IE8 Vista JRE1.6.0.14
					backImageBuffer = createVolatileImage(width, height);
					frontImageBuffer = createVolatileImage(width, height);
				} catch (Exception ignored) {

				}
			}
			if (!GraphicsEnvironment.isHeadless()) {
				try {
					GraphicsConfiguration config = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getDefaultScreenDevice().getDefaultConfiguration();
					backImageBuffer = config.createCompatibleImage(width,
							height);
					frontImageBuffer = config.createCompatibleImage(width,
							height);
				} catch (Exception ignored) {
				}
			}

			// as a fall-back we can still use slower BufferedImage with
			// arbitrary RGB model
			if (frontImageBuffer == null) {
				// System.err.println("Creating BufferedImage buffers");
				backImageBuffer = new BufferedImage(bufferSize.width,
						bufferSize.height, BufferedImage.TYPE_INT_RGB);
				frontImageBuffer = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
			}
			master.flipBuffer();// set up graphics, including font and color
			// state
			paintImmediately = true; // actually, user has not yet called
			// flipBuffer
		}

	}; // End class ZenMaster

	/*
	 * --------------------- IMPLEMENTATION ----------------------
	 */
	private static ThreadLocal<ZenInstance> instanceMap = new ThreadLocal<ZenInstance>();

	private static synchronized ZenInstance getInstanceFromThread() {
		ZenInstance instance = instanceMap.get();
		return instance != null ? instance : create(DEFAULT_SIZE.width,
				DEFAULT_SIZE.height, DEFAULT_OPTIONS);
	}

	private static boolean mustBeAnApplication; // true if create called init
	private static boolean mustBeAnWebApplet; // true if init called before
	// create
	private ZenInstance master = new ZenInstance();

	private Graphics2D g;
	private Image backImageBuffer, frontImageBuffer;
	private Map<String, Image> nameToImage = Collections
			.synchronizedMap(new HashMap<String, Image>());

	private boolean stretchToFit;
	private boolean[] keyPressed = new boolean[256];
	private boolean[] virtualKeyPressed = new boolean[1024];

	private int mouseX, mouseY, mouseClickX, mouseClickY;
	private long mouseClickTime;
	private boolean isMouseClicked;
	private StringBuilder editText = new StringBuilder();
	private Dimension bufferSize = new Dimension(DEFAULT_SIZE);
	private String bufferOptions = DEFAULT_OPTIONS;
	private Color currentColor = Color.WHITE;
	private Font currentFont = Font.decode("Times-18");
	private int mouseButtonsAndModifierKeys;
	private boolean isRunning = true;
	private Thread mainThread;
	private int paintAtX, paintAtY, windowWidth, windowHeight;
	protected boolean paintImmediately;

	@Override
	public Dimension getMinimumSize() {
		return bufferSize;
	}

	@Override
	public final Dimension getPreferredSize() {
		return getMinimumSize();
	}

	// JApplet methods
	@Override
	public final void init() {
		if (!mustBeAnApplication)
			mustBeAnWebApplet = true;

		instanceMap.set(master);

		setSize(bufferSize);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				addMouseListener(mouseListener);
				addMouseMotionListener(mouseMotionListener);
				addKeyListener(keyListener);
				setFocusTraversalKeysEnabled(false);
				setFocusable(true);
				setVisible(true);
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			} // run
		});
	}

	@Override
	@SuppressWarnings("deprecation")
	public final void stop() {
		isRunning = false;
		if (mainThread == null)
			return;
		mainThread.interrupt();
		sleep(500);
		if (mainThread.isAlive())
			mainThread.stop();
		mainThread = null;
	}

	@Override
	public final void start() {
		master
				.createBuffers(bufferSize.width, bufferSize.height,
						bufferOptions);
		isRunning = true;
		if (mustBeAnWebApplet) {
			mainThread = new Thread("main") {

				@Override
				public void run() {
					if (VERBOSE)
						System.out.println("Starting main thread...");
					try {
						instanceMap.set(Zen.this.master);
						String paramKey = "zen-main-class";
						String classNameToRunForApplet = getParameter(paramKey);
						if (classNameToRunForApplet == null) {
							String mesg = paramKey
									+ " parameter is not set; where is your main method?";
							System.err.println(mesg);
							setFont("Courier-12");
							drawText(mesg, 0, 10);
							return;
						}
						Class<?> clazz = Class.forName(classNameToRunForApplet);
						String[] argValue = new String[0];
						Class<?>[] argTypes = { argValue.getClass() };
						Method main = clazz.getMethod("main", argTypes);
						// System.err.println(clazz + ".main()...");
						main.invoke(null, new Object[] { argValue });

					} catch (ThreadDeath ignored) {
					} catch (Exception ex) {
						System.err.println("Exception:" + ex.getMessage());
						ex.printStackTrace();
						setFont("Courier-12");
						drawText(ex.toString(), 0, 12);
					} finally {
						instanceMap.remove();
					}

				} // end method
			};
			mainThread.start();
		}
	}

	@Override
	public final void destroy() {
		super.destroy();
	}

	@Override
	public void update(Graphics windowGraphics) {
		paint(windowGraphics);
	}

	@Override
	public void paint(Graphics windowGraphics) {
		if (windowGraphics == null)
			return;
		windowWidth = getWidth();
		windowHeight = getHeight();

		if (frontImageBuffer == null) {
			// no image to display
			windowGraphics.clearRect(0, 0, windowWidth, windowHeight);
			return;
		}
		synchronized (Zen.class) {
			Image image = paintImmediately ? backImageBuffer : frontImageBuffer;
			if (stretchToFit) {
				paintAtX = paintAtY = 0;
				windowGraphics.drawImage(image, 0, 0, windowWidth,
						windowHeight, this);
			} else { // Blacken unused sides
				int x = windowWidth - bufferSize.width;
				int y = windowHeight - bufferSize.height;
				paintAtX = x / 2;
				paintAtY = y / 2;
				windowGraphics.setColor(Color.BLACK);
				// Notes: Some of the +1's may be unnecessary.
				// Notes: Actually there's some overlap in the 4 corners that
				// could
				// be removed
				if (y > 0) {
					windowGraphics.fillRect(0, 0, windowWidth + 1, paintAtY);
					windowGraphics.fillRect(0, windowHeight - paintAtY - 1,
							windowWidth + 1, paintAtY + 1);
				}
				if (x > 0) {
					windowGraphics.fillRect(0, 0, paintAtX + 1,
							windowHeight + 1);
					windowGraphics.fillRect(windowWidth - paintAtX - 1, 0,
							paintAtX + 1, windowHeight + 1);
				}
				windowGraphics.drawImage(image, paintAtX, paintAtY, this);
			}
		}
	} // paint

	private void paintWindowImmediately() {
		Graphics windowGraphics = getGraphics();
		// IE8 windowGraphics is not null
		if (VERBOSE)
			System.err.println("paintWindowImmediately graphics="
					+ windowGraphics);
		if (windowGraphics != null)
			paint(getGraphics());
		else
			repaint();
	}

	private KeyListener keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			char c = e.getKeyChar(); // may be CHAR_UNDEFINED
			mouseButtonsAndModifierKeys = e.getModifiersEx();
			if (c >= 0 && c < keyPressed.length)
				keyPressed[c] = true;
			int vk = e.getKeyCode();
			if (vk >= 0 && vk < virtualKeyPressed.length)
				virtualKeyPressed[vk] = true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar(); // may be CHAR_UNDEFINED
			mouseButtonsAndModifierKeys = e.getModifiersEx();
			if (c >= 0 && c < keyPressed.length)
				keyPressed[c] = false;
			int vk = e.getKeyCode();
			if (vk >= 0 && vk < virtualKeyPressed.length)
				virtualKeyPressed[vk] = false;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			char typed = e.getKeyChar();
			if (!Character.isISOControl(typed))
				editText.append(typed);
			else if (typed == 8 && editText.length() > 0)
				editText.deleteCharAt(editText.length() - 1);
		}
	}; // KeyListener
	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent me) {
			if (windowWidth == 0 || windowHeight == 0)
				return; // no display window yet
			mouseClickX = (stretchToFit ? (int) (0.5 + me.getX()
					* bufferSize.width / (double) windowWidth) : me.getX()
					- paintAtX);
			mouseClickY = (stretchToFit ? (int) (0.5 + me.getY()
					* bufferSize.height / (double) windowHeight) : me.getY()
					- paintAtY);
			mouseClickTime = me.getWhen();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			isMouseClicked = true;
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			isMouseClicked = false;
		}

	}; // MouseListener
	private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
		@Override
		public void mouseMoved(MouseEvent me) {
			if (windowWidth == 0 || windowHeight == 0)
				return; // no display window yet
			mouseX = (stretchToFit ? (int) (0.5 + me.getX() * bufferSize.width
					/ (double) windowWidth) : me.getX() - paintAtX);
			mouseY = (stretchToFit ? (int) (0.5 + me.getY() * bufferSize.height
					/ (double) windowHeight) : me.getY() - paintAtY);
			mouseButtonsAndModifierKeys = me.getModifiersEx();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			mouseListener.mouseClicked(e);
			mouseMoved(e);
		}
	}; // MouseMotionListener
};// End of class