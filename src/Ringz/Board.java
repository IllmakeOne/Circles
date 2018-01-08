package Ringz;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;


public class Board extends Observable {
	
	private Color[][][] borg;

	private int[] sum;
	
	
	/**
	 * creati empty board.
	 * 0 menas empty
	 */
	enum Color { BLUE, PURPLE, YELLOW, GREEN, EMPTY };
	
	
	/**
	 * initialize the  oard wiht EMptuy fields.
	 */
	public Board() {
		sum = new int[5];
		
		int rand1 = getRandom(1, 4);
		int rand2 = getRandom(1, 4);
		this.borg = new Color[5][5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 5; k++) {
					borg[i][j][k] = Color.EMPTY;
				}
			}
		}
		
	}
	
	
	/**
	 * this funciton returns the index of the maxim value in an array.
	 * it will return -1 if there are multipe indexe wiht the same maximum value;
	 * @param array
	 * @return
	 */
	public int arrayMaximum(int[] array) {
		int max1 = 0, maxindex1 = 1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= max1) {
				max1 = array[i];
				maxindex1 = i;
			}
		}		
		for (int i = 0; i < maxindex1; i++) {
			if (array[i] == max1) {
				return -1;
			}
		}
		return maxindex1;
	}
	
	
	/**
	 * crutch fucntion .
	 * return 0 for blue, 1 for purple , 2 for yelo2 and 3 for green
	 * @param color
	 * @return
	 */
	public int colorIndex(Color color) {
		switch (color) {
			case BLUE: {
				return 0;
			}
			case PURPLE: {
				return 1;
			}
			case YELLOW: {
				return 2;
			}
			case GREEN: {
				return 3;
			}
			default:
				return -1;
		}
	}
	
	/**
	 * this fucntion cacualtes the total on a given place on the map.
	 * it returns an array, index 0 is blue , 1 is purple, 2 is yellow, 3 is green.
	 * @return
	 * x is lien y is column.
	 */
	public int[] tallyUp(int x, int y) {
		int[] tally = new int[4];
		for (int i = 1; i < 5; i++) {
			colorIndex(getField(x, y, i));
		}
		return tally;
	}
	
	
	/**
	 * calculates the total score of the game.
	 * it returns an int array.
	 * index 0 blue, index 1 purple, index 2 yellow, index 3 green.
	 * on each ndex it says how many points each has
	 */
	public int total() {
		int[] sum = new int[4];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (getField(i, j, 0) != Color.EMPTY) {
					sum[colorIndex(getField(i, j, 0))]++;
				} else {
					if (arrayMaximum(tallyUp(i, j)) != -1) {
						sum[arrayMaximum(tallyUp(i, j))]++;
					}
				}
			}
		}
	
		return arrayMaximum(sum);
	}
	
	
	/**
	 * places the first piece of the game
	 * @param a
	 */
	public void placeStart(int[] a) {
		//ads a multicolored cirlc ona aradonm place so the game can start
		borg[a[0]][a[1]][4] = Color.BLUE;
		borg[a[0]][a[1]][1] = Color.GREEN;
		borg[a[0]][a[1]][2] = Color.PURPLE;
		borg[a[0]][a[1]][3] = Color.YELLOW;
	}
	
	
	/**
	 * tests if a field of the board has a certain colored ring .
	 * @param x is the line
	 * @param y is the colum
	 * @param col is the color being looked for
	 * @return true if the field has the color
	 */
	public boolean fieldHas(int x, int y, Color col) {
		for (int i = 0; i < 5; i++) {
			if (this.borg[x][y][i] == col) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCompletlyEmpty(int x, int y) {
		for (int i = 0; i < 5; i++) {
			if (this.borg[x][y][i] != Color.EMPTY) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 */
	public boolean hasFriend(int x, int y, Color col) {
		int[] handy = {-1, 0, 1};
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (this.inBounds(x + handy[i], y + handy[j])) {
					if (fieldHas(x + handy[i], y + handy[j], col)) {
						return true;
					}
				}
			}
		}
		return false;
		
	}
	
	/**
	 * tests if the move is valid.
	 * @param x the line
	 * @param y the column
	 * @param col the color being tested
	 * @return true if the move is valid
	 */
	public boolean validMove(int x, int y, Color col, int circleSize) {
		if (getField(x, y, 0) == Color.EMPTY) {	
			if (this.hasFriend(x, y, col)) {
				if (circleSize == 0) {
					if (this.isCompletlyEmpty(x, y)) {
						return true;
					} else {
						System.out.println("There are peices here, cant put base");
						return false;
					}
				}
				if (this.borg[x][y][circleSize] == Color.EMPTY) {
					return true;
				} else {
					System.out.println("Field not empty");

					return false;
				}
			} else {
				System.out.println("Has no near piece of the same coolor NEAR IT");

				return false;
			}
		} else {
			System.out.println("The space already has a BASE");
			return false;
		}
		
	}
	
	
	/**
	 * ads a cricle if it is valid. 
	 * @param circleSeize the seize of the circle
	 * @param col the color of the circle
	 * @param x the line
	 * @param y the column
	 */
	public boolean addCircle(Move move) {
		int x, y, circleSeize;
		Color col;
		x = move.getLine();
		y = move.getColumn();
		circleSeize = move.getCircle();
		col = move.getColor();
		if (validMove(x, y, col, circleSeize)) {
			this.borg[x][y][circleSeize] = col;
			System.out.println("circles aded");
			return true;
		} else {
			System.out.println("circles  not aded");
			return  false;
		}
	}
	
	/**
	 * return the epcific field 
	 * @param x line
	 * @param y column
	 * @param circleSeize hight
	 * @return
	 */
	public Color getField(int x, int y, int circleSeize) {
		return this.borg[x][y][circleSeize];
	}
	
	/**
	 * gives all the cirlces in a board coordinate
	 * @param x line
	 * @param y column 
	 * @return a vector of Color 
	 */
	public Color[] getFields(int x, int y) {
		Color[] life = new Color[5];
		for (int i = 0; i < 5; i++) {
			life[i] = this.getField(x, y, i);
		}
		return life;
	}
	
	
	/**
	 * tests if the boar dis full or not
	 * @return
	 */
	public boolean isFull() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 5; k++) {
					if (getField(i, j, k) == Color.EMPTY) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * test is x and y are without bounds
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean inBounds(int x, int y) {
		if (x > 4 || x < 0) {
			return false;
		}
		
		if (y > 4 || y < 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * tests if the board is still playable.
	 * it is not playabl ehwen no aplyer is able to palce any more rings.
	 * @return
	 */
	public boolean isPayable() {
		boolean test = isFull();
		if (this.isFull()) {
			return false;
		}
		return true;
	}
	
	
	
	
	/**
	 * crutch fucntion, generate a random number betewn min and max.
	 * @param min
	 * @param max
	 * @return
	 */
	public int getRandom(int min, int max) {
		return (int) (Math.floor(Math.random() * (max - min)) + min);
	}
	
	public void display() {
		for (int i = 0; i < 5; i++) {
			String croth = "";
			for (int j = 0; j < 5; j++) {
				String stringy = "";
				for (int k = 0; k < 5; k++) {
					switch (borg[j][i][k]) {
						case EMPTY: {
							stringy += "E";
							break;
						}
						case BLUE: {
							stringy += "B";
							break;
						}
						case YELLOW: {
							stringy += "Y";
							break;
						}
						case PURPLE: {
							stringy += "P";
							break;
						}
						case GREEN: {
							stringy += "G";
							break;
						}
					}
				}
				croth += stringy + "    ";
			}
			System.out.println(croth);
		}
		
	}
	
}
