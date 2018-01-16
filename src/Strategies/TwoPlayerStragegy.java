package Strategies;

import java.util.Random;

import Ringz.Board;
import Ringz.Color;
import Ringz.Move;

public class TwoPlayerStragegy implements Strategy {
	
	
	Random rand = new Random();
	Board board ;
	Color[] colors;

	public TwoPlayerStragegy(Color[] colors) {
		this.colors = colors; 
//		this.board = b;
	}
	
	@Override
	public Move determineMove() {
		int[] cord =  new int[3];
		cord[0] = rand.nextInt(5) + 1;
		cord[1] = rand.nextInt(5) + 1;
		cord[2] = rand.nextInt(5) + 1;
		return new Move(cord, colors[0]);
	}

	@Override
	public String getName() {
		return "this is dumb as fuck ";
	}

}
