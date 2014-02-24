Zen
===
Zen is a graphics utility for teaching about object-oriented programming and modular game design with Java. The core Zen graphics lives in the Zen folder, while the other folders are example games created with Zen.

![Flappy Bird](http://i.imgur.com/3lRx3Ti.jpg)


Zen is adapted from Lawrence Angrave's Zen library, and has been heavily modified and extended to make it suitable for teaching about object-oriented design patterns and real-time Internet connectivity. It integrates the [Firebase API](https://firebase.com/docs) to allow real-time Internet communication in Zen games. 

Zen was designed as an alternative to [Greenfoot](http://greenfoot.org/door), with a couple notable advantages:
  * can be easily integrated to any Java library. The current version of Zen requires the Firebase API, and makes it incredibly easy to make multiplayer games.
  * you're not locked into a specific IDE - I recommend using [Eclipse](http://eclipse.org).
  * no predefined structure that you are forced to follow. There are a couple abstract classes *to make your life easier*, but you can make an application using only the basic graphics functions if you so desire.
  * heavier emphasis on visualization.
  * no built-in collision detection - forces students to learn how to implement it on their own.

## Getting Started

These instructions are for Eclipse, but they can be easily changed to work with any IDE.

1. Download this repository, either using the git command line tool or by clicking on the "download zip" button in the bottom right corner.
2. Create a Java project in your Eclipse workspace.
3. Double click on the project to view its content. Right click on the "src" folder, and select "New > Package".
4. A window will pop up asking you what to name your package. Name it "Zen", with a capital Z. **It is very important that you name it "Zen" exactly.**
5. Copy all the files from the Zen folder that you downloaded in step 1, to the "Zen" package.
6. You'll see some errors - that's because we haven't loaded in the Firebase library, which allows Zen to communicate in real time over the Internet. Copy the firebase.jar by dragging it to **your project folder** (not to a package or to the src folder). Then, right click on firebase.jar and click on "Build Path > Add to Build Path".
7. You should be all set! If you have any troubles, issues, or questions, you are more than welcome to email me at keshav@techlabeducation.com. 

## Anatomy of a Zen Game

### Setting up the main game class

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

### Adding sprites

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

### Drawing shapes

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

### Positioning things

```ZenShape``` and ```ZenSprite``` inherit a number of methods from ```Point```, which stores coordinates in their precise (double) form. This means that for basically every shape and sprite in ZenGame, you can call the functions:

```java
Circle c = new Circle(50, 70, 20);
c.setX( 60 );		// Sets the x position to 60
c.changeX( -10 );	// Changes the x position by -10, so it is now 50
c.setY( 100 );
c.changeY( 20 );

c.set( 200, 250 );	// Sets the x, y position of the circle to (200, 250)
c.change( 5, 4 );	// Changes the x position by 5 and the y position by 4
```

There are also a couple math functions in Zen right now, with many more on the way.
```java
Rectangle r = new Rectangle(40, 20, 100, 100);
double dist = c.distanceTo(r);		// The distance between the center of the circle and the rectangle
double angle = c.angleTo(r);		// The angle of elevation or depression to reach r
```

### Coloring shapes

Coloring in Zen works as if you are an artist with a palette of colors. Before you draw something, you can select the color you want to draw with using
```java
Zen.setColor("turquoise");
```
There are 500 built-in colors, and you can add your own by calling
```java
Zen.addColor("foo", red, green, blue);
```
Where you replace red, green, and blue with the RGB values of the color you want to add. You can look up colors on [this website](http://html-color-codes.info) or create color schemes on [this website](http://www.colorschemer.com/online.html).

### Getting key presses

To find out if a key is pressed, use ```Zen.isKeyPressed( key )```. The key is a String, which can either be a letter ("a" - "z"), a number ("0" - "9"), or a special key: ```"left"```, ```"right"```, ```"up"```, ```"down"```, ```"space"```, ```"escape"```, ```"tab"```, ```"shift"```, ```"control"```, ```"alt"```, ```"delete"```, ```"home"```.

```java
Zen.isKeyPressed("a")
```

### Getting mouse motion

To get the mouse position, use ```Zen.getMouseX()``` and ```Zen.getMouseY()```. To get the *last position that the mouse clicked on*, use ```Zen.getMouseClickX()``` and ```Zen.getMouseClickY()```.

I'll add a couple examples here of how to tightly integrate clicks and mouse motion into your game.

### Firebase

You can use Zen to share data over the Internet with other Zen instances. This means that multiple students at different computers can all hook into a database to make their games truly multiplayer. This is, by far, the aspect of Zen that students enjoy the most.

To start, you need to [make an account on Firebase.com](https://firebase.com). Firebase is a free BAAS (backend as a service) - basically, they host your data in the cloud and make it accessible in real time. Once you're in your account, create a new Firebase. Every Firebase gets a unique URL like "voldemort.firebaseio.com". 

Once you have that Firebase set up, you can connect to it with Zen with one line.
```java
Zen.connect("voldemort");
```
This connects to the Firebase at voldemort.firebaseio.com. If you're using ZenGame, this line should go in your ```void setup()``` function. The game will pause for around 2 seconds as it makes the connection.

Now that you're connected, you can write data to Firebase with

```java
Zen.write("foo", "bar");	// Write a String
Zen.write("baz", 5);		// Write an integer
Zen.write("quux", 4.129502);	// Write a double
Zen.write("happy", true);	// Write a boolean
```
Any other Zen application that has performed ```Zen.connect("voldemort")``` can now read the data you just stored:

```java
String message = Zen.read("foo");	// message is now "bar"
int position = Zen.readInt("baz");	// position is now 5
boolean q = Zen.readBoolean("happy"); 	// q is now true
double foo = Zen.readDouble("bar");	// foo is now 4.129502
```

One thing to note is that data propagates almost instantly. This means that the time between one person doing ```Zen.write``` and another person being able to read it with ```Zen.read``` is so short (~ 200 ms) that it will appear to happen instantaneously.

### Frame rate

Sometimes you have games that are doing a lot of collision detection or drawing in each frame. ZenGame automatically does double buffering for you to reduce flicker, but if you're still experiencing a flicker effect, you should adjust the FPS by calling

```java
Zen.setFPS(15);
```
If you're not using ZenGame, i.e. using only the core Zen class, you would structure your application as

```java
Zen.create(WIDTH, HEIGHT);
// do some setup

// main game loop
while (! done) {
	// do your per-frame computing
	Zen.buffer( milliseconds );
}
```
The ```Zen.buffer( ms )``` function flips the buffer and sleeps for the specified number of milliseconds. If you want to do some precomputing on each frame, you can split apart this instruction into
```java
Zen.flipBuffer();
// precompute the next frame
Zen.sleep( ms );
```

You can also hook in a Timer object to [schedule a TimerTask to run at a fixed rate](http://www.tutorialspoint.com/java/util/timer_scheduleatfixedrate_delay.htm), but this is beyond the scope of what I normally cover with my students. It is very rare to have a student make a game which takes more than 2 - 5 ms to compute each frame, but whenever there is such a student, they're usually skilled enough to figure out Timers and TimerTask.

## Curriculum

We have an extensive curriculum for Zen that is used at my education startup, [TechLab Education](http://www.techlabeducation.com). If you are an education organization or public/private school looking to license our curriculum, send an email to [sales@techlabeducation.com](mailto:sales@techlabeducation.com) for licensing options. If you are a passionate teacher from an underfunded school district who is looking to start a Java afterschool program or robotics club, let us know, because we love supporting people like you and would be happy to provide our teaching material to you at no cost. 

Our curriculum comes with professional development opportunities at our Saratoga facility. Even if you've never programmed before, you can get your kids making awesome 2D games in no time.

## Contributing

Zen is just a layer of abstraction on top of JFrame. Some parts are nicely commented, while others have no commenting whatsoever. The goal is to make a powerful education tool that can be extended easily, so there will be much more documentation coming soon.
