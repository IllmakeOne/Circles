package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Players.HumanPalyer;
import Players.Player;
import Ringz.Board;
import Ringz.Color;

class HumanPlayerTest {
	private HumanPalyer playervs1;
	private HumanPalyer playervs2;
	private HumanPalyer playervs3;
	@BeforeEach
	void setUp() throws Exception {
		playervs3 = new HumanPalyer(Color.YELLOW, "the best playa ewaa");
		playervs2 = new HumanPalyer(3, Color.GREEN, Color.YELLOW, "the best playa ewaa");
		playervs1 = new HumanPalyer(2, Color.GREEN, Color.YELLOW, "the best playa ewaa");
	}

	@Test
	void testsetUpVS3() {
		int[][] pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			pieces[0][i] = 3;
			assertEquals(pieces[0][i],playervs3.getPieces()[0][i]);
		}
		assertEquals("YELLOW", playervs3.getColor()[0].toString());
		assertEquals("the best playa ewaa", playervs3.getName());	
	}
	@Test
	void testSetUpVS2() {
		int[][] pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			pieces[0][i] = 3;
			pieces[1][i] = 1;
			assertEquals(pieces[0][i],playervs2.getPieces()[0][i]);
			assertEquals(pieces[1][i],playervs2.getPieces()[1][i]);
		}
		assertEquals("GREEN, YELLOW", playervs2.getColor()[0].toString() +
				", " + playervs2.getColor()[1].toString());
		assertEquals("the best playa ewaa", playervs2.getName());
	}
	@Test
	void testSetUpVS1() {
		int[][] pieces = new int[2][5];
		for (int i = 0; i < 5; i++) {
			pieces[0][i] = 3;
			pieces[1][i] = 3;
			assertEquals(pieces[0][i],playervs1.getPieces()[0][i]);
			assertEquals(pieces[1][i],playervs1.getPieces()[1][i]);
		}
		assertEquals("GREEN, YELLOW", playervs1.getColor()[0].toString() +
				", " + playervs1.getColor()[1].toString());
		assertEquals("the best playa ewaa", playervs1.getName());
	}
	@Test
	void testOutOfPiecesVS3() {
		for (int i = 0; i < Board.DIFFPIECES; i++) {
			for (int j = 0; j < 3; j++) {
				playervs3.decresePiece(0, i);
			}
		}
		assertTrue(playervs3.isOutOfPieces());
	}
	@Test
	void testOutOfPiecesVS2() {
		for (int i = 0; i < Board.DIFFPIECES; i++) {
			for (int j = 0; j < 3; j++) {
				playervs2.decresePiece(0, i);
			}
			playervs2.decresePiece(1, i);
		}
		assertTrue(playervs2.isOutOfPieces());
	}
	@Test
	void testOutOfPiecesVS1() {
		for (int i = 0; i < Board.DIFFPIECES; i++) {
			for (int j = 0; j < 3; j++) {
				playervs1.decresePiece(0, i);
				playervs1.decresePiece(1, i);
			}
		}
		assertTrue(playervs1.isOutOfPieces());
	}
	@Test
	void testDecreasePiece() {
		playervs3.decresePiece(0, 0);
		assertEquals(playervs3.getPieces()[0][0],2);
		playervs3.decresePiece(0, 0);
		assertEquals(playervs3.getPieces()[0][0],1);
		playervs3.decresePiece(0, 0);
		assertEquals(playervs3.getPieces()[0][0],0);
		playervs3.decresePiece(0, 3);
		assertEquals(playervs3.getPieces()[0][3],2);
		
		playervs2.decresePiece(1, 3);
		assertEquals(playervs2.getPieces()[1][3],0);
	}
	@Test
	void TestDetermineMove() {
		assertEquals(1,1);
		//not needed
		}
}
