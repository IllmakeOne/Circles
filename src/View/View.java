package View;

import Ringz.*;

public interface View {
	public void updateDisplay(Board board);
	public void outOfPieces(String name);
	public Move askMove(Player play, Board board);
	public void showPieces(Player play);
	public void notAbletoPlay(String name);
}
