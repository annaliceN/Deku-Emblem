package character;

import processing.core.*;

public class Todoroki extends Hero {

	private PApplet parent;
	private PImage todoroki;
	
	public Todoroki(PApplet p, int newX, int newY) {
		super(p, "Todoroki", 4, 3, 2, 4, 5, 4, 4, 8, 6);
		setLoc(newX, newY);
		
		parent = p;
		todoroki = parent.loadImage("todoroki.png");	
	}
	
	public PImage getImage() {
		return todoroki;
	}
	
	/*
	public void specialAttack(Character enemy) throws InterruptedException {
		if (enemy.heroOrVillain() == 'v') {
			super.attack(enemy);
			enemy.changeTilesLeft(0); //makes enemy unit unable to move in that round
		} else {
			cannotAttack();
		}
	} */
}