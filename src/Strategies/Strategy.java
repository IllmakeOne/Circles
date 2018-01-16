package Strategies;

import Ringz.Board;
import Ringz.Move;

public interface Strategy {

	
	public String getName();

	public	Move determineMove(Board bord, int[][] pieces);

}
