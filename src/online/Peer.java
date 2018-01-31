package online;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Observable;

import players.ComputerPlayer;
import players.HumanPalyer;
import players.Player;
import ringz.Board;
import ringz.Color;
import ringz.Move;
import view.*;


public class Peer extends Observable implements Runnable {
	
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
	public static final String MAKE_MOVE = "mm";
	public static final String MOVE = "mv";
	//------ending game----
	public static final String GAME_ENDED = "ge";
	public static final String PLAYER_DISCONNECTED = "pc";
	
	
	
	
    protected String name;
    protected Socket sock;
    protected BufferedReader in;
    protected BufferedWriter out;
    
   //this is the client's copy of the board, it is only modified with moves comming from the server.
    private Board board;
    
//this is the client's player class which keeps track of his/her pieces and asks for moves and such.
    private Player player;
    
    /* this is the client's user interface, it is a TUI but,
 space is left for an easy implementation of other UIs, as long as they respect the view interface*/
    private View view;
    
    //this keeps track of every's colors.
    private HashMap<String, Color> playerColors;
    
    // the nature is 0 or 1 which is human or computer
    private String nature;
    
    private int numberPlayers;
    private boolean gameinProgress;
    private boolean yourTurn = false;
    
    //the thinking time is basically the level of smartness of the AI if the client has one.
    public int thinkingtime;

 
   
    /**
     * Constructor. creates a peer object based in the given parameters.
     * @param   nameArg name of the Peer-process
     * @param   sockArg Socket of the Peer-process
     */
    /*
    * @requires (playerName != null) && (sockArg != null);
    * @requires playerType.equals("0") || playerType.equals("1");
    * @ensure in != null && out != null && getNature().equals(playerType);
    * @ensure name.equals(playerName) && view != null;
    * @ensure nature.equals(COMPUTER_PLAYER) ==> thinkingtime != 0;
     */
    public Peer(String playerName, String playerType, Socket sockArg) throws IOException {
    	try {
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
    		if (this.nature.equals(COMPUTER_PLAYER)) {
    			thinkingtime = view.timeTothink(); 
    		}
    	} catch (IOException e) {
    		System.out.println("shouldnt be any problem here");
    		System.exit(0);
		}
    } 

    /**
     * Reads strings of the stream of the socket-connection and
     * writes the characters to the default output.
     */
    public void  run() { 
    	try {
    		String message = in.readLine();
    		while (message != null) {
    			//System.out.println(message); was used for testing
    			dealWithMessage(message);  
    			message = in.readLine();
    		}
    		shutDown();
		} catch (SocketException e) {
			shutDown();
			
		} catch (IOException e) {
			System.out.println("Something else went wrong");
			shutDown();
		}
    }

