package online;

import java.net.Socket;
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
	private HashMap<Socket, String[]> playersWaiting;
	
	public Lobby(String name) {
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
	
	public void addtoWaitingList(Socket socc, String[] preferences) {
		playersWaiting.put(socc, preferences);
	}


	public Socket[] startableGame(String[] preferences) {

		//Preferences is of this format : 
		//[0]number_players//[1]player_type//[2]prefered_oponent_type
		int intnrplayers = Integer.valueOf(preferences[0]);
		Socket[] players = new Socket[intnrplayers];
		//the players that want to play against as many people the the player being tested
		int inPlay = 0;
		for (Socket key:playersWaiting.keySet()) {
			if (playersWaiting.get(key)[1].equals(preferences[0]) && inPlay < intnrplayers) {
				players[inPlay] = key;
				inPlay++;
			}
		}
		if (!preferences[2].equals(ServerPeer.NEUTRAL)) {
			//this will go though only if the players wants to go against something specific
			for (Socket key:playersWaiting.keySet()) {
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
			for (Socket key:playersWaiting.keySet()) {
				if (playersWaiting.get(key)[0].equals(preferences[0])
					/*tests if they want the same number of players*/ &&
					inPlay < intnrplayers /*tests if it already has enough players*/) {
					//ads player to the array of players which will start a game
					players[inPlay] = key;
				}
			}
		}
		//if inPlay is the same as intnrplayers that means
		//enough players of the preferred type have been gathered
		if (inPlay == intnrplayers) {
			return players;
		} else {
			return null;
		}
	}

	public void startGame(Socket[] plays) {
		Board board = new Board();
		int current = 0;
		int numberOfplayers = plays.length;
    	ArrayList<Socket> aux = new ArrayList<>();
    	for (int i = 0; i < numberOfplayers; i++) {
    		aux.add(plays[i]);   		
    	}
    	Collections.shuffle(aux);
    	ArrayList<ClientPlayer> players = createPlayers(aux);
    	int[] tappers = new int[numberOfplayers];
    	//this keep track of who is still able to play, 0 if still able to , and 1 if is out
    	while (!board.isFull() && !allTapped(tappers)) {
    		if (players.get(current).isOutOfPieces() ||
    				!board.isStrillAbleToPlace(players.get(current))) {
    			tappers[current] = 1;
    		} else {
    			Move currentmove = players.get(current).askForMove();
    			if (board.addCircle(currentmove)) {
    				players.get(current).decresePiece(currentmove);
    				current = (current + 1) % numberOfplayers;
    			} 
    		}
    	}
    	
    	
	//while(board);
    	
		
	}
	
	public ArrayList<ClientPlayer> createPlayers(ArrayList<Socket> plays){
		ArrayList<ClientPlayer> players = new ArrayList<ClientPlayer>();
		if (plays.size() == 2) {
			players.add(new ClientPlayer(2, Color.BLUE, Color.PURPLE, "1", plays.get(0)));
			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, "2", plays.get(1)));
		} else if (plays.size() == 3) {
			players.add(new ClientPlayer(2, Color.BLUE, Color.GREEN, "1", plays.get(0)));
			players.add(new ClientPlayer(2, Color.PURPLE, Color.GREEN, "2", plays.get(1)));
			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, "3", plays.get(2)));
		} else {
			players.add(new ClientPlayer(Color.BLUE, "1", plays.get(0)));
			players.add(new ClientPlayer(Color.PURPLE, "2", plays.get(1)));
			players.add(new ClientPlayer(Color.YELLOW, "3", plays.get(2)));
			players.add(new ClientPlayer(Color.GREEN, "4", plays.get(3)));
		}
		
		return players;
	}
	
	public boolean allTapped(int[] tappers) {
		return false;
	}

	
}

		
