package ringz;



public class Move {
	
	private Color color;
	private int line;
	private int column;
	private int circle;
	
	public Move(int[] cord, Color c) {
		circle = cord[0];
		line = cord[1];
		column = cord[2];
		this.color = c;
	}
	
	public int getLine() {
		return line;
	} 
	
	public int getColumn() {
		return column;
	}
	
	public int getCircle() {
		return circle;
	}
	
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


