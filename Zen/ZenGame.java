package Zen;

import java.util.ArrayList;

public abstract class ZenGame {
	
	private int width = 500, height = 500, fps = 30;
	
	private boolean running = false;
	
	private ArrayList <ZenSprite> sprites;
	
	public abstract void setup();
	public abstract void loop();
	public abstract String name();
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void addSprite(ZenSprite sprite) {
		if (sprites != null)
			sprites.add(sprite);
	}
	
	public void run() {
		if (! running) {
			sprites = new ArrayList <ZenSprite> ();
			Zen.setWindowName(this.name());
			Zen.create(this.width, this.height);
			running = true;
			setup();
			while (true) {
				loop();
				for (ZenSprite sprite : sprites) {
					sprite.move();
					sprite.draw();
				}
				Zen.buffer(1000 / this.fps);
			}
		}
	}
}
