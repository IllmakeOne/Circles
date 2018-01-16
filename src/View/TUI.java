package View;
 import java.util.Scanner;

import Ringz.*;
 
public class TUI implements View {
	
	private static final String SHOW = "show";
	private static final String PIECES = "pieces";
	private static final String MOVE = "move";
	
	
	//@Override
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
		return null;
	}

	@Override
	public void updateDisplay(Board board) {
		for (int i = 0; i < board.DIM; i++) {
			String croth = "";
			for (int j = 0; j < board.DIM; j++) {
				String stringy = "";
				for (int circlesize = 0; circlesize < board.DIFFPIECES; circlesize++) {
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

	/**
	 * this function returns a string of the colors of the player play.
	 * @param play
	 * @return
	 */
	public String getStringColor(Player play) {
		Color[] color = play.getColor();
		String stringy ="";
		if (color[1] != null) {
			stringy += color[0].toString() + "/" + color[1].toString();
		} else {
			stringy += color[0].toString();
		}
		return stringy;
	}

	@Override
	public void showPieces(Player play) {
		int[][] pieces = play.getPieces();
		Color[] color= play.getColor();
		for( int i=0;i<color.length;i++) {
		//	color[0] will result in YEllOW
			System.out.println("you have the following "+color[i]+" pieces:");
			for (int j=0;i<Board.DIFFPIECES;j++) {
				switch (j) {
				case 0:{
					System.out.println(pieces[i][j]+" bases");
				}
				}
				System.out.println(pieces[i][j]);
			}
		}
	}

	@Override
	public void notAbletoPlay() {
		
	}

	



}
