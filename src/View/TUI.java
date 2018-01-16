package View;
 import java.util.Scanner;

import Ringz.*;
 
public class TUI implements View {
	
	private static final String SHOW = "show";
	private static final String PIECES = "pieces";
	private static final String MOVE = "move";
	
	
	//@Override
	public Move askMove(Player play, Board board) {
		Scanner in = new Scanner(System.in);
		System.out.println("What would you like to do?" + 
				" \n 'show' to display the board again" +
				" \n 'pieces' to show your pieces " +
				" \n 'move' to make a move");
		int flag = 1;
		String imput;
		while (flag != 0) {
			imput = in.nextLine();
			if (imput.equals(SHOW)) {
				updateDisplay(board);
			} else if (imput.equals(PIECES)) {
				showPieces(play);
			} else if (imput.equals(MOVE)) {
				flag = 0;
				System.out.println(" You will make a move now " +
						" \n You have the colors " + getStringColor(play));
			}
		}
		int[] move = new int[3];
		flag = 1;
		int colorIndex = 0;
		if (play.getColor()[1] != null) {
			Color c = this.chooseColor(play.getColor()[0], play.getColor()[1]);
			if (c == play.getColor()[1]) {
				colorIndex = 1;
			}
			readMove(move, colorIndex, play);
		} else {
			readMove(move, colorIndex, play);
		}
		
		play.getPieces()[colorIndex][move[0]] -= 1;
		return new Move(move, play.getColor()[colorIndex]);
	}
	
	

	@Override
	public void updateDisplay(Board board) {
		for (int i = 0; i < board.DIM; i++) {
			String croth = "";
			for (int j = 0; j < board.DIM; j++) {
				String stringy = "";
				for (int circlesize = 0; circlesize < board.DIFFPIECES; circlesize++) {
					switch (board.getRing(j, i, circlesize)) {
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

	

	/**
	 * this function returns a string of the colors of the player play.
	 * @param play
	 * @return
	 */
	public String getStringColor(Player play) {
		Color[] color = play.getColor();
		String stringy = "";
		if (color[1] != null) {
			stringy += color[0].toString() + "/" + color[1].toString();
		} else {
			stringy += color[0].toString();
		}
		return stringy;
	}
	
	
	//------------------Stuff to help with the reading of a mvoe ----------------
	
	/**
	 * reads form user three numbers, cirlce zize, lien and column.
	 * @param move is the arraty int that remebrts the user's input
	 * @param colorIndex is the index of the color of the player
	 */
	public void readMove(int[] move, int colorIndex, Player play) {
		Scanner in = new Scanner(System.in);
		int flag = 1;
		String[] words;
		while (flag == 1) {
			System.out.println("Please give move \n"
					+ "( first number the size of the piece, \n"
					+ "second number the line and the thirds , the column \n");
			String line = in.nextLine();
			words = line.split(" ");
			if (words.length == 3) {
				for (int i = 0; i < words.length; i++) {
					move[i] = Integer.parseInt(words[i]) - 1;
				}
				if (this.isValid(move, colorIndex, play)) {
					flag = 0;
				}
			}
			in = new Scanner(System.in);
		}
	}
	
	/**
	 * a fcntion that tests if the given parametre, a int arraty, is valid as a move.
	 * this fucntion is only used in determineMove as a cruthch.
	 * @param x
	 * @return
	 */
	public boolean isValid(int[] x, int colorIndex, Player play) {
		if (x[0] > 4 || x[0] < 0) {
			System.out.println("No such piece");
			return false;
		}
			
		if (x[1] > 4 || x[1] < 0) {
			System.out.println("line too big");
			return false;
		}
		if (x[2] > 4 || x[2] < 0) {
			System.out.println("Column too big");
			return false;
		}
		if (play.getPieces()[colorIndex][x[0]] == 0) {
			System.out.println("Youare aout of these pieces");
			return false;
		}
		
		return true;
	}
	
	/**
	 * determine which color to place.
	 */
	public Color chooseColor(Color c1, Color c2) {
		Color choce = null;
		int flag = 1;
		Scanner in = new Scanner(System.in);
		String[] words;
		while (flag == 1) {
			System.out.println("Please choose ofne hte fhtse colros "
					+ c1 + " and " + c2);
			String line = in.nextLine();
			if (line.equals(c1.toString())) {
				choce = c1;
				flag = 0;
			} else {
				if (line.equals(c2.toString())) {
					choce = c2;
					flag = 0;
				} else {
					System.out.println("Invalid color choice");
				}
			}
			
			in = new Scanner(System.in);
		}
		return choce;
	}
	

	@Override
	public void outOfPieces() {
		System.out.println("you are out of pieces");
		
	}


	@Override
	public void showPieces(Player play) {
		int[][] pieces = play.getPieces();
		Color[] color = play.getColor();
		for (int i = 0; i < color.length; i++) {
			System.out.println("you have the following " + color[i] + " pieces:");
			for (int j = 0; i < Board.DIFFPIECES; j++) {
				switch (j) {
					case 0: {
						System.out.println(pieces[i][j] + " bases");
						break;
					} 
					case 1: {
						System.out.println(pieces[i][j] + " dots");
						break;
					} 
					case 2: {
						System.out.println(pieces[i][j] + " small rings");
						break;
					} 
					case 3: {
						System.out.println(pieces[i][j] + " medium rings");
						break;
					} 
					case 4: {
						System.out.println(pieces[i][j] + " large rings");
						break;
					} 
				}
			}
		}
	}

	@Override
	public void notAbletoPlay() {
		System.out.println("you are not able to place any pieces anymore");
	}



}
