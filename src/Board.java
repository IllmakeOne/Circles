
public class Board {
	
	private Color[][][] borg;
	
	
	/**
	 * creati empty board.
	 * 0 menas empty
	 */
	enum Color {RED,BLUE,YELLOW,EMPTY};
	
	
	/**
	 * initialize the  oard wiht EMptuy fields.
	 */
	public Board() {
		this.borg = new Color[5][5][5];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				for (int k = 0; k < 6; k++) {
					borg[i][j][k] = Color.EMPTY;
				}
			}
		}
	}
	
	
	/**
	 * tests if a field of the board has a certain colored ring .
	 * @param x is the line
	 * @param y is the colum
	 * @param col is the color being looked for
	 * @return true if the field has the color
	 */
	public boolean fieldHas(int x, int y, Color col) {
		for (int i = 0; i < 6; i++) {
			if (this.borg[x][y][i] == col) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * tests if the move is valid.
	 * @param x the line
	 * @param y the column
	 * @param col the color being tested
	 * @return true if the move is valid
	 */
	public boolean validMove(int x, int y, Color col, int circleSize) {
		int[] handy = {-1, 0, 1};
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (fieldHas(x + i, y + i, col)) {
					if (borg[x][y][circleSize] == Color.EMPTY) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * ads a cricle if it is valid. 
	 * @param circleSeize the seize of the circle
	 * @param col the color of the circle
	 * @param x the line
	 * @param y the column
	 */
	public void addCircle(int circleSeize, Color col, int x, int y) {
		if (this.validMove(x, y, col, circleSeize)) {
			this.borg[x][y][circleSeize] = col;
		}		
	}
	
	/**
	 * return the epcific field 
	 * @param x line
	 * @param y column
	 * @param circleSeize hight
	 * @return
	 */
	public Color getField(int x, int y, int circleSeize) {
		return this.borg[x][y][circleSeize];
	}
	
	/**
	 * gives all the cirlces in a board coordinate
	 * @param x line
	 * @param y column 
	 * @return a vector of Color 
	 */
	public Color[] getFields(int x, int y) {
		Color[] life = new Color[5];
		for (int i = 0; i < 5; i++) {
			life[i] = this.getField(x, y, i);
		}
		return life;
	}
	
	public boolean isFull() {
		this.borg = new Color[5][5][5];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				for (int k = 0; k < 6; k++) {
					if (getField(i, j, k) == Color.EMPTY) {
						return false;
					}
				}
			}
		}
		return true;
		
	}
	
	
	public static void main(String[] args) {
		Board bord = new Board();
		bord.addCircle(3, Color.BLUE, 0, 1);
	}
	
	
	
	
}
