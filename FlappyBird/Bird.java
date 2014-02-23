package FlappyBird;

import Zen.*;

/**
 * A Pipe represents the green pipe that moves toward you in a game of Flappy Bird.
 * @author (your name)
 */
public class Bird extends ZenSprite {
	
	int velocity = 0;			// The downward velocity of the bird
	boolean pressed = false;	// Is the space key pressed	
	
	Image myself;
	
	public Bird() {
		myself = new Image("FlappyBird/flappy.png");
	}
	
	/**
	 * How should this object move during each time step?
	 */
	public void move() {
		if (! pressed && Zen.isKeyPressed("space")) {
			velocity = -1 * FlappyBird.THRUST;
		}
		if (! Zen.isKeyPressed("space")) {
			pressed = false;
		}
		velocity += FlappyBird.GRAVITY;
		if (getY() + velocity < FlappyBird.GROUND) {
			changeY(velocity);
		}
		else {
			setY(FlappyBird.GROUND);
		}
	}
	
	/**
	 * How should this object draw itself at each time step?
	 */
	public void draw() {
		myself.set(getX(), getY());
		myself.draw();
	}
}