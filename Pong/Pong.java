package Pong;

import Zen.*;

public class Pong extends ZenGame {

	public static void main(String[] args) {
		Pong game = new Pong();
		game.run();
	}
	
	Ball ball;
	Paddle player1;
	Paddle player2;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;

	public Pong() {
		setSize(WIDTH, HEIGHT);
	}
	
	@Override
	public void setup() {
		ball = new Ball(WIDTH / 2, HEIGHT / 2);
		player1 = new Paddle(50, HEIGHT / 2, "red");
		player1.setKeys("w", "s");
		player2 = new Paddle(WIDTH - 50, HEIGHT / 2, "blue");
		player2.setKeys("o", "l");
		addSprite(ball);
		addSprite(player1);
		addSprite(player2);
		ball.goRight();
	}

	@Override
	public void loop() {
		Zen.setBackground("white");
		if (ball.getX() <= 70 && ball.getX() >= 40 && ball.getY() >= player1.getY() && ball.getY() <= player1.getY() + 100) {
			ball.bounceHorizontally(50);
		}
		if (ball.getX() <= 770 && ball.getX() >= 740 && ball.getY() >= player2.getY() && ball.getY() <= player2.getY() + 100) {
			ball.bounceHorizontally(750);
		}
	}

}
