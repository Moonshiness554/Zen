package Zen;

public class Point {
	private int x, y;
	
	public Point() {
		this(0, 0);
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void set(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void change(int x, int y) {
		changeX(x);
		changeY(y);
	}
	
	public int getX() {
		return (int) x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void changeX(int amount) {
		this.x += amount;
	}

	public int getY() {
		return (int) y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void changeY(int amount) {
		this.y += amount;
	}
	
	public double distanceTo(Point other) {
		return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
	}
	
	public boolean equals(Point other) {
		return this.x == other.x && this.y == other.y;
	}
	
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
