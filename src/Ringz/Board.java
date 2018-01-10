package Ringz;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

//lalalalala comment git
public class Board extends Observable {
	
	private Color[][][] bord;//x, y, pin
	public static final int DIM = 5;
	public static final int DIFFPIECES = 5;//different amount of pieces(12 rings in 4 sizes, 3 bases)
	private int[] sum;
//	public enum Color { BLUE, PURPLE, YELLOW, GREEN, EMPTY };
	
	
	/**
	 * initialize the  board with empty fields.
	 */
	public Board() {
		this.bord = new Color[DIM][DIM][DIFFPIECES];
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				for (int k = 0; k < DIFFPIECES; k++) {
					bord[i][j][k] = Color.EMPTY;
				}
			}
		}
		
	}
	/**
	 * 
	 * @param x x and y coordinate to be checked for a winner.
	 * @param y
	 * @return color of winner field.
	 */
	public Color fieldwon(int x, int y) {
		Color[] pin = getPin(x,y);
		if (!pin[4].equals(Color.EMPTY)) {//if there is a base this field is instant won.
			return pin[4];
		} else {
		}
		return null;
	}
	
	/**
	 * this function returns the index of the maxim value in an array.
	 * it will return -1 if there are multiple indexes with the same maximum value;
	 * @param array input array(pin on board) to be checked 
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
	public Color intIndex(int i) {
		switch (i){
			case 0: {
				return Color.BLUE;
			}
			case 1: {
				return Color.PURPLE;
			}
			case 2: {
				return Color.YELLOW;
			}
			case 3: {
				return Color.GREEN;
			}	
			default:
				return Color.EMPTY;
		}
	}
	
	/**
	 * this fucntion cacualtes the total on a given place on the map.
	 * it returns an array, index 0 is blue , 1 is purple, 2 is yellow, 3 is green.
	 * @return
	 * x is line y is column.
	 */
	public int[] tallyUp(int x, int y) {
		int[] tally = new int[4];
		for (int i = 1; i < 5; i++) {
			colorIndex(getRing(x, y, i));
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
				if (getRing(i, j, 0) != Color.EMPTY) {
					sum[colorIndex(getRing(i, j, 0))]++;
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
		//ads a multicolored cirlc on a random place so the game can start
		bord[a[0]][a[1]][4] = Color.BLUE;
		bord[a[0]][a[1]][1] = Color.GREEN;
		bord[a[0]][a[1]][2] = Color.PURPLE;
		bord[a[0]][a[1]][3] = Color.YELLOW;
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
			if (this.bord[x][y][i] == col) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCompletlyEmpty(int x, int y) {
		for (int i = 0; i < 5; i++) {
			if (this.bord[x][y][i] != Color.EMPTY) {
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
		if (getRing(x, y, 0) == Color.EMPTY) {	
			if (this.hasFriend(x, y, col)) {
				if (circleSize == 0) {
					if (this.isCompletlyEmpty(x, y)) {
						return true;
					} else {
						System.out.println("There are peices here, cant put base");
						return false;
					}
				}
				if (this.bord[x][y][circleSize] == Color.EMPTY) {
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
			this.bord[x][y][circleSeize] = col;
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
	public Color getRing(int x, int y, int circleSeize) {
		return this.bord[x][y][circleSeize];
	}
	
	/**
	 * gives all the cirlces in a board coordinate
	 * @param x line
	 * @param y column 
	 * @return a vector of Color 
	 */
	public Color[] getPin(int x, int y) {
		Color[] pin = new Color[DIFFPIECES];
		for (int i = 0; i < DIFFPIECES; i++) {
			pin[i] = this.getRing(x, y, i);
		}
		return pin;
	}
	public Color[][][] getBoard(){
		return bord;
	}
	
	/**
	 * tests if the boar dis full or not
	 * @return
	 */
	public boolean isFull() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 5; k++) {
					if (getRing(i, j, k) == Color.EMPTY) {
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
	public void display() {
		for (int i = 0; i < 5; i++) {
			String croth = "";
			for (int j = 0; j < 5; j++) {
				String stringy = "";
				for (int k = 0; k < 5; k++) {
					switch (bord[j][i][k]) {
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
