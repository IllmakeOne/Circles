package strategies;

import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
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
	
	/**
	 * determines the move to be made. it will itterate till @param miniItteration 
	 * is surpassed and the parameter @param returns true. this will happen when the
	 * values no longer change
	 * @param bord the current board, 
	 * @param pieces the pieces the player currently has
	 * 
	 */
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
		ArrayList<Move> moves = bord.getPossibleMoves(colors, pieces);
		HashMap<Move, Double> gains = new HashMap<Move, Double>();
		Double badMove;
		Double newScore;
		int[][] remainingPiece;
		
		for (int i = 0; i < moves.size(); i++) { //itterate trough all the possible moves
			Board copy = board.deepCopy();
			Move apply = moves.get(i);
			badMove = new Double(0);
			Double cbc = new Double(1); //if the player doesn't have te field it
			//should not look for this.
			if (this.board.hasField(apply.getLine(), apply.getColumn(), colors[0])) {
				badMove = new Double(-30000); //don't place a ring on a field already won
			} else { //if it is not ours, check if it is someone else's, 
				//if it cannot be contested, dont place
				if (isEnemys(apply) != board.colorIndex(colors[0]) && isEnemys(apply) != -1 
						&& !canBeContested(apply, isEnemys(apply)))  {
					cbc = new Double(-30);
				}
			}
			
			int circle = apply.getCircle();
			remainingPiece = pieces;
			remainingPiece[0][circle] = -1;
			
			copy.addCircle(apply);
			newScore =  new Double(copy.total()[board.colorIndex(colors[0])]);
			Double mm = movesModifier(bord, pieces, copy, remainingPiece);
			//Double em = remainingEnemyMoves();
			newScore = newScore * mm + cbc;
			if (badMove > newScore) {
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
		for (int i = 0; i < 4; i++) {
			if (board.hasField(m.getLine(), m.getColumn(), board.intIndex(i))) {
				return i;
			}
		}
		return -1;
	}
	public int circlesPlaced(int x, int y) {
		int[] pin = board.tallyUp(x, y);
		int total = 0;
		for (int i = 0; i < 4; i++) {
			total = total + pin[i];
		}
		return total;	
	}
	public boolean canBeContested(Move m, int enemyColor) {
		int x = m.getLine();
		int y = m.getLine();
		int owncolor = board.colorIndex(colors[0]);
		int[] pin = board.tallyUp(x, y);
		int otherColor = circlesPlaced(x, y) - owncolor - pin[enemyColor];
		switch (pin[enemyColor]) {
			case 2: {
				if (otherColor == 1) {
					return false;
				} else if (otherColor == 0 && pin[owncolor] == 1) {
					return true;
				}	
				break;
			}
			case 1: { //only if he has one and no other players
				//(other players are always 0 because test person had this place)
				return true;
			}
			case 3: {
				return false;
			}
			case 4: {
				return false;
			}
		}
		return false;	
	}
	public Double movesModifier(Board bord, int[][] pieces,
			Board copy, int[][] remainPiece) {
		//HashMap<Move, Double> moves = new HashMap<Move, Double>();
		double begin = 0.00;
		double after = 0.00;
		begin = ((Integer) bord.getPossibleMoves(colors, 
				pieces).size()).doubleValue(); //shit to get it to Double
		after = ((Integer) copy.getPossibleMoves(colors, 
				remainPiece).size()).doubleValue(); //shit to get it to Double
		Double modifier = begin - after;
		
		return modifier;
	}
	
	public HashMap<Move, Double> getHighestGain(HashMap<Move, Double> moveScores) {
		Double currentgain = new Double(0);
		HashMap<Move, Double> result = new HashMap<Move, Double>();
		Move maxcounter = moveScores.keySet().iterator().next(); 
		//to emsure it will always return a move
		for (Move m:moveScores.keySet()) { //should itterate trough gains to find the best one
			if (currentgain < moveScores.get(m)) {
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


