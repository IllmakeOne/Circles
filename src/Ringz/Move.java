package Ringz;

import java.util.Scanner;

import Ringz.Board.Color;

public class Move {
	
	private Color color;
	private int line;
	private int column;
	private int circle;
	
	public Move(int[] cord, Color c) {
		line = cord[1];
		column = cord[2];
		circle = cord[0];
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

}


