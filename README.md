Zen
===

# Overview

Zen is a graphics utility for teaching about object-oriented programming and modular game design with Java. If you're looking for examples, check out the [Zen examples repository](https://github.com/keshavsaharia/ZenExamples).

# Getting Started

These instructions are for Eclipse, but they can be easily changed to work with any IDE.

1. Download this repository, either using the git command line tool or by clicking on the "download zip" button in the bottom right corner.
2. Create a Java project in your Eclipse workspace.
3. Double click on the project to view its content. Right click on the "src" folder, and select "New > Package".
4. A window will pop up asking you what to name your package. Name it "Zen", with a capital Z. **It is very important that you name it "Zen" exactly.**
5. Copy all the files from the Zen folder that you downloaded in step 1, to the "Zen" package.
6. You'll see some errors - that's because we haven't loaded in the Firebase library, which allows Zen to communicate in real time over the Internet. Copy the firebase.jar by dragging it to **your project folder** (not to a package or to the src folder). Then, right click on firebase.jar and click on "Build Path > Add to Build Path".
7. You should be all set! If you have any troubles, issues, or questions, you are more than welcome to email me at keshav@techlabeducation.com. 

# Anatomy of a Zen Game

## Setting up the main game class

Start by making a new package (right click on the "src" folder, then click "New > Package"). Name it whatever you want to call your game - I'm calling mine "FlappyBird". Every class for your game should go in this package, so when you make a new class you should be right clicking on the "FlappyBird" package and not on "src".

Start by making a class that extends ZenGame - Eclipse automatically adds the methods you need.

```java
package FlappyBird;

import Zen.*;

public class FlappyBird extends ZenGame {

  public FlappyBird() {
    setFPS(30);               // Change frames per second
    setName("Flappy Bird");   // The name of the window
    setSize(400, 600);        // The width and height of the window, in that order
  }
  
  public void setup() {
    // Setup instructions go here.
  }
  
  public void loop() {
    // Instructions to be looped forever go here.
  }
}
```

When the game is run, ```void setup()``` is called once, then ```void loop()``` is called repeatedly. The constructor ```public FlappyBird()``` is optional, but is useful if you want to change the settings for the window. This structure was largely inspired by the setup of the [Arduino IDE](http://arduino.cc).

## Adding sprites

Once you have the game laid out, you need to create the moving objects in your game. In Zen, these moving objects are called "sprites", a common term that any student who has done Scratch will be familiar with.

```java
package FlappyBird;

import Zen.*;

public class Bird extends ZenSprite {
  public Bird() {
    // Initialize the bird.
  }

  public void move() {
    // How should this sprite move during each step?
  }
	
  public void draw() {
    // How should this sprite draw itself at each step?
  }
}
```
Here's a simple example of a complete sprite for the pipes that are constantly approaching the flappy bird. Every step, the pipe moves 2 pixels to the left (at 30 frames per second, this translates to 60 pixels/second). 

```java
package FlappyBird;

import Zen.*;

public class Pipe extends ZenSprite {	
	public Pipe() {
	  setPosition(450, 300)			// Start this pipe off the screen on the right
	  gap = Zen.getRandomNumber(100, 450);	// Get a random number between 100 and 450
	}
	
	public void move() {
		changeX(-2);
	}
	
	/**
	 * How should this object draw itself at each time step?
	 */
	public void draw() {
		Rectangle top = new Rectangle(getX(), 0, 50, gap - 75);
		top.setColor("light green");
		Rectangle bottom = new Rectangle(getX(), gap + 75, 50, 550 - (gap + 75));
		bottom.setColor("light green");
		Zen.draw(top);
		Zen.draw(bottom);
	}
}
```

## Drawing shapes

As you can see from the draw function, Zen uses objects to represent shapes on the screen. In general, if you want a certain shape like a circle to show up on the screen:

```java
Circle c = new Circle();		// A circle of diameter 1 at (0, 0)
Circle c = new Circle(50, 70);		// A circle of diameter 1 at (50, 70)
Circle c = new Circle(50, 100, 20); 	// A circle of diameter 20 at (50, 100)
Circle c = new Circle(50, 100, 20, "red");	// A red circle of diameter 20 at (50, 100)
```
Check the documentation for more information on the different shapes and constructors. Once you've made the shape, you can draw it by passing it to ```Zen.draw( shape )```:

```java
Zen.draw(c);
```

## Coloring shapes

Coloring in Zen works as if you are an artist with a palette of colors. Before you draw something, you can select the color you want to draw with using
```java
Zen.setColor("turquoise");
```
There are 500 built-in colors, and you can add your own by calling
```java
Zen.addColor("foo", red, green, blue);
```
Where you replace red, green, and blue with the RGB values of the color you want to add. You can look up colors on [this website](http://html-color-codes.info) or create color schemes on [this website](http://www.colorschemer.com/online.html).

## Getting key presses

To find out if a key is pressed, use ```Zen.isKeyPressed( key )```. The key is a String, which can either be a letter ("a" - "z"), a number ("0" - "9"), or a special key: ```"left"```, ```"right"```, ```"up"```, ```"down"```, ```"space"```, ```"escape"```, ```"tab"```, ```"shift"```, ```"control"```, ```"alt"```, ```"delete"```, ```"home"```.

```java
Zen.isKeyPressed("a")
```

## There's more!

This readme is a bit incomplete - I'll update it with

[ ] - how to connect to Firebase
[ ] - how to use the mouse
[ ] - how to handle games that lag

Curriculum
===
We have an extensive curriculum for Zen that is used at my education startup, [TechLab Education](http://www.techlabeducation.com). If you are an education organization or public/private school looking to license our curriculum, send an email to [sales@techlabeducation.com](mailto:sales@techlabeducation.com) for licensing options. If you are a passionate teacher from an underfunded school district who is looking to start a Java afterschool program or robotics club, let us know, because we love supporting people like you and would be happy to provide our teaching material to you at no cost. 

Our curriculum comes with professional development opportunities at our Saratoga facility. Even if you've never programmed before, you can get your kids making awesome 2D games in no time.

Contributing
===
Zen is just a layer of abstraction on top of JFrame. Some parts are nicely commented, while others have no commenting whatsoever. The goal is to make a powerful education tool that can be extended easily, so there will be much more documentation coming soon.
