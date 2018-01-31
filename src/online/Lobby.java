package online;


import java.util.ArrayList;
import java.util.HashMap;

import view.ServerTUI;

public class Lobby {
	 //list of all the clients who are connected at the moment
	private ArrayList<String> clients; 
	
	//list of all the clients who ever connected to the server
	private ArrayList<String> alltimeclients;
	
	//list of all the clients who are looking for a game at the moment
	private HashMap<ServerPeer, String[]> playersWaiting; 
	
	private ArrayList<OnlineGame> ongoingGames; //list of all ongoing games
	private ServerTUI tui;
	
	/**
	 * constructor.
	 * @param tui the visual interface 
	 */
	public Lobby(ServerTUI tui) {
		this.tui = tui;
		this.clients = new ArrayList<String>();
		this.alltimeclients = new ArrayList<String>();
		this.playersWaiting = new HashMap<ServerPeer, String[]>();
		this.ongoingGames = new ArrayList<>();
	}  
	
	/**
	 * this functions tests if the clientname is already in use.
	 * if it is not it will @return true and 
	 * add the clientname to the clients who are currently online.
	 * if it is not in a the alltimeclients lists, it adds it there too.
	 * if the clientname is already online, it will @return false;
	 * @param clientname the client who just connected
	 */
	/*
	 * !getClients().contains(clientname) ==> \result true;
	 * getClients().contains(clientname) ==> \result false;
	 */
	public boolean addtoClientList(String clientname) {
		if (!alltimeclients.contains(clientname)) {
			alltimeclients.add(clientname);
		}
		if (clients.contains(clientname)) {
			return false;
		} else {
			clients.add(clientname);
			return true;
		}
	}
	
	/**
	 * this functions adds a client to the waiting list.
	 * the waiting list is for the clients who are waiting to be connected to a game.s
	 * @param socc the client.
	 * @param preferences ; the preferences of the client.
	 */
	/*
	 * @requires preferences.lenght == 2;
	 * @ensure getPlayersWaiting().keyset().contains(socc) 
	 * 		&& getPlayersWaiting().get(socc) == preferences;
	 */
	public void addtoWaitingList(ServerPeer socc, String[] preferences) {
		playersWaiting.put(socc, preferences);
		ServerPeer[] players = startableGame(socc);
		if (players !=  null) {
			new Thread(() -> startGame(players)).start();
		}
	}


	
	/**
	 * this functions is called when a new player requires a game.
	 * It will go through all the players who are waiting for a game
	 *  and select those who match in preference.
	 * @param preferences
	 * @return
	 */
	/*
	 * @requires getPlayersWaiting().keyset().contains(current);
	 */
	public ServerPeer[] startableGame(ServerPeer current) {
		//ServerPeer current is the new clients who entered the waiting list.
		//Preferences is of this format : 
		//[0]number_players//[1]player_type//[2]prefered_oponent_type
		String[] preferences = playersWaiting.get(current);
		int intnrplayers = Integer.valueOf(preferences[0]);
		ServerPeer[] players = new ServerPeer[intnrplayers];
		//the players that want to play against as many people the the player being tested
		players[0] = current;
		int inPlay = 1;
		//if the player actually wants to play against a certain player type
		if (!preferences[2].equals(ServerPeer.NEUTRAL)) {
			for (ServerPeer key:playersWaiting.keySet()) {
				if (playersWaiting.get(key)[0].equals(preferences[0]) &&
		/* this tested if current and the key have want to play against the same number of players*/
					playersWaiting.get(key)[1].equals(preferences[2])  &&
			/*this tests the tested player matches the preferred player type of the current player*/
					key != current &&
					/* so it does not add the current player twice */
					inPlay < intnrplayers 
			/*tests if it already has enough players*/) {
					if (playersWaiting.get(key)[2].equals(ServerPeer.NEUTRAL)) {
						players[inPlay] = key;
						inPlay++; 
					} else if (playersWaiting.get(key)[2].equals(playersWaiting.get(current)[1])) {
						players[inPlay] = key;
						inPlay++; 
					}
				}
			}
		} else {
			for (ServerPeer key:playersWaiting.keySet()) {
				if (playersWaiting.get(key)[0].equals(preferences[0]) && 
					/*tests if they want the same number of players*/ 
					key != current &&
					/* so it does not add the current player twice */
					inPlay < intnrplayers /*tests if it already has enough players*/) {
					if (playersWaiting.get(key)[2].equals(ServerPeer.NEUTRAL)) {
						players[inPlay] = key;
						inPlay++; 
					} else if (playersWaiting.get(key)[2].equals(playersWaiting.get(current)[1])) {
						players[inPlay] = key;
						inPlay++; 
					}
				}
			}
		}	
	
		//if inPlay is the same as intnrplayers that means
		//enough players of the preferred type have been gathered
		//else return null 
		if (inPlay == intnrplayers) {
			removePlayerSfromWaiting(players);
			return players;
		} else {
			return null;
		}
	}

