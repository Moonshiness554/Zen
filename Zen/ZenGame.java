package Zen;

import java.util.ArrayList;

public abstract class ZenGame {
	
	private int width = 500, height = 500, fps = 30;
	private boolean running = false;
	private String name = "Zen";
	
	private ArrayList <ZenSprite> sprites;
	
	public abstract void setup();
	public abstract void loop();
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void addSprite(ZenSprite sprite) {
		if (sprites != null)
			sprites.add(sprite);
	}
	
	public void clearSprites() {
		if (sprites != null)
			sprites.clear();
	}
	
	public void run() {
		if (! running) {
			sprites = new ArrayList <ZenSprite> ();
			Zen.setWindowName(name);
			Zen.create(width, height);
			running = true;
			setup();
			while (true) {
				loop();
				for (ZenSprite sprite : sprites) {
					sprite.move();
					sprite.draw();
				}
				Zen.buffer(1000 / fps);
			}
		}
	}
}
