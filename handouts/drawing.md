Drawing with Zen
===
This is a comprehensive guide to drawing shapes with Zen. This tutorial only covers the object-oriented drawing patterns - if you'd like to learn more about how to interact directly with the Zen library, read about [what goes on under the hood](https://github.com/keshavsaharia/Zen/tree/master/handouts/contributing.md).

## Drawing rectangles

When drawing a rectangle, you can visualize the process as the same when you create a rectangle in Paint or some similar drawing software. You first click on a point, then drag outwards to set the width and height of the rectangle. When you draw a rectangle with Zen, you have to give it four pieces of information:
  * the x coordinate of the top left corner
  * the y coordinate of the top left corner
  * the width
  * the height

Specifically, this is done by creating a rectangle object:

```java
Rectangle r = new Rectangle(x, y, width, height);
Zen.draw(r);
```

## Drawing squares

## Drawing circles

## Drawing ovals

## Drawing lines

## Drawing triangles

## Drawing polygons

## Combining shapes
