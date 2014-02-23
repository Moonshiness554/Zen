package FirePad;

import Zen.Zen;

public class Pen {
	int x;
	int y;
	int prevX;
	int prevY;
	boolean down;
	String name;
	
	public Pen(String name) {
		this.name = name;
		x = Zen.readInt(name + "x");
		y = Zen.readInt(name + "y");
		down = false;
	}
	
	public void move() {
		prevX = x;
		prevY = y;
		x = Zen.readInt(name + "x");
		y = Zen.readInt(name + "y");
		if (Zen.readInt(name + "down") == 1) {
			down = true;
		}
		else {
			down = false;
		}
	}
	
	public void draw() {
		if (down) {
			Zen.setColor(Zen.readInt(name + "red"), Zen.readInt(name + "green"), Zen.readInt(name + "blue"));
			Zen.drawLine(prevX, prevY, x, y);
		}
	}
}
