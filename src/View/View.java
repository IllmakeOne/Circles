package View;

import Players.Player;
import Ringz.*;

public interface View {
	
	public void updateDisplay(Board board);
	
	public void outOfPieces(String name);
	
	public Move askMove(Player play, Board board);
	
	public int[] getStart();
	
	public void showPieces(Player play);
	
	public void notAbletoPlay(String name);
	
	public String acceptGame(String[] message);
	
	public void displayEnd(String[] words);
	
	public void disconnected(String disconee);
	
	public String whattoDo(String nature);
}
