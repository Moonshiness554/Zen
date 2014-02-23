package SenseiJump;

import Zen.*;

public class SenseiJump extends ZenGame {

	public static void main(String[] args) {
		SenseiJump game = new SenseiJump();
		game.run();
	}
	
	public static final int GROUND = 320;
	public static final int GRAVITY = 1;
	public static final int FIRST_JUMP = -10;
	public static final int SECOND_JUMP = -10;
	public static final int SPEED = 5;
	
	Sensei sensei;
	World world;
	
	public SenseiJump() {
		setSize(500, 400);
	}
	
	@Override
	public void setup() {
		sensei = new Sensei();
		sensei.set(50, 300);
		sensei.myself.set(50, 300);
		sensei.myself.set(60, 300);
		addSprite(sensei);
		world = new World();
		world.addPlatform(new Platform(200, 200));
		world.addPlatform(new Platform(100, 300));
		world.addPlatform(new Platform(300, 100));
		world.addPlatform(new Platform(350, 150));
		sensei.w = world;
	}

	@Override
	public void loop() {
		Zen.setBackground("light blue");
		Zen.setColor("green");
		Zen.fillRect(0, 370, 500, 30);
		world.draw();
	}
}
