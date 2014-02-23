package Zen;

public abstract class ZenSprite {
	private int x, y;
	
	public ZenSprite() {
		this(0, 0);
	}

	public ZenSprite(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void draw();
	public abstract void move();
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() { 
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
