package Pong;

import Zen.Circle;
import Zen.Zen;
import Zen.ZenSprite;

public class Ball extends ZenSprite {
	
	double xspeed, yspeed;
	
	Circle ball;
	
	public Ball(int x, int y) {
		set(x, y);
		ball = new Circle(20, "black");
	}
	
	public void goLeft() {
		xspeed = -5;
		yspeed = Math.random() * 10 - 5;
	}
	
	public void goRight() {
		xspeed = 5;
		yspeed = Math.random() * 10 - 5;
	}
	
	@Override
	public void draw() {
		ball.set(getX(), getY());
		Zen.draw(ball);
	}

	@Override
	public void move() {
		if (getX() + xspeed > 0 && getX() + xspeed < Pong.WIDTH) {
			changeX(xspeed);
		}
		else if (getX() + xspeed <= 0) {
			bounceHorizontally(0);
		}
		else {
			bounceHorizontally(Pong.WIDTH);
		}
		
		if (getY() + yspeed > 0 && getY() + yspeed < Pong.HEIGHT) {
			changeY(yspeed);
		}
		else if (getY() + yspeed <= 0) {
			bounceVertically(0);
		}
		else {
			bounceVertically(Pong.HEIGHT);
		}
	}
	
	public void bounceHorizontally(int horizontal) {
		xspeed = xspeed * -1.1;
		changeX(xspeed * 2);
	}
	
	public void bounceVertically(int vertical) {
		yspeed = yspeed * -1.1;
		changeY(yspeed * 2);
	}

}
