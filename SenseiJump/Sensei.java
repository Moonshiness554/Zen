package SenseiJump;

import Zen.*;

public class Sensei extends ZenSprite {
	
	Shape myself;
	int leftArmOffset = 0;
	
	int velocity = 0;
	int jumps = 0;
	boolean jumping = false;
	
	World w;
	
	public Sensei() {
		Circle head = new Circle(0, 0, 25, "tan");
		Shape eye = new Shape(
				new Circle(8, 0, 6, "white"),
				new Circle(8, 0, 5, "black")
			);
		Triangle hat = new Triangle(-20, -5, 0, -15, 20, -5, "brown");
		Oval body = new Oval(0, 30, 20, 40, "black");
		Oval torso = new Oval(0, 50, 16, 25, "black");
		Rectangle staff = new Rectangle(15, 0, 3, 60, "brown");
		Line leftArm = new Line(5, 20, 20, 25, 8, "black");
		Line rightArm = new Line(-5, 20, -15, 35, 8, "black");
		Oval foot = new Oval(5, 60, 20, 5, "tan");
		myself = new Shape(head, eye, hat, body, torso, staff, leftArm, rightArm, foot);
	}
	
	@Override
	public void draw() {
		myself.set(getX(), getY());
		Zen.draw(myself);
	}

	@Override
	public void move() {
		if (Zen.isKeyPressed("right")) {
			setX( getX() + SenseiJump.SPEED );
		}
		if (Zen.isKeyPressed("left")) {
			setX( getX() - SenseiJump.SPEED );
		}
		
		if ( jumps == 0 && Zen.isKeyPressed("space")) {
			jumps++;
			velocity = SenseiJump.FIRST_JUMP;
			setY(getY() + velocity);
		}
		if ( jumps == 1 && ! Zen.isKeyPressed("space")) {
			jumps++;
		}
		if ( jumps == 2 && Zen.isKeyPressed("space")) {
			jumps++;
			velocity = SenseiJump.SECOND_JUMP;
			setY(getY() + velocity);
		}
		velocity += SenseiJump.GRAVITY;
		
		// Account for gravity
		int level = SenseiJump.GROUND;
		Platform p = w.getNearestPlatform(getX(), getY() + 60);
		
		if (p.getY() - 60 == getY() && p.getX() - 60 < getX() && p.getX() + 60 > getX()) {
			level = p.getY() - 60;
		}
		else {
			if ( velocity >= 0 
					&& getY() + 60 < p.getY() && getY() + 60 > p.getY() - 60 
					&& getX() < p.getX() + 60 && getX() > p.getX() - 60) {
				level = p.getY() - 60;
			}
		}
		if (getY() + velocity <= level) {
			setY(getY() + velocity);
		}
		else {
			setY(level);
			velocity = 0;
			jumps = 0;
		}
	}

}
