package dekuemblem;

import java.util.ArrayList;

import character.Character;
import character.Hero;
import character.Villain;
import processing.core.PApplet;
import processing.core.PImage;

public class Map {
	PApplet parent;
	PImage moveRange;
	PImage atkRange;
	
	private int gridX;			// width of the map in grid blocks
	private int gridY;			// height of the map in grid blocks
	private Terrain[][] mapTerrain;
	private ArrayList<Character> heroes;
	private ArrayList<Character> villains;
	private Character[][] mapChars;
	private boolean[][] attackSquares;
	private boolean[][] moveSquares;
	
	// send in an array of symbols that represent various terrain types
	// also send in an array of characters to determine the character starting positions
	public Map(PApplet p, int width, int height, char[][] newMapTerrain, ArrayList<Character> newHeroes, ArrayList<Character> newVillains){
		parent = p;
		gridX = width;
		gridY = height;

		// build the 2D terrain map
		mapTerrain = new Terrain[gridY][gridX];
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				if (newMapTerrain[i][j] == 'f'){ 			// forest terrain
					mapTerrain[i][j] = new Terrain(parent, "forest");
				} else if (newMapTerrain[i][j] == 'b'){ 	// building terrain
					mapTerrain[i][j] = new Terrain(parent, "building");
				} else if (newMapTerrain[i][j] == 'm'){ 	// mountain terrain
					mapTerrain[i][j] = new Terrain(parent, "mountain");
				} else if (newMapTerrain[i][j] == 'w'){ 	// water terrain
					mapTerrain[i][j] = new Terrain(parent, "water");
				} else {									// everything else is field terrain
					mapTerrain[i][j] = new Terrain(parent, "field");
				}
			}
		}
		
		// build the character placement map (mapChars)
		mapChars = new Character[gridY][gridX];
		heroes = new ArrayList<Character>();
		for (int i = 0; i < newHeroes.size(); i++){
			heroes.add(newHeroes.get(i));
		}
		villains = new ArrayList<Character>();
		for (int i = 0; i < newVillains.size(); i++){
			villains.add(newVillains.get(i));
		}
		updateCharacterArray();
		
		// build the overlay for the attack and move range indicator
		attackSquares = new boolean[gridY][gridX];
		moveSquares = new boolean[gridY][gridX];
		
		moveRange = p.loadImage("moveableTile.png");
		atkRange = p.loadImage("attackableTile.png");
	}
	
	public void heroKill(Hero deadGuy){
		heroes.remove(deadGuy);
	}
	
	public void villainKill(Villain deadGuy){
		villains.remove(deadGuy);
	}
		
	public void display() {
		updateCharacterArray();
		for (int i = 0; i < gridY; i++) {
			for (int j = 0; j < gridX; j++) {
				parent.image(mapTerrain[i][j].returnImage(), j*70, i*70);
			}
		}
	}
	
	public void displayCharRange(Cursor cursor){
		if (cursor.selectedCharacter() != null){
			findMovable(cursor.selectedCharacter());
			findAttackable(cursor.selectedCharacter());
			for (int i = 0; i < gridY; i++){
				for (int j = 0; j < gridX; j++){
					if (moveSquares[i][j]) parent.image(moveRange, j*70, i*70);
				}
			}
			for (int i = 0; i < gridY; i++){
				for (int j = 0; j < gridX; j++){
					if (attackSquares[i][j]) parent.image(atkRange, j*70, i*70);
				}
			}
			resetRange();
		}
	}
	
	public void updateCharacterArray(){
		for (int i = 0; i < mapChars.length; i++){
			for (int j = 0; j < mapChars[i].length; j++){
				mapChars[i][j] = null;
			}
		}
		for (int i = 0; i < heroes.size(); i++){
			mapChars[heroes.get(i).getY()][heroes.get(i).getX()] = heroes.get(i);
		}
		for (int i = 0; i < villains.size(); i++){
			mapChars[villains.get(i).getY()][villains.get(i).getX()] = villains.get(i);
		}
	}
	
	public void move(Character selectedChar, char direction){
		int[] destination = {selectedChar.getX(), selectedChar.getY()};
		if (direction == 'N'){
			destination[1]--;
		} else if (direction == 'S'){
			destination[1]++;
		} else if (direction == 'W'){
			destination[0]--;
		} else if (direction == 'E'){
			destination[0]++;
		}
		
		if (destination[1] >= 0 && destination[1] <= gridY-1 && destination[0] >= 0 && destination[0] <= gridX-1
				&& mapChars[destination[1]][destination[0]] == null){
			int tileSub = mapTerrain[destination[1]][destination[0]].terrainMove();
			if (selectedChar.canMove(tileSub)) {
				if (destination[0]-selectedChar.getX() != 0){
					selectedChar.moveX(destination[0]-selectedChar.getX(), tileSub);
				} else {
					selectedChar.moveY(destination[1]-selectedChar.getY(), tileSub);
				}
			}
		}
	}
	
	public void findMovable(Character selectedChar){
		// finds all the spaces the character can travel to (using recursion!)
		findMovableRecursion(selectedChar.getTilesLeft(), selectedChar.getX(), selectedChar.getY());
	}
	
	public void findMovableRecursion(int moveSpaces, int x, int y){
		// base case
		if (moveSpaces <= 0) moveSquares[y][x] = true;
		else {
			moveSquares[y][x] = true;
			if (y > 0 && mapTerrain[y-1][x].terrainMove() <= moveSpaces 							// upward
					&& mapChars[y-1][x] == null){
				findMovableRecursion(moveSpaces-mapTerrain[y-1][x].terrainMove(), x, y-1);
			}
			if (y < mapTerrain.length-1 && mapTerrain[y+1][x].terrainMove() <= moveSpaces		// downward
					&& mapChars[y+1][x] == null){
				findMovableRecursion(moveSpaces-mapTerrain[y+1][x].terrainMove(), x, y+1);
			}
			if (x > 0 && mapTerrain[y][x-1].terrainMove() <= moveSpaces							// left
					&& mapChars[y][x-1] == null){
				findMovableRecursion(moveSpaces-mapTerrain[y][x-1].terrainMove(), x-1, y);
			}
			if (x < mapTerrain[y].length-1 && mapTerrain[y][x+1].terrainMove() <= moveSpaces		// right
					&& mapChars[y][x+1] == null){
				findMovableRecursion(moveSpaces-mapTerrain[y][x+1].terrainMove(), x+1, y);
			}			
		}
	}

	public void findAttackable(Character selectedChar){
		// finds all the characters in range of the unit and highlights the spots
		int atkRng = selectedChar.getAttackRange();
		for (int i = 0; i < attackSquares.length; i++){
			for (int j = 0; j < attackSquares[i].length; j++){
				if (moveSquares[i][j] == true){
					if (i-atkRng >= 0 && mapChars[i-atkRng][j] != null && mapChars[i-atkRng][j].heroOrVillain() != selectedChar.heroOrVillain()){
						// checks if the space atkRng tiles upward has a enemy
						attackSquares[i-atkRng][j] = true;
					}
					if (i+atkRng <= attackSquares.length-1 && mapChars[i+atkRng][j] != null && mapChars[i+atkRng][j].heroOrVillain() != selectedChar.heroOrVillain()){
						// checks if the space atkRng tiles downward has a enemy
						attackSquares[i+atkRng][j] = true;
					}
					if (j-atkRng >= 0 && mapChars[i][j-atkRng] != null && mapChars[i][j-atkRng].heroOrVillain() != selectedChar.heroOrVillain()){
						// checks if the space atkRng tiles left has a enemy
						attackSquares[i][j-atkRng] = true;
					}
					if (j+atkRng <= attackSquares[i].length-1 && mapChars[i][j+atkRng] != null && mapChars[i][j+atkRng].heroOrVillain() != selectedChar.heroOrVillain()){
						// checks if the space atkRng tiles right has a enemy
						attackSquares[i][j+atkRng] = true;
					}
				}
			}
		}
	}
	
	public Character chooseAttack(Character selectedChar){
		int x = selectedChar.getX();
		int y = selectedChar.getY();
		int atkRng = selectedChar.getAttackRange();
		ArrayList<Character> selectableChars = new ArrayList<Character>();
		
		if (y-atkRng >= 0 && mapChars[y-atkRng][x] != null && mapChars[y-atkRng][x].heroOrVillain() != selectedChar.heroOrVillain()){
			// checks if the space atkRng tiles upward has a enemy
			selectableChars.add(mapChars[y-atkRng][x]);
		}
		if (y+atkRng <= attackSquares.length-1 && mapChars[y+atkRng][x] != null && mapChars[y+atkRng][x].heroOrVillain() != selectedChar.heroOrVillain()){
			// checks if the space atkRng tiles downward has a enemy
			selectableChars.add(mapChars[y+atkRng][x]);
		}
		if (x-atkRng >= 0 && mapChars[y][x-atkRng] != null && mapChars[y][x-atkRng].heroOrVillain() != selectedChar.heroOrVillain()){
			// checks if the space atkRng tiles left has a enemy
			selectableChars.add(mapChars[y][x-atkRng]);
		}
		if (x+atkRng <= attackSquares[y].length-1 && mapChars[y][x+atkRng] != null && mapChars[y][x+atkRng].heroOrVillain() != selectedChar.heroOrVillain()){
			// checks if the space atkRng tiles right has a enemy
			selectableChars.add(mapChars[y][x+atkRng]);
		}
		
		if (selectableChars.size() == 0){
			return null;
		} else if (selectableChars.size() == 1){
			return selectableChars.get(0);
		} else {
			Character lowestHP = selectableChars.get(0);
			for (int i = 1; i < selectableChars.size(); i++){
				if (lowestHP.getCurrentHP() > selectableChars.get(i).getCurrentHP()) lowestHP = selectableChars.get(i);
			}
			return lowestHP;
			
//			char direction = ' ';
//			while (direction != 'w' && direction != 'a' && direction != 's' && direction != 'd'){
//				if (parent.keyCode == parent.UP){
//					direction = 'w';
//				}
//				if (parent.keyCode == parent.LEFT){
//					direction = 'a';
//				}
//				if (parent.keyCode == parent.DOWN){
//					direction = 's';
//				}
//				if (parent.keyCode == parent.RIGHT){
//					direction = 'd';
//				}
//			}
//			
//			if (direction == 'w') {
//				return mapChars[y-atkRng][x];
//			} else if (direction == 'a') {
//				return mapChars[y][x-atkRng];
//			} else if (direction == 's') {
//				return mapChars[y+atkRng][x];
//			} else if (direction == 'd') {
//				return mapChars[y][x+atkRng];
//			} else return null;
		}
	}
	
	public void resetRange(){
		// resets the moveSquares and attackSquares
		for (int i = 0; i < attackSquares.length; i++){
			for (int j = 0; j < attackSquares[i].length; j++){
				attackSquares[i][j] = false;
			}
		}
		for (int i = 0; i < moveSquares.length; i++){
			for (int j = 0; j < moveSquares[i].length; j++){
				moveSquares[i][j] = false;
			}
		}
	}
	
	// return the terrain type at the given location
	public String getTerrain(int row, int col){
		return mapTerrain[row][col].getType();
	}
	
	public Character getCharacter(int row, int col){
		return mapChars[row][col];
	}	//GUI methods
}
