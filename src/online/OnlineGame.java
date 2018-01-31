package online;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import players.ClientPlayer;
import ringz.Board;
import ringz.Color;
import ringz.Move;

public class OnlineGame implements Runnable, Observer {

	private ServerPeer[] plays;
	private boolean startable = true;
	private int replies;
	private int numberOfplayers;
	private Lobby lobby;
	private boolean somoneoneDisconected = false;
	private ServerPeer decliner;
	
	/*
	 * @requires plays.length < 5 && plays.length > 1;
	 * \forall(int i; 0 <= i < plays.length; plays[i]
	 */
	public OnlineGame(ServerPeer[] plays, Lobby lobby) {
		this.lobby = lobby;
		this.plays = plays;
		for (int i = 0; i < plays.length; i++) { 
			this.plays[i].addObserver(this);
		}
		this.numberOfplayers = plays.length;
		
	}
	
	
	@Override
	public void run() {
		//the array plays is converted into a list to it can be easily shuffled.
		ArrayList<ServerPeer> aux = new ArrayList<>();
		for (int i = 0; i < numberOfplayers; i++) {
			plays[i].inGame();
			aux.add(plays[i]);   		
		}
		Collections.shuffle(aux);
		ArrayList<ClientPlayer> players = createPlayers(aux);
   
		if (askPlayerToJoin(players)) {
			Board board = new Board();
			int current = 0;
			itStarts(plays);
			
			try { 
				//placing of the first piece
				Move firstmove = players.get(current).determineMove(board);
				int[] a = {firstmove.getLine(), //line of the first move
						firstmove.getColumn()}; //column of the first move

				if (!somoneoneDisconected) {
					while (validFirstPiece(a) != true) {
						firstmove = players.get(current).determineMove(board);
						a[0] = firstmove.getLine(); //line of the first move
						a[1] = firstmove.getColumn(); //column of the first moves
						System.out.println(a[0] + " " + a[1]);
					}
					board.placeStart(a);
					sendMoveToall(firstmove, players.get(current).getName());
					current = (current + 1) % numberOfplayers;
				}
			} catch (StackOverflowError e) {
				lobby.diconected(players.get(current).getSocket());
			} catch (NullPointerException e) {
				lobby.diconected(players.get(current).getSocket());
			}
    		
	    	
	    	int[] tappers = new int[numberOfplayers];
	    	//this keep track of who is still able to play, 0 if still able to , and 1 if is out.
	    	
	    	while (!board.isFull() && !allTapped(tappers) && !somoneoneDisconected) {
	    		if (!canPlace(players.get(current), board)) {
	    			tappers[current] = 1;
					current = (current + 1) % numberOfplayers;
	    			
	    		} else {
	    			Move currentmove = players.get(current).determineMove(board);
	    			try {
	    				if (board.addCircle(currentmove)) { 
	    					players.get(current).decresePiece(currentmove);
	    					sendMoveToall(currentmove, players.get(current).getName());
	    					current = (current + 1) % numberOfplayers;
	    				}
	    			} catch (NullPointerException e) {
	    				lobby.diconected(players.get(current).getSocket());
	    			}
	    		} 
	    	}
	    	
	    	if (somoneoneDisconected != true) {
	    		//send the result to the involved players if one one disconected
		    	sendResults(board, numberOfplayers, players);	
	    	}
	    
	    	
		} else {
			someoneDecline(plays);
		}
	}


	/**
	 * this function @returns false if the @param player cannot place pieces on this turn.
	 */
	public boolean canPlace(ClientPlayer player, Board board) {
		if (player.isOutOfPieces() == true) {
			return false;
		}
		if (!board.isStillAbleToPlace(player)) {
			return false;
		}
		return true;
	}
	
	/**
	 * this function send the move to all players.
	 * @param move
	 * @param whoMadetheMove
	 */
	public void sendMoveToall(Move move, String whoMadetheMove) {
		String stringy = moveTostring(move, whoMadetheMove);
		for (int i = 0; i < plays.length; i++) {
			plays[i].sendPackage(stringy);
		}
		
	}
	
	
	
