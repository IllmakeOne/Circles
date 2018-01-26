package view;

import java.util.Observable;
import java.util.Observer;

import online.ServerPeer;

public class ServerTUI implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		if (arg.toString().startsWith("tried")) {
			System.out.println(arg.toString().substring(5) + 
					" tried to connect, but somone else has the username");
			
		} else if (arg.equals("came")) {
			System.out.println(o.toString() + " connected");
		} else if (arg.equals(ServerPeer.JOINED_LOBBY)) {
			System.out.println(o.toString() + " entered the waiting lobby");
		} else if (arg.equals(ServerPeer.CONNECT)) {
			System.out.println(o.toString() + " connected");
		} else if (arg.equals(ServerPeer.ACCEPT)) {
			System.out.println(o.toString() + " accepted a game");
		} else if (arg.equals(ServerPeer.DECLINE)) {
			System.out.println(o.toString() + " declined a game");
		}
		
	}

	
	
	public void gameStarted(ServerPeer[] players) {
		String stringy = "";
		for (int i = 0; i < players.length; i++) {
			stringy += players[i].getName() + ",";
		}
		System.out.println("The game with " + stringy + "started");
	}
	
	public void gameEnded(ServerPeer[] players) {
		String stringy = "";
		for (int i = 0; i < players.length; i++) {
			stringy += players[i].getName() + ",";
		}
		System.out.println("The game with " + stringy + "ended");
	}
}
