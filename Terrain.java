package dekuemblem;

import processing.core.PApplet;
import processing.core.PImage;

public class Terrain {
	private String type;
	private PApplet parent;
	private PImage picture;
	// moveReduction is how many tile movement points it will take to move the character to that spot
	private int moveReduction;	
	
	public Terrain(PApplet p, String newTerrain){
		type = newTerrain;
		parent = p;
		if (type.equals("forest")){ 				// forest terrain
			moveReduction = 2;
			picture = parent.loadImage("forest.png");
		} else if (type.equals("building")){ 		// building terrain
			moveReduction = 99;
			picture = parent.loadImage("building.png");
		} else if (type.equals("mountain")){ 		// mountain terrain
			moveReduction = 3;
			picture = parent.loadImage("mountain.png");
		} else if (type.equals("water")){ 			// water terrain
			moveReduction = 99;
			picture = parent.loadImage("water.png");
		} else {									// everything else is field terrain
			moveReduction = 1;
			picture = parent.loadImage("field.png");
		}
	}
	
	public PImage returnImage(){
		return picture;
	}
	
	public String getType(){
		return type;
	}
	
	public int terrainMove(){
		return moveReduction;
	}
}
