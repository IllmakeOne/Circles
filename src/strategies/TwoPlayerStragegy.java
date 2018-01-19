package strategies;

import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class TwoPlayerStragegy implements Strategy {
	
	
	Random rand = new Random();
	Board board;
	Color[] colors;

	public TwoPlayerStragegy(Color[] colors) {
		this.colors = colors; 
//		this.board = b;
	}
	
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

	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		// TODO Auto-generated method stub
		return null;
	}

}
