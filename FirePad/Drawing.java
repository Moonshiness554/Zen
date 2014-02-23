package FirePad;

import Zen.Zen;

public class Drawing {

	public static void main(String[] args) {
		Zen.create(800, 600);
		Zen.fillRect(0, 0, 800, 600);
		// setup
		
		Zen.setColor(0, 0, 0);
		int prevX = 0;
		int prevY = 0;
		double slope;
		while (true) {
			int x = Zen.getMouseClickX();
			int y = Zen.getMouseClickY();
			Zen.fillOval(x - 5, y - 5, 10, 10);
			// bridge gap from prevx, prevy to x, y
			// Zen.drawLine(prevX, prevY, x, y);
			int fillers = Math.abs(x - prevX);
			if (x - prevX != 0) {
				slope = (y - prevY) / (x - prevX);
				for (int i = 0 ; i < fillers ; i++) {
					if (x >= prevX) {
						Zen.fillOval(prevX + i - 5, (int) (y + i * slope) - 5, 10, 10);
					} else {
						Zen.fillOval(prevX - i - 5, (int) (y - i * slope) - 5, 10, 10);
					}
				}
			}
			else {
				if (y > prevY) {
					for (int i = prevY ; i < y ; i++) {
						Zen.fillOval(x - 5, i - 5, 10, 10);
					}
				}
				else {
					for (int i = prevY ; i > y ; i--) {
						Zen.fillOval(x - 5, i - 5, 10, 10);
					}
				}
			}
			
			prevX = x;
			prevY = y;
		}
		
	}

}
