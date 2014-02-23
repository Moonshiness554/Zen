package SenseiJump;

import Zen.*;


public class Platform extends ZenSprite {

	public Platform(int x, int y) {
		set(x, y);
	}
	
	@Override
	public void draw() {
		Zen.setColor("brown");
		Zen.fillRect(getX() - 50, getY(), 100, 10);
	}

	@Override
	public void move() {
		
	}

}
