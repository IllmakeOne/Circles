package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ringz.Color;
import ringz.Move;

class MoveTest {
	private Move mastermove;
	
	@BeforeEach
	void setUp() throws Exception {
		mastermove = new Move(new int[] {2, 3, 1}, Color.BLUE);
	}

	@Test
	void test() {
		assertEquals(3, mastermove.getLine());
		assertEquals(1, mastermove.getColumn());
		assertEquals(2, mastermove.getCircle());
		assertEquals(Color.BLUE, mastermove.getColor());
		
		assertEquals(mastermove.toString(), "Line 3 Column  1 Circle sieze 2 Color: BLUE");
	}

}
