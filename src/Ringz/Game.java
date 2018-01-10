package Ringz;

import Ringz.Color;

public class Game {
	
	private Board board;
	private Player[] players;
	private int numberPlayers;
	
	
	//----------------Constructors---------
	
	/**
	 * consturcotr for two players.
	 * generates aborad and two players
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
	 * play game
	 */
	public void play() {
		int current = 0;
		board.placeStart(players[current].getStart());
		this.display();
		while (this.board.isPayable()) {
			System.out.println(this.players[current].getName() + " with the colors " +
					this.players[current].getColor() + " please place ");
			if (this.makeMove(this.players[current])) {
				current = (current + 1) % this.numberPlayers;
				this.display();
			}
		}
		winner(board.total());
	}
	
	
	
	/**
	 * displays the winnder
	 * @param indexWin
	 */
	public void winner(int indexWin) {
		//WAH IF IT 2 PLAYERS HMMMMMMMMMMMM
		if (numberPlayers == 2) {
			switch (indexWin) {
				case -1: {
					System.out.println("It is a draw"); break;
				}
				case 0: { 
					System.out.println(players[0].getName() + "won"); break;
				}
				case 1: { 
					System.out.println(players[0].getName() + "won"); break;
				}
				case 2: { 
					System.out.println(players[1].getName() + "won"); break;
				}
				case 3: { 
					System.out.println(players[1].getName() + "won"); break;
				}
			}
		}
		
		if (numberPlayers == 4 || numberPlayers == 3) {
			switch (indexWin) {
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
	 * @param play <- th eplayer making the move.
	 * @param c1
	 * @param c2
	 */
	public boolean makeMove(Player play) {
		return this.board.addCircle(play.determineMove());
	}
	
	/**
	 * shitty dsiplay board.
	 */
	public void display() {
		this.board.display();
	}
	
	

	

}
