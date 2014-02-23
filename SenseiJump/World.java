package SenseiJump;

import java.util.ArrayList;

import Zen.*;

public class World extends ZenSprite {

	ArrayList <Platform> platforms;
	
	public World() {
		platforms = new ArrayList <Platform> ();
	}
	
	public void addPlatform(Platform p) {
		platforms.add(p);
	}
	
	public Platform getNearestPlatform(int x, int y) {
		double min = Double.MAX_VALUE;
		Platform closest = null;
		for (Platform p : platforms) {
			double distance = Math.sqrt( Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
			if (distance < min) {
				min = distance;
				closest = p;
			}
		}
		return closest;
	}

	@Override
	public void draw() {
		for (Platform p : platforms) {
			p.draw();
		}
	}

	@Override
	public void move() {
		for (Platform p : platforms) {
			p.move();
		}
	}
}
