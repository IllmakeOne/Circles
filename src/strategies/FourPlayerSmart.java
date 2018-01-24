package strategies;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import players.Player;

import java.util.HashMap;
import java.util.Map;

import ringz.Board;
import ringz.Color;

import ringz.Move;

public class FourPlayerSmart implements Strategy {
	
	
	Random rand = new Random();
	Board board;
	Color[] colors;
	public static final int MINITTERATION = 3;
	public FourPlayerSmart(Color[] colors) {
		this.colors = colors; 
		//this.board = b;
		
	} 	
	
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		boolean noMoreChange = true;
		int itteration = 0;
		Double previous = null;
		Move found = null;
		while (noMoreChange && itteration > MINITTERATION) {
			HashMap<Move, Double> move = findBestMove(bord, pieces);
			
			if (move.values().iterator().next().equals(previous)) {
				noMoreChange = false;
				
			}
			itteration = itteration + 1;
			previous = move.values().iterator().next();
			found = move.keySet().iterator().next();
		}
		return found;
	}
	public HashMap<Move, Double> findBestMove(Board bord, int[][] pieces) {
		ArrayList<Move> moves = bord.getPossibleMoves(colors[0], pieces);
		HashMap<Move, Double> gains = new HashMap<Move, Double>();
		Double badMove;
		Double newScore;
		int[][] remainingPiece;
		
		for (int i = 0; i < moves.size(); i++) { //itterate trough all the possible moves
			Board copy = board.deepCopy();
			Move apply = moves.get(i);
			badMove = new Double(0);
			Double cc = new Double(1);//if the player doesn't have te field it should not look for this.
			if(this.board.fieldHas(apply.getLine(), apply.getColumn(),colors[0])) {
				badMove = new Double(-30000);//don't place a ring on a field already won
			} else {
				if (isEnemys(apply) >= 0 );
				//canBeContested
			}
			
			int circle = apply.getCircle();
			remainingPiece = pieces;
			remainingPiece[0][circle] =- 1;
			
			copy.addCircle(apply);
			newScore =  new Double(copy.total()[board.colorIndex(colors[0])]);
			Double mm = MovesModifier(bord, pieces, copy, remainingPiece);
			//Double cc = canBeContested(apply);
			//Double em = remainingEnemyMoves();
			//newScore = newScore*mm*em+canBeContested;
			if(badMove > newScore) {
				gains.put(apply, badMove);
			} else {
				gains.put(apply, newScore);
			}
		}
		return getHighestGain(gains);
	}
	/**
	 * 
	 * @param m = move to be checked if enemy has this pin
	 * @return int index of the player having this field, -1 if no one has it yet.
	 */
	public int isEnemys(Move m) {
		for (int i = 0; i < 4; i++){
			if (board.fieldHas(m.getLine(), m.getColumn(),board.intIndex(i))){
				return i;
			}
		}
		return -1;
	}
	public boolean canBeContested(Move m) {
		//Color[] pin = board.getPin(m.getLine(), m.getColumn());
		int[] pin = board.tallyUp(m.getLine(), m.getColumn());
		int skipthisone = board.colorIndex(colors[0]); //somehow don't check for own place
		int 
		if(pin[skipthisone] == 1 && board.tallyUp(x, y)) {//if you have 0 and another color already has 2 don't place,
			for (int i = 0; i < skipthisone ; i++) {//if someone has more then 3 pieces, not contestable
				if(  pin[i] >= 3 ) {
					return false;
				}
			}
			for (int i = skipthisone + 1; i < 4 ; i++) {
				if(  pin[i] > 3 ) {
					return false;
				}
			}
			for (int i = 0; i < skipthisone ; i++) {//if someone has more then 3 pieces, not contestable
				if(  pin[i] >= 3 ) {
					return false;
				}
			}
			for (int i = skipthisone + 1; i < 4 ; i++) {
				if(  pin[i] >= 3 ) {
					return false;
				}
			}
		}
		return true;		
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
	
	public HashMap<Move, Double> getHighestGain(HashMap<Move, Double> moveScores) {
		Double currentgain = new Double(0);
		HashMap<Move, Double> result = null;
		Move maxcounter = moveScores.keySet().iterator().next();//to emsure it will always return a move
		for (Move m:moveScores.keySet()) {//should itterate trough gains to find the best one
			if(currentgain<moveScores.get(m)) {
				currentgain = moveScores.get(m);
				maxcounter = m;
			}
		}
		result.put(maxcounter, currentgain);
		return result;
	}
		
	@Override
	public String getName() {
		return "smarter ai";
	}

	
}


