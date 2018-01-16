package Ringz;

import java.util.Scanner;

import Ringz.Color;

public class HumanPalyer implements Player {
	 
	
	private int[][] pieces;
	private Color[] color;
	private String name;
	
	/**
	 * contrustor of the huma player.  if the gaems ahs 4 players.
	 * it receives a color which wil hte the players's color
	 * @param c
	 */
	public HumanPalyer(Color c, String name) {
		this.color = new Color[2];
		this.color[0] = c;
		this.name = name;
		this.pieces = new int[5][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
	}
	
	public HumanPalyer(int nrplayers, Color c1, Color c2, String name) {
		this.color = new Color[2];
		this.color[0] = c1;
		this.color[1] = c2;
		this.name = name;
		this.pieces = new int[5][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
		if (nrplayers == 2) {
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 3;
			}
			
		} else {
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 1;
			}
		}
	}
	
//	public HumanPalyer(Color c1, Color c2,  String name) {
//		this.color = c1;
//		this.secondcolor = c2;
//		this.name = name;
//		this.pieces = new int[5];
//		this.secondpieces = new int[5];
//		for (int i = 0; i < 5; i++) {
//			this.pieces[i] = 3;
//			this.secondpieces[i] = 3;
//		}
//	}
	
	 /**
	  * function that testes if the player is out of pieces.
	  */
	public boolean isOutOfPieces() {
		for (int i = 0; i < 5; i++) {
			if (pieces[0][i] != 0) {
				return false;
			}
		}
		for (int i = 0; i < 5; i++) {
			if (pieces[1][i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	
	
//	/**
//	 * a fcntion that tests if the given parametre, a int arraty, is valid as a move.
//	 * this fucntion is only used in determineMove as a cruthch.
//	 * @param x
//	 * @return
//	 */
//	public boolean isValid(int[] x, int colorIndex) {
//		if (x[0] > 4 || x[0] < 0) {
//			System.out.println("No such piece");
//			return false;
//		}
//			
//		if (x[1] > 4 || x[1] < 0) {
//			System.out.println("line too big");
//			return false;
//		}
//		if (x[2] > 4 || x[2] < 0) {
//			System.out.println("Column too big");
//			return false;
//		}
//		if (this.pieces[colorIndex][x[0]] == 0) {
//			System.out.println("Youare aout of these pieces");
//			return false;
//		}
//		
//		return true;
//	}
		
	
	/**
	 * reads form user three numbers, cirlce zize, lien and column.
	 * @param move is the arraty int that remebrts the user's input
	 * @param colorIndex is the index of the color of the player
	 */
//	public void readMove(int[] move, int colorIndex) {
//		Scanner in = new Scanner(System.in);
//		int flag = 1;
//		String[] words;
//		while (flag == 1) {
//			System.out.println("Please give move \n"
//					+ "( first number the size of the piece, \n"
//					+ "second number the line and the thirds , the column \n");
//			String line = in.nextLine();
//			words = line.split(" ");
//			if (words.length == 3) {
//				for (int i = 0; i < words.length; i++) {
//					move[i] = Integer.parseInt(words[i]) - 1;
//				}
//				if (this.isValid(move, colorIndex)) {
//					flag = 0;
//				}
//			}
//			in = new Scanner(System.in);
//		}
//	}
	
//	/**
//	 * detemines the move of human player form keyboard input. 
//	 */
//	@Override
//	public Move determineMove(Board board) {
//		int[] move = new int[3];
//		int flag = 1;
//		int colorIndex = 0;
//		if (this.color[1] != null) {
//			Color c = this.chooseColor(this.color[0], this.color[1]);
//			if (c == this.color[1]) {
//				colorIndex = 1;
//			}
//			readMove(move, colorIndex);
//		} else {
//			readMove(move, colorIndex);
//		}
//		
//		this.pieces[colorIndex][move[0]] -= 1;
//		return new Move(move, this.color[colorIndex]);
//	}
	
	
	/**
	 * detemine where hte firt multicoloed piece will be palced.
	 */
	public int[] getStart() {
		int[] move = new int[2];
		Scanner in = new Scanner(System.in);
		String[] words;
		int flag = 1;
		while (flag == 1) {
			System.out.println("Please the coordinate of the first piece \n"
					+ "( must be in the inneer circle \n"
					+ " first lien then column, both msu tbe betwen 2 and 4 \n");
			String line = in.nextLine();
			words = line.split(" ");
			if (words.length == 2) {
				for (int i = 0; i < words.length; i++) {
					move[i] = Integer.parseInt(words[i]) - 1;
				}
				if (move[0] < 4 && move[0] > 0 && move[1] > 0 && move[1] < 4) {
					flag = 0;
				}
			}
			in = new Scanner(System.in);
		}
		
		return move;
	}
	
	
	
//	/**
//	 * determin which color to place.
//	 */
//	public Color chooseColor(Color c1, Color c2) {
//		Color choce = null;
//		int flag = 1;
//		Scanner in = new Scanner(System.in);
//		String[] words;
//		while (flag == 1) {
//			System.out.println("Please choose ofne hte fhtse colros "
//					+ c1 + " and " + c2);
//			String line = in.nextLine();
//			if (line.equals(c1.toString())) {
//				choce = c1;
//				flag = 0;
//			} else {
//				if (line.equals(c2.toString())) {
//					choce = c2;
//					flag = 0;
//				} else {
//					System.out.println("Invalid color choice");
//				}
//			}
//			
//			in = new Scanner(System.in);
//		}
//		return choce;
//	}
	

	@Override
	public String getName() {
		return this.name;
	}

	
	public int[][] getPieces() {
		return this.pieces;
	}

	@Override
	public Color[] getColor() {
		return this.color;
	}




	
	
//	public static void main(String[] args) {
//		HumanPalyer boi = new HumanPalyer(Color.BLUE);
//		boi.determineMove();
//	}
	
	
}