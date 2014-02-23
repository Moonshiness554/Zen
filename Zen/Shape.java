package Zen;

public class Shape extends ZenShape {
	private ZenShape[] components;
	
	public Shape(ZenShape... components) {
		this.components = components;
		if (components.length > 0) {
			this.setX(components[0].getX());
			this.setY(components[0].getY());
		}
	}
	
	public void set(int x, int y) {
		this.change(x - this.getX(), y - this.getY());
	}
	
	public void change(int dx, int dy) {
		for (ZenShape component : components)
			component.change(dx, dy);
	}
	
	public void set(double x, double y) {
		this.change(x - this.getX(), y - this.getY());
	}
	
	public void change(double dx, double dy) {
		for (ZenShape component : components)
			component.change(dx, dy);
	}
	
	@Override
	public void draw() {
		for (ZenShape component : components)
			component.colorAndDraw();
	}
}
