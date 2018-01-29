package strategies;

import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class ThreePlayerSmart implements Strategy {

	Random rand = new Random();
	Color[] colors;

	public ThreePlayerSmart(Color[] colors) {
		this.colors = colors; 
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		// TODO Auto-generated method stub
		return null;
	}

}
