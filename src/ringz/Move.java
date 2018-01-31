package ringz;



public class Move {
	
	private Color color;
	private int line;
	private int column;
	private int circle;
	
	/**
	 * creates a move.
	 * @param cord which is of the form : 
	 * [0] circlesieze
	 * [1] line
	 * [2] column
	 * @param c the Color of the move
	 */
	public Move(int[] cord, Color c) { 
		circle = cord[0];
		line = cord[1];
		column = cord[2];
		this.color = c;
	}
	
	/**
	 * return the line.
	 */
	public int getLine() {
		return line;
	} 
	/**
	 * return the column.
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * return the cirlcesize.
	 */
	public int getCircle() {
		return circle;
	}
	/**
	 * return the color .
	 */
	public Color getColor() {
		return this.color;
	} 
	
	public String toString() {
		return "Line " + line +
				" Column  " + column +
				" Circle sieze " + circle +
				" Color: " + color;
	}
}


