package character;

import processing.core.*;

public class Midoriya extends Hero {

	private PApplet parent;
	private PImage midoriya;
	
	public Midoriya(PApplet p, int newX, int newY) {
		super(p, "Midoriya", 3, 4, 1, 6, 3, 2, 5, 8, 6);
		setLoc(newX, newY);
		
		parent = p;
		midoriya = parent.loadImage("Deku.png");	
	}
	
	public PImage getImage() {
		return midoriya;
	}
}