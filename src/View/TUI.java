package View;
 import Ringz.*;
public class TUI implements View{

	private static final int[][][] bord = null;

	@Override
	public void updateDisplay(Board board) {
			for (int i = 0; i < 5; i++) {
				String croth = "";
				for (int j = 0; j < 5; j++) {
					String stringy = "";
					for (int circlesize = 0; circlesize < 5; circlesize++) {
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

	@Override
	public void outOfPieces() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Move askMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showPieces() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notAbletoPlay() {
		// TODO Auto-generated method stub
		
	}

}
