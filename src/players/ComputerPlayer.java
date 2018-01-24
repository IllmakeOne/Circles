package players;

import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;
import strategies.FourPlayerSmart;
import strategies.FourPlayerStrategy;
import strategies.Strategy;
import strategies.ThreePlayerStrategy;
import strategies.TwoPlayerStragegy;

public class ComputerPlayer implements Player {

	private int[][] pieces;
	private Color[] color;
	private String name;
	private Strategy strateg;
	
	
	public ComputerPlayer(Color c, String name) {
		this.color = new Color[2];
		this.color[0] = c;	
		this.name = "computer two players " + name + " " + strateg.getName();
		this.pieces = new int[5][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
		this.strateg = new FourPlayerSmart(color);
	}
	
	public ComputerPlayer(int nrplayers, Color c1, Color c2, String nam) {
		this.color = new Color[2];
		this.color[0] = c1;
		this.color[1] = c2;
		this.name = "computer";
		this.pieces = new int[5][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
		if (nrplayers == 2) {
			this.strateg = new TwoPlayerStragegy(this.color);
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 3;
			} 
			
		} else {
			this.strateg = new ThreePlayerStrategy(this.color);
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 1;
			}
		}
	}

	
	@Override
	public Move determineMove(Board bord) {
		Move move = strateg.determineMove(bord, this.pieces);
		pieces[0][move.getCircle()]--;
		return move;
	}

	@Override
	public String getName() {
		return this.name;
	}

	

	

	@Override
	public int[] getStart() {
		int[] firstMove = new int[2];
		Random rand = new Random();
		firstMove[0] = rand.nextInt(3) + 1;
		firstMove[1] = rand.nextInt(3) + 1;
		return firstMove;
	}

	@Override
	public Color[] getColor() {
		return color;
	}

	@Override
	public int[][] getPieces() {
		return this.pieces;
	}

	@Override
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

	@Override
	public void decresePiece(int col, int circleSize) {
		pieces[col][circleSize]--;		
	}
 
}
