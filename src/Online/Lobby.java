package Online;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import Ringz.Board;
import Ringz.Move;

public class Lobby {
	
	private ArrayList<String> clients;
	private Board board;
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

	public Socket[] startableGame(String nrPlayers) {
		int intnrplayers = Integer.valueOf(nrPlayers);
		Socket[] players = new Socket[intnrplayers];
		int inPlay = 0;
		for (Socket key:playersWaiting.keySet()) {
			if (playersWaiting.get(key)[1].equals("" + nrPlayers) && inPlay < intnrplayers) {
				players[inPlay] = key;
				inPlay++;
			}
		}
		if (inPlay == intnrplayers) {
			return players;
		} else {
			return null;
		}
	}

}
