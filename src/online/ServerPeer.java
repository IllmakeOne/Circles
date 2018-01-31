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
import java.util.concurrent.TimeUnit;
import java.util.prefs.PreferencesFactory;

import javax.management.Notification;

import org.junit.platform.commons.util.ReflectionUtils;

import players.Player;
import ringz.Board;
import ringz.Color;
import view.ServerTUI;
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
    private boolean ingame = false;;
    private Lobby lobby;
    private String message;
    private String[] preferences;
    
    
    /**
     * constructor
     * @param socc the socket that with which it communicates 
     * @param lobby the lobby in which the client is
     * @param servertui the view
     */
    /*
     * @requires socc != null;
     * @requires lobby != null;
     * @requires servertui != null;
     * @ensure in != null;
     * @ensure out != null;
     * @ensure sock == socc && this.lobby == lobby;
     */
    public ServerPeer(Socket socc, Lobby lobby, ServerTUI servertui) {
    	this.lobby = lobby;
    	this.sock = socc;
    	addObserver(servertui);
    	preferences = new String[3];
    	try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Sth wrong in ServerPeer creation");
			e.printStackTrace();
		} 
    //	System.out.println(PLAYER_STATUS + DELIMITER + ACCEPT);
    }
    
    /**
     * this reads the messages from the associated client.
     */
    public void  run() {
    	message = "";
    	try {
    		
    		while (message != null) { 
    			while (ingame) {
    				try {
    					TimeUnit.MILLISECONDS.sleep(100);
    				} catch (InterruptedException e) { 
    					System.out.println("somehitng wrong in asking players to join");
    				}
    			}
    			message = in.readLine();
    			dealWithMessage(message); 
    		}
    		shutDown();
    	} catch (SocketException e) {
    		shutDown();
		} catch (IOException e) {
			System.out.println("Something unexpected went wrong");
    		shutDown();
		} catch (NullPointerException e) {
			shutDown();
		}
   
    }
    
    /**
     * this functions deals in different ways with the message it got from the client.
     * @param input the message from the associated client.
     * if the message does not respect the protocol then it is ignored.
     */
    /* @requires input != null; */
    public void dealWithMessage(String input) {
    	String[] words = input.split(DELIMITER); 
		setChanged();
    	switch (words[0]) {
    		case CONNECT: {
    			if (lobby.addtoClientList(words[1])) {
    				sendPackage(CONNECT + DELIMITER + ACCEPT);
    				this.name = words[1];
    				notifyObservers(CONNECT);
    				Thread.currentThread().setName(name);
    			} else {
    				sendPackage(CONNECT + DELIMITER + DECLINE);
    				System.out.println("it was in delcined");
    	    		shutDown();
    			}
    			break;
    		}
    		case PLAYER_STATUS: {
    			if (words[1].equals(ACCEPT)) {
    				notifyObservers("gameaccepted");
    				notifyObservers(ACCEPT);
    			} else if (words[1].equals(DECLINE)) {
    				notifyObservers("gamedeny");
    				notifyObservers(DECLINE);
    			}
    			break;
    		}
    		case GAME_REQUEST: {
    			sendPackage(JOINED_LOBBY);
    			notifyObservers(JOINED_LOBBY);
    			//Preferences is of this format : 
    			//[0]number_players//[1]player_type//[2]prefered_oponent_type
    			preferences[0] = words[1]; 
    			preferences[1] = words[2];
    			if (words.length == 4) {
    				preferences[2] = words[3];
    			} else {
    				preferences[2] = NEUTRAL;
    			}
    			lobby.addtoWaitingList(this, preferences);
    			notifyObservers("added");
    			break;
    		}
    		case MOVE: {
    			notifyObservers(message);
    			break;
    		}
    	}
    }
    	
    /**
     * @return the prefferences of the associated client. 
     * it can be empty as it can be called when the client has not requested a game yet.
     */
    public String[] getPreferences() {
    	return this.preferences;
    }
    
    /**
     * @return the name of the client.
     */
    public String getName() {
    	return this.name;
    }
    
    /**
     * @return the socket of the client.
     */
    public Socket getSocket() {
    	return this.sock; 
    }
    
    /**
     * this function puts the client in game.
     * when the client is in game, all the messages from the client are read elsewhere.
     */
    /* @ensure ingame == true ;*/
    public void inGame() {
    	ingame = true;
    }
    
    /**
     * this function sets the ingame variable to false, so the messages are read in run again.
     */
    /* @ensure ingame == false ;*/
    public void gameOver() {
    	ingame = false;
    }
    
    /**
     * Closes the connection, the sockets will be terminated.
     */
    public void shutDown() {
    	try {
    		setChanged();
    		notifyObservers("disconected");
    		lobby.diconected(this);
			sock.close();
		} catch (StackOverflowError e) {
			System.out.println("cant shut down in servepeer");
		} catch (IOException e) {
			System.out.println("cant close socket in serverpeer");
		}
    }
    
    /**
     * this returns the bufferreader of the class so other classes can read from it.
     * @return
     */
    public BufferedReader getIN() {
    	return this.in;
    }
    
    
    
    /**
     * this is just to make things easier when debugging in debugging view.
     * @see java.lang.Object#toString()
     */
    @Override 
    public String toString() {
    	return this.name;
    }
 
    
    /**
     * sends a package.
     * @param sendPackage
     */
    public void sendPackage(String sendPackage) {
    	try {
    		out.write(sendPackage);
    		out.newLine();
    		out.flush();
    	} catch (IOException e) {
    		System.out.println("Something wrong in sending Package in ServerPeer");
    		shutDown();
    	}
    }
    
    
    
}