    /**
     * sends the first pack to the server, which contains the request to connect and the name.
     * this function is called in the constructor.
     */
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
    	if (todo != null && !todo.equals("exit")) {
    		sendPackage(todo);
    	} else {
    		shutDown();
    	}
    	
    }
    
    /**
     * sends the @param sendPackage to the server.
     */
    public void sendPackage(String sendPackage) {
    	try {
    		out.write(sendPackage);
    	//	System.out.println(sendPackage); was used for testing
    		out.newLine();
    		out.flush();
    	} catch (IOException e) {
    		shutDown();
    	}
    }

    /**
     * deals with messages comming from the server.
     * @param message
     */
    public void dealWithMessage(String message) {
    	String[] words = message.split(DELIMITER);
    	this.setChanged();
    	switch (words[0]) {
    		case CONNECT: {
    			if (words[1].equals(ACCEPT)) {
        			this.notifyObservers("accepted");
        			lobby();
    			} else {
        			this.notifyObservers("denied");
        			shutDown();
                    System.exit(0);
    			}
    			break;
    		}
    		case JOINED_LOBBY: {
    			this.notifyObservers("lobby");
    			if (gameinProgress == true) { 
    				//System.exit(0);
    				notifyObservers("sdeclined");
    				//lobby();
    				gameinProgress = false;
    			}
    			break;
    		}
    		case ALL_PLAYERS_CONNECTED: {
    			String acceptance = view.acceptGame(words);
    			setNumberPlayers(words);
    			if (acceptance.equals("0")) {
    				gameinProgress = true;
        			this.notifyObservers("gameacc");
    				sendPackage(PLAYER_STATUS + DELIMITER + acceptance);
    				this.createBoard(words);
    			} else {
    				sendPackage(PLAYER_STATUS + DELIMITER + acceptance);
        			this.notifyObservers("gamedeny");
        			gameinProgress = false;
        			lobby();
    			}
    			break;
    		}
    		case GAME_STARTED: {
    			this.notifyObservers("gamestart");
    			break;
    		}
    		case MAKE_MOVE: {
    			makeMove();
    			break;
    		}
    		case MOVE: {
    			notifyObservers("move" + words[3]);
    			if (words[4].equals(STARTING_BASE)) {
    				int[] startCoordinates = {Integer.parseInt(words[1]), //line 
    						Integer.parseInt(words[2]) }; //column
    				board.placeStart(startCoordinates);
    				view.updateDisplay(board);
    			} else {
    				board.addCircle(stringTomove(words));
    				view.updateDisplay(board);
    			}
    			break;
    		}
    		case GAME_ENDED: {
    			view.displayEnd(words);
    			gameinProgress = false;
    			lobby();
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
			sendPackage(moveTostring(player.determineMove(board)));	
			yourTurn = true;
		}
    }
    
    /**
     * this function converts a move into a Protocol correct string.
     * @param move
     * @return
     */
    public String moveTostring(Move move) {
    	Move mov = move;
    	while (!this.board.validMove(
    			mov.getLine(), mov.getColumn(), mov.getColor(), mov.getCircle())) {
    		mov = player.determineMove(board);
    	}
    	String stringy = MOVE + DELIMITER;
    	stringy += mov.getLine() + DELIMITER 
    				+ mov.getColumn() + DELIMITER 
    				+ (mov.getCircle() + 1);
    	if (numberPlayers != 4) {
    		if (move.getColor() == player.getColor()[0]) {
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
    	Move move = new Move(coordinates, color);
    	if (yourTurn == true) {
    		player.decresePiece(move);
    	}
    	yourTurn = false;
    	return move;	
    }
    
    /**
     * this asks the player for the first move, in the case they that they go first.
     * @return
     * view.getStart() will return an array of two numbers,
     *  one is the line and one is the column of the first piece
     */
    public String askFirst() {
    	int[] firstMove;
		firstMove = player.getStart(); 
		String stringy = "";
		stringy += MOVE + DELIMITER + firstMove[0] + DELIMITER + firstMove[1] 
												 + DELIMITER + STARTING_BASE;
		return stringy;
    }
    
    /**
     * this creates the Hashmap with the colors and players,and it creates a player for this socket.
     * @param words
     */
    public void createBoard(String[] words) {
    	
    	this.board = new Board();
    	//adds the view as observer to the board, so the player is informed why a move is not valid.
    	this.board.addObserver(view);
    	
    	if (numberPlayers == 2) {
			//Creates Hashmap with name of the player + SECONDARY/PRIMARY color
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[1] + SECONDARY, Color.PURPLE);
			playerColors.put(words[2] + PRIMARY, Color.YELLOW);
			playerColors.put(words[2] + SECONDARY, Color.GREEN);
			
		   //Also creates local player so we can keep track of the pieces and can be asked for moves
			if (this.nature.equals(HUMAN_PLAYER)) {
				this.player = new HumanPalyer(numberPlayers, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name, this.view);
			} else {
				this.player = new ComputerPlayer(numberPlayers, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name, thinkingtime);
			}
		} else if (numberPlayers == 3) {
			//Creates Hashmap with the first three color being unique to a player
			// then puts the same SECONDARY color to all the players
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[2] + PRIMARY, Color.PURPLE);
			playerColors.put(words[3] + PRIMARY, Color.YELLOW);
			playerColors.put(words[1] + SECONDARY, Color.GREEN);
			playerColors.put(words[2] + SECONDARY, Color.GREEN);
			playerColors.put(words[3] + SECONDARY, Color.GREEN);
			
		   //Also creates local player so we can keep track of the pieces and can be asked for moves
			if (this.nature.equals(HUMAN_PLAYER)) {
				this.player = new HumanPalyer(numberPlayers, 
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name, this.view);
			} else {
				this.player = new ComputerPlayer(numberPlayers,
						playerColors.get(name + PRIMARY),
						playerColors.get(name + SECONDARY), 
						this.name, thinkingtime);
			}
				
		} else if (numberPlayers == 4) {
			//this just gives a color to everyone
			playerColors.put(words[1] + PRIMARY, Color.BLUE);
			playerColors.put(words[2] + PRIMARY, Color.PURPLE);
			playerColors.put(words[3] + PRIMARY, Color.YELLOW);
			playerColors.put(words[4] + PRIMARY, Color.GREEN);
			
		   //Also creates local player so we can keep track of the pieces and can be asked for moves
			if (this.nature.equals(HUMAN_PLAYER)) {
				this.player = new HumanPalyer(playerColors.get(name + PRIMARY), 
						name, this.view);
			} else {
				this.player = new ComputerPlayer(
						playerColors.get(name + PRIMARY), name, thinkingtime);
			}
		}
	} 
    	
    
    /**
     * this function sets the number of players.
     */
	public void setNumberPlayers(String[] words) {
		if (words.length == 3) {
			numberPlayers = 2;
		} else if (words.length == 4) {
			numberPlayers = 3;
		} else {
			numberPlayers = 4;
		}
	}

    
    
    /**
     * Closes the connection, the sockets will be terminated.
     */
    public void shutDown() {
    	try {
    		setChanged();
    		notifyObservers("disco");
			sock.close(); 
		} catch (IOException e) {
			System.err.println("sth wrong in shutdow");
			e.printStackTrace();
		} catch (StackOverflowError e) {
			System.out.println(" StackOverflowError , should be here ");
		}
    }

   /**
    * @return the name of the client.
    */
    public String getName() {
        return name;
    }
    
    /**
     * @return the player type, "0" or "1".
     */
    public String getNature() {
    	return nature;
    }
    
    /**
     * @return the name of the client, this function is for debugging purposes. 
     */
    @Override 
    public String toString() {
    	return name;
    }

}
