package Zen;

public abstract class ZenSprite extends Point {
	
	public ZenSprite() {
		super();
	}

	public ZenSprite(int x, int y) {
		super(x, y);
	}
	
	public ZenSprite(double x, double y) {
		super(x, y);
	}
	
	public abstract void draw();
	public abstract void move();
}
