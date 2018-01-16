package Ringz;

import Ringz.Color;

public interface Player {

	public String getName();
	
	public String getStringColor();
	
	public Color[] getColor();
	
	public int[][] getPieces();
	
	public Color chooseColor(Color c1, Color c2);
	
	public int[] getStart();
	
	public boolean isOutOfPieces();

	public Move determineMove(Board bord);
}
 