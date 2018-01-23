package view;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;


import players.HumanPalyer;
import players.Player;
import ringz.*;
 
public class TUI implements Observer, View {
	
	private static final String SHOW = "show";
	private static final String PIECES = "pieces";
	private static final String MOVE = "move";
	
	private String name;
	
	
	public TUI(String playername) {
		this.name = playername;
	}
	
	
    public String acceptGame(String[] message) {
    	System.out.println("A game has been found");
    	System.out.println("You would play against:");
    	for (int i = 1; i < message.length; i++) {
    		if (!message[i].equals(this.name)) {
    			System.out.println(i + ".  " + message[i]);
    		}
    	}
    	System.out.println("Do you wish to accept Accept or Decline");
    	String input = readString("][< ");
    	int flag = 0;
		while (flag == 0) {
			if (input.equals("Accept")) {
				flag = 1;
			} else if (input.equals("Decline")) {
				flag = 1;
			} else {
				System.out.println("Please give valid input");
				input = readString("[]>");
			}
		}
    	
    	if (input.equals("Accept")) {
    		return "0";
    	} else {
    		return "1";
    	}
    	
    }
	
	@Override
	public Move askMove(Player play, Board board) {
		Scanner in = new Scanner(System.in);
		System.out.println("What would you like to do?" + 
				" \n 'show' to display the board again" +
				" \n 'pieces' to show your pieces " +
				" \n 'move' to make a move");
		int flag = 1;
		String imput;
		while (flag != 0) {
			imput = in.nextLine();
			if (imput.equals(SHOW)) {
				updateDisplay(board);
			} else if (imput.equals(PIECES)) {
				showPieces(play);
			} else if (imput.equals(MOVE)) {
				flag = 0;
				System.out.println(" You will make a move now " +
						" \n You have the colors " + getStringColor(play));
			}
		}
		int[] move = new int[3];
		flag = 1;
		int colorIndex = 0;
		if (play.getColor()[1] != null) {
			Color c = this.chooseColor(play.getColor()[0], play.getColor()[1]);
			if (c == play.getColor()[1]) {
				colorIndex = 1;
			}
			readMove(move, colorIndex, play);
		} else {
			readMove(move, colorIndex, play);
		}
		
		((HumanPalyer) play).decresePiece(colorIndex, move[0]);
		in.close();
		return new Move(move, play.getColor()[colorIndex]);
	}
	
	

	@Override
	public void updateDisplay(Board board) {
		for (int i = 0; i < Board.DIM; i++) {
			String croth = "";
			for (int j = 0; j < Board.DIM; j++) {
				String stringy = "";
				for (int circlesize = 0; circlesize < Board.DIFFPIECES; circlesize++) {
					switch (board.getRing(j, i, circlesize)) {
						case EMPTY: {
							stringy += "E";
							break;
						}
						case BLUE: {
							stringy += "B";
							break;
						}
						case YELLOW: {
							stringy += "Y";
							break;
						}
						case PURPLE: {
							stringy += "P";
							break;
						}
						case GREEN: {
							stringy += "G";
							break;
						}
					}
				}
				croth += stringy + "    ";
			}
			System.out.println(croth);
		}
			
		
		
	}

	

	/**
	 * this function returns a string of the colors of the player play.
	 * @param play
	 * @return
	 */
	public String getStringColor(Player play) {
		Color[] color = play.getColor();
		String stringy = "";
		if (color[1] != null) {
			stringy += color[0].toString() + "/" + color[1].toString();
		} else {
			stringy += color[0].toString();
		}
		return stringy;
	}
	
	
	
	@Override
	public void outOfPieces() {
		System.out.println(getName() + " ,you are out of pieces");
		
	}
	public String getName() {
		return this.name;
	}
	


