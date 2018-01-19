package strategies;

import java.util.ArrayList;
import java.util.Random;

import players.Player;

import java.util.HashMap;
import java.util.Map;

import ringz.Board;
import ringz.Color;

import ringz.Move;

public class FourPlayerSmart implements Strategy{
	
	
	Random rand = new Random();
	Board board;
	Color[] colors;
	
	public FourPlayerSmart(Color[] colors, Player play) {
		this.colors = colors; 
		//this.board = b;
		
	} 	
	
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		ArrayList<Move> moves = bord.getPossibleMoves(colors[0], pieces);
		HashMap<Move, Double> gains = new HashMap<Move, Double>();
		int currentScore;
		Double newScore;
		for (int i = 1; i < moves.size(); i++) { //itterate trough all the possible moves
			Color[][][] copy = board.deepCopy();
			Move apply = moves.get(i);
			currentScore = board.total()[board.colorIndex(colors[0])];
			newScore = new Double(currentScore);
			
			
			gains.put(apply, newScore);
		}
		
		return moves.iterator().next();
	}
//	public <Move, Double>HashMap<Move, Double> possibleMoves(Board bord, int[][] pieces) {
//		HashMap<Move, Double> moves = new HashMap<Move, Double>();
//		double begin = 0.00;
//		int countMoves = board.getPossibleMoves(colors[0], pieces).size();
//		board.getPossibleMoves(colors[0], pieces).
//		return moves;
//	}
	

	@Override
	public String getName() {
		return "smarter ai";
	}

	
}