	/**
	 * this functions tests of the coodinates for the first move are valid.
	 * @param cord
	 * @return
	 */
	public boolean validFirstPiece(int[] cord) {
		if (cord[0] > 0 && cord[0] < 4 && cord[1] > 0 && cord[1] < 4) {
			return true;
		}
		return false;
	}
 
	/**
 	* this function returns true if all elemets of tappers are 1.
 	*  meaning all players cannot place piece anymore.
 	* @param tappers
 	* @return
	*/
	/* @requires tappers != null */
	public /*pure*/ boolean allTapped(int[] tappers) {
		for (int i = 0; i < tappers.length; i++) {
			if (tappers[i] == 0) {
				return false;
			}
		}
		return true;
	}
	

	/**
	 * inform all the players in the game that he game has started.
	 * @param client
	 */
	public void itStarts(ServerPeer[] client) {
		String stringy = ServerPeer.GAME_STARTED;
		for (int i = 0; i < client.length; i++) {
			client[i].sendPackage(stringy);
		}
	}
	
	/**
	 * this function creates an array of ClinetsPlayers with which a game will start
	 * this keeps track of everyone's pieces.
	 * @param plays
	 * @return
	 */
	public ArrayList<ClientPlayer> createPlayers(ArrayList<ServerPeer> player) {
		ArrayList<ClientPlayer> players = new ArrayList<ClientPlayer>();
		if (player.size() == 2) {
			players.add(new ClientPlayer(2, Color.BLUE, Color.PURPLE, player.get(0)));
			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, player.get(1)));
		} else if (player.size() == 3) {
			players.add(new ClientPlayer(3, Color.BLUE, Color.GREEN, player.get(0)));
			players.add(new ClientPlayer(3, Color.PURPLE, Color.GREEN, player.get(1)));
			players.add(new ClientPlayer(3, Color.YELLOW, Color.GREEN, player.get(2)));
		} else {
			players.add(new ClientPlayer(Color.BLUE, player.get(0)));
			players.add(new ClientPlayer(Color.PURPLE, player.get(1)));
			players.add(new ClientPlayer(Color.YELLOW, player.get(2)));
			players.add(new ClientPlayer(Color.GREEN, player.get(3)));
		}
		
		return players;
	}
	
	public String moveTostring(Move move, String whoMadetheMove) {
		String result = ServerPeer.MOVE + ServerPeer.DELIMITER;
		result += move.getLine() + ServerPeer.DELIMITER + 
				move.getColumn() + ServerPeer.DELIMITER +
				whoMadetheMove + ServerPeer.DELIMITER +
				(move.getCircle() + 1);
		
		//this adds secondary or priamry color
		if (numberOfplayers == 3) {
			if (move.getColor() == Color.GREEN) {
				result += ServerPeer.DELIMITER + ServerPeer.SECONDARY;
			} else {
				result += ServerPeer.DELIMITER + ServerPeer.PRIMARY;
			}
		} else if (numberOfplayers == 2) {
			if (move.getColor() == Color.PURPLE || move.getColor() == Color.GREEN) {
				result += ServerPeer.DELIMITER + ServerPeer.SECONDARY;
			} else {
				result += ServerPeer.DELIMITER + ServerPeer.PRIMARY;
			}
		}
		
		return result;
	}
	
	
	
	
	/**
	 * this functions sends the result of the game to the clients involved.
	 * @param board the board the clients were playing on
	 * @param numberPlayers number of players 
	 * @param players the players
	 */
	/*
	 * @requires someoneDisconected == false;
	 */
	public /*pure*/ void sendResults(Board board,
				int numberPlayers, ArrayList<ClientPlayer> players) {
		//results is of this form : 
		//[0]Blue points;[1] Purple points;[2]Yellow points;[3] Green points
		int[] results = board.total();
		String stringy = ServerPeer.GAME_ENDED + ServerPeer.DELIMITER; 
		if (numberPlayers == 2) {
			stringy += players.get(0).getName() + ServerPeer.DELIMITER 
					+ (results[0] + results[1]) + ServerPeer.DELIMITER
					+ players.get(1).getName() + ServerPeer.DELIMITER 
					+ (results[2] + results[3]);
		} else if (numberPlayers == 3) {
			stringy += players.get(0).getName() + ServerPeer.DELIMITER 
					+ results[0] + ServerPeer.DELIMITER 
					+ players.get(1).getName() + ServerPeer.DELIMITER 
					+ results[1] + ServerPeer.DELIMITER 
					+ players.get(2).getName() + ServerPeer.DELIMITER 
					+ results[2];
		} else {
			stringy += players.get(0).getName() + ServerPeer.DELIMITER 
					+ results[0] + ServerPeer.DELIMITER
					+ players.get(1).getName() + ServerPeer.DELIMITER 
					+ results[1] + ServerPeer.DELIMITER 
					+ players.get(2).getName() + ServerPeer.DELIMITER 
					+ results[2] + ServerPeer.DELIMITER 
					+ players.get(3).getName() + ServerPeer.DELIMITER 
					+ results[3];
		}
		
		for (int i = 0; i < numberPlayers; i++) {
			players.get(i).getSocket().sendPackage(stringy);
			players.get(i).getSocket().gameOver();
		}
		

		
		
	}



	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals("gameaccepted")) {
			replies++;
		} else if (arg.equals("gamedeny")) {
			startable = false;
			decliner = (ServerPeer) o;
			replies++;
		} else if (arg.equals("disco")) {
			somoneoneDisconected = true;
		}
