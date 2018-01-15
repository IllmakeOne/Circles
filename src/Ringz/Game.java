package Ringz;

import Ringz.Color;
import Ringz.Board;

public class Game {
	
	private Board board;
	private Player[] players;
	private int numberPlayers;
	
	
	//----------------Constructors---------
	
	/**
	 * consturctor
	 */
	public Game(Player[] players) {
		this.numberPlayers = players.length;
		this.board = new Board();
		this.players = new Player[4];
		for (int i = 0; i < numberPlayers; i++) {
			this.players[i] = players[i];
		}		
	}
	
//	public Game(Player name1, Player name2, Player name3) {
//		this.numberPlayers = 3;
//		this.board = new Board();
//		this.players = new Player[4];
//		this.players[0] = name1;
//		this.players[1] = name2;
//		this.players[2] = name3;
//		
//	}
//	
//	public Game(Player name1, Player name2, Player name3, Player name4) {
//		this.numberPlayers = 4;
//		this.board = new Board();
//		this.players = new Player[4];
//		this.players[0] = name1;
//		this.players[1] = name2;
//		this.players[2] = name3;
//		this.players[3] = name4;
//		
//	}
//	
	
	/**
	 * play game.
	 */
	public void play() {
		int current = 0;
		System.out.println(players[current].getName());
		board.placeStart(players[current].getStart());
		this.display();
		while (this.board.isPayable()) {
			if (this.players[current].isOutOfPieces()) {
				System.out.println(this.players[current].getName() 
						+ " is out of pieces");
				current = (current + 1) % this.numberPlayers;
			}
			if (!this.board.isStrillAbleToPlace(players[current])) {
				System.out.println(players[current].getName() + "has nowhere to place pieces");
				current = (current + 1) % this.numberPlayers;
				
			}
			System.out.println(this.players[current].getName() 
					+ " with the colors " +
					this.players[current].getStringColor() 
					+ " please place ");
			if (this.makeMove(this.players[current])) {
				current = (current + 1) % this.numberPlayers;
				this.display();
			}
		}
		winner(board.total());
	}
	
	
	
	/**
	 * displays the winnder.
	 * @param indexWin
	 */
	public void winner(int[] indexWin) {
		if (numberPlayers == 2) {
			int player1 = indexWin[0] + indexWin[1];
			int player2 = indexWin[2] + indexWin[3];
			if (player1 > player2) {
				System.out.println("Player " + players[0].getName() + " won");
			} else {
				if (player1 < player2) {
					System.out.println("Player " + players[1].getName() + " won");
				} else {
					System.out.println("It is a draw");
				}
			}
		}
		
		if (numberPlayers == 4 || numberPlayers == 3) {
			switch (Board.arrayMaximum(indexWin)) {
				case -1: {
					System.out.println("It is a draw"); break;
				}
				case 0: { 
					System.out.println(players[0].getName() + "won"); break;
				}
				case 1: { 
					System.out.println(players[1].getName() + "won"); break;
				}
				case 2: { 
					System.out.println(players[2].getName() + "won"); break;
				}
				case 3: { 
					System.out.println(players[3].getName() + "won"); break;
				}
			}
		}
	}
	
	/**
	 * make a move 
	 * @param play <- the player making the move.
	 * @param c1
	 * @param c2
	 */
	public boolean makeMove(Player play) {
		if (play instanceof ComputerPlayer) {
			return this.board.addCircle(play.determineMove(board));
		}
		return this.board.addCircle(play.determineMove());
	}
	
	/**
	 * shitty dsiplay board.
	 */
	public void display() {
		this.board.display();
	}
	
	public int getNrPlayers() {
		return this.numberPlayers;
	}
	
	
	public static void main(String[] args) {
		Player[] players = new Player[4];
		players[0] = new ComputerPlayer(Color.BLUE, "1");
		players[1] = new ComputerPlayer(Color.PURPLE, "2");
		players[2] = new ComputerPlayer(Color.YELLOW, "3");
		players[3] = new ComputerPlayer(Color.GREEN, "4");
		
		Game game = new Game(players);
		game.play();
		
	}
}
