package view;

import java.util.Observer;

import players.Player;
import ringz.*;

public interface View extends Observer {
	
	
	public void updateDisplay(Board board);
	
	public void outOfPieces();
	
	public Move askMove(Player play, Board board);
	
	public int[] getStart();
	
	public void showPieces(Player play);
	
	public void notAbletoPlay();
	
	public String acceptGame(String[] message);
	
	public void displayEnd(String[] words);
	
	public void disconnected(String disconee);
	
	public String whattoDo(String nature);
	
	public int timeTothink();
	
}