	/**
	 * this function starts a game.
	 * when it is called , it is called in a new thread.
	 * @param players are the players who are going into a game together.
	 */
	/*
	 * @requires players.lenght > 1 && players.lenght < 5;
	 */
	public void startGame(ServerPeer[] players) {
		for (int i = 0; i < players.length; i++) {
			playersWaiting.remove(players[i]);
		}
		OnlineGame game = new OnlineGame(players, this);
		ongoingGames.add(game);
		game.run();
		tui.gameStarted(players);
		
		if (ongoingGames.contains(game)) {
			ongoingGames.remove(game);
			tui.gameEnded(players);
		}
		
	}
	
	
	/**
	 * this function removes someone form the lobby.
	 * it is called when someone disconnects while not in a game
	 * @param client
	 */
	/*
	 * @ensure !getClients().contains(client) && !getPlayerWaiting().keyset().contains(client);
	 */
	public void diconected(ServerPeer client) {
		removePlayerfromLobby(client);
		removePlayerfromwaiting(client);
		for (int i = 0; i < ongoingGames.size(); i++) {
			if (ongoingGames.get(i).getPlayersasList().contains(client)) {
				ongoingGames.get(i).someoneLeft(client);
				tui.gameEnded(ongoingGames.get(i).getPlayersasArray());
				ongoingGames.remove(ongoingGames.get(i));
				break;
			}
		}
	}
	

	/**
	 * this functions removes the players who started a game, from the waiting list.
	 * @param players
	 */
	public void removePlayerSfromWaiting(ServerPeer[] players) {
		for (int i = 0; i < players.length; i++) {
			playersWaiting.remove(players[i]);
		}
	}
	
	/**
	 * this function removes a client (who disconnects) form the client list.
	 */
	public void removePlayerfromLobby(ServerPeer client) {
		clients.remove(client.getName());
	}
	
	/**
	 * this functions removes the player (who disconnects) form the waiting list.
	 */
	public void removePlayerfromwaiting(ServerPeer client) {
		if (playersWaiting.containsKey(client)) {
			playersWaiting.remove(client);
		}
	}
	
	/**
	 * return a list of ongoing game.
	 */
	public /*pure */ ArrayList<OnlineGame> getOngoingGame() {
		return ongoingGames;
	}
	
	/**
	 * returns the list of current clients online.
	 */
	public /*pure*/ ArrayList<String> getClients() {
		return clients;
	}
	
	/**
	 * return a list of all the players who have ever been online in the server.
	 */
	public /*pure */ ArrayList<String> getAlltimeclients() {
		return alltimeclients;
	}
	
	/**
	 * this return the map of the players waiting for a game and their preferences.
	 */
	public /*pure */ HashMap<ServerPeer, String[]> getPlayersWaiting() {
		return playersWaiting;
	}
	
	
	
}