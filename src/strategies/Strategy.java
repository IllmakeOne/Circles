package strategies;

import ringz.Board;
import ringz.Move;

public interface Strategy {

	
	public String getName();

	public	Move determineMove(Board bord, int[][] pieces);

}
