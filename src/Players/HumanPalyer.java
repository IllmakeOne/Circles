package Players;

import java.util.Scanner;

import Ringz.Board;
import Ringz.Color;
import Ringz.Move;

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
		this.pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
	}
	
	public HumanPalyer(int nrplayers, Color c1, Color c2, String name) {
		this.color = new Color[2];
		this.color[0] = c1;
		this.color[1] = c2;
		this.name = name;
		this.pieces = new int[2][5];
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
		
	

	public void decresePiece(int col, int circleSize) {
		pieces[col][circleSize]--;
	}
	
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

	@Override
	public Move determineMove(Board bord) {
		return null;
	}

	@Override
	public int[] getStart() {
		// TODO Auto-generated method stub
		return null;
	}




	
	
//	public static void main(String[] args) {
//		HumanPalyer boi = new HumanPalyer(Color.BLUE);
//		boi.determineMove();
//	}
	
	
}