package online;

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


import players.ComputerPlayer;
import players.HumanPalyer;
import players.Player;
import ringz.Board;
import ringz.Color;
import ringz.Move;
import view.*;


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
    
    
    private Board board;
    private Player clientPlayer;
    private TUI view;
    private HashMap<String, Color> playerColors;
    private String nature;
    private int numberPlayers;
    private boolean gameinProgress;


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
    	this.playerColors = new HashMap<String, Color>();
    	this.sock = sockArg;
    	this.gameinProgress = false;
    	this.name = playerName;
    	this.view = new TUI(name);
    	this.addObserver(view);
    	in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
    	out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
    	sendfirstPack();
    }

    /**
     * Reads strings of the stream of the socket-connection and
     * writes the characters to the default output.
     */
    public void  run() {
    	try {
    		String message = in.readLine();
    		while (message != null) {
    			System.out.println(message);
    			dealWithMessage(message);  
    			message = in.readLine();
    		}
    		shutDown();
		} catch (IOException e) {
			System.out.println("sth wrong in run");
			shutDown();
			e.printStackTrace();
		}
    }

    public void sendfirstPack() {
    	sendPackage(CONNECT + DELIMITER + this.name);
    }

    /**
     * Reads a string from the console and sends this string over
     * the socket-connection to the Peer process.
     * On Peer.EXIT the method ends
     */
    public  void lobby() {
    	this.setChanged();
    	notifyObservers("joined");
    	String todo = view.whattoDo(this.nature);
    	while (!todo.equals("exit") && gameinProgress == false) {
    		sendPackage(todo);
    		todo = view.whattoDo(this.nature);
    	}
    	//shutDown();
    	
    }
    
    public void sendPackage(String sendPackage) {
    	try {
    		out.write(sendPackage);
    		out.newLine();
    		out.flush();
    	} catch (IOException e) {
    		System.out.println("Something wrong in sending Package in Peer");
    		shutDown();
    	}
    }

    
    public void dealWithMessage(String message) {
    	String[] words = message.split(DELIMITER);
    	this.setChanged();
    	switch (words[0]) {
    		case JOINED_LOBBY: {
    			this.notifyObservers("lobby");
    			this.gameinProgress = true;
    			break;
    		}
    		case CONNECT: {
    			if (words[1].equals(ACCEPT)) {
        			this.notifyObservers("accepted");
    			} else {
        			this.notifyObservers("denied");
        			shutDown();
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
        			shutDown();
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
    			break;
    		}
    		case MOVE: {
    			board.addCircle(stringTomove(words));
    			break;
    		}
    		case GAME_ENDED: {
    			view.displayEnd(words);
    			break;
    		}
    		case PLAYER_DISCONNECTED: {
    			view.disconnected(words[1]);
    			this.notifyObservers("lobby");
    			break;
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
				sendPackage(MOVE + moveTostring(view.askMove(clientPlayer, board)));	
			} else {
				sendPackage(MOVE + moveTostring(clientPlayer.determineMove(board)));
			}
		}
    }
    
    /**
     * this function converts a move into a Protocol correct string.
     * @param move
     * @return
     */
    public String moveTostring(Move move) {
    	String stringy = "";
    	stringy += move.getLine() + DELIMITER 
    				+ move.getColumn() + DELIMITER 
    				+ (move.getCircle() + 1);
    	if (numberPlayers != 4) {
    		if (move.getColor() == clientPlayer.getColor()[0]) {
    			stringy += DELIMITER + PRIMARY;
    		} else {
    			stringy += DELIMITER + SECONDARY;
    		}
    	}
    	return stringy;
    }
    
    /**
     * this function converts a Protocol string into a move.
     * @param words
     * @return
     */
    public Move stringTomove(String[] words) {
    	int line, column, circlesize;
    	Color color;
    	line = Integer.valueOf(words[1]);
    	column = Integer.valueOf(words[2]);
    	circlesize = Integer.valueOf(words[4]) - 1;
    	color = playerColors.get(words[3] + PRIMARY);
    	if (words.length == 6) {
    		color = playerColors.get(words[3] + words[5]);
    	}
    	int[] coordinates = {circlesize, line, column};
    	
    	return new Move(coordinates, color);    	
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
    		numberPlayers = 2;
			//Creates Hashmap with name of the player + SECONDARY/PRIMARY color
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[1] + SECONDARY, Color.PURPLE);
			playerColors.put(words[2] + PRIMARY, Color.YELLOW);
			playerColors.put(words[2] + SECONDARY, Color.GREEN);
			
			//Also creates local player so we can keep track of the pieces and show them
			if (this.nature.equals(HUMAN_PLAYER)) {
				this.clientPlayer = new HumanPalyer(numberPlayers, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			} else {
				this.clientPlayer = new ComputerPlayer(numberPlayers,
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			}
		} else if (words.length == 4) {
			numberPlayers = 3;
			//Creates Hashmap with the first three color being unique to a player
			// then puts the same SECONDARY color to all the players
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[2] + PRIMARY, Color.PURPLE);
			playerColors.put(words[3] + PRIMARY, Color.YELLOW);
			playerColors.put(words[1] + SECONDARY, Color.BLUE);
			playerColors.put(words[2] + SECONDARY, Color.PURPLE);
			playerColors.put(words[3] + SECONDARY, Color.YELLOW);
			//Also creates local player so we can keep track of the pieces and show them
			if (this.nature.equals(HUMAN_PLAYER)) {
				this.clientPlayer = new HumanPalyer(numberPlayers, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			} else {
				this.clientPlayer = new ComputerPlayer(numberPlayers,
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name);
			}
				
		} else if (words.length == 5) {
			numberPlayers = 4;
			//this just gives a color to everyone
			playerColors.put(words[1], Color.BLUE);
			playerColors.put(words[2], Color.PURPLE);
			playerColors.put(words[3], Color.YELLOW);
			playerColors.put(words[4], Color.GREEN);
			//Also creates local player so we can keep track of the pieces and show them
			if (this.nature.equals(HUMAN_PLAYER)) {
				this.clientPlayer = new HumanPalyer(playerColors.get(name + PRIMARY), name);
			} else {
				this.clientPlayer = new ComputerPlayer(playerColors.get(name + PRIMARY), name);
			}
		}
	} 
    	

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Closes the connection, the sockets will be terminated.
     */
    public void shutDown() {
    	try {
    		//System.out.println("'his bin shot");
    		//System.out.println(Thread.currentThread().getStackTrace());
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
