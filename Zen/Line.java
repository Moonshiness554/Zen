package Zen;

public class Line extends ZenShape {
	private Point end;
	private int thickness = 1;
	
	public Line(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, 1, null);
	}
	
	public Line(int x1, int y1, int x2, int y2, int thickness) {
		this(x1, y1, x2, y2, thickness, null);
	}
	
	public Line(int x1, int y1, int x2, int y2, String color) {
		this(x1, y1, x2, y2, 1, color);
	}
	
	public Line(int x1, int y1, int x2, int y2, int thickness, String color) {
		this.setX(x1);
		this.setY(y1);
		this.end = new Point(x2, y2);
		this.thickness = thickness;
		this.setColor(color);
	}

	public void draw() {
		if (thickness > 1) {
			double dy = end.getY() - getY();
			double dx = end.getX() - getX();
			if (dy != 0) {
				double invSlope = Math.atan(dy / dx) + ((dx < 0) ? Math.PI : ((dy < 0) ? 2 * Math.PI : 0));
				int xr = (int) (Math.sin(invSlope) * thickness / 2), yr = (int) (Math.cos(invSlope) * thickness / 2);
				Zen.fillPolygon(
						new int[] { this.getX() + xr, end.getX() + xr,
									end.getX() - xr, this.getX() - xr }, 
						new int[] { this.getY() - yr, end.getY() - yr, 
									end.getY() + yr, this.getY() + yr }); 
			} else {
				Zen.fillPolygon(
					new int[] { this.getX() + thickness / 2, end.getX() + thickness / 2,  
								end.getX() - thickness / 2, this.getX() - thickness / 2}, 
					new int[] { this.getY(), end.getY(), end.getY(), this.getY()}); 
			}
			
		}
		else Zen.drawLine(getX(), getY(), end.getX(), end.getY());
	}
	
	public void changeX(int amount) {
		setX(getX() + amount);
		end.changeX(amount);
	}
	
	public void changeY(int amount) {
		setY(getY() + amount);
		end.changeY(amount);
	}
	
	public Point end() { 
		return this.end;
	}
	
	public void setEnd(int x2, int y2) {
		this.end.set(x2, y2);
	}
	
	public void setEndX(int x2) {
		this.end.setX(x2);
	}
	
	public int getEndX() {
		return this.end.getX();
	}
	
	public void setEndY(int y2) {
		this.end.setY(y2);
	}
	
	public int getEndY() {
		return this.end.getY();
	}
}
