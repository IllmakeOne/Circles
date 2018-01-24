package online;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Observable;
import java.util.prefs.PreferencesFactory;

import javax.management.Notification;

import players.Player;
import ringz.Board;
import ringz.Color;
import view.TUI;

public class ServerPeer extends Observable implements Runnable {
	
	public static final String DELIMITER = ";";
	
    public static final String ACCEPT	= "0";
	public static final String DECLINE	= "1";
	
	public static final String NEUTRAL	= "2";
	public static final String COMPUTER_PLAYER	= "0";
	public static final String HUMAN_PLAYER	= "1";
	//---------Pieces------
	public static final String STARTING_BASE	= "0";
	public static final String BASE	= "1";
	public static final String RING_SMALLEST	= "2";
	public static final String RING_SMALL	= "3";
	public static final String RING_MEDIUM	= "4";
	public static final String RING_LARGE	= "5";
	public static final String PRIMARY = "0";
	public static final String SECONDARY = "1";
	//-----Chatting---------
	public static final String GLOBAL	= "0";
	public static final String LOBBY	= "1";
	public static final String PRIVATE	= "2";
	
	public static final String EXTENSION_CHATTING = "chat";
	public static final String EXTENSION_CHALLENGING = "chal";
	public static final String EXTENSION_LEADERBOARD = "lead";
	public static final String EXTENSION_SECURITY = "secu";
	
	//-------Connecting------
	public static final String CONNECT = "cn";
	//------Request game------
	public static final String GAME_REQUEST = "gr";
	public static final String JOINED_LOBBY = "jl";
	//----------Starting game------
	public static final String ALL_PLAYERS_CONNECTED = "ap";
	public static final String PLAYER_STATUS = "ps";
	public static final String GAME_STARTED = "gs";
	//--------Playing game-------
	public static final String STARTING_PLAYER = "sp";
	public static final String MAKE_MOVE = "mm";
	public static final String MOVE = "mv";
	//------ending game----
	public static final String GAME_ENDED = "ge";
	public static final String PLAYER_DISCONNECTED = "pc";

	
	protected String name;
	protected Socket sock;
    protected BufferedReader in;
    protected BufferedWriter out;
    
    protected boolean ready = false;
    
    private String nature;
    private int numberPlayers;
    private boolean ingame = false;;
    private Lobby lobby;
    private String message;
    
    public ServerPeer(Socket socc, Lobby lobby) {
    	this.lobby = lobby;
    	this.sock = socc;
    	try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Sth wrong in ServerPeer creation");
			e.printStackTrace();
		}
    //	System.out.println(PLAYER_STATUS + DELIMITER + ACCEPT);
    }
    
    public void  run() {
    	try {
			message = in.readLine();
    		while (message != null) { 
    			dealWithMessage(message); 
    			System.out.println(message + " message read in run " + this.name); 
    			message = in.readLine();
    		}
    		//shutDown();
    	} catch (SocketException e) {
			System.out.println(name + " disconected");
			shutDown();
		} catch (IOException e) {
			System.out.println("Something else went wrong");
			shutDown();
		}
    }
    
//    /**
//     * reads only one package.
//     */
//    public String getMessage() {
//    	String message = null;
//    	try {
//    		message = in.readLine();
//    	} catch (SocketException e) {
//			System.out.println("cant read only one package");
//			shutDown();
//		} catch (IOException e) {
//			System.out.println("Something else went wrong");
//			shutDown();
//		}
//    	return message;
//    }
//    
    
    
    public void dealWithMessage(String input) {
    	String[] words = input.split(DELIMITER); 
    	switch (words[0]) {
    		case CONNECT: {
    			if (lobby.addtoClientList(words[1])) {
    				sendPackage(CONNECT + DELIMITER + ACCEPT);
    				this.name = words[1];
    				Thread.currentThread().setName(name);
    				System.out.println(this.name);
    			} else {
    				sendPackage(CONNECT + DELIMITER + DECLINE);
    				shutDown();
    			}
    			break;
    		}
    		case PLAYER_STATUS: {
    			setChanged();
    			if (words[1].equals(ACCEPT)) {
    				notifyObservers("gameaccepted");
    			} else if (words[1].equals(DECLINE)) {
    				notifyObservers("gamedeny");
    			}
    			break;
    		}
    		case GAME_REQUEST: {
    			sendPackage(JOINED_LOBBY);
    			//Preferences is of this format : 
    			//[0]number_players//[1]player_type//[2]prefered_oponent_type
    			String[] preferences = new String[3];
    			preferences[0] = words[1]; 
    			preferences[1] = words[2];
    			if (words.length == 4) {
    				preferences[2] = words[3];
    			} else {
    				preferences[2] = NEUTRAL;
    			}
    			lobby.addtoWaitingList(this, preferences);
    			ServerPeer[] players = lobby.startableGame(this);
    			if (players != null) { 
    				ingame = true; 
    				lobby.startGame(players);
    			}
    			break;
    		}
    		case MOVE: {
    			setChanged();
    			notifyObservers(message);
    			break;
    		}
    		case PLAYER_DISCONNECTED: {
    			lobby.diconected(this);
    			shutDown();
    			break;
    		}
    	}
    }
    		
    
    public String getName() {
    	return this.name;
    }
    
    public Socket getSocket() {
    	return this.sock; 
    }
    
    public void inGame() {
    	ingame = true;
    }
    
    public boolean isReady() {
    	return ready;
    }
    
    /**
     * Closes the connection, the sockets will be terminated.
     */
    public void shutDown() {
    	try {
			sock.close();
			lobby.diconected(this);
		} catch (IOException e) {
			System.out.println("sth wrong in shutting down");
			e.printStackTrace();
		}
    }
    
    public BufferedReader getIN() {
    	return this.in;
    }
    
    
//    public String getCurretMessage() {
//    	return this.message;
//    }
    
    /**
     * this is just to make things easier when debugging in deugging view.
     * @see java.lang.Object#toString()
     */
    @Override 
    public String toString() {
    	return this.name + " " + this.message;
    }
 
    
    /**
     * sends a package.
     * @param sendPackage
     */
    public void sendPackage(String sendPackage) {
    	try {
    		System.out.println(sendPackage + " in send packaage");
    		out.write(sendPackage);
    		out.newLine();
    		out.flush();
    	} catch (IOException e) {
    		System.out.println("Something wrong in sending Package in ServerPeer");
    		shutDown();
    	}
    }
    
    
    
}
