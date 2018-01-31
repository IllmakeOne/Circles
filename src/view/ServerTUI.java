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
			/**Someone connected to the server and was accepted*/
			System.out.println(o.toString() + " connected");
		} else if (arg.equals(ServerPeer.JOINED_LOBBY)) {
			/**Someone joined the lobby after requesting a game*/
			System.out.println(o.toString() + " entered the waiting lobby");
		} else if (arg.equals(ServerPeer.CONNECT)) {
			/**Someone connected to the server*/
			System.out.println(o.toString() + " connected");
		} else if (arg.equals("gameaccepted")) {
			/**Someone accepted a game*/
			System.out.println(o.toString() + " accepted a game");
		} else if (arg.equals(ServerPeer.DECLINE)) {
			/**Someone decline a game*/
			System.out.println(o.toString() + " declined a game");
		} else if (arg.equals("disconected")) {
			/**Someone disconected*/
			System.out.println(o.toString() + " disconected");
		} 
		
	} 

	
	/**
	 * print out that a game has started with @param palyers.
	 */
	public void gameStarted(ServerPeer[] players) {
		String stringy = "";
		for (int i = 0; i < players.length; i++) {
			stringy += players[i].getName() + ",";
		}
		System.out.println("The game with " + stringy + "started");
	}
	
	/**
	 * print out that a game has ended with @param palyers.
	 */
	public void gameEnded(ServerPeer[] players) {
		String stringy = "";
		for (int i = 0; i < players.length; i++) {
			stringy += players[i].getName() + ",";
		}
		System.out.println("The game with " + stringy + "ended");
	}
}
