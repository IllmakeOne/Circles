package online;


import java.io.IOException;
import java.net.SocketException;

import players.Player;
import ringz.Board;
import ringz.Color;
import ringz.Move;

public class ClientPlayer implements Player {

	private int[][] pieces;
	private Color[] color;
	private ServerPeer sock;
	
	
	public ClientPlayer(Color c, ServerPeer socc) { 
	
		this.sock = socc;
		this.color = new Color[2];
		this.color[0] = c;
		this.pieces = new int[2][5];
		for (int i = 0; i < Board.DIFFPIECES; i++) {
			this.pieces[0][i] = 3;
		}
	}

	
	
	public ClientPlayer(int nrplayers, Color c1, Color c2, ServerPeer socc) {

		this.sock = socc;
		this.color = new Color[2];    	
		this.color[0] = c1;
		this.color[1] = c2;
		this.pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			this.pieces[0][i] = 3;
		}
		if (nrplayers == 2) {
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 3;
			}
			
		} else {
			for (int i = 0; i < 5; i++) {
				this.pieces[1][i] = 1;
			}
		}
	}
	
//	public Move askForMove() {
//		sock.sendPackage(ServerPeer.MAKE_MOVE);
//		Move move = null;
//		try { 
//			String message = sock.getIN().readLine();
//			String[] words = message.split(ServerPeer.DELIMITER);
//			if (words[0].equals(ServerPeer.MOVE)) {
//				move = stringTomove(words);
//			}
//			
//		} catch (IOException e) {
//			System.out.println("Coulnt read move when asked ot make a move");
//			e.printStackTrace();
//		}
//		return move;
//		
//	}

	public Move stringTomove(String[] words) {
		int line, column, cirlcesize;
		Color colorr;
		line = Integer.valueOf(words[1]);
    	column = Integer.valueOf(words[2]);
    	cirlcesize = Integer.valueOf(words[3]) - 1;
    	colorr = this.color[0];
    	if (words.length == 5) {
    		int colorindex = Integer.valueOf(words[4]);
    		colorr = this.color[colorindex];
    	}
    	int[] coordinates = {cirlcesize, line, column};
    	
    	return new Move(coordinates, colorr);    	
	}
	
	public boolean isOutOfPieces() {
		for (int i = 0; i < 5; i++) {
			if (pieces[0][i] != 0) {
				return false;
			}
		}
		for (int i = 0; i < 5; i++) {
			if (pieces[1][i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see players.Player#decresePiece(ringz.Move)
	 * @requires move != null;
	 * @requires move.getColor() == getColor()[0] || move.getColor() == getColor()[1];
	 */
	public void decresePiece(Move move) {
		int colorindex;
		if (color[0] == move.getColor()) {
			colorindex = 0;
		} else {
			colorindex = 1;
		}
		pieces[colorindex][move.getCircle()]--;
	}

	@Override
	public String getName() { 
		return this.sock.getName();
	}

	@Override
	public Color[] getColor() {
		return this.color;
	} 

	@Override
	public int[][] getPieces() {
		return this.pieces;
	}

	public ServerPeer getSocket() {
		return this.sock;
	}

	@Override
	public int[] getStart() {
		return null;
	}

	/**
	 * this function first sends the package "mm" so the client knows it is his/her turn to play.
	 * then it receives an (hopefully protocol) string protocol move, 
	 * convert it into a move and @return it.
	 * if the associated client sends a non-protocol string, it will cancel the game.
	 * 
	 */
	@Override
	public Move determineMove(Board bord) { 
		sock.sendPackage(ServerPeer.MAKE_MOVE);
		Move move = null;
		try { 
			String message = sock.getIN().readLine();
			//System.out.println(message + " read in Determine move");
			String[] words = message.split(ServerPeer.DELIMITER);
			if (words[0].equals(ServerPeer.MOVE)) {
				move = stringTomove(words);
			}
		} catch (SocketException e) {
			sock.shutDown();
		} catch (IOException e) {
			System.out.println("Couldn't read move when asked ot make a move");
			e.printStackTrace();
		} 
		return move;
	}

	@Override
	public String toString() {
		return this.sock.getName();
	}

	
	
}
