package Zen;

public abstract class ZenShape {
	private Point position;
	private String color;
	
	public ZenShape() {
		position = new Point();
	}
	
	public abstract void draw();
	
	public void colorAndDraw() {
		if (color != null)
			Zen.setColor(color);
		this.draw();
	}
	
	public Point position() {
		return position;
	}
	
	public void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void changePosition(int dx, int dy) {
		this.changeX(dx);
		this.changeY(dy);
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return position.getY();
	}
	
	public void setX(int x) {
		position.setX(x);
	}
	
	public void setY(int y) {
		position.setY(y);
	}
	
	public void changeX(int amount) {
		position.changeX(amount);
	}
	
	public void changeY(int amount) {
		position.changeY(amount);
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
}
