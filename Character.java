package character;

import processing.core.*;
import java.util.concurrent.*;

public abstract class Character {
	
	PApplet parent;
	//moveRange, maxHP, attackRange, attack, defense, accuracy
	private String name;
	
	//character position
	private int xLoc;
	private int yLoc;
	
	private int gridX;
	private int gridY;
	
	private int moveRange;
	private int maxHP;
	private int attackRange;
	private int attack;
	private int defence;
	private int accuracy;
	
	private int currentHP;
	
	//round mechanics
	private int tilesLeft; //default = moveRange
	private boolean charDone;
	private boolean selected;
	private boolean attacking; //makes some text only display if the character is attacking something
	private boolean canAttack; //controls character can still do a normal attack
	
	//stat buffs
	private double attackBuff;
	private double defenceBuff;
	private double accuracyBuff;
	private int attackBLength;
	private int defenceBLength;
	private int accuracyBLength;
	private int specialCountMax; //number of turns needed for special cooldown
	private int specialAttackCount; //increment for cooldown
	private boolean canSpecialAttack;
	
	public Character (PApplet p, String charName, int newMoveRange, int newHPStat, int newAttackRange, int newAttack, int newDefence, int newAccuracy, int newSpecialCount, int newGridX, int newGridY) {
		parent = p;
		
		name = charName;
		
		//char position
		xLoc = 0;
		yLoc = 0;
		
		//game grid size
		gridX = newGridX;
		gridY = newGridY;
		
		//stats
		moveRange = newMoveRange;
		maxHP = 25 + newHPStat * 6;
		attackRange = newAttackRange;
		attack = 20 + newAttack * 5;
		defence = 20 + newDefence * 4;
		accuracy = 50 + 10 * newAccuracy;
		
		currentHP = maxHP;
		tilesLeft = moveRange;
		charDone = false;
		selected  = false;
		attacking = false;
		canAttack = true;
		
		//buffChanges
		attackBuff = 1;
		defenceBuff = 1;
		accuracyBuff = 1;
		attackBLength = 0;
		defenceBLength = 0;
		accuracyBLength = 0;
		specialCountMax = newSpecialCount;
		specialAttackCount = specialCountMax;
		canSpecialAttack = false;
		
	}
	
	//gets called every game round to reset/modify stats
	public void update() {
		//decreases the buff counter and changes buff amount accordingly
		if (attackBLength > 0) {
			attackBLength--;
		} else attackBuff = 1;
		
		if (defenceBLength > 0) {
			defenceBLength--;
		} else defenceBuff = 0;
		
		if (accuracyBLength > 0) {
			accuracyBLength--;
		} else accuracyBuff = 0;
		
		//increments special attack cooldown every round and checks if the character can use
		//the special attack
		if (specialAttackCount == 0) {
			canSpecialAttack = true;
		} else if (specialAttackCount == -1) {
			canSpecialAttack = false;
			specialAttackCount = specialCountMax;
		} else if (specialAttackCount <= specialCountMax) {
			specialAttackCount--;
		}
		
		//resets turn mechanics
		tilesLeft = moveRange;	//restores character's movement count to normal after each round
		charDone = false;
		selected = false;
		attacking = false;
		canAttack = true;
	}
	
	//GUI display methods
	public void display(PImage image) {
		//displays character image on map
		parent.image(image, getX()*70+5, getY()*70+5);	
	}
	
	public void statBar() {
		//displays character's stats on right side of screen
		if (getSelected()) {
			//character name
			parent.fill(255, 252, 220);
			parent.textSize(28);
			parent.text(getName(), 570, 30);
			
			//character stats
			parent.textSize(14);
			parent.text("HP: " + getCurrentHP() + "/" + getMaxHP(), 570, 60);
			parent.text("Moves Left: " + getTilesLeft(), 570, 75);
			parent.text("Position: (" + (getX()+1) + ", " + (getY()+1) + ")", 570, 90);
			parent.text("Attack: " + getAttack(), 570, 120);
			parent.text("Defence: " + getDefence(), 570, 135);
			parent.text("Accuracy: " + getAccuracy(), 570, 150);
			parent.text("Atk Range: " + getAttackRange(), 570, 180);
			parent.text("Can attack: " + getCanAttack(), 570, 195);
		}
	}
	
	public void attackDisplay(Character enemy, int damage) {
		if (getAttacking()) {
			parent.fill(255, 255, 255);
			parent.textSize(14);
			String s = this.getName() + " attacked " + enemy.getName() + " and did " + damage + " damage.";
			parent.text(s, 570, 260, 130, 50);
		}
	}
	
