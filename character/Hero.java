package character;

import dekuemblem.*;
import dekuemblem.Map;
import processing.core.*;

public abstract class Hero extends Character {

	PApplet parent;
	
	public Hero(PApplet p, String name, int moveRange, int hpStat, int attackRange, int attack, int defence, int accuracy, int specialCount, int newGridX, int newGridY) {
		super(p, name, moveRange, hpStat, attackRange, attack, defence, accuracy, specialCount, newGridX, newGridY);
	
		parent = p;
	}

	public void attack(Character enemy) {
		//int damage = (int) (Math.random() * (max_dmg - min_dmg) + min_dmg);
		if (enemy.heroOrVillain() == 'v') {
			super.attack(enemy);
		} else {
			cannotAttack();
		}
	}

	public void keyPressed(Map map, Game g, Cursor cursor) {
		//selected thing works!
		if (getSelected() && g.getHeroTurn()) {
			if (parent.keyCode == parent.RIGHT) {
				map.move(this, 'E');
			}
			if (parent.keyCode == parent.LEFT) {
				map.move(this, 'W');
			}
			if (parent.keyCode == parent.UP) {
				map.move(this, 'N');
			}
			if (parent.keyCode == parent.DOWN) {
				map.move(this, 'S');
			}
			cursor.setPos(this.getX(), this.getY());
		}
	}
	
	public void takeDamage(int damage, Map map) {
		if (getCurrentHP() - damage < 0) {
			subtractHP(getCurrentHP());
			removeChar(map);
		} else {
			subtractHP(damage);
		}
	}
	
	public void removeChar(Map map) {
		if (isDead()) {
			map.heroKill(this);
		}
	}
	
	public char heroOrVillain() {
		return 'h'; 	// returns that it's a hero
	}
}