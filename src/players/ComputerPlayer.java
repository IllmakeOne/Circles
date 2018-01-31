package players;

import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;
import strategies.*;
import view.TUI;

public class ComputerPlayer implements Player {

	private int[][] pieces;
	private Color[] color;
	private String name;
	private Strategy strateg;
	private int numberplayers;
	
	
	public ComputerPlayer(Color c, String name, int time) {
		numberplayers = 4;
		this.color = new Color[2];
		this.color[0] = c;	
		this.pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3; 
		}
		if (time == 1) {
			this.strateg = new NotsoSmartStrategy(color);
		} else {
			this.strateg = new SmartStrategy(color, 4);
		}	
		this.name = "computer " + name + " " + strateg.getName();
	}
	
	public ComputerPlayer(int nrplayers, Color c1, Color c2, String nam, int time) {
		this.color = new Color[2];
		this.color[0] = c1;
		this.color[1] = c2;	
		this.pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
		if (nrplayers == 2) {

			numberplayers = 2;
			if (time == 1) {
				this.strateg = new NotsoSmartStrategy(color);
			} else {
				this.strateg = new SmartStrategy(color, 2);
			}
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 3;
			} 
			
		} else {

			numberplayers = 3;
			if (time == 1) {
				this.strateg = new NotsoSmartStrategy(color);
			} else {
				this.strateg = new SmartStrategy(color, 3);
			}
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 1;
			}
		}
		this.name = "computer " + name + " " + strateg.getName();
	}

	
	@Override
	public Move determineMove(Board bord) {
		TUI tui = new TUI("gaU");
		tui.showPieces(this);
		Move move = strateg.determineMove(bord, this.pieces);
		//pieces[0][move.getCircle()]--;
		return move;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	
	@Override
	public int getnumberPlayers() {
		return numberplayers;
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

	/*
	 * (non-Javadoc)
	 * @see players.Player#decresePiece(ringz.Move)
	 * @requires move != null;
	 * @requires move.getColor() == getColor()[0] || move.getColor() == getColor()[1];
	 */
	@Override
	public void decresePiece(Move move) {
		int colorindex;
		if (color[0] == move.getColor()) {
			colorindex = 0;
		} else {
			colorindex = 1;
		}
		pieces[colorindex][move.getCircle()]--;
	}
 
}
