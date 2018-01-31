package tests;

import org.junit.Before;
import org.junit.Test;

import players.HumanPalyer;
import players.Player;
import ringz.Board;
import ringz.Color;
import ringz.Move;
import view.TUI;
import view.View;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;


public class BoardTest {
	
	private Board bord;

	@Before
	public void setUp() {
		int[] a = {2, 2}; 
		bord = new Board();
		bord.placeStart(a);
		
	}
	
	@Test
	public void initialTest() {
		for (int x = 0; x < Board.DIM; x++) {
			for (int y = 0; y < Board.DIM; y++) { 
				for (int pieces = 1; pieces < Board.DIFFPIECES; pieces++) {
					if (x == 2 && y == 2) {
						System.out.println(bord.getRing(x, y, pieces));
						assertFalse(bord.getRing(x, y, pieces).equals(Color.EMPTY));
					} else {
						assertEquals(bord.getRing(x, y, 0), Color.EMPTY);
					}
				}
			}
		} 
	}
	
	@Test
	public void arrayMaxumimTest2() {
		int[] array1 = {1, 2, 3, 4};
		assertEquals(3, Board.arrayMaximum(array1));
		int[] array2 = {1, 4, 3, 4};
		assertEquals(-1, Board.arrayMaximum(array2));
	}
	@Test
	public void tallyUpTest() {
		//tally up method is not finished.
		assertTrue(bord.addCircle(new Move(new int[] {1, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {2, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {3, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {4, 1, 1}, Color.GREEN)));
		assertEquals(4, bord.tallyUp(1, 1)[3]);
	}
	@Test
	public void totalTest() {
		assertTrue(bord.addCircle(new Move(new int[] {1, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {2, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {3, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {4, 1, 1}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {1, 3, 3}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {2, 3, 3}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {3, 3, 3}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] {1, 4, 4}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] {1, 1, 2}, Color.GREEN)));
		assertEquals(bord.getRing(2, 2, 1), Color.GREEN); 
		assertEquals(bord.getRing(2, 2, 2), Color.PURPLE); 
		assertEquals(bord.getRing(2, 2, 3), Color.YELLOW); 
		assertEquals(bord.getRing(2, 2, 4), Color.BLUE); 
		System.out.println("1 basic is printed \n \n \n \n");
		int[] total = bord.total();
		System.out.println("points: " + total[0]
				+ total[1] + total[2] + total[3]);
		assertEquals(4, bord.total()[3]); 	 
	}
	@Test
	public void testArrayMaximum() {
		assertEquals(0, Board.arrayMaximum(new int[] {3, 1, 1, 1}));
		assertEquals(0, Board.arrayMaximum(new int[] {3, 2, 1, 2}));
		assertEquals(2, Board.arrayMaximum(new int[] {3, 2, 4, 3}));
		assertEquals(1, Board.arrayMaximum(new int[] {3, 5000, 1, 1}));
		assertEquals(-1, Board.arrayMaximum(new int[] {3, 1, 3, 1}));

//		System.out.println(bord.total()[1]);
//		assertEquals(1, bord.total()[0]); 
//		assertEquals(1, bord.total()[1]); 
//		assertEquals(1, bord.total()[2]); 
//		assertEquals(1, bord.total()[3]); 

	}
	
	@Test
	public void isCompletyEmptyTest() {
		assertTrue(bord.isCompletlyEmpty(0, 0));
		assertTrue(bord.isCompletlyEmpty(4, 4));
		assertFalse(bord.isCompletlyEmpty(2, 2));
	}
	
	@Test
	public void testHasFriend() {
		assertTrue(bord.hasFriend(1, 1, Color.BLUE));
		assertFalse(bord.hasFriend(0, 0, Color.BLUE));
	}
	@Test 
	public void testAddCircle() {
		assertTrue(bord.addCircle(new Move(new int[]
		{3, 1, 2}, Color.GREEN)));
		assertEquals(Color.GREEN, bord.getRing(1, 2, 3));
		assertTrue(bord.addCircle(new Move(new int[]
		{1, 1, 1}, Color.BLUE)));
		assertEquals(Color.BLUE, bord.getRing(1, 1, 1));
		assertTrue(bord.addCircle(new Move(new int[] 
		{3, 3, 3}, Color.PURPLE)));
		assertEquals(Color.PURPLE, bord.getRing(3, 3, 3));
	}
	@Test
	public void testGetRingGetPin() {
		assertEquals(Color.GREEN, bord.getRing(2, 2, 1));
		assertNotEquals(Color.YELLOW, bord.getRing(2, 2, 1));
		assertNotEquals(Color.YELLOW, bord.getRing(1, 2, 1));
		Color[] pin = new Color[] {Color.EMPTY,
			Color.GREEN, Color.PURPLE, Color.YELLOW, Color.BLUE};
		assertArrayEquals(bord.getPin(2, 2), pin);
	}
	@Test
	public void testValidMove() {
		assertFalse(bord.addCircle(new Move(new int[]
		{4, 4, 3}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{3, 3, 3}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{3, 3, 2}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{4, 4, 2}, Color.PURPLE)));
		assertFalse(bord.addCircle(new Move(new int[]
		{3, 3, 0}, Color.PURPLE)));
		assertFalse(bord.addCircle(new Move(new int[]
		{3, 3, 3}, Color.PURPLE)));
		assertFalse(bord.addCircle(new Move(new int[]
		{3, 3, 3}, Color.GREEN)));
		//Assert.assertFalse( bord.addCircle( new Move(new int[]
		//{5, 2, 1}, Color.PURPLE)));
		//should the == test be replaced with .equals?
	}
	@Test
	public void testIsStillAbleToPlay() {
		View v = new TUI("tester");
		Player blue = new HumanPalyer(Color.BLUE, "tester", v);
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 1, 1}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 1, 3}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 1, 2}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 2, 1}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 2, 3}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 3, 1}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 3, 2}, Color.PURPLE)));
		assertTrue(bord.addCircle(new Move(new int[] 
		{0, 3, 3}, Color.PURPLE)));
		assertFalse(bord.isStillAbleToPlace(blue));
		System.out.println("something wrong in checking/placing"
				+ " bases, size 3 is able to have one closeby, bases have not");
	}
	@Test
	public void testColorAndIntIndex() {
		assertEquals(0, bord.colorIndex(Color.BLUE));
		assertEquals(1, bord.colorIndex(Color.PURPLE));
		assertEquals(2, bord.colorIndex(Color.YELLOW));
		assertEquals(3, bord.colorIndex(Color.GREEN));
		assertEquals(-1, bord.colorIndex(Color.EMPTY));
		
		assertEquals(Color.BLUE, bord.intIndex(0));
		assertEquals(Color.PURPLE, bord.intIndex(1));
		assertEquals(Color.YELLOW, bord.intIndex(2));
		assertEquals(Color.GREEN, bord.intIndex(3));
		assertEquals(Color.EMPTY, bord.intIndex(40));
	}
	@Test
	public void testGetPossibleMoves() {
		int[][] pieces = new int[5][5];
		Color[] c = new Color[2];
		c[0]  = Color.GREEN;
		for (int i = 0; i < 5; i++) {
			pieces[0][i] = 0;
		}
		assertEquals(0, bord.getPossibleMoves(c, pieces).size());
		pieces[0][3] = 1;
		assertEquals(8, bord.getPossibleMoves(c, pieces).size());
		
		ArrayList<Move> moves = bord.getPossibleMoves(c, pieces);
		Move o = new Move(bord.createArray(1, 1, 3), Color.GREEN);
		Move o2 = new Move(bord.createArray(3, 3, 2), Color.GREEN);
		Move o6 = new Move(bord.createArray(4, 4, 3), Color.GREEN);
	

		assertTrue(moves.toString().contains(o.toString()));
		assertFalse(moves.toString().contains(o2.toString()));
		assertFalse(moves.toString().contains(o6.toString()));
		
		assertTrue(bord.addCircle(new Move(new int[] 
		{3, 3, 2}, Color.PURPLE)));
		moves = bord.getPossibleMoves(c, pieces);
		Move o3 = new Move(bord.createArray(3, 3, 2), Color.GREEN);
		Move o4 = new Move(bord.createArray(3, 3, 1), Color.GREEN);
		pieces[0][0] = 1;
		Move o5 = new Move(bord.createArray(3, 3, 0), Color.GREEN);
		Move o7 = new Move(bord.createArray(2, 3, 0), Color.GREEN);
		assertFalse(moves.toString().contains(o3.toString()));
		assertFalse(moves.toString().contains(o5.toString()));
		assertFalse(moves.toString().contains(o4.toString()));
		moves = bord.getPossibleMoves(c, pieces);
		assertTrue(moves.toString().contains(o7.toString()));
		
		pieces[0][2] = 3;
		pieces[0][0] = 0;
		assertEquals(15, bord.getPossibleMoves(c, pieces).size());
		pieces[0][0] = 1;
		pieces[0][1] = 2;
		assertEquals(30, bord.getPossibleMoves(c, pieces).size());
	}
	@Test
	public void testDeepCopy() {
		//nullpointer exception, probably in initiation of copy in method.
		Board copy = bord.deepCopy();
		assertNotEquals(bord.emptyBoard(), copy);
		for (int x = 0; x < Board.DIM; x++) {
			for (int y = 0; y < Board.DIM; y++) {
				for (int piece = 0; piece < Board.DIFFPIECES; piece++) {
					assertEquals(bord.getRing(x, y, piece), copy.getRing(x, y, piece));
				}	
			}
		}
	}
	@Test
	public void testHasField() {
		assertFalse(bord.hasField(1, 1, Color.PURPLE));
		
		assertTrue(bord.addCircle(new Move(new int[] 
			{0, 1, 1}, Color.PURPLE)));
		assertTrue(bord.hasField(1, 1, Color.PURPLE));
		
		assertTrue(bord.addCircle(new Move(new int[] //only 1 circle should be purple's
			{3, 2, 1}, Color.PURPLE)));
		assertTrue(bord.hasField(2, 1, Color.PURPLE));
		
		assertTrue(bord.addCircle(new Move(new int[] 
			{4, 2, 1}, Color.YELLOW)));
		assertFalse(bord.hasField(2, 1, Color.PURPLE));
		
		assertTrue(bord.addCircle(new Move(new int[] 
			{2, 2, 1}, Color.PURPLE)));
		assertTrue(bord.hasField(2, 1, Color.PURPLE));
		
		assertTrue(bord.addCircle(new Move(new int[] 
			{1, 2, 1}, Color.GREEN)));
		assertTrue(bord.hasField(2, 1, Color.PURPLE));
		
		assertTrue(bord.addCircle(new Move(new int[] 
			{4, 4, 4}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] 
			{3, 4, 4}, Color.GREEN)));
		assertTrue(bord.addCircle(new Move(new int[] 
			{2, 4, 4}, Color.YELLOW)));
		assertTrue(bord.addCircle(new Move(new int[] 
			{1, 4, 4}, Color.YELLOW)));
		assertFalse(bord.hasField(4, 4, Color.GREEN));
		assertFalse(bord.hasField(4, 4, Color.PURPLE));
		
	}
	@Test
	public void testFieldHas() {
		assertFalse(bord.fieldHas(1, 1, Color.PURPLE));
		assertTrue(bord.addCircle(new Move(new int[] 
		{3, 1, 1}, Color.PURPLE)));
		assertTrue(bord.fieldHas(1, 1, Color.PURPLE));	
	}
	@Test
	public void testIsFull() {
		for (int x = 0; x < Board.DIM; x++) {
			for (int y = 0; y < Board.DIM; y++) {	
				bord.addCircle(new Move(bord.createArray(0, x, y),
								Color.GREEN));	
			}	
		}
		assertTrue(bord.isFull());
	}

	@Test
	public void testEmptyBoard() {
		Board board = new Board();
		assertTrue(board.emptyBoard());
		assertTrue(!bord.emptyBoard());
	}
	

}

