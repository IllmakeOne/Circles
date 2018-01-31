package strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class SmartStrategy implements Strategy {

	private Random rand = new Random();
	private Color[] colors;
	private int numberPlayers;

	public SmartStrategy(Color[] colors, int nrpls) {
		this.colors = colors; 
		this.numberPlayers = nrpls;
	} 	
	
	@Override
	public String getName() {
		return "Smart";
	} 

	/**
	 * this will determine the move of the smart AI.
	 * this also first puts the bases then it puts it assigned scores to move;
	 * based on the effect they'd have on the game.
	 */
	@Override
	public Move determineMove(Board bord, int[][] pieces) {
		ArrayList<Move> move = bord.getPossibleMoves(colors, pieces); 
		int random = rand.nextInt(move.size());
		Move mov = move.get(random);
		if (!move.isEmpty()) {
			mov = getMove(bord, pieces);
			return mov;
		} else {
			return null;
		} 
	}
	
	public boolean isBase(Move move) {
		if (move.getCircle() == 0) {
			return true;
		}
		return false;
	}
	

	public int movePoints(Move move, Board board, int[][] pieces) {
		int result = 0;
		if (board.isCompletlyEmpty(move.getLine(), move.getColumn())
				&& move.getCircle() == 0) {
			result += 100;
		}
		if (board.isCompletlyEmpty(move.getLine(), move.getColumn())
				&& !hasBases(pieces)) {
			result += 40;
		}
		if (fromPintoEnemies(board.getPin(move.getLine(), move.getColumn())) < 0) {
			
		}
		return result;
	}
	
	/**
	 * this tests if the @param move list has a move which has a base as its piece.
	 */
	public boolean movesetHasBases(ArrayList<Move> move) {
		for (int i = 0; i < move.size(); i++) {
			if (move.get(i).getCircle() == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasBases(int[][] pieces) {
		if (pieces[0][0] != 0 || pieces[1][0] != 0) {
			return true;
		}
		return false;
	}
	
	public Move getMove(Board board, int[][] pieces) {
		ArrayList<Move> move = board.getPossibleMoves(colors, pieces);
		HashMap<Move, Integer> movescores = new HashMap<Move, Integer>();
		for (int i = 0; i < move.size(); i++) {
			movescores.put(move.get(i), casePoints(move.get(i), board));
		}
		return maxScore(movescores);
	}
	
	public Move maxScore(HashMap<Move, Integer> movescores) {
		Move move = null;
		int max = -100;
		for (Move mov:movescores.keySet()) {
			if (movescores.get(mov) > max) {
				move = mov;
				max = movescores.get(mov);
			}
		}
		return move;
	}
	
	public Color[] enemyColors() {
		Color[] result = new Color[3];
		if (numberPlayers == 2) {
			if (colors[0] == Color.BLUE) {
				result[0] = Color.YELLOW;
				result[1] = Color.GREEN;
			} else {
				result[0] = Color.BLUE;
				result[1] = Color.PURPLE;
			}
		} else if (numberPlayers == 3) {
			if (colors[0] == Color.BLUE) {
				result[0] = Color.PURPLE;
				result[1] = Color.YELLOW;
			} else if (colors[0] == Color.PURPLE) {
				result[0] = Color.BLUE;
				result[1] = Color.YELLOW;
			} else if (colors[0] == Color.YELLOW) {
				result[0] = Color.PURPLE;
				result[1] = Color.BLUE;
			}
		} else {
			if (colors[0] == Color.BLUE) {
				result[0] = Color.YELLOW;
				result[1] = Color.GREEN;
				result[2] = Color.PURPLE;
			} else if (colors[0] == Color.YELLOW) {
				result[0] = Color.BLUE;
				result[1] = Color.GREEN;
				result[2] = Color.PURPLE;
			} else if (colors[0] == Color.GREEN) {
				result[0] = Color.YELLOW;
				result[1] = Color.BLUE;
				result[2] = Color.PURPLE;
			} else {
				result[0] = Color.YELLOW;
				result[1] = Color.BLUE;
				result[2] = Color.GREEN;
			}
		}
		return result;
		
	}
	 
	public int casePoints(Move move, Board board) {
		int x = move.getLine();
		int y = move.getColumn();
		int sum = 0;
		Color[] encolor = enemyColors();
		Color[] pin = board.getPin(x, y);
		
		if (numberPlayers == 2) {
			int en1 = 0;
			int en2 = 0;
			int neutral = 0;
			int own1 = 0;
			int own2 = 0;
			for (int i = 1; i < pin.length; i++) {
				if (pin[i] == encolor[0]) {
					en1 += 1;
				} else if (pin[i] == encolor[1]) {
					en2 += 1;
				} else if (pin[i] == Color.GREEN) {
					neutral += 1;
				} else if (pin[i] == colors[0]) {
					own1 += 1;
				} else if (pin[i] == colors[1]) {
					own2 += 1;
				}
			}
			if (en1 == 2 && en2 == 1 || en2 == 2 && en1 == 1) {
				sum += -20;
			} else  if (en1 == 1 && en2 == 1 && own1 == 1 && move.getColor() == colors[0]) {
				sum += 150;
			} else  if (en1 == 1 && en2 == 1 && own2 == 1 && move.getColor() == colors[1]) {
				sum += 150;
			} else  if (en1 == 1 && en2 == 1 && own2 == 1 && move.getColor() == colors[1]) {
				sum += 150;
			} else  if (en1 == 0 && en2 == 0 && own2 == 1 && move.getColor() == colors[1]) {
				sum += 150;
			} else  if (en1 == 0 && en2 == 0 && own1 == 1 && move.getColor() == colors[0]) {
				sum += 150;
			} else  if (en1 == 0 && en2 == 0 && own1 == 1 && move.getColor() == colors[0]) {
				sum += 150;
			} else  if (en1 == 2 && en2 == 0 || en1 == 0 && en2 == 2) {
				sum += -10;
			} else  if (en1 == 2 && en2 == 0 && own1 == 1 && move.getColor() == colors[0]) {
				sum += 50;
			} else  if (en1 == 0 && en2 == 1 && own2 == 1 && move.getColor() == colors[1]) {
				sum += 50;
			} else  if (en1 == 1 && en2 == 0 || en1 == 0 && en2 == 1) {
				sum += 25;
			} else  if (en1 == 0 && en2 == 0 && own1 == 0 && own2 == 1 &&
						move.getColor() == colors[0] ||
						en1 == 0 && en2 == 0 && own1 == 1 && own2 == 2 
						&& move.getColor() == colors[1]) {
				sum += 100;
			}
			
		} else if (numberPlayers == 3) {
			int en1 = 0;
			int en2 = 0;
			int neutral = 0;
			int own = 0;
			for (int i = 1; i < pin.length; i++) {
				if (pin[i] == encolor[0]) {
					en1 += 1;
				} else if (pin[i] == encolor[1]) {
					en2 += 1;
				} else if (pin[i] == Color.GREEN) {
					neutral += 1;
				} else if (pin[i] == colors[0]) {
					own += 1;
				}
			}
			
			if (en1 == 1 && en2 == 1 && neutral == 1 && move.getColor() == Color.GREEN) {
				sum += 75; //superior block
			} else if (en1 == 1 && en2 == 1 && neutral == 1 && move.getColor() == colors[0]) {
				sum += 50; //superior block
			} else if ((en1 == 1 || en2 == 1) && neutral == 1 && own == 1
						&& move.getColor() == colors[0]) {
				sum += 150; //gain point
			} else if ((en1 == 1 || en2 == 1) && neutral == 1 && own == 1
					&& move.getColor() == Color.GREEN) {
				sum += 50; //block
			} else if ((en1 == 1 || en2 == 1) && neutral == 1 && own == 1
						&& move.getColor() == colors[0]) {
				sum += 50; //sad block
			} else if (en1 == 0 && en2 == 0 && neutral == 1 && own == 0 
						&& move.getColor() == Color.BLUE) {
				sum += 25; //build twards point
			} else if (en1 == 0 && en2 == 0 && neutral == 0 && own == 0 
					&& move.getColor() == colors[0]) {
				sum += 25; //build twards point
			} else if (en1 == 0 && en2 == 0 && neutral == 0 && own == 0 
					&& move.getColor() == Color.GREEN) {
				sum += -10; //dont put neutral pieces on empty field
			} else if (en1 == 0 && en2 == 0 && neutral == 1 && own == 1 
					&& move.getColor() == colors[0]) {
				sum += 150; //gain point
			} else if (en1 == 0 && en2 == 0 && neutral == 0 && own == 1 
					&& move.getColor() == colors[0]) {
				sum += 150; //gain point
			} else if ((en1 == 1 || en2 == 1) && neutral == 0 && own == 0 
					&& move.getColor() == colors[0]) {
				sum += 0; //gain point
			} else if ((en1 == 1 && en2 == 0 || en1 == 0 && en2 == 1) && neutral == 0 && own == 1 
					&& move.getColor() == colors[0]) {
				sum += 150; //gain point
			} else if ((en1 == 1 && en2 == 0 || en1 == 0 && en2 == 1) && neutral == 1 && own == 1 
					&& move.getColor() == colors[0]) {
				sum += 150; //gain point
			} else if ((en1 == 2 && en2 == 0 || en1 == 0 && en2 == 2) && neutral == 0 && own == 0 
					&& move.getColor() == colors[0]) {
				sum += -10; 
			} else if ((en1 == 2 && en2 == 0 || en1 == 0 && en2 == 2) && neutral == 1 && own == 0 
					&& move.getColor() == Color.GREEN) {
				sum += 50; //block
			} else if ((en1 == 2 && en2 == 0 || en1 == 0 && en2 == 2) && neutral == 0 && own == 1 
					&& move.getColor() == colors[0]) {
				sum += -10; //gain point
			} else if (en1 == 3 || en2 == 3) {
				sum += -100; 
			} else if (en1 == 2 && neutral == 1 || en2 == 2 && neutral == 1) {
				sum += 50;
			} else if (en1 + en2 == 3) {
				sum -= 10;
			}
		}
		
		return sum;
	}
	 
	
	
	/**
	 * this function transforms a pin into an int array where.
	 *  if the position has 1 then it means an enemy(diff color) is there
	 */
	public int fromPintoEnemies(Color[] pincolors) {
		int sum = 0;
		for (int i = 1; i < 5; i++) {
			if (this.colors[1] == null) {
				if (pincolors[i] != this.colors[0] && pincolors[i] != Color.EMPTY) {
					sum += -1;
				} else {
					sum += 1;
				}
			} else if (pincolors[i] != this.colors[0] && pincolors[i] != Color.EMPTY &&
					pincolors[i] != this.colors[1]) {
				sum += -1;
			} else {
				sum += 1;
			}
		}
		//return result;
		return sum;
	}
	
	public int stateofThePin(Color[] pincolors) {
		int result;
		int index;
		HashMap<Color, Integer> map = createMap(pincolors);
		if (colors[1] == null) {
			index = colortoint(colors[0]);
			
		}
		return 0;
	}
	
	public HashMap<Color, Integer> createMap(Color[] pincolors) {
		HashMap<Color, Integer> result = new HashMap<>();
		result.put(Color.BLUE, 0);
		result.put(Color.YELLOW, 0);
		result.put(Color.GREEN, 0);
		result.put(Color.PURPLE, 0);
		result.put(Color.EMPTY, 0);
		for (int i = 1; i < 5; i++) {
			switch (pincolors[i]) {
				case BLUE: {
					result.put(Color.BLUE, result.get(Color.BLUE));
					break;
				}
				case EMPTY: {
					result.put(Color.EMPTY, result.get(Color.EMPTY));
					break;
				}
				case PURPLE: {
					result.put(Color.PURPLE, result.get(Color.PURPLE));
					break;
				}
				case GREEN: {
					result.put(Color.GREEN, result.get(Color.GREEN));
					break;
				}
				case YELLOW: {
					result.put(Color.YELLOW, result.get(Color.YELLOW));
					break;
				}
			}
		}
		return result;
	}
	
	public int colortoint(Color color) {
		switch (color) {
			case BLUE: {
				return 1;
			}
			case PURPLE: {
				return 2;
			}
			case GREEN: {
				return 4;
			}
			case YELLOW: {
				return 3;
			}
		}
		return 0;
	}
	
	public int[] createInt(Color[] pincolors) {
		int[] reult = new int[5];
		for (int i = 1; i < 5; i++) {
			switch (pincolors[i]) {
				case BLUE: {
					reult[1]++;
					break;
				}
				case EMPTY: {
					reult[0]++;			
					break;
				}
				case PURPLE: {
					reult[2]++;
					break;
				}
				case GREEN: {
					reult[4]++;
					break;
				}
				case YELLOW: {
					reult[3]++;
					break;
				}
			}
		}
		return reult;
	}
	
}
