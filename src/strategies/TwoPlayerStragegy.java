package strategies;

import java.util.ArrayList;
import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class TwoPlayerStragegy implements Strategy {
	
	
	Random rand = new Random();
	Color[] colors;

	public TwoPlayerStragegy(Color[] colors) {
		this.colors = colors; 
	}
	
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
		return "this is dumb as fuck ";
	}



}
