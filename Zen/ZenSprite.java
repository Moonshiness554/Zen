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
	
	/**
	 * Returns the distance between this sprite and another sprite.
	 */
	public double distanceTo(ZenSprite other) {
		return Math.sqrt(Math.pow(this.rawX() - other.rawX(), 2) + Math.pow(this.rawY() - other.rawY(), 2));
	}
}
