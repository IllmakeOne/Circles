package Online;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.text.View;

import Ringz.Board;
import Ringz.ComputerPlayer;
import Ringz.Game;
import Ringz.HumanPalyer;
import Ringz.Move;
import Ringz.Player;
import View.TUI;
import View.*;
import Ringz.Color;

/**
 * Peer for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Peer extends Observable implements Runnable{
	
	public static final String DELIMITER = ";";
	
    public static final String ACCEPT	= "0";
	public static final String DECLINE	= "1";
	
	
	
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
    
    private Board board;
    private Player clientPlayer;
    private TUI view;
    private HashMap<String, Color> playerColors;
    private String nature;


    /*@
       requires (nameArg != null) && (sockArg != null);
     */
    /**
     * Constructor. creates a peer object based in the given parameters.
     * @param   nameArg name of the Peer-proces
     * @param   sockArg Socket of the Peer-proces
     */
    public Peer(String playerName, String playerType, Socket sockArg) throws IOException {
    	this.nature = playerType;
    	this.sock = sockArg;
    	this.name = playerName;
    	this.view = new TUI(name);
    	in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
    	out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
    	
    }

    /**
     * Reads strings of the stream of the socket-connection and
     * writes the characters to the default output.
     */
    public void run() {
    	try {
    		String message = in.readLine();
    		while (message != null && !message.equals(EXIT)) {
    			System.out.println(message);
    			//System.out.flush();
    			message = in.readLine();
    			//ready = true;	
    		}
    		shutDown();
    		System.out.println("other exited");
		} catch (IOException e) {
			System.out.println("sth wrong in run");
			shutDown();
			e.printStackTrace();
		}
    }


    /**
     * Reads a string from the console and sends this string over
     * the socket-connection to the Peer process.
     * On Peer.EXIT the method ends
     */
    public void handleTerminalInput() {
    	String scan = readString("> ");
    	while (!scan.equals(EXIT) && scan != null) {
    		try {
				out.write(scan);
	    		out.newLine();
	    		out.flush();
    			scan = readString("> "); 
			} catch (IOException e) {
				System.out.println("sth swrong in haldeterminal");
		    	shutDown();
				e.printStackTrace();
			}
    		//while (!ready) {
    			//ready = false;
    			//scan = readString("> ");     			
    		//}
    	}
    }
    
    public void sendPackage(String sendPackage) {
    	try {
    		out.write(sendPackage);
    		out.newLine();
    		out.flush();
    	} catch (IOException e) {
    		System.out.println("Something wrong in sneding Package");
    		shutDown();
    	}
    }

    
    public void dealWithMessage(String message) {
    	String[] words = message.split(";");
    	this.hasChanged();
    	switch (words[0]) {
    		case JOINED_LOBBY: {
    			this.notifyObservers("lobby");
    			System.out.println("You are in the lobby waiting");
    			break;
    		}
    		case CONNECT: {
    			if (words[1].equals(ACCEPT)) {
        			this.notifyObservers("accepted");
    				System.out.println("You have veen accepted, you ahve been connected");
    			} else {
        			this.notifyObservers("denied");
    				System.out.println("Thee not worthy");
    			}
    			break;
    		}
    		case ALL_PLAYERS_CONNECTED: {
    			String acceptance = view.acceptGame(words);
    			if (acceptance.equals("0")) {
        			this.notifyObservers("gameacc");
    				sendPackage(PLAYER_STATUS + DELIMITER + acceptance);
    				this.createBoard(words);
    			}
    			else {
    				sendPackage(PLAYER_STATUS + DELIMITER + acceptance);
        			this.notifyObservers("gamedeny");
    			}
    			break;
    		}
    		case GAME_STARTED: {
    			this.notifyObservers("gamestart");
    			view.showPieces(clientPlayer);
    			break;
    		}
    		case MAKE_MOVE: {
    			makeMove();
    		}
    		
    	}
    }

    
    /**
     * this function asks the client for a move.
     */
    public void makeMove() {
    	if (board.emptyBoard()) {
			notifyObservers("first");
			sendPackage(askFirst());
		} else {
			if (this.nature.equals(HUMAN_PLAYER)) {
				view.askMove(clientPlayer, board);	
			} else {
				
			}
		}
    }
    
    public String moveTostring(Move move) {
    	String stringy = "";
    	return stringy;
    }
    
    /**
     * this asks the player for the first move, in the case they that they go first.
     * @return
     * view.getStart() will return an array of two numbers,
     *  one is the line and one is the column of the first piece
     */
    public String askFirst() {
    	int[] firstMove;
		if (this.nature.equals(HUMAN_PLAYER)) {
			firstMove = view.getStart();
		} else {
			firstMove = clientPlayer.getStart();
		}
		String stringy = "";
		stringy += MOVE + firstMove[0] + DELIMITER + firstMove[1] + DELIMITER + STARTING_BASE;
		return stringy;
    }
    
    /**
     * this creates the Hashmap with the colors and players and createas a player for this socket.
     * @param words
     */
    public void createBoard(String[] words) {
    	this.board = new Board();
    	if (words.length == 3) {
			//Creates Hashmap with name of the player + SECONDARY/PRIMARY color
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[1] + SECONDARY, Color.PURPLE);
			playerColors.put(words[2] + PRIMARY, Color.YELLOW);
			playerColors.put(words[2] + SECONDARY, Color.GREEN);
			//Also creates local player so we can keep track of the pieces and show them
			if (this.nature.equals("H")) {
				this.clientPlayer = new HumanPalyer(2, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			} else {
				this.clientPlayer = new ComputerPlayer(2,
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			}
		} else if (words.length == 4) {
			//Creates Hashmap with the first three color being unique to a player
			// then puts the same SECONDARY color to all the players
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[2] + PRIMARY, Color.PURPLE);
			playerColors.put(words[3] + PRIMARY, Color.YELLOW);
			playerColors.put(words[1] + SECONDARY, Color.BLUE);
			playerColors.put(words[2] + SECONDARY, Color.PURPLE);
			playerColors.put(words[3] + SECONDARY, Color.YELLOW);
			//Also creates local player so we can keep track of the pieces and show them
			if (this.nature.equals("H")) {
				this.clientPlayer = new HumanPalyer(3, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			} else {
				this.clientPlayer = new ComputerPlayer(3,
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			}
				
		} else if (words.length == 5) {
			//this just gives a color to everyone
			playerColors.put(words[1], Color.BLUE);
			playerColors.put(words[2], Color.PURPLE);
			playerColors.put(words[3], Color.YELLOW);
			playerColors.put(words[4], Color.GREEN);
			//Also creates local player so we can keep track of the pieces and show them
			if (this.nature.equals("H")) {
				this.clientPlayer = new HumanPalyer(playerColors.get(name + PRIMARY), name);
			} else {
				this.clientPlayer = new ComputerPlayer(playerColors.get(name + PRIMARY), name);
			}
		}
	} 
    	

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Closes the connection, the sockets will be terminated
     */
    public void shutDown() {
    	try {
			sock.close();
		} catch (IOException e) {
			System.err.println("sth wrong in shutdow");
			e.printStackTrace();
		}
    }

    /**  returns name of the peer object*/
    public String getName() {
        return name;
    }

}