	@Override
	public void showPieces(Player play) {
		int[][] pieces = play.getPieces();
		Color[] color = play.getColor(); 
		int colorcount = 2;
		if (color[1] == null) {
			colorcount = 1;
		}
		for (int i = 0; i < colorcount; i++) {
			System.out.println("you have the following " + color[i] + " pieces:");
			for (int j = 0; j < Board.DIFFPIECES; j++) {
				switch (j) {
					case 0: {
						System.out.println(pieces[i][j] + " bases");
						break;
					} 
					case 1: {
						System.out.println(pieces[i][j] + " dots");
						break;
					} 
					case 2: {
						System.out.println(pieces[i][j] + " small rings");
						break;
					} 
					case 3: {
						System.out.println(pieces[i][j] + " medium rings");
						break;
					} 
					case 4: {
						System.out.println(pieces[i][j] + " large rings");
						break;
					} 
				}
			}
		}
	}

	
	@Override
	public void notAbletoPlay() {
		System.out.println(getName() + ",you are not able to place any pieces");
	}

	public void disconnected(String disconee) {
		System.out.println(disconee + " has disconnected, game canceled");
	}
	
	/**
	 * this function displays the end score.
	 */
	public void displayEnd(String[] words) {
		for (int i = 1; i < words.length; i += 2) {
			System.out.println("Player " + words[i] + " has " 
						+ words[i + 1] + " points");
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1.equals("notCompEmpty")) {
			System.out.println("Cant put base, it is not completly empty \n");
			
		} else if (arg1.equals("notEmpty")) {
			System.out.println("Cant put piece there, field not empty \n");
			
		} else if (arg1.equals("noFriend")) {
			System.out.println("Cant put piece there, it has no friends around it \n");
		
		} else if (arg1.equals("hasBase")) {
			System.out.println("cant put piece there, Pin has a base \n");
		
		} else if (arg1.equals("added")) {
			System.out.println("Valid move, piece added \n");
		
		} else if (arg1.equals("lobby")) {
			System.out.println("You are in the lobby waiting");
		
		} else if (arg1.equals("accepted")) {
			System.out.println("You have veen accepted, you ahve been connected");
		
		} else if (arg1.equals("denied")) {
			System.out.println("Thee not worthy (some else has the username)");
		
		} else if (arg1.equals("gameacc")) {
			System.out.println("You have accepted the game");
		
		} else if (arg1.equals("gamedeny")) {
			System.out.println("You have denied the game");
		
		} else if (arg1.equals("gamestart")) {
			System.out.println("The game has started");
			
		} else if (arg1.equals("joined")) {
			System.out.println("Hello, what would you like to do?");
			
		} else if (arg1.equals("sdeclined")) {
			System.out.println("Someone decline the game");
			
		} else if (arg1.equals("first")) {
			System.out.println("You are going first");
		}
	}
	
	
	/**
	 * this functions determine what the clients wants to do while he/she is in the lobby.
	 * 
	 */
	public String whattoDo(String nature) {
		String stringy = "";
		System.out.println("For now just look for a game , type Request game\n");
		String input = readString("[]>");
		int flag = 0;
		while (flag == 0) {
			if (input.equals("Request game")) {
				flag = 1;
				String[] end = requestGame();
				stringy += "gr" + ";" + end[0] + ";" + nature;
				if (end[1] != null) {
					stringy += ";" + end[1];
				}
			} else if (input.equals("exit")) {
				flag = 1;
				stringy = input;
			} else {
				System.out.println("Please give valid input");
				input = readString("[]>");
			}
		}
		
		return stringy;
	}
	
//	/**
//	 * this function display the oponents.
//	 */
//	public void showOponents(String[] oponents) {
//		for (int i = 1; i < oponents.length; i++) {
//			System.out.println(i + ". " + oponents[i]);
//		}
//	}
	
	/**
	 * if the clients decides to request a game this will determine which type of game they want.
	 * @return
	 */
	public String[] requestGame() {
		
		String[] end = new String[2];
		System.out.println("What type of gmae, 2 , 3, 4 players?");
		String input = readString("[]>");
		int flag = 0;
		while (flag == 0) {
			if (input.equals("2") || input.equals("3") || input.equals("4")) {
				flag = 1;
			} else {
				System.out.println("Please give valid number of players");
				input = readString("[]>");
			}
		}
		
		System.out.println("So you want to play a game of " + input);
		end[0] = input;
		
		System.out.println("Do you want to play against anything specific? (yes/no)");
		input = readString("[]>");
		flag = 0;
		int flag2 = 0;
		while (flag == 0) {
			if (input.equals("yes")) {
				flag = 1;
				System.out.println("Agains human(0) or computer(1)");
				input = readString("[]>");
				while (flag2 == 0) {
					if (input.equals("0") || input.equals("1")) {
						flag2 = 1;

						end[1] = input;
					} else {
						System.out.println("0 or 1 please");
						input = readString("[]>");
					}
				}
			} else if (input.equals("no")) {
				flag = 1;
			} else {
				System.out.println("yes or no please");
				input = readString("[]>");
			}
		}
		return end;
	}
	
