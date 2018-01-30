//package ringz;
//
//
//import players.ComputerPlayer;
//import players.HumanPalyer;
//import players.Player;
//import ringz.Board;
//import ringz.Color;
//import view.*;
//
//import java.util.Observer;
//
//
//public class Game {
//	
//	private Board board;
//	private Player[] players;
//	private int numberPlayers;
//	private View view;
//	
//	
//	//----------------Constructors---------
//	
//	/**
//	 * constructor.
//	 */
//	public Game(Player[] players, View view) {
//		this.numberPlayers = players.length;
//		this.board = new Board();
//		this.view = view;
//		this.players = new Player[4];
//		board.addObserver((Observer) view);
//		for (int i = 0; i < numberPlayers; i++) {
//			this.players[i] = players[i];
//		}		
//	}
//	
//
//	/**
//	 * play game.
//	 */
//	public void play() {
//		int current = 0;
//		System.out.println(players[current].getName());
//		board.placeStart(players[current].getStart());
//		this.display();
//		int[] plays = new int[numberPlayers];
//		while (!this.board.isFull() && allStoped(plays) != true) {
//			if (this.players[current].isOutOfPieces()) {
//				view.outOfPieces(players[current]);
//				plays[current] = 1;
//				current = (current + 1) % this.numberPlayers;
//			} else if (!this.board.isStrillAbleToPlace(players[current])) {
//				view.notAbletoPlay(players[current].getName());
//				plays[current] = 1;
//				current = (current + 1) % this.numberPlayers;
//			} else {
//				if (this.makeMove(this.players[current])) {
//					System.out.println("Player " + (current + 1) + " made a move \n");
//					current = (current + 1) % this.numberPlayers;
//					display();
//				}
//			}
//		}	
//		winner(board.total());
//	}
//	
//	
//	public boolean allStoped(int[] plays) {
//		for (int i = 0; i < plays.length; i++) {
//			if (plays[i] == 0) {
//				return false;
//			}
//		}
//		return true;
//	}
//	
//	/**
//	 * displays the winner.
//	 * @param indexWin
//	 */
//	public void winner(int[] indexWin) {
//		if (numberPlayers == 2) {
//			int player1 = indexWin[0] + indexWin[1];
//			int player2 = indexWin[2] + indexWin[3];
//			if (player1 > player2) {
//				System.out.println("Player " + players[0].getName() + " won");
//			} else {
//				if (player1 < player2) {
//					System.out.println("Player " + players[1].getName() + " won");
//				} else {
//					System.out.println("It is a draw");
//				}
//			}
//		}
//		
//		if (numberPlayers == 4 || numberPlayers == 3) {
//			switch (Board.arrayMaximum(indexWin)) {
//				case -1: {
//					System.out.println("It is a draw"); break;
//				}
//				case 0: { 
//					System.out.println(players[0].getName() + "won"); break;
//				}
//				case 1: { 
//					System.out.println(players[1].getName() + "won"); break;
//				}
//				case 2: { 
//					System.out.println(players[2].getName() + "won"); break;
//				}
//				case 3: { 
//					System.out.println(players[3].getName() + "won"); break;
//				}
//			}
//		}
//	}
//	
//	/**
//	 * make a move .
//	 * @param play <- the player making the move.
//	 * @param c1
//	 * @param c2
//	 */
//	public boolean makeMove(Player play) {
//		if (play instanceof ComputerPlayer) {
//			return this.board.addCircle(((ComputerPlayer) play).determineMove(board));
//		}
//		return this.board.addCircle(view.askMove(play, board));
//	}
//	
//
//	public void display() {
//		view.updateDisplay(this.board);
//	}
//	
//	public int getNrPlayers() {
//		return this.numberPlayers;
//	}
//	
//	
//	public static void main(String[] args) {
//		Player[] players = new Player[4];
//		players[0] = new HumanPalyer(3, Color.BLUE, Color.GREEN, "Tester1");
//		players[1] = new HumanPalyer(3, Color.PURPLE, Color.GREEN, "Tester2");
//		players[2] = new HumanPalyer(3, Color.YELLOW, Color.GREEN, "Tester3");
//		
////		players[2] = new ComputerPlayer(Color.YELLOW, "3");
////		players[3] = new ComputerPlayer(Color.GREEN, "4");
//		
//		Game game = new Game(players, new TUI("name"));
//		game.play();
//		
//	}
//}
