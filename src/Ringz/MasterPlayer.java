package Ringz;

import Ringz.Board.Color;

public class MasterPlayer implements Player {

	private Player[] plays;
	private String name;
	
	public MasterPlayer(String name, Color c1, Color c2) {
		this.plays = new Player[2];
		this.name = name;
		this.plays[0] = new HumanPalyer(c1, name);
		this.plays[1] = new HumanPalyer(c2, name);
		
	}
	
	@Override
	public int[] determineMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColor() {
		// TODO Auto-generated method stub
		return null;
	}

}
