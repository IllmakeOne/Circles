package online;


import java.util.ArrayList;
import java.util.HashMap;

import view.ServerTUI;

public class Lobby {
	
	private ArrayList<String> clients; //list of all the clients who are connected at the moment
	private ArrayList<String> alltimeclients; //list of all the clients who ever 
	
	//list of all the clients who are looking for a game at the moment
	private HashMap<ServerPeer, String[]> playersWaiting; 
	
	private ArrayList<OnlineGame> ongoingGames; //list of all ongoing games
	private ServerTUI tui;
	
	public Lobby(ServerTUI tui) {
		this.tui = tui;
		this.clients = new ArrayList<String>();
		this.alltimeclients = new ArrayList<String>();
		this.playersWaiting = new HashMap<ServerPeer, String[]>();
		this.ongoingGames = new ArrayList<>();
	} 
	
	public boolean addtoClientList(String clientname) {
		if (clients.contains(clientname)) {
			return false;
		} else {
			alltimeclients.add(clientname);
			clients.add(clientname);
			return true;
		}
	}
	
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
	
//	/**
//	 * this function removes someone form the lobby.
//	 * it is called when someone dissconects while it is in a game.
//	 * @param client
//	 */
//	public void diconectedWhileinGame(ServerPeer client) {
//		removePlayerfromLobby(client);
//		for (int i = 0; i < ongoingGames.size(); i++) {
//			if (ongoingGames.get(i).getPlayersasList().contains(client)) {
//				ongoingGames.get(i).someoneLeft(client);
//				ongoingGames.remove(ongoingGames.get(i));
//				break;
//			}
//		}
//		client.shutDown();
//	}
	
	/**
	 * this function removes someone form the lobby.
	 * it is called when someone disconnects while not in a game
	 * @param client
	 */
	public void diconected(ServerPeer client) {
		removePlayerfromLobby(client);
		removePlayerfromwaiting(client);
		for (int i = 0; i < ongoingGames.size(); i++) {
			if (ongoingGames.get(i).getPlayersasList().contains(client)) {
				ongoingGames.get(i).someoneLeft(client);
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
	
	
	
}