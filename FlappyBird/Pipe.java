package FlappyBird;

import Zen.*;

/**
 * A Pipe represents the green pipe that moves toward you in a game of Flappy Bird.
 * @author (your name)
 */
public class Pipe extends ZenSprite {	
	
	int gap;
	
	Shape myself;
	
	public Pipe() {
		gap = Zen.getRandomNumber(100, 450);	// Get a random number between 100 and 450
		
		Rectangle topOutline = new Rectangle(-2, 0, 54, gap - 73, "black");
		Rectangle top = new Rectangle(0, 0, 50, gap - 75, "pipe green");
		Rectangle topLeftShadow = new Rectangle(0, 0, 10, gap - 75, "pipe light green");
		Rectangle topRightShadow = new Rectangle(40, 0, 10, gap - 75, "pipe dark green");
		Rectangle bottomOutline = new Rectangle(-2, gap + 73, 54, 550 - (gap + 73), "black");
		Rectangle bottom = new Rectangle(0, gap + 75, 50, 550 - (gap + 75), "pipe green");
		Rectangle bottomLeftShadow = new Rectangle(0, gap + 75, 10, 550 - (gap + 75), "pipe light green");
		Rectangle bottomRightShadow = new Rectangle(40, gap + 75, 10, 550 - (gap + 75), "pipe dark green");
		myself = new Shape(topOutline, top, topLeftShadow, topRightShadow, 
						   bottomOutline, bottom, bottomLeftShadow, bottomRightShadow);
		
		set(450, 0);
	}
	
	/**
	 * How should this object move during each time step?
	 */
	public void move() {
		changeX(-1 * FlappyBird.SPEED);
	}
	
	/**
	 * How should this object draw itself at each time step?
	 */
	public void draw() {
		myself.set(getX(), getY());
		myself.draw();
	}
}
