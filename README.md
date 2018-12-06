# AIslandBase

A codebase for sprite-based programming, built on Swing from Java.

Currently in Alpha, so expect lots of bugs and glitchy repainting.

## Background

Similarly to how Swing uses a JPanel to hold Components, AIslandBase uses a Canvas to hold Sprites.

Sprites are the guts of sprite-based programming. A Sprite is an image that can be moved, turned, or otherwise transformed. 

Each Canvas is backed by a Grid object. All Sprites are held to a coordinate on the Grid. 
As the Canvas is resized, all sprites are repositioned according to their grid coordinates.

JSprites are similar to Sprites, however JSprites hold Swing JComponents rather than images. JSprites allow you to add buttons, labels, and other Swing functionality to a Canvas.

## Getting Started

To start using AIslandBase, call the

`Init.getInit(String title, String version, int length, int width, Image icon);`

function with the window title, the (optional) program version, the dimensions of the Grid, and an optional program icon.
This returns an instance of Canvas. 

To add a sprite to the canvas, create an AbstractSprite and use the `addSprite()` method. To add a JSprite, use the `addJSprite()` method.
