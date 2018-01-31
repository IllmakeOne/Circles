package strategies;

import java.util.ArrayList;
import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class NotsoSmartStrategy implements Strategy {

	Random rand = new Random();
	Color[] colors;

	public NotsoSmartStrategy(Color[] colors) {
		this.colors = colors; 
	} 	
	
	//The not so mart strategy simply first puts bases and then it puts randomly.
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		ArrayList<Move> move = bord.getPossibleMoves(colors, pieces);
		int random = rand.nextInt(move.size());
		Move mov = move.get(random);
		if (!move.isEmpty()) {
			if (movesetHasBases(move)) {
				while (isBase(mov) != true) {
					random = rand.nextInt(move.size());
					mov = move.get(random);
				}
			}
			return mov;
		} else {
			return null;
		} 
	}
	
	public boolean isBase(Move move) {
		if (move.getCircle() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * this tests if the @param move list has a move which has a base as its piece.
	 */
	public boolean movesetHasBases(ArrayList<Move> move) {
		for (int i = 0; i < move.size(); i++) {
			if (move.get(i).getCircle() == 0) {
				return true;
			}
		}
		return false;
	}
	
	

	@Override
	public String getName() {
		return "notSmart";
	}

}
