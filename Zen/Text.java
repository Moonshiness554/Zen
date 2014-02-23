package Zen;

public class Text extends ZenShape {
	private String text, font;
	private int size;
	
	public Text(String text) {
		this("Helvetica", text, 16, "black");
	}
	
	public Text(String text, String color) {
		this("Helvetica", text, 16, color);
	}
	
	public Text(String font, String text, String color) {
		this(font, text, 16, color);
	}
	
	public Text(String text, int size) {
		this("Helvetica", text, size, "black");
	}
	
	public Text(String font, String text, int size, String color) {
		this.font = font;
		this.text = text;
		this.size = size;
		setColor(color);
	}
	
	@Override
	public void draw() {
		Zen.setFont(font, size);
		Zen.drawText(this.text, getX(), getY());
	}

}
