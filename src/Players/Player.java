package Players;

import Ringz.Board;
import Ringz.Color;
import Ringz.Move;

public interface Player {

	public String getName();
		
	public Color[] getColor();
	
	public int[][] getPieces();
	
//	public Color chooseColor(Color c1, Color c2);
	
	public int[] getStart();
	
	public boolean isOutOfPieces();


	public Move determineMove(Board bord);
}
 