	public void missedDisplay(Character enemy) {
		if (getAttacking()) {
			parent.fill(255, 255, 255);
			parent.textSize(14);
			String s = this.getName() + " attacked " + enemy.getName() + ", but they missed!";
			parent.text(s, 570, 260, 130, 50);
		}
	}
	
	public void cannotAttack() {
		if (getAttacking()) {
			parent.fill(255, 255, 255);
			parent.textSize(14);
			String s = "A unit cannot attack its allies.";
			parent.text(s, 570, 260, 130, 50);
		}
	}
	
	//getter methods
	public String getName() {
		return name;
	}

	public int getX() {
		return xLoc;
	}
	
	public int getY() {
		return yLoc;
	}
	
	public int getMoveRange() {
		return moveRange;
	}
	
	public int getMaxHP() {
		return maxHP;
	}
	
	public int getAttackRange() {
		return attackRange;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getDefence() {
		return defence;
	}
	
	public int getAccuracy() {
		return accuracy;
	}
	
	public int getCurrentHP() {
		return currentHP;
	}
	
	public double getAttackBuff() {
		return attackBuff;
	}
	
	public double getDefenceBuff() {
		return defenceBuff;
	}
	
	public double getAccuracyBuff() {
		return accuracyBuff;
	}
	
	public int getAttackBCount() {
		return attackBLength;
	}
	
	//turn mechanics
	public int getTilesLeft() {
		return tilesLeft;
	}
	
	public boolean getCharDone() {
		return charDone;
	}
	
	public boolean getSelected() {
		return selected;
	}
	
	public boolean getAttacking() {
		return attacking;
	}
	
	public boolean getCanAttack() {
		return canAttack;
	}
	
	public abstract PImage getImage();
	
	public String toString() {
		return (name + ": HP = " + currentHP + "|Death = " + isDead() + "|Tiles Left = ");
	}
	
	//modifier methods
	public void setLoc(int newX, int newY) {
		xLoc = newX;
		yLoc = newY;
	}
	
	//for movement on game grid
	//character can only move if it has tiles left to move
	public void moveX(int tiles, int moveDecrease){
		xLoc += tiles;
		tilesLeft -= moveDecrease;
		if (xLoc < 0) {
			xLoc = 0;
		} else if (xLoc > gridX - 1) {
			xLoc = gridX - 1;
		}
	}
	
	public void moveY(int tiles, int moveDecrease){
		yLoc += tiles;
		tilesLeft -= moveDecrease;
		if (yLoc < 0) {
			yLoc = 0;
		} else if (yLoc > gridY - 1) {
			yLoc = gridY - 1;
		}
	}
	
	public void addHP(int amount) {
		currentHP += amount;
	}
	
	public void subtractHP(int amount) {
		currentHP -= amount;
	}
	
	//modifier methods
	public void changeTilesLeft(int newTiles) {
		tilesLeft = newTiles;
	}
	
	public void changeCharDone() {
		charDone = !charDone;
	}
	
	public void changeSelected() {
		selected = !selected;
	}
	
	public void changeAttacking() {
		attacking = !attacking;
	}
	
	public void attackDone() {
		canAttack = false;
	}
		
	//action methods
	public void subtractMoves(int subMoves) {
		tilesLeft -= subMoves;
	}
	
	public void decreaseBuffCount() {
		attackBLength--;
		defenceBLength--;
		accuracyBLength--;
	}
	
	public void attack(Character enemy) {
		//general attack method
		if (enemy != null){
			changeAttacking(); //makes sure character is in attacking status
			int random = (int) (Math.random() * 100 + 1);
			if (random <= getAccuracy()) {
				int damage;
				if (getAttack() - enemy.getDefence() > 0) {
					damage = getAttack() - enemy.getDefence();
				} else {
					damage = 0;
				}
				enemy.takeDamage(damage);
				System.out.println(this.getName() + " attacked " + enemy.getName() + " and did " + damage + " damage.");
				attackDisplay(enemy, damage);
				attackDone();
			} else {
				System.out.println(this.getName() + " attacked " + enemy.getName() + ", but they missed!");
				missedDisplay(enemy);
				attackDone();
			}
		}
	}
	
	public void takeDamage(int damage) {
		if (currentHP - damage < 0) {
			currentHP = 0;
		} else {
			subtractHP(damage);
		}
	}

	//checker methods
	public boolean isDead() {
		if (currentHP <= 0) return true;
		else return false;
	}
	
	public boolean canMove(int subMoves) {
		//checks if players can move further on map
		return (tilesLeft - subMoves >= 0);
	}
	
	public abstract char heroOrVillain();
}