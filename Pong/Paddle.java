package Pong;

import Zen.*;

public class Paddle extends ZenSprite {
	
	Rectangle paddle;
	String upkey;
	String downkey;
	
	public Paddle(int x, int y, String color) {
		paddle = new Rectangle(10, 100, color);
		set(x, y);
	}
	
	public void setKeys(String up, String down) {
		upkey = up;
		downkey = down;
	}
	
	@Override
	public void draw() {
		paddle.set(getX() - 5, getY() - 50);
		Zen.draw(paddle);
	}

	@Override
	public void move() {
		if (Zen.isKeyPressed(upkey)) {
			changeY(-5);
		}
		if (Zen.isKeyPressed(downkey)) {
			changeY(5);
		}
	}
	
}
