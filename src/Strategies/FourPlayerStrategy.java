package Strategies;

import java.util.ArrayList;
import java.util.Random;

import Ringz.Board;
import Ringz.Color;
import Ringz.Move;

public class FourPlayerStrategy implements Strategy {

	
	
	Random rand = new Random();
	Board board;
	Color[] colors;

	public FourPlayerStrategy(Color[] colors) {
		this.colors = colors; 
//		this.board = b;
	} 	
	
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		ArrayList<Move> move = bord.getPossibleMoves(colors[0], pieces);
		int random = rand.nextInt(move.size());
		return move.get(random);
	}

	@Override
	public String getName() {
		return "this is dumb as fuck ";
	}

	
}
