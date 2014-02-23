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
	
	public void setPosition(int x, int y) {
		this.changePosition(x - this.getX(), y - this.getY());
	}
	
	public void changePosition(int dx, int dy) {
		for (ZenShape component : components)
			component.changePosition(dx, dy);
	}
	
	@Override
	public void draw() {
		for (ZenShape component : components)
			component.colorAndDraw();
	}
}
