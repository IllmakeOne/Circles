package strategies;

import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class ThreePlayerStrategy implements Strategy {
	
	Random rand = new Random();
	Board board;
	Color[] colors;
	public ThreePlayerStrategy(Color[] color) {
		this.colors = color; 
	}
	public Move determineMove() {
		// TODO Auto-generated mhod stub
		return null;
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