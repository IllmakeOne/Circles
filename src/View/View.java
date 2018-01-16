package View;

import Ringz.*;

public interface View {
	public void updateDisplay(Board board);
	public void outOfPieces();
	public Move askMove(Player play, Board board);
	public void showPieces(Player play);
	public void notAbletoPlay();
}
