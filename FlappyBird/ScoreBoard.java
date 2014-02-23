package FlappyBird;

import Zen.Text;
import Zen.Zen;
import Zen.ZenSprite;

public class ScoreBoard extends ZenSprite {
	
	int score = 0;
	Text display;
	
	public ScoreBoard() {
		display = new Text("Helvetica", "Score: 0", 24, "black");
		display.set(280, 30);
	}
	
	public void addScore() {
		score++;
	}
	
	@Override
	public void draw() {
		Zen.setColor("black");
		display.setText("Score: " + score);
		display.draw();
	}

	@Override
	public void move() {
		
	}

}