//		} else if (words[0].equals(ServerPeer.MOVE) && 
//					words[3].equals(ServerPeer.STARTING_BASE)) {
//			currentMove = ClientPlayer;
//		}
	}  
	
	/**
	 * return the players as a list.
	 */
	public ArrayList<ServerPeer> getPlayersasList() {
		ArrayList<ServerPeer> aux = new ArrayList<>();
		for (int i = 0; i < numberOfplayers; i++) {
			aux.add(plays[i]);   		
		}
		return aux;
	}
	
	/**
	 * if someone leaves the game is cancelled.
	 *  and all the other players get a package saying that it's over.
	 * @param leaver the player who left
	 */
	public void someoneLeft(ServerPeer leaver) {
		this.somoneoneDisconected = true;
		String stringy = ServerPeer.PLAYER_DISCONNECTED + ServerPeer.DELIMITER + leaver.getName();
		for (int i = 0; i < plays.length; i++) {
			if (plays[i] != leaver) {
				plays[i].sendPackage(stringy);
				lobby.addtoWaitingList(plays[i], plays[i].getPreferences());
			}
		}
		System.exit(0);
	}
	
	/** 
	 * @return the players as an array.
	 */
	public ServerPeer[] getPlayersasArray() {
		return plays;
	}

	
	
//-----------------------Asking for approval------------------
	
	/**
	 * this functions asks all the clients who have.
	 * matching preferences if they want to start a game.
	 * @param client
	 */
	public boolean askPlayerToJoin(ArrayList<ClientPlayer> client) {
		sendallConnected(client);		
		while (replies != numberOfplayers) {
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) { 
				System.out.println("somehitng wrong in asking players to join");
			}
		}
		if (startable == false) { 
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * this function notifies the players that a game is about to start.
	 * @param players
	 */
	public void sendallConnected(ArrayList<ClientPlayer> players) {
		String message = ServerPeer.ALL_PLAYERS_CONNECTED;
		for (int i = 0; i < players.size(); i++) {
			message += ServerPeer.DELIMITER + players.get(i).getName();
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).getSocket().sendPackage(message);
		}
		
	}
	
	
	/**
	 * this sends a package with LOBBY in case someone decline a game.
	 * @param client
	 */
	public void someoneDecline(ServerPeer[] clients) {
		String stringy = ServerPeer.JOINED_LOBBY;
		decliner.gameOver();
		for (int i = 0; i < clients.length; i++) {
			if (clients[i] != decliner) {
				clients[i].gameOver();
				lobby.addtoWaitingList(clients[i], clients[i].getPreferences());
				clients[i].sendPackage(stringy);
			}
		}
	}
	
	
	
}
