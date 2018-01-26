package online;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import view.ServerTUI;

public class Lobby implements Runnable{
	
	private ArrayList<String> clients;
	private String name;
	public boolean running = true;
	private HashMap<ServerPeer, String[]> playersWaiting; 
	private ArrayList<OnlineGame> ongoingGames;
	private ServerTUI tui;
	
	public Lobby(String name, ServerTUI tui) {
		this.tui = tui;
		this.clients = new ArrayList<String>();
		this.playersWaiting = new HashMap<ServerPeer, String[]>();
		this.ongoingGames = new ArrayList<>();
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
		ServerPeer[] players = startableGame(socc);
		if (players !=  null) {
			new Thread(() -> startGame(players)).start();
		}
	}

	public void run() {
		while (running) {
			
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
					players[inPlay] = key;
					inPlay++; 
				}
			}
		} else {
			for (ServerPeer key:playersWaiting.keySet()) {
				if (playersWaiting.get(key)[0].equals(preferences[0]) && 
					/*tests if they want the same number of players*/ 
					key != current &&
					/* so it does not add the current player twice */
					inPlay < intnrplayers /*tests if it already has enough players*/) {
					//ads player to the array of players which will start a game
					players[inPlay] = key;
					inPlay++; 
					//playersWaiting.remove(key); Mistake!
				}
			}
		}	
	
		//if inPlay is the same as intnrplayers that means
		//enough players of the preferred type have been gathered
		//else return null 
		if (inPlay == intnrplayers) {
			removePlayerfromWaiting(players);
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
		//Thread newGame = new Thread(game);
		ongoingGames.add(game);
		game.run();
		tui.gameStarted(players);
//		try {
//			game.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		if (ongoingGames.contains(game)) {
			ongoingGames.remove(game);
			tui.gameEnded(players);
		}
		
	}
	
	/**
	 * this function removes someone form the lobby.
	 * it is called when someone dissconects
	 * @param client
	 */
	public void diconected(ServerPeer client) {
		try {
			clients.remove(client.getName());
			if (playersWaiting.containsKey(client)) {
				playersWaiting.remove(client);
			}
		} catch (StackOverflowError e) {
			// do nothing, it means the payer was already removed somewhere
			
		}
		for (int i = 0; i < ongoingGames.size(); i++) {
			if (ongoingGames.get(i).getPlayersasList().contains(client)) {
				ongoingGames.get(i).someoneLeft(client);
				ongoingGames.remove(ongoingGames.get(i));
				break;
			}
		}
		client.shutDown();
	}
	

	/**
	 * this functions removes the players who started a game, form the waiting list.
	 * @param players
	 */
	public void removePlayerfromWaiting(ServerPeer[] players) {
		for (int i = 0; i < players.length; i++) {
			playersWaiting.remove(players[i]);
		}
	}
	
	
	/**
	 * this shuts down the lobby.
	 */
	public void shutDown() {
		running = false;
	}
	
//
//	@Override
//	public void update(Observable o, Object arg) {
//		if (arg.equals("added")) {
//			ServerPeer[] players = startableGame((ServerPeer) o);
//			if (players !=  null) {
//				new Thread(() -> startGame(players)).start();
//				
//			}
//		}
//		
//	}
//	
	
//	/**
//	 * this functions asks all the clients who have.
//	 * matching preferences if they want to start a game.
//	 * @param client
//	 */
//	public boolean askPlayerToJoin(ServerPeer[] client) {
//		sendallConnected(client);
//		String stringy = "";
//		int flag = 1;
//		String crutch  = ServerPeer.PLAYER_STATUS + ServerPeer.DELIMITER;
//		for (int i = 0; i < client.length; i++) {
//			while (client[i].isReady() != true) {
//				stringy = client[i].getCurretMessage();
//			}
//			System.out.println(stringy + " Reply to ask people to join");
//			if (stringy.equals(crutch + ServerPeer.DECLINE)) {
//				System.out.println("should work in askPlayertoJoin");
//				flag = 0;
//			}
//		}
//		if (flag == 0) {
//			return false;
//		} else {
//			return true;
//		}
//	}
	
//	
//	/**
//	 * this sends a package with LOBBY in case someone decline a game.
//	 * @param client
//	 */
//	public void someoneDecline(ServerPeer[] client) {
//		String stringy = ServerPeer.LOBBY;
//		System.out.println(stringy + "in someone declined");
//		for (int i = 0; i < client.length; i++) {
//			client[i].sendPackage(stringy);
//		}
//	}
//	
//	

//	/**
//	 * this function notifies the players that a game is about to start.
//	 * @param players
//	 */
//	public void sendallConnected(ServerPeer[] players) {
//		String message = ServerPeer.ALL_PLAYERS_CONNECTED;
//		for (int i = 0; i < players.length; i++) {
//			message += ServerPeer.DELIMITER + players[i].getName();
//		}
//		for (int i = 0; i < players.length; i++) {
//			players[i].inGame();
//			players[i].sendPackage(message);
//		}
//	}
	
	
//	/**
//	 * this function returns true if all elemets of tappers are 1.
//	 *  meaning all players cannot place piece anymore.
//	 * @param tappers
//	 * @return
//	 */
//	/* @requires tappers != null */
//	public /*pure*/ boolean allTapped(int[] tappers) {
//		for (int i = 0; i < tappers.length; i++) {
//			if (tappers[i] == 0) {
//				return false;
//			}
//		}
//		return true;
//	}
//	
	
