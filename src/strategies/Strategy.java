package strategies;

import ringz.Board;
import ringz.Move;

public interface Strategy {

	/**
	 * @return the name of the strategy.
	 */
	public String getName();

	/**
	 * return the best Move out of the possible moves.
	 * the possible moves are determined by the @param board, the strategy's colors
	 * and the pieces the player currently has which is @param pieces
	 */
	public	Move determineMove(Board bord, int[][] pieces);

}
