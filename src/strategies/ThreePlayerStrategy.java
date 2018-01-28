package strategies;

import java.util.ArrayList;
import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class ThreePlayerStrategy implements Strategy {
	
	Random rand = new Random();
	Board board;
	Color[] colors;
	public ThreePlayerStrategy(Color[] color) {
		this.colors = color; 
	}
	
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		ArrayList<Move> move = bord.getPossibleMoves(colors, pieces);
		
		if (!move.isEmpty()) {
			int random = rand.nextInt(move.size());
			return move.get(random);
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


}
