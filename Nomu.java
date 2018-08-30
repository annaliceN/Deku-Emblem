package character;

import processing.core.*;

public class Nomu extends Villain {

	private PApplet parent;
	private PImage nomu;
	
	public Nomu(PApplet p, int newX, int newY) {
		super(p, "Nomu", 3, (int)(Math.random() * 3 + 2), 1, (int)(Math.random() * 3 + 4), (int)(Math.random() * 3 + 2), (int)(Math.random() * 2 + 3), 0, 8, 6);
		setLoc(newX, newY);
		
		parent = p;
		nomu = parent.loadImage("Nomu.png");
	}

	public PImage getImage() {
		return nomu;
	}
}