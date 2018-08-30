package dekuemblem;

import character.*;

import character.Character;
import processing.core.PApplet;
import processing.core.PImage;

public class Cursor {
	private PApplet parent;
	private PImage cursorPic;
	private PImage cursorPicSel;
	private int x;		// The cursor's positions
	private int y;
	private int xBound;
	private int yBound;
	private Character selectedChar;
	
	public Cursor(PApplet p, int xBoundary, int yBoundary){ 
		// pass in dimensions of board so that the cursor knows where to stop 
		parent = p;
		cursorPic = parent.loadImage("cursor.png");
		cursorPicSel = parent.loadImage("cursorSelect.png");
		x = 0;
		y = 0;
		xBound = xBoundary;
		yBound = yBoundary;
		selectedChar = null;
	}
	
	public void display(){
		if (selectedChar == null) parent.image(cursorPic, x*70, y*70);
		else parent.image(cursorPicSel, x*70, y*70);
		
	}
	
	public void keyPressed(Map map, Game g){
		//move cursor
		if (selectedCharacter() == null) {
			if (parent.keyCode == parent.RIGHT) {
				moveX(1);
			}
			if (parent.keyCode == parent.LEFT) {
				moveX(-1);
			}
			if (parent.keyCode == parent.UP) {
				moveY(-1);
			}
			if (parent.keyCode == parent.DOWN) {
				moveY(1);
			}
		} else if (g.getHeroTurn() == true && selectedChar.heroOrVillain() == 'h') {
			x = selectedCharacter().getX();
			y = selectedCharacter().getY();
		} else if (g.getHeroTurn() == false && selectedChar.heroOrVillain() == 'v') {
			x = selectedCharacter().getX();
			y = selectedCharacter().getY();
		}
		
		if (parent.key == ' '){
			if (selectedChar == null) selectCharacter(map);
			else deselectCharacter();
		}
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	//moves cursor within game space limits
	public void moveX(int tiles){
		x += tiles;
		if (x < 0) x = 0;
		else if (x > xBound-1) x = xBound-1;
	}
	
	public void moveY(int tiles){
		y += tiles;
		if (y < 0) y = 0;
		else if (y > yBound-1) y = yBound-1;
	}
	
	public void setPos(int newX, int newY){
		x = newX;
		y = newY;
	}
	
	public void selectCharacter(Map map) {
		selectedChar = map.getCharacter(y, x);
		if (selectedChar != null) selectedChar.changeSelected();
		
	}
		
	public void deselectCharacter() {
		selectedChar.changeSelected();
		
		selectedChar = null;
	}
	
	public Character selectedCharacter(){
		return selectedChar;
	}
	
	public boolean onHero(Map map){
		return (map.getCharacter(y, x).heroOrVillain() == 'h');
	}
	
	public boolean onVillain(Map map){
		return (map.getCharacter(y, x).heroOrVillain() == 'v');
	}
}
