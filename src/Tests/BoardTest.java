package Tests;

import org.junit.Before;
import org.junit.Test;

import Ringz.Board;
import Ringz.Color;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.Assert;

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
		for (int x = 0 ; x < Board.DIM;x++) {
			for (int y = 0; y < Board.DIM;y++){
				for (int pieces = 1; pieces <Board.DIFFPIECES;pieces++) {
					if(x==2&&y==2) {
						System.out.println(bord.getRing(x, y, pieces));
						Assert.assertFalse(bord.getRing(x, y, pieces).equals(Color.EMPTY));
					}else {
						assertEquals(bord.getRing(x,y,0),Color.EMPTY);
					}
				}
			}
		}
	}
	
	@Test
	public void arrayMaxumumTest() {
		int[] array1 = {1, 2, 3, 4};
		assertEquals(3, bord.arrayMaximum(array1));
		int[] array2 = {1, 4, 3, 4};
		assertEquals(-1, bord.arrayMaximum(array2));
	}
	
	@Test
	public void totalTest() {
//		int[] array1 = {2, 2};
//		bord.placeStart(array1); should be done already in setup
		assertEquals(bord.getRing(2, 2, 1), Color.GREEN); 
		assertEquals(bord.getRing(2, 2, 2), Color.PURPLE); 
		assertEquals(bord.getRing(2, 2, 3), Color.YELLOW); 
		assertEquals(bord.getRing(2, 2, 4), Color.BLUE); 
		System.out.println("1 basic is printed");
		System.out.println(bord.total()[1]);
		assertEquals(1, bord.total()[0]); 
		assertEquals(1, bord.total()[1]); 
		assertEquals(1, bord.total()[2]); 
		assertEquals(1, bord.total()[3]); 
		
	}
	
	@Test
	public void isCompletyEmptyTest() {
		assertTrue(bord.isCompletlyEmpty(0, 0));
		assertTrue(bord.isCompletlyEmpty(4, 4));
		Assert.assertFalse(bord.isCompletlyEmpty(2, 2));
	}
	
	@Test
	public void testHasFriend() {
		assertTrue(bord.hasFriend(1, 1, Color.BLUE));
		Assert.assertFalse(bord.hasFriend(0, 0, Color.BLUE));
	}
	
	

}
