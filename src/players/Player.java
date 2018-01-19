package players;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public interface Player {

	public String getName();
		
	public Color[] getColor();
	
	public int[][] getPieces();
	
//	public Color chooseColor(Color c1, Color c2);
	
	public int[] getStart();
	
	public boolean isOutOfPieces();


	public Move determineMove(Board bord);

	void decresePiece(int col, int circleSize);
}
 