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
	
	public FourPlayerSmart(Color[] colors) {
		this.colors = colors; 
		//this.board = b;
		
	} 	
	
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		ArrayList<Move> moves = bord.getPossibleMoves(colors[0], pieces);
		HashMap<Move, Double> gains = new HashMap<Move, Double>();
		Double currentgain;
		Double newScore;
		int[][] remainingPiece;
		
		for (int i = 0; i < moves.size(); i++) { //itterate trough all the possible moves
			Board copy = board.deepCopy();
			Move apply = moves.get(i);
			int circle = apply.getCircle();
			remainingPiece = pieces;
			remainingPiece[0][circle] =- 1;
			currentgain = new Double(0);
			copy.addCircle(apply);
			newScore =  new Double(copy.total()[board.colorIndex(colors[0])]);
			Double mm = MovesModifier(bord, pieces, copy, remainingPiece);
			newScore = newScore*mm*lowerenemymoves+canBeContested;
			if(currentgain < newScore) {
				gains.put(apply, newScore);
			} else {
				gains.put(apply, currentgain);
			}
		}
		return getHighestGain(gains);
	}
	public Double canBeContested() {
		
		return null;
	}
	public Double MovesModifier(Board board, int[][] pieces,Board copy, int[][] remainPiece) {
		//HashMap<Move, Double> moves = new HashMap<Move, Double>();
		double begin = 0.00;
		double after = 0.00;
		begin = ((Integer)board.getPossibleMoves(colors[0], pieces).size()).doubleValue();//shit to get it to Double
		after = ((Integer)copy.getPossibleMoves(colors[0], remainPiece).size()).doubleValue();//shit to get it to Double
		Double modifier = begin - after;
		
		return modifier;
	}
	
	public Move getHighestGain(HashMap<Move, Double> moveScores) {
		Double currentgain = new Double(0);
		Move maxcounter = moveScores.keySet().iterator().next();//to emsure it will always return a move
		for (Move m:moveScores.keySet()) {//should itterate trough gains to find the best one
			if(currentgain<moveScores.get(m)) {
				currentgain = moveScores.get(m);
				maxcounter = m;
			}
		}
		return  maxcounter;
	}
		
	@Override
	public String getName() {
		return "smarter ai";
	}

	
}


