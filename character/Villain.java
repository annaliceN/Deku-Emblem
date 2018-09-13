package character;

import dekuemblem.Cursor;
import dekuemblem.Game;
import dekuemblem.Map;
import processing.core.*;

public abstract class Villain extends Character{

	public Villain(PApplet p, String charName, int moveRange, int hpStat, int attackRange, int attack, int defence, int accuracy, int specialCount, int newGridX, int newGridY) {
		super(p, charName, moveRange, hpStat, attackRange, attack, defence, accuracy, specialCount, newGridX, newGridY);
	}

	public void attack(Character enemy) {
		if (enemy.heroOrVillain() == 'h') {
			super.attack(enemy);
		} else {
			cannotAttack();
		}
	}
	
	public void keyPressed(Map map, Game g, Cursor cursor) {
		//selected thing works!
		if (!g.getHeroTurn() && getSelected()) {
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
			map.villainKill(this);
		}
	}
	
	public char heroOrVillain() {
		return 'v';		// returns that it's a villain
	}
}
