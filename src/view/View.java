package view;

import java.util.Observer;

import players.Player;
import ringz.*;

public interface View extends Observer {
	
	/**
	 * this displays the board.
	 * @param board 
	 */
	/* @requires board != null */
	public /*pure*/ void updateDisplay(Board board);
	
	/**
	 * this informs the player that he/she has ran out of pieces.
	 */
	public /*pure*/ void outOfPieces();
	
	/**
	 * this asks the user to make a move.
	 * @param play
	 * @param board
	 * @return
	 */
	/* @requires play.isOutOfPieces() == false; 
	 * @requires board.isFull() == false;
	 * @requires board.isStillAbleToPlace(play) != false;
	 */
	public /*pure*/ Move askMove(Player play, Board board);
	
	/**
	 * this function asks the player to give the starting coodinates.
	 */
	public /*pure*/ int[] getStart();
	
	/**
	 * this function displays the pieces of the player @param play.
	 */
	public /*pure*/ void showPieces(Player play);
	
	/**
	 * this function prints out a message saying that the client.
	 *  who's at the console can not place pieces.
	 * 
	 */
	public /*pure*/ void notAbletoPlay();
	
	/**
	 * this function asks the player if they want to join a game.
	 * @param message is an string array which contains the opponents.
	 * @return "0" for accept or "1" for decline.
	 */
	public /*pure*/ String acceptGame(String[] message);
	
	/**
	 * this displays the end result of the game.
	 * @param words
	 */
	/* @requires words.lenght > 5 */ 
	public /*pure*/ void displayEnd(String[] words);
	
	/**
	 * this informs the client that someone disconected from the game.
	 * and that the game has been canceled.
	 * @param disconee is the player who disconected
	 */
	public /*pure*/ void disconnected(String disconee);
	
	/**
	 * this asks the player for purpose. 
	 * The player can choose to request a game or quit. (maybe will implment security)
	 * @param nature ; the nature of the player (AI or human), 
	 * this is for sending the request game package.
	 * @return the message that is going to be send
	 */
	public /*pure*/ String whattoDo(String nature);
	
	/**
	 * this asks the player (if they have an AI) how much time it will have to think.
	 * @return
	 */
	public /*pure*/ int timeTothink();
	
	
	/**
	 * this function displays a hint, which a possible move he/she could make.
	 * @param play
	 * @param board
	 */
	public /*pure*/ void showHint(Player play, Board board);
	
}