//	/**
//	 * this function starts a game based on an array of players who have the same preferences.
//	 * @param plays
//	 */
//	public void startGame(ServerPeer[] plays) {
//		Board board = new Board();
//		int current = 0;
//		int numberOfplayers = plays.length;
//   	//the array plays is converted into a list to it can be easily shuffled.
//   	ArrayList<ServerPeer> aux = new ArrayList<>();
//   	for (int i = 0; i < numberOfplayers; i++) {
//   		aux.add(plays[i]);   		
//   	}
//   	Collections.shuffle(aux);
//   	ArrayList<ClientPlayer> players = createPlayers(aux);
//   	
//   	
//   	//placing of the first piece
//   	Move firstmove = players.get(current).determineMove(board);
//   	int[] a = {firstmove.getLine(), //line of the first move
//   			firstmove.getColumn()}; //column of the first move
//   	board.placeStart(a);
//		current = (current + 1) % numberOfplayers;
//   	
//   	int[] tappers = new int[numberOfplayers];
//   	//this keep track of who is still able to play, 0 if still able to , and 1 if is out.
//   	
//   	while (!board.isFull() && !allTapped(tappers)) {
//   		if (players.get(current).isOutOfPieces()) {
//   			tappers[current] = 1;
//				current = (current + 1) % numberOfplayers;
//   		} else if (!board.isStrillAbleToPlace(players.get(current))) {
//   			tappers[current] = 1;
//				current = (current + 1) % numberOfplayers;
//   		} else {
//   			Move currentmove = players.get(current).determineMove(board);
//   			if (board.addCircle(currentmove)) {
//   				players.get(current).decresePiece(currentmove);
//   				current = (current + 1) % numberOfplayers;
//   			} 
//   		}
//   	}
//   	
//   	//send the result to the involved players
//   	sendResults(board, numberOfplayers, players);	
//	}
	
	
	
//	/**
//	 * this functions sends the result of the game to the clients involved.
//	 * @param board the board the clients were playing on
//	 * @param numberPlayers number of players 
//	 * @param plays the players
//	 */
//	public /*pure*/ void sendResults(Board board,
//				int numberPlayers, ArrayList<ClientPlayer> plays) {
//		//results is of this form : 
//		//[0]Blue points;[1] Purple points;[2]Yellow points;[3] Green points
//		int[] results = board.total();
//		String stringy = ServerPeer.GAME_ENDED + ServerPeer.DELIMITER;
//		if (numberPlayers == 2) {
//			stringy += plays.get(0).getName() + ServerPeer.DELIMITER 
//					+ (results[0] + results[1]) + ServerPeer.DELIMITER
//					+ plays.get(1).getName() + ServerPeer.DELIMITER 
//					+ (results[2] + results[3]);
//		} else if (numberPlayers == 3) {
//			stringy += plays.get(0).getName() + ServerPeer.DELIMITER 
//					+ results[0] + ServerPeer.DELIMITER
//					+ plays.get(1).getName() + ServerPeer.DELIMITER 
//					+ results[1] + ServerPeer.DELIMITER 
//					+ plays.get(2).getName() + ServerPeer.DELIMITER 
//					+ results[3];
//		} else {
//			stringy += plays.get(0).getName() + ServerPeer.DELIMITER 
//					+ results[0] + ServerPeer.DELIMITER
//					+ plays.get(1).getName() + ServerPeer.DELIMITER 
//					+ results[1] + ServerPeer.DELIMITER 
//					+ plays.get(2).getName() + ServerPeer.DELIMITER 
//					+ results[2]
//					+ plays.get(3).getName() + ServerPeer.DELIMITER 
//					+ results[3];
//		}
//		
//		for (int i = 0; i < numberPlayers; i++) {
//			plays.get(i).getSocket().sendPackage(stringy);
//		}
//	}
	
	
//	/**
//	 * this function creates an array of ClinetsPlayers with which a game will start
//	 * this keeps track of everyone's pieces.
//	 * @param plays
//	 * @return
//	 */
//	public ArrayList<ClientPlayer> createPlayers(ArrayList<ServerPeer> plays) {
//		ArrayList<ClientPlayer> players = new ArrayList<ClientPlayer>();
//		if (plays.size() == 2) {
//			players.add(new ClientPlayer(2, Color.BLUE, Color.PURPLE, 
//					plays.get(0).getName(), plays.get(0)));
//			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, 
//					plays.get(0).getName(), plays.get(1)));
//		} else if (plays.size() == 3) {
//			players.add(new ClientPlayer(2, Color.BLUE, Color.GREEN, 
//					plays.get(0).getName(), plays.get(0)));
//			players.add(new ClientPlayer(2, Color.PURPLE, Color.GREEN, 
//					plays.get(0).getName(), plays.get(1)));
//			players.add(new ClientPlayer(2, Color.YELLOW, Color.GREEN, 
//					plays.get(0).getName(), plays.get(2)));
//		} else {
//			players.add(new ClientPlayer(Color.BLUE, plays.get(0).getName(), plays.get(0)));
//			players.add(new ClientPlayer(Color.PURPLE, plays.get(0).getName(), plays.get(1)));
//			players.add(new ClientPlayer(Color.YELLOW, plays.get(0).getName(), plays.get(2)));
//			players.add(new ClientPlayer(Color.GREEN, plays.get(0).getName(), plays.get(3)));
//		}
//		
//		return players;
//	}
//	
//	
}

		
