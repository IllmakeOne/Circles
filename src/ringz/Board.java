package ringz;

import java.util.ArrayList;
import java.util.Observable;

import players.Player;


public class Board extends Observable {
	
	private Color[][][] bord; //x, y, pin
	public static final int DIM = 5;
	public static final int DIFFPIECES = 5; 
	//different amount of pieces(12 rings in 4 sizes, 3 bases)
//	public enum Color { BLUE, PURPLE, YELLOW, GREEN, EMPTY };
	
	/**
	 * initialize the  board with empty fields.
	 */
	public /*@ pure */ Board() {
		this.bord = new Color[DIM][DIM][DIFFPIECES];
		for (int x = 0; x < DIM; x++) {
			for (int y = 0; y < DIM; y++) {
				for (int pieces = 0; pieces < DIFFPIECES; pieces++) {
					bord[x][y][pieces] = Color.EMPTY;
				}
			}
		}
	}
	
	/**
	 * this function tests if the board is completely and utterly empty.
	 * @return false when not empty, @return true when empty
	 */
	
	public  /*@ pure */ boolean emptyBoard() {
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				for (int k = 0; k < DIFFPIECES; k++) {
					if (getRing(i, j, k) != Color.EMPTY) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * crutch function.
	 * puts together the array for making a move with it.
	 * @param x is the line
	 * @param y is the column
	 * @param circlesize is the circlesize
	 * @return the array
	 */
	/* @ requires 0 <= x && x <= 4; 
	 * @ requires 0 <= y && y <= 4;
	 * @ requires 0 <= circlesize && circlesize <= 4;
	 */
		 
	public  /*@ pure */ int[] createArray(int x, int y, int circlesize) {
		int[] cord = new int[3];
		cord[0] = circlesize;
		cord[1] = x;
		cord[2] = y;
		return cord;
	}
	
	
	/**
	 * return an array of possible moves a color can take.
	 * @param color the color of player's possible moves 
	 * @return ArrayList<Move> list with all the possible moves
	 */
	
	/*
	 * @requires color[0] != null;
	 * @requires pieces != null;
	 */
	public /*@ pure */ ArrayList<Move> getPossibleMoves(Color[] color, int[][] pieces) {
		Move move = null;
		ArrayList<Move> list = new ArrayList<Move>();
		for (int cnr = 0; cnr < color.length; cnr++) {
			for (int i = 0; i < DIM; i++) {
				for (int j = 0; j < DIM; j++) {
					if (hasFriend(i, j, color[cnr])) {
						if (isCompletlyEmpty(i, j) &&
								pieces[cnr][0] != 0) {
							move = new Move(createArray(i, j, 0), color[cnr]);
							list.add(move);
						} 
						if (getRing(i, j, 0) == Color.EMPTY) {
							for (int k = 1; k < DIFFPIECES; k++) {
								if (getRing(i, j, k) == Color.EMPTY &&
										pieces[cnr][k] != 0) {
									move = new Move(createArray(i, j, k), color[cnr]);
									list.add(move);
								}
							}
						}
					}
				}
			}	
		}
		
		return list;

	}		
	
	/**
	 * return a deep copy of the board.
	 * @return copy of the board
	 */
	public  /*@ pure */ Board deepCopy() {
		Board copy = new Board();
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {
				for (int k = 0; k < DIFFPIECES; k++) {
					Move move = new Move(new int[]{k, i, j}, getRing(i, j, k));
					copy.addCircleNoCheck(move);
				}
			}
		}
		return copy;	
	}
	
	/**
	 * crutch function
	 * this function calculates the index of the maximum value in an array.
	 * it will return -1 if there are multiple indexes with the same maximum value;
	 * @param array input array(pin on board) to be checked 
	 * @return this function returns the index of the maximum value in an array.
	 * it will return -1 if there are multiple indexes with the same maximum value;
	 */
	/*
	 * @ensures (/result >= -1) && (/result <= array.length());
	 */
	public static  /*@ pure */ int arrayMaximum(int[] array) {
		int highestScore = 0, maxindex1 = 1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] >= highestScore) {
				highestScore = array[i];
				maxindex1 = i;
			}
		}		
		for (int i = 0; i < maxindex1; i++) {
			if (array[i] == highestScore) {
				return -1;
			}
		}
		return maxindex1;
	}
	
	
	/**
	 * crutch function .
	 * return 0 for blue, 1 for purple , 2 for yelo2 and 3 for green
	 * @param color, the colour which needs to be converted
	 * @return integer correlated with the colour.
	 */
	/*
	 * @ensures (\result >= -1) && (\result <= 3);
	 */
	public  /*@ pure */ int colorIndex(Color color) {
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
	 * crutch function. inverse of colorIndex
	 * return blue for 0, purple for 1 , yellow for 2 and green for 3.
	 * @param i, integer which needs to be converted.
	 * @return color, the color correlated with this integer.
	 */
	/*
	 * @ensures (\result >= -1) && (\result <= 3);
	 * @ensures \result.instanceof(Color);
	 */
	public  /*@ pure */ Color intIndex(int i) {
		switch (i) {
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
	 * this function calculates the total amount of rings on a given place on the map.
	 * stored in an array, value 0 if none rings of this colour present.
	 * @return  it returns an array, index 0 is blue , 1 is purple, 2 is yellow, 3 is green.
	 * @param line is line
	 * @param column is column.
	 */
	/*
	 * @requires (line >= 0) && (line <= 4);
	 * @requires (column >= 0) && (column <= 4);
	 */
	public  /*@ pure */ int[] tallyUp(int line, int column) {
		int[] tally = new int[4];
		for (int ringsize = 0; ringsize < DIFFPIECES; ringsize++) {
			if (colorIndex(getRing(line, column, ringsize)) != -1) {
				tally[colorIndex(getRing(line, column, ringsize))]++;
			}	
		}
		return tally;
	}
	
	
	/**
	 * calculates the total score of the game.
	 * it returns an int array.
	 * index 0 blue, index 1 purple, index 2 yellow, index 3 green.
	 * on each index it says how many points each has
	 */
	public  /*@ pure */ int[] total() {
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
	
		return sum;
	}
	
	
	/**
	 * places the first piece of the game.
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
	 * @param y is the column
	 * @param col is the color being looked for
	 * @return true if the field has the color
	 */
	public  /*@ pure */ boolean fieldHas(int x, int y, Color col) {
		for (int i = 0; i < DIFFPIECES; i++) {
			if (this.bord[x][y][i] == col) {
				return true;
			}
		}
		return false;
	}
	
	public /*@ pure */ boolean isCompletlyEmpty(int x, int y) {
		for (int i = 0; i < DIFFPIECES; i++) {
			if (this.bord[x][y][i] != Color.EMPTY) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * test if a color on a position is has around it at least one other piece of the same color.
	 */
	public  /*@ pure */ boolean hasFriend(int x, int y, Color col) {
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
	public  /*@ pure */ boolean validMove(int x, int y, Color col, int circleSize) {
		//display();
		this.setChanged();
		if (getRing(x, y, 0) == Color.EMPTY) {	
			if (this.hasFriend(x, y, col)) {
				if (circleSize == 0) {
					if (this.isCompletlyEmpty(x, y)) {
						notifyObservers("added");
						return true;
					} else {
						//System.out.println("There are peices here, cant put base");
						notifyObservers("notCompEmpty");
						return false;
					}
				}
				if (this.bord[x][y][circleSize] == Color.EMPTY) {
					return true;
				} else {
					//System.out.println("Field not empty");
					notifyObservers("notEmpty");
					return false;
				}
			} else {
				//System.out.println("Has no near piece of the same color NEAR IT");
				notifyObservers("noFriend");
				return false;
			}
		} else {
			//System.out.println("The space already has a BASE");
			notifyObservers("hasBase");
			return false;
		}
		
	}
	/**
	 * checks if the player with color c has the field.
	 * @param x x
	 * @param y y
	 * @param c color
	 * @return true if person has won this field, false if not
	 */
	public boolean hasField(int x, int y, Color c) {
		if (!fieldHas(x, y, c)) {
			return false;
		}
		int[] score = tallyUp(x, y);
		int total = 0;
		for (int i = 0; i < 4; i++) {
			total = total + score[i];
		}
		if (bord[x][y][0].equals(c)) {
			return true;
		}
		if (score[colorIndex(c)] > 2) {
			return true;
		}
		if (score[colorIndex(c)] == 2) {
			if (total < 2) {
				return true;
			} else {
				int diffcolor = 0;
				for (int i = 0; i < colorIndex(c); i++) {
					diffcolor = diffcolor + score[i];
				}
				for (int i =  colorIndex(c); i < 4; i++) {
					diffcolor = diffcolor + score[i];
				}
				if (diffcolor == 2) {
					return true;
				}
			}
		}
		System.out.println(score[colorIndex(c)]);
		if (score[colorIndex(c)] == 1 && total == 0) {
			return true;
		}
		return false;
		
	}
	
	/**
	 * this functions tests if a given player has options to play.
	 * it tests by going though all the possible places the player can play with each color, and 
	 * tests if the player has anymore piece of that kind.
	 * @param play
	 * @return true if is still able to play
	 */
	public  /*@ pure */ boolean isStillAbleToPlace(Player play) {
		ArrayList<Move> move = this.getPossibleMoves(play.getColor(), play.getPieces());
		if (move.isEmpty()) {
			return false;
		} else {
			return true;
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
			//System.out.println("circles aded");
			return true;
		} else {
			return  false;
		}
	}
	/**
	 * ads a cricle does not check if it is valid. 
	 * @param copy 
	 * @param circleSeize the seize of the circle
	 * @param col the color of the circle
	 * @param x the line
	 * @param y the column
	 */
	public void addCircleNoCheck(Move move) {
		int x, y, circleSeize;
		Color col;
		x = move.getLine();
		y = move.getColumn();
		circleSeize = move.getCircle();
		col = move.getColor();
		
		this.bord[x][y][circleSeize] = col;
			//System.out.println("circles aded");
			
	}
	
	/**
	 * return the specific ring. 
	 * @param x line
	 * @param y column
	 * @param circleSeize hight
	 * @return 
	 */
	public  /*@ pure */ Color getRing(int x, int y, int circleSeize) {
		return this.bord[x][y][circleSeize];
	}
	
	/**
	 * gives all the cirlces in a board coordinate.
	 * @param x line
	 * @param y column 
	 * @return a vector of Color 
	 */
	public  /*@ pure */ Color[] getPin(int x, int y) {
		Color[] pin = new Color[DIFFPIECES];
		for (int i = 0; i < DIFFPIECES; i++) {
			pin[i] = this.getRing(x, y, i);
		}
		return pin;
	}
	public /*@ pure */ Color[][][] getBoard() {
		return bord;
	}
	
	/**
	 * tests if the board is full or not.
	 * @return true when full, false when at least 1 spot is free.
	 */
	public /*@ pure */ boolean isFull() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (isCompletlyEmpty(i, j)) {
				//	System.out.println("is one entire free");
					return false;
				}
				for (int k = 1; k < 5; k++) {
					if (getRing(i, j, k) == Color.EMPTY
							&& getRing(i, j, 0) == Color.EMPTY) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * test is x and y are within bounds.
	 * @param x
	 * @param y
	 * @return
	 */
	public /*@ pure */ boolean inBounds(int x, int y) {
		if (x > 4 || x < 0) {
			return false;
		}
		
		if (y > 4 || y < 0) {
			return false;
		}
		
		return true;
	}
	

	public /*@ pure */ void display() {
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
	
	public /*@ pure */ boolean validMoveWithoutObservers(int x, int y, Color col, int circleSize) {
		//display();
		if (getRing(x, y, 0) == Color.EMPTY) {	
			if (this.hasFriend(x, y, col)) {
				if (circleSize == 0) {
					if (this.isCompletlyEmpty(x, y)) {
						return true;
					} else {
						//System.out.println("There are peices here, cant put base");
						return false;
					}
				}
				if (this.bord[x][y][circleSize] == Color.EMPTY) {
					return true;
				} else {
				//	System.out.println("Field not empty");
					return false;
				}
			} else {
			//	System.out.println("Has no near piece of the same coolor NEAR IT");
				return false;
			}
		} else {
			//System.out.println("The space already has a BASE");
			return false;
		}
		
	}
	
}
