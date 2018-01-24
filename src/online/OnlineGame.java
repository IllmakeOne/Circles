package online;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import ringz.Board;
import ringz.Color;
import ringz.Move;

public class OnlineGame implements Runnable, Observer{

	private ServerPeer[] plays;
	private boolean startable = true;
	private int replies;
	private int numberOfplayers;
	private Lobby lobby;
	private boolean somoneoneDisconected = false;
	
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
		if (askPlayerToJoin(plays)) {
			Board board = new Board();
			int current = 0;
			itStarts(plays);
			//the array plays is converted into a list to it can be easily shuffled.
			ArrayList<ServerPeer> aux = new ArrayList<>();
			for (int i = 0; i < numberOfplayers; i++) {
				aux.add(plays[i]);   		
			}
			Collections.shuffle(aux);
			ArrayList<ClientPlayer> players = createPlayers(aux);
	   
	    	//placing of the first piece
	    	Move firstmove = players.get(current).determineMove(board);
	    	int[] a = {firstmove.getLine(), //line of the first move
	    			firstmove.getColumn()}; //column of the first move
	    	if (!somoneoneDisconected) {
	    		board.placeStart(a);
	    		current = (current + 1) % numberOfplayers;
	    	}
	    	
	    	int[] tappers = new int[numberOfplayers];
	    	//this keep track of who is still able to play, 0 if still able to , and 1 if is out.
	    	
	    	while (!board.isFull() && !allTapped(tappers) && !somoneoneDisconected) {
	    		if (players.get(current).isOutOfPieces()) {
	    			tappers[current] = 1;
					current = (current + 1) % numberOfplayers;
	    		} else if (!board.isStrillAbleToPlace(players.get(current))) {
	    			tappers[current] = 1;
					current = (current + 1) % numberOfplayers;
	    		} else {
	    			Move currentmove = players.get(current).determineMove(board);
	    			if (board.addCircle(currentmove)) {
	    				players.get(current).decresePiece(currentmove);
	    				current = (current + 1) % numberOfplayers;
	    			} 
	    		}
	    	}
	    	
	    	if (somoneoneDisconected != true) {
	    		//send the result to the involved players if one one disconected
		    	sendResults(board, numberOfplayers, players);	
		    	lobby.removePlayerfromWaiting(plays);
	    	}
	    	
		} else {
			someoneDecline(plays);
		}
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
	

	
	public void itStarts(ServerPeer[] client) {
		String stringy = ServerPeer.GAME_STARTED;
		System.out.println(stringy + " in itStarts");
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
			players.add(new ClientPlayer(2, Color.BLUE, Color.GREEN, player.get(0)));
			players.add(new ClientPlayer(2, Color.PURPLE, Color.GREEN, player.get(1)));
			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, player.get(2)));
		} else {
			players.add(new ClientPlayer(Color.BLUE, player.get(0)));
			players.add(new ClientPlayer(Color.PURPLE, player.get(1)));
			players.add(new ClientPlayer(Color.YELLOW, player.get(2)));
			players.add(new ClientPlayer(Color.GREEN, player.get(3)));
		}
		
		return players;
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
					+ results[3];
		} else {
			stringy += players.get(0).getName() + ServerPeer.DELIMITER 
					+ results[0] + ServerPeer.DELIMITER
					+ players.get(1).getName() + ServerPeer.DELIMITER 
					+ results[1] + ServerPeer.DELIMITER 
					+ players.get(2).getName() + ServerPeer.DELIMITER 
					+ results[2]
					+ players.get(3).getName() + ServerPeer.DELIMITER 
					+ results[3];
		}
		
		for (int i = 0; i < numberPlayers; i++) {
			players.get(i).getSocket().sendPackage(stringy);
		}
		
		
	}

	
	/**
	 * this functions asks all the clients who have.
	 * matching preferences if they want to start a game.
	 * @param client
	 */
	public boolean askPlayerToJoin(ServerPeer[] client) {
		sendallConnected(client);		
		while (replies != numberOfplayers) {
			try {
				TimeUnit.MILLISECONDS.sleep(10);
				System.out.println("aghhhh");
			} catch (InterruptedException e) { 
				System.out.println("somehitng wrong in asking players to join");
			}
		}
		System.out.println(" \n it jsut went out");
		int x = 0;
		x++;
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
	public void sendallConnected(ServerPeer[] players) {
		String message = ServerPeer.ALL_PLAYERS_CONNECTED;
		String words;
		for (int i = 0; i < players.length; i++) {
			message += ServerPeer.DELIMITER + players[i].getName();
		}
		for (int i = 0; i < players.length; i++) {
			players[i].inGame();
			players[i].sendPackage(message);
		}
		
		try {
			words = players[0].getIN().readLine();
				
			System.out.println(words + " + " + players[0].getName());
				
			if (words.equals(ServerPeer.PLAYER_STATUS 
					+ ServerPeer.DELIMITER + ServerPeer.ACCEPT)) {
				replies++;
				System.out.println("it did it in sendall conected");
			} else if (words.equals(ServerPeer.PLAYER_STATUS 
					+ ServerPeer.DELIMITER + ServerPeer.DECLINE)) {
				replies++;
				startable = false;
			}
		} catch (IOException e) {
			System.out.println("Sth wrong in sendallConnected");
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * this sends a package with LOBBY in case someone decline a game.
	 * @param client
	 */
	public void someoneDecline(ServerPeer[] client) {
		String stringy = ServerPeer.JOINED_LOBBY;
		System.out.println(stringy + " in someone declined");
		for (int i = 0; i < client.length; i++) {
			client[i].sendPackage(stringy);
		}
	}


	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals("gameaccepted")) {
			replies++;
		} else if (arg.equals("gamedeny")) {
			startable = false;
			replies++;
		} else if (arg.equals("disco")) {
			somoneoneDisconected = true;
		}
//		} else {
//			move = s
//			somoneoneDisconected = true;
//		}
	}
	
	public ArrayList<ServerPeer> getPlayersasList() {
		ArrayList<ServerPeer> aux = new ArrayList<>();
		for (int i = 0; i < numberOfplayers; i++) {
			aux.add(plays[i]);   		
		}
		return aux;
	}
	
	public void someoneLeft() {
		this.somoneoneDisconected = true;
	}
	
	
	
}
