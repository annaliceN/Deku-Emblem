//Annalice Ni and Kai Iwasaki
//AP CS, Period 2
//Second Semester Final Project
//Project credits:
//We do not own any of the graphics used in the GUI
//Characters: My Hero Academia (Hirokoshi Kohei)
//Game inspiration: Fire Emblem franchise
//GUI: Processing Library (Processing Foundation

package dekuemblem;

import java.util.ArrayList;
import java.util.concurrent.*;
import character.*;
import character.Character;
import processing.core.PApplet;

public class Game extends PApplet{
	private Midoriya deku;
	private Bakugou bakugou;
	private Todoroki todoroki;
	
	private Nomu nomu1;
	private Nomu nomu2;
	private Nomu nomu3;
	
	ArrayList<Character> heroes;
	ArrayList<Character> villains;
	
	private Cursor cursor1;
	private Map map1;
	
	//screen size details
	private int gridX = 8;
	private int gridY = 6;
	private int gameSpaceX = 560;
	private int gameSpaceY = 420;
	private int charWinX = 140;
	private int keyX = gameSpaceX;
	private int keyY = 100;
	
	private int windowX = gameSpaceX + charWinX;
	private int windowY = gameSpaceY + keyY;
	
	private int charWinY = 210;
	private int gameStatsX = charWinX;
	private int gameStatsY = windowY - charWinY;
	
	private boolean heroTurn = true;
	
	public static void main(String[] args) {
		PApplet.main("dekuemblem.Game");
	}
	
	public void settings() {
		size(windowX, windowY);
	}
	
	public void setup() {
		deku = new Midoriya(this, 0, 0);
		bakugou = new Bakugou(this, 2, 3);
		todoroki = new Todoroki(this, 3, 4);
		
		nomu1 = new Nomu(this, 5, 5);
		nomu2 = new Nomu(this, 6, 2);
		nomu3 = new Nomu(this, 7, 0);
		
		cursor1 = new Cursor(this, 8, 6);
		
		heroes = new ArrayList();
		heroes.add(deku);
		heroes.add(bakugou);
		heroes.add(todoroki);
		
		villains = new ArrayList();
		villains.add(nomu1);
		villains.add(nomu2);
		villains.add(nomu3);
	
		char[][] terrainMap = { {'-', '-', '-', '-', '-', 'f', 'f', 'f'},
								{'w', 'w', 'f', 'f', '-', '-', '-', 'f'},
								{'w', 'w', 'w', 'f', '-', '-', '-', 'f'},
								{'-', '-', '-', 'f', '-', '-', '-', 'f'},
								{'f', 'b', '-', 'f', '-', 'f', 'f', 'm'},
								{'f', 'b', 'f', 'f', 'f', 'f', 'm', 'm'} };
		map1 = new Map(this, 8, 6, terrainMap, heroes, villains);
	}
	
	//update loop of game
	public void draw() {
		//game space
		fill(50, 50, 200);
		rect(0, 0, gameSpaceX, gameSpaceY);
		
		//character info bar on right
		//actual information is coded in Character class
		fill(66, 55, 79);
		rect(560, 0, charWinX, charWinY);
		
		drawKey();
		
		drawGameStats();
		
		map1.display();
		
		//heroes
		if (!deku.isDead()) {
			deku.display(deku.getImage());
			deku.statBar();
		}
		if (!bakugou.isDead()) {
			bakugou.display(bakugou.getImage());
			bakugou.statBar();
		}
		if (!todoroki.isDead()) {
			todoroki.display(todoroki.getImage());
			todoroki.statBar();
		}

		//villains
		if (!nomu1.isDead()) {
			nomu1.display(nomu1.getImage());
			nomu1.statBar();
		}
		if (!nomu2.isDead()) {
			nomu2.display(nomu2.getImage());
			nomu2.statBar();
		}
		if (!nomu3.isDead()) {
			nomu3.display(nomu3.getImage());
			nomu3.statBar();
		}
		
		map1.displayCharRange(cursor1);
		
		cursor1.display();
		
		//color(0, 0, 0);
		//text("Hero turn:" + getHeroTurn(), 570, 200);
	}

	//controls all keyboard input of game
	public void keyPressed() {
		cursor1.keyPressed(map1, this);
		deku.keyPressed(map1, this, cursor1);
		todoroki.keyPressed(map1, this, cursor1);
		bakugou.keyPressed(map1, this, cursor1);
		nomu1.keyPressed(map1, this, cursor1);
		nomu2.keyPressed(map1, this, cursor1);
		nomu3.keyPressed(map1, this, cursor1);
		
		//controls attack key
		if (key == 'a' || key == 'A') {
			if (cursor1.selectedCharacter() != null && cursor1.selectedCharacter().getCanAttack()) {
				cursor1.selectedCharacter().attack(map1.chooseAttack(cursor1.selectedCharacter()));
			}
		}
		
		//controls turn change key
		if (key == 'e' || key == 'E') {
			if (getHeroTurn()) {
				endHeroRound();
			} else endVillainRound();			
		}
	}
	
	//at the end of the heroes' turn, method updates all of the character stats
	public void endHeroRound() {
		for (int i = 0; i < heroes.size(); i++) {
			heroes.get(i).update();
			heroes.get(i).update();
		}
		if (cursor1.selectedCharacter() != null) {
			cursor1.deselectCharacter();
		}
		changeHeroTurn();
	}
	
	//at the end of the villains' turn, method updates all of the character stats
	public void endVillainRound() {
		for (int i = 0; i < villains.size(); i++) {
			villains.get(i).update();
			villains.get(i).update();
		}
		if (cursor1.selectedCharacter() != null) {
			cursor1.deselectCharacter();
		}
		changeHeroTurn();
	}
	
	public void drawKey() {
		//game controls bar at bottom
		fill(173, 216, 230);
		rect(0, 420, keyX, keyY);
		//text
		fill(50, 14, 71);
		textSize(18);
		text("Arrow keys: move cursor + characters", 15, 440);
		text("Spacebar: select character for movement/stats", 15, 463);
		text("A: attacks enemy when in range", 15, 486);
		text("E: end one side's turn", 15, 509);
	}
	
	public void drawGameStats() {
		//game info on lower right
		fill(62, 109, 153);
		rect(gameSpaceX, 210, gameStatsX, gameStatsY);
	
		//shows which side's turn it is
		fill(255, 255, 255);
		textSize(20);
		if (getHeroTurn()) {
			text("Heroes' Turn", gameSpaceX + 5, charWinY + 25);
		} else {
			text("Villains' Turn", gameSpaceX + 5, charWinY + 25);
		}
		
		textSize(14);
		text("See attack info in console.", gameSpaceX + 10, charWinY + 50, 100, 70);
	}
	
	public boolean getHeroTurn() {
		return heroTurn;
	}
	
	public void changeHeroTurn() {
		heroTurn = !heroTurn;
	}
}