Zen
===

## Overview

Zen is a graphics utility for teaching about object-oriented programming and modular game design with Java. 

## Getting Started

These instructions are for Eclipse, but they can be easily changed to work with any IDE.

1. Download this repository, either using the git command line tool or by clicking on the "download zip" button in the bottom right corner.
2. Create a Java project in your Eclipse workspace.
3. Double click on the project to view its content. Right click on the "src" folder, and select "New > Package".
4. A window will pop up asking you what to name your package. Name it "Zen", with a capital Z. **It is very important that you name it "Zen" exactly.**
5. Copy all the files from the Zen folder that you downloaded in step 1, to the "Zen" package.
6. You'll see some errors - that's because we haven't loaded in the Firebase library, which allows Zen to communicate in real time over the Internet. Copy the firebase.jar by dragging it to **your project folder** (not to a package or to the src folder). Then, right click on firebase.jar and click on "Build Path > Add to Build Path".


Contributing
===
Zen is just a layer of abstraction on top of JFrame.
