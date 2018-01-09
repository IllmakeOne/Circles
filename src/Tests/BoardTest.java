package Tests;

import org.junit.Before;
import org.junit.Test;

import Ringz.Board;
import Ringz.Color;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {
	
	private Board bord;

	@Before
	public void setUp() {
		bord = new Board();
	}
	@Test
	public void initialTest() {
		for (int x = 0 ; x < Board.DIM;x++) {
			for (int y = 0; y < Board.DIM;y++) {
				assertEquals(bord.getField(x,0,0),Color.EMPTY);	
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
		int[] array1 = {2,2};
		bord.placeStart(array1);
		
	}

}