	//------------------Stuff to help with the reading of a move ----------------
	
		/**
		 * reads form user three numbers, cirlce zize, lien and column.
		 * @param move is the arraty int that remebrts the user's input
		 * @param colorIndex is the index of the color of the player
		 */
	public void readMove(int[] move, int colorIndex, Player play) {
		Scanner in = new Scanner(System.in);
		int flag = 1;
		String[] words;
		while (flag == 1) {
			System.out.println("Please give move \n"
					+ "The first number is the size of the piece, \n"
					+ "The second number the line and the thirds , the column \n");
			String line = in.nextLine();
			words = line.split(" ");
			if (words.length == 3) {
				for (int i = 0; i < words.length; i++) {
					move[i] = Integer.parseInt(words[i]) - 1;
				}
				if (this.isValid(move, colorIndex, play)) {
					flag = 0;
				}
			}
			in = new Scanner(System.in);
		}
		in.close();
	}
	
	/**
	 * a fcntion that tests if the given parametre, a int arraty, is valid as a move.
	 * this fucntion is only used in determineMove as a cruthch.
	 * @param x
	 * @return
	 */
	public boolean isValid(int[] x, int colorIndex, Player play) {
		if (x[0] > 4 || x[0] < 0) {
			System.out.println("No such piece");
			return false;
		}
			
		if (x[1] > 4 || x[1] < 0) {
			System.out.println("line too big");
			return false;
		}
		if (x[2] > 4 || x[2] < 0) {
			System.out.println("Column too big");
			return false;
		}
		if (play.getPieces()[colorIndex][x[0]] == 0) {
			System.out.println("Youare aout of these pieces");
			return false;
		}
		
		return true;
	}
	
	/**
	 * determine which color to place.
	 */
	public Color chooseColor(Color c1, Color c2) {
		Color choce = null;
		int flag = 1;
		Scanner in = new Scanner(System.in);
		while (flag == 1) {
			System.out.println("Please choose one of these colors "
					+ c1 + " and " + c2);
			String line = in.nextLine();
			if (line.equals(c1.toString())) {
				choce = c1;
				flag = 0;
			} else {
				if (line.equals(c2.toString())) {
					choce = c2;
					flag = 0;
				} else {
					System.out.println("Invalid color choice");
				}
			}
			
			in = new Scanner(System.in);
		}
		in.close();
		return choce;
	}
	
    /** 
     * reads a string from the console.
     * @param prompt
     * @return
     */
    static public String readString(String prompt) {
    	
        System.out.print(prompt);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                System.in));
        String input = null;
        try {
            input = in.readLine();
        } catch (IOException e) {
        }

        return (input == null) ? "" : input;
    }


    
/**
 * determine where the first multicolored piece will be placed.
 */
    public int[] getStart() {
    	int[] move = new int[2];
    	Scanner in = new Scanner(System.in);
    	String[] words;
    	int flag = 1;
    	while (flag == 1) {
    		System.out.println("Please the coordinate of the first piece \n"
    				+ "( must be in the inneer circle \n"
    				+ " first lien then column, both msu tbe betwen 1 and 3 \n");
    		String line = in.nextLine();
    		words = line.split(" ");
    		if (words.length == 2) {
    			for (int i = 0; i < words.length; i++) {
    				move[i] = Integer.parseInt(words[i]);
    			}
    			if (move[0] < 4 && move[0] > 0 && move[1] > 0 && move[1] < 4) {
    				flag = 0;
    			} else {
    				System.out.println("Must be between 1 and 3");
    			}
    		}
    		in = new Scanner(System.in);
    	}
    	in.close();
    	return move;
    }



}


