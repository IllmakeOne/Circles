package Ringz;

import Ringz.Color;

public interface Player {
	
	public Move determineMove();

	public String getName();
	
	public String getColor();
	
	public Color chooseColor(Color c1, Color c2);
	
	public int[] getStart();
}
