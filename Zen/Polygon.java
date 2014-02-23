package Zen;

public class Polygon extends ZenShape {
	private int[] x;
	private int[] y;
	
	public Polygon(Point... points) {
		this(null, points);
	}
	
	public Polygon(String color, Point... points) {
		this.setColor(color);
		x = new int[points.length];
		y = new int[points.length];
		for (int i = 0 ; i < points.length ; i++) {
			x[i] = points[i].getX();
			y[i] = points[i].getY();
		}
		if (points.length > 0) {
			setX(x[0]);
			setY(y[0]);
		}
	}
	
	@Override
	public void draw() {
		if (x != null && x.length > 0)
			Zen.fillPolygon(x, y);
	}
	
	@Override
	public void setPosition(int x, int y) {
		changePosition(x - this.getX(), y - this.getY());
	}
	
	@Override
	public void changeX(int amount) {
		for (int i = 0 ; i < x.length ; i++)
			x[i] += amount;
	}
	
	@Override
	public void changeY(int amount) {
		for (int i = 0 ; i < y.length ; i++)
			y[i] += amount;
	}

	public Point getPoint(int index) {
		if (index >= 0 && index < x.length)
			return new Point(x[index], y[index]);
		return null;
	}
	
}
