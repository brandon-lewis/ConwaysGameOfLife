package com.lewis.brandon.conway;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LifeGameTest {
	ConwaysGameOfLife gameOfLife;
	private static final int BOARD_WIDTH = 8, BOARD_HEIGHT = 6;
	
	@Before
	public void beforeHook() {
		gameOfLife = new ConwaysGameOfLife(BOARD_WIDTH, BOARD_HEIGHT);
	}

	// ####################################################
	//		CONSTRUCTOR TESTS
	// ####################################################
	@Test(expected=IllegalArgumentException.class)
	public void testBoardWidthTooSmallConstructor() {
		gameOfLife = new ConwaysGameOfLife(-5, 5);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBoardHeightTooSmallConstructor() {
		gameOfLife = new ConwaysGameOfLife(5, -5);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testBoardWidthAndHeightTooSmallConstructor() {
		gameOfLife = new ConwaysGameOfLife(-5, -5);
	}

	@Test
	public void testCellsAreInitiallyDead() {
		for(boolean[] ba : gameOfLife.getMatrix()) {
			for(boolean cell : ba) { assertFalse(cell); }
		}
	}

	@Test
	public void testGetBoardWidth() {
		assertEquals(gameOfLife.getBoardWidth(), BOARD_WIDTH);
	}

	@Test
	public void testGetBoardHeight() {
		assertEquals(gameOfLife.getBoardHeight(), BOARD_HEIGHT);
	}

	@Test
	public void testGetBoardWidthAfterConstructingDifferentSizeBoard() {
		int newHeight = 15;
		gameOfLife = new ConwaysGameOfLife(gameOfLife.getBoardWidth(), newHeight);
		assertEquals(gameOfLife.getBoardHeight(), newHeight);
	}

	@Test
	public void testGetBoardHeightAfterConstructingDifferentSizeBoard() {
		int newWidth = 20;
		gameOfLife = new ConwaysGameOfLife(newWidth, gameOfLife.getBoardHeight());
		assertEquals(gameOfLife.getBoardWidth(), newWidth);
	}

	// ####################################################
	//		CELL BEHAVIOR TESTS
	// ####################################################
	@Test
	public void testCellCanBeToggledFromDeadToAlive() {
		gameOfLife.toggleCell(0, 0);
		assertTrue(gameOfLife.getMatrix()[0][0]);
	}

	@Test
	public void testCellCanBeToggledFromAliveToDead() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(0, 0);
		assertFalse(gameOfLife.getMatrix()[0][0]);
	}

	@Test
	public void testOneLiveCellCanBeNeighborForMultipleAdjacentCells() {
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(2, 2);
		gameOfLife.toggleCell(2, 3);
		gameOfLife.toggleCell(2, 4);
		gameOfLife.calculateNextGeneration();
		assertTrue(gameOfLife.getMatrix()[2][2]);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTogglingAnInvalidCellByColumnIndexResultsInIllegalArgumentException() {
		gameOfLife.toggleCell(10, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTogglingAnInvalidCellByRowIndexResultsInIllegalArgumentException() {
		gameOfLife.toggleCell(0, 10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTogglingAnInvalidCellByNegativeColumnIndexResultsInIllegalArgumentException() {
		gameOfLife.toggleCell(-10, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTogglingAnInvalidCellByNegativeRowIndexResultsInIllegalArgumentException() {
		gameOfLife.toggleCell(0, -10);
	}

	/* This test breaks my rule of a unit test testing only 1 thing
	 * (e.g. one assertion) but seems useful in that this is a mocked
	 * end-to-end test of 1 passing generation. */
	@Test
	public void testOneGenerationRunsSuccessfully() {
		gameOfLife.toggleCell(0, 0);
		assertTrue(gameOfLife.getMatrix()[0][0]);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(4, 0);

		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);

		gameOfLife.toggleCell(3, 2);
		gameOfLife.toggleCell(4, 2);

		gameOfLife.toggleCell(1, 3);
		gameOfLife.toggleCell(4, 3);

		gameOfLife.toggleCell(1, 4);
		gameOfLife.toggleCell(4, 4);
		
		gameOfLife.calculateNextGeneration();

		assertTrue(gameOfLife.getMatrix()[0][0]);
		assertFalse(gameOfLife.getMatrix()[1][0]);
		assertTrue(gameOfLife.getMatrix()[2][0]);
		assertTrue(gameOfLife.getMatrix()[3][0]);
		assertFalse(gameOfLife.getMatrix()[4][0]);

		assertTrue(gameOfLife.getMatrix()[0][1]);
		assertFalse(gameOfLife.getMatrix()[1][1]);
		assertFalse(gameOfLife.getMatrix()[2][1]);
		assertFalse(gameOfLife.getMatrix()[3][1]);
		assertTrue(gameOfLife.getMatrix()[4][1]);

		assertTrue(gameOfLife.getMatrix()[0][2]);
		assertFalse(gameOfLife.getMatrix()[1][2]);
		assertFalse(gameOfLife.getMatrix()[2][2]);
		assertTrue(gameOfLife.getMatrix()[3][2]);
		assertTrue(gameOfLife.getMatrix()[4][2]);

		assertFalse(gameOfLife.getMatrix()[0][3]);
		assertFalse(gameOfLife.getMatrix()[1][3]);
		assertTrue(gameOfLife.getMatrix()[2][3]);
		assertFalse(gameOfLife.getMatrix()[3][3]);
		assertTrue(gameOfLife.getMatrix()[4][3]);

		assertFalse(gameOfLife.getMatrix()[0][4]);
		assertFalse(gameOfLife.getMatrix()[1][4]);
		assertFalse(gameOfLife.getMatrix()[2][4]);
		assertFalse(gameOfLife.getMatrix()[3][4]);
		assertFalse(gameOfLife.getMatrix()[4][4]);
	}

	// ####################################################
	//		DEAD CELL VALID NEIGHBOR TESTS
	// ####################################################
	@Test
	public void testDeadCellStaysDeadWithZeroLivingNeighbors() {
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithOneLivingNeighbor() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithTwoLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellBirthsWithThreeLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(3, 1);
		gameOfLife.calculateNextGeneration();
		assertTrue(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithFourLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(3, 1);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithFiveLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(3, 1);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.toggleCell(3, 2);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithSixLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(3, 1);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.toggleCell(3, 2);
		gameOfLife.toggleCell(1, 3);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithSevenLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(3, 1);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.toggleCell(3, 2);
		gameOfLife.toggleCell(1, 3);
		gameOfLife.toggleCell(2, 3);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	@Test
	public void testDeadCellStaysDeadWithEightLivingNeighbors() {
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(3, 1);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.toggleCell(3, 2);
		gameOfLife.toggleCell(1, 3);
		gameOfLife.toggleCell(2, 3);
		gameOfLife.toggleCell(3, 3);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}

	// ####################################################
	//		LIVING CELL NEIGHBOR COUNT TESTS
	// ####################################################
	@Test
	public void testZeroNeighborsResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[0][0]);
	}

	@Test
	public void testOneNeighborResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[0][0]);
	}

	@Test
	public void testFourNeighborsResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[1][1]);
	}

	@Test
	public void testFiveNeighborsResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[1][1]);
	}

	@Test
	public void testSixNeighborsResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(0, 2);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[1][1]);
	}

	@Test
	public void testSevenNeighborsResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(0, 2);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[1][1]);
	}

	@Test
	public void testEightNeighborsResultsInDeathForLivingCell() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.toggleCell(0, 2);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.toggleCell(2, 2);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[1][1]);
	}

	// ####################################################
	//		LIVING CELL VALID NEIGHBORS TESTS
	// ####################################################
	@Test
	public void testTopDiagonalAdjacentLivingCellsAreConsideredNeighbors() {
		gameOfLife.toggleCell(0, 0);
		gameOfLife.toggleCell(2, 0);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.calculateNextGeneration();
		assertTrue(gameOfLife.getMatrix()[1][1]);
	}

	@Test
	public void testBottomDiagonalAdjacentLivingCellsAreConsideredNeighbors() {
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.calculateNextGeneration();
		assertTrue(gameOfLife.getMatrix()[1][0]);
	}
	
	@Test
	public void testHorizontalAdjacentLivingCellsAreConsideredNeighbors() {
		gameOfLife.toggleCell(0, 1);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(2, 1);
		gameOfLife.calculateNextGeneration();
		assertTrue(gameOfLife.getMatrix()[1][1]);
	}
	
	@Test
	public void testVerticalAdjacentLivingCellsAreConsideredNeighbors() {
		gameOfLife.toggleCell(1, 0);
		gameOfLife.toggleCell(1, 1);
		gameOfLife.toggleCell(1, 2);
		gameOfLife.calculateNextGeneration();
		assertTrue(gameOfLife.getMatrix()[1][1]);
	}
	
	@Test
	public void testLivingCellsFurtherThanOneCellAwayAreNotConsideredNeighbors() {
		gameOfLife.toggleCell(0, 2);
		gameOfLife.toggleCell(4, 2);

		gameOfLife.toggleCell(2, 2);
		gameOfLife.calculateNextGeneration();
		assertFalse(gameOfLife.getMatrix()[2][2]);
	}
}