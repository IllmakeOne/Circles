package players;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public interface Player {

	/**
	 * @return the name of the player
	 */
	/* @ensure \result != null */
	public /*pure*/ String getName();
		

	/**
	 * @return the Color array of the player.
	 */
	/* @ensure \result != null */
	public /*pure*/ Color[] getColor();
	
	/**
	 * @return the bidimensional array of pieces of the player.
	 */
	/* @ensure \result != null */
	public /*pure*/ int[][] getPieces();
	
	/**
	 * @return the starting coordinate of the first piece into an int array.
	 */
	public int[] getStart();
	
	/**
	 * @return false if the player still has pieces.
	 */
	public boolean isOutOfPieces();
	
	/**
	 * @return the move the players wants to make.
	 */
	public Move determineMove(Board bord);
	
	/**
	 * decrease the player;s pieces based on a move.
	 */
	void decresePiece(Move move);
}
 