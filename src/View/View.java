package View;

import Ringz.*;

public interface View {
	public void updateDisplay(Board board);
	public void outOfPieces();
	public Move askMove();
	public void showPieces();
	public void notAbletoPlay();
}
