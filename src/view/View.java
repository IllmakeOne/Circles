package view;

import players.Player;
import ringz.*;

public interface View {
	
	public void updateDisplay(Board board);
	
<<<<<<< HEAD
	public void outOfPieces(String name);
	
	public Move askMove(Player play, Board board);
	
	public int[] getStart();
	
	public void showPieces(Player play);
	
	public void notAbletoPlay(String name);
=======
	public void outOfPieces();
	
	public Move askMove(Player play, Board board);
	
	public int[] getStart();
	
	public void showPieces(Player play);
	
	public void notAbletoPlay();
>>>>>>> branch 'master' of https://git.snt.utwente.nl/s1942727/Circles.git
	
	public String acceptGame(String[] message);
	
	public void displayEnd(String[] words);
	
	public void disconnected(String disconee);
	
	public String whattoDo(String nature);
}
