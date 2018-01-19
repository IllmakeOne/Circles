package online;

import java.net.ServerPeer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.prefs.PreferencesFactory;

import ringz.Board;
import ringz.Move;
import ringz.Color;

public class Lobby {
	
	private ArrayList<String> clients;
	private String name;
	private HashMap<ServerPeer, String[]> playersWaiting;
	
	public Lobby(String name) {
		this.clients = new ArrayList<String>();
		this.playersWaiting = new HashMap<ServerPeer, String[]>();
		this.name = name;
	}
	
	public boolean addtoClientList(String clientname) {
		if (clients.contains(clientname)) {
			return false;
		} else {
			clients.add(clientname);
			return true;
		}
	}
	
	public void addtoWaitingList(ServerPeer socc, String[] preferences) {
		playersWaiting.put(socc, preferences);
	}


	public ServerPeer[] startableGame(String[] preferences) {

		//Preferences is of this format : 
		//[0]number_players//[1]player_type//[2]prefered_oponent_type
		int intnrplayers = Integer.valueOf(preferences[0]);
		ServerPeer[] players = new ServerPeer[intnrplayers];
		//the players that want to play against as many people the the player being tested
		int inPlay = 0;
		for (ServerPeer key:playersWaiting.keySet()) {
			if (playersWaiting.get(key)[1].equals(preferences[0]) && inPlay < intnrplayers) {
				players[inPlay] = key;
				inPlay++;
			}
		}
		if (!preferences[2].equals(ServerPeer.NEUTRAL)) {
			//this will go though only if the players wants to go against something specific
			for (ServerPeer key:playersWaiting.keySet()) {
				if (playersWaiting.get(key)[0].equals(preferences[0])
					/*tests if they want the same number of players*/ &&
					playersWaiting.get(key)[1].equals(preferences[2])  &&
			   /*this tests the tested player matches the preferred player type of the new player)*/
					inPlay < intnrplayers /*tests if it already has enough players*/) {
					//ads player to the array of players which will start a game
					players[inPlay] = key;
				}
			}
		} else {
			for (ServerPeer key:playersWaiting.keySet()) {
				if (playersWaiting.get(key)[0].equals(preferences[0])
					/*tests if they want the same number of players*/ &&
					inPlay < intnrplayers /*tests if it already has enough players*/) {
					//ads player to the array of players which will start a game
					players[inPlay] = key;
					playersWaiting.remove(key);
				}
			}
		}
		//if inPlay is the same as intnrplayers that means
		//enough players of the preferred type have been gathered
		if (inPlay == intnrplayers) {
			//removePlayerfromWaiting(players);
			return players;
		} else {
			return null;
		}
	}

	
	/**
	 * this function starts a game based on an array of players who have the same preferrences
	 * @param plays
	 */
	public void startGame(ServerPeer[] plays) {
		Board board = new Board();
		int current = 0;
		int numberOfplayers = plays.length;
    	ArrayList<ServerPeer> aux = new ArrayList<>();
    	for (int i = 0; i < numberOfplayers; i++) {
    		aux.add(plays[i]);   		
    	}
    	Collections.shuffle(aux);
    	ArrayList<ClientPlayer> players = createPlayers(aux);
    	int[] tappers = new int[numberOfplayers];
    	//this keep track of who is still able to play, 0 if still able to , and 1 if is out
    	while (!board.isFull() && !allTapped(tappers)) {
    		if (players.get(current).isOutOfPieces()) {
    			tappers[current] = 1;
				current = (current + 1) % numberOfplayers;
    		} else if (!board.isStrillAbleToPlace(players.get(current))) {
    			tappers[current] = 1;
				current = (current + 1) % numberOfplayers;
    		} else {
    			Move currentmove = players.get(current).askForMove();
    			if (board.addCircle(currentmove)) {
    				players.get(current).decresePiece(currentmove);
    				current = (current + 1) % numberOfplayers;
    			} 
    		}
    	}
    	
    	//send the result to the involved players
    	sendResults(board, numberOfplayers, players);	
		
	}
	
	/**
	 * this functions sends the result of the game to the clients involved.
	 * @param board the board the clients were playing on
	 * @param numberPlayers number of players 
	 * @param plays the players
	 */
	public void sendResults(Board board, int numberPlayers, ArrayList<ClientPlayer> plays) {
		int[] results = board.total();
		String del = ServerPeer.DELIMITER;
		String stringy = ServerPeer.GAME_ENDED + del;
		if (numberPlayers == 2) {
			stringy += plays.get(0).getName() + del 
					+ (results[0] + results[1]) + del
					+ plays.get(1).getName() + del 
					+ (results[2] + results[3]);
		} else if (numberPlayers == 3) {
			stringy += plays.get(0).getName() + del 
					+ results[0] + del
					+ plays.get(1).getName() + del 
					+ results[1] + del 
					+ plays.get(2).getName() + del 
					+ results[3];
		} else {
			stringy += plays.get(0).getName() + del 
					+ results[0] + del
					+ plays.get(1).getName() + del 
					+ results[1] + del 
					+ plays.get(2).getName() + del 
					+ results[2]
					+ plays.get(3).getName() + del 
					+ results[3];
		}
		
		for (int i = 0; i < numberPlayers; i++) {
			plays.get(i).getSocket().sendPackage(stringy);
		}
	}
	
	
	/**
	 * this function creates an array of ClinetsPlayers with which a game will start
	 * this keeps track of everyone's pieces.
	 * @param plays
	 * @return
	 */
	public ArrayList<ClientPlayer> createPlayers(ArrayList<ServerPeer> plays) {
		ArrayList<ClientPlayer> players = new ArrayList<ClientPlayer>();
		if (plays.size() == 2) {
			players.add(new ClientPlayer(2, Color.BLUE, Color.PURPLE, 
					plays.get(0).getName(), plays.get(0)));
			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, 
					plays.get(0).getName(), plays.get(1)));
		} else if (plays.size() == 3) {
			players.add(new ClientPlayer(2, Color.BLUE, Color.GREEN, 
					plays.get(0).getName(), plays.get(0)));
			players.add(new ClientPlayer(2, Color.PURPLE, Color.GREEN, 
					plays.get(0).getName(), plays.get(1)));
			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, 
					plays.get(0).getName(), plays.get(2)));
		} else {
			players.add(new ClientPlayer(Color.BLUE, plays.get(0).getName(), plays.get(0)));
			players.add(new ClientPlayer(Color.PURPLE, plays.get(0).getName(), plays.get(1)));
			players.add(new ClientPlayer(Color.YELLOW, plays.get(0).getName(), plays.get(2)));
			players.add(new ClientPlayer(Color.GREEN, plays.get(0).getName(), plays.get(3)));
		}
		
		return players;
	}
	
	
	public boolean allTapped(int[] tappers) {
		for (int i = 0; i < tappers.length; i++) {
			if (tappers[i] == 0) {
				return false;
			}
		}
		return true;
	}

	
}

		
