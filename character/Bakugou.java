package character;

import processing.core.*;

public class Bakugou extends Hero {

	private PApplet parent;
	private PImage bakugou;
	
	public Bakugou(PApplet p, int newX, int newY) {
		super(p, "Bakugou", 3, 3, 1, 6, 3, 4, 3, 8, 6);
		
		setLoc(newX, newY);
		
		parent = p;
		bakugou = parent.loadImage("bakugo.png");	
	}
	
	public PImage getImage() {
		return bakugou;
	}
}