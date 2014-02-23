package FirePad;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Zen.Zen;

public class SimpleDraw {
	
	static int red;
	static JTextField redInput;
	static int green;
	static JTextField greenInput;
	static int blue;
	static JTextField blueInput;
	
	public static void main(String[] args) {
		Zen.create(800, 600);
		Zen.setColor(255, 255, 255);
		Zen.fillRect(0, 0, 800, 600);
		makeColorChooser();
		Zen.connect("techlab-drawing");
		Zen.sleep(2000);
		
		Pen ryan = new Pen("ryan");
		Pen ojas = new Pen("ojas");
		
		Zen.setColor(0, 0, 0);
		int prevX = 0;
		int prevY = 0;
		long prevTime = 0;
		boolean mousePressed = false;
		
		while (true) {
			int x = Zen.getMouseClickX();
			int y = Zen.getMouseClickY();
			//drawToolbar();
			//checkToolbar(x, y);
			
			Zen.write("keshavx", x);
			Zen.write("keshavy", y);
			Zen.write("keshavred", red);
			Zen.write("keshavgreen", green);
			Zen.write("keshavblue", blue);
			
			if (mousePressed) {
				Zen.write("keshavdown", 1);
			}
			else {
				Zen.write("keshavdown", 0);
			}
			
			Zen.setColor(red, green, blue);
			if (mousePressed) {
				Zen.drawLine(prevX, prevY, x, y);
			}
			if ( prevTime != Zen.getMouseClickTime() ) {
				mousePressed = true;
			}
			else {
				mousePressed = false;
			}
			ryan.move();
			ryan.draw();
			ojas.move();
			ojas.draw();
			prevX = x;
			prevY = y;
			prevTime = Zen.getMouseClickTime();
			Zen.sleep(33);
		}
		
	}

	private static void checkToolbar(int x, int y) {
		if (x <= 50 && x >= 0 && y >= 0 && y <= 50) {
			red = 255;
			green = 0;
			blue = 0;
		}
		if (x <= 100 && x >= 51 && y >= 0 && y <= 50) {
			red = 0;
			green = 255;
			blue = 0;
		}
		if (x <= 150 && x >= 101 && y >= 0 && y <= 50) {
			red = 0;
			green = 0;
			blue = 255;
		}
		if (Zen.isKeyPressed("x")) {
			Zen.setColor(255, 255, 255);
			Zen.fillRect(0, 0, 800, 600);
		}
		Zen.setColor(red, green, blue);
	}

	private static void drawToolbar() {
		Zen.setColor(255, 0, 0);
		Zen.fillRect(0, 0, 50, 50);
		Zen.setColor(0, 255, 0);
		Zen.fillRect(50, 0, 50, 50);
		Zen.setColor(0, 0, 255);
		Zen.fillRect(100, 0, 50, 50);
	}
	
	private static void makeColorChooser() {
		JFrame colorChooser = new JFrame("color picker");
		colorChooser.setSize(200, 200);
		colorChooser.setLayout(new GridLayout(4, 1));
		redInput = new JTextField(10);
		greenInput = new JTextField(10);
		blueInput = new JTextField(10);
		JButton button = new JButton("change color");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				red = Integer.parseInt( redInput.getText() );
				green = Integer.parseInt( greenInput.getText() );
				blue = Integer.parseInt( blueInput.getText() );
			}
		});
		colorChooser.add(redInput);
		colorChooser.add(greenInput);
		colorChooser.add(blueInput);
		colorChooser.add(button);
		
		colorChooser.setVisible(true);
	}

}
