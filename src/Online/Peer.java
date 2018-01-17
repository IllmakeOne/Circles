package Online;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import Ringz.Board;
import Ringz.ComputerPlayer;
import Ringz.Game;
import Ringz.HumanPalyer;
import Ringz.Player;
import Ringz.Color;

/**
 * Peer for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Peer implements Runnable {
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
	//--------Playing gmae-------
	public static final String STARTING_PLAYER = "sp";
	public static final String MAKE_MOVE = "mm";
	public static final String MOVE = "mv";
	//------ending gmae----
	public static final String GAME_ENDED = "ge";
	public static final String PLAYER_DISCONNECTED = "pc";
	
	
	
	
	private Game game;
    protected String name;
    protected Socket sock;
    protected BufferedReader in;
    protected BufferedWriter out;
    protected boolean ready = false;
    private Board board;


    /*@
       requires (nameArg != null) && (sockArg != null);
     */
    /**
     * Constructor. creates a peer object based in the given parameters.
     * @param   nameArg name of the Peer-proces
     * @param   sockArg Socket of the Peer-proces
     */
    public Peer(String playerName, Socket sockArg) throws IOException {
    	this.sock = sockArg;
    	this.name = playerName;
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
    
    public void dealWithMessage(String message) {
    	String[] words = message.split(";");
    	switch (words[0]) {
    		case JOINED_LOBBY: {
    			System.out.println("You are in the lobby waiting");
    			break;
    		}
    		case CONNECT: {
    			if (words[1].equals(ACCEPT)) {
    				System.out.println("You have veen accepted, you ahve been connected");
    			} else {
    				System.out.println("Thee not worthy");
    			}
    			break;
    		}
    		case ALL_PLAYERS_CONNECTED: {
    			if (words.length == 3) {
    				Player player1 = new HumanPalyer(2, Color.BLUE, Color.PURPLE, name);
    			}
    			break;
    		}
    		case GAME_STARTED: {
    			board = new Board();
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

    /** read a line from the default input */
    static public String readString(String tekst) {
        System.out.print(tekst);
        String antw = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            antw = in.readLine();
        } catch (IOException e) {
        }

        return (antw == null) ? "" : antw;
    }
}
