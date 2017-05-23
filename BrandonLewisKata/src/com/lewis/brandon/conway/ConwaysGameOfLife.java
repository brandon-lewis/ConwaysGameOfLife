package com.lewis.brandon.conway;

import java.util.concurrent.ThreadLocalRandom;

/**
 * An implementation of Conway's well-known Game of Life.  The
 * rules of the game are simple:
 * <ul>
 * 	<li>Any live cell with fewer than two live neighbors dies (as if by under-population)</li>
 * 	<li>Any live cell with more than three live neighbors dies (as if by overcrowding)</li>
 * 	<li>Any live cell with two or three live neighbors lives on the next generation</li>
 * 	<li>Any dead cell with exactly three neighbors becomes a live cell</li>
 * 	<li>A cell's neighbors are those cells which are horizontally, vertically, or diagonally adjacent.</li>
 * </ul>
 * 
 * @author	Brandon Lewis
 * @since	April 19, 2017
 */
public class ConwaysGameOfLife {
	
	private boolean[][] gameMatrix;
	/**
	 * @return	a clone of the 2D array representing the game's board of living/dead cells
	 */
	public boolean[][] getMatrix() { return gameMatrix.clone(); }
	

	private int boardWidth, boardHeight;
	/**
	 * @param boardWidth	the number of cells wide the board is.  A playable board must have a boardWidth of at least 2
	 * @param boardHeight	the number of cells tall the board is.  A playable board must have a boardHeight of at least 2
	 * 
	 * @throws IllegalArgumentException	if the board is constructed with fewer than 2 cells wide or fewer than 2 cells tall
	 * @throws NullPointerException		if a null Integer object is passed as an argument for either parameter
	 */
	public ConwaysGameOfLife(int boardWidth, int boardHeight) {
		if(boardWidth < 2) {
			throw new IllegalArgumentException("A game of Conways Game of Life cannot have a board less than 2 cells wide");
		} else if(boardHeight < 2) {
			throw new IllegalArgumentException("A game of Conways Game of Life cannot have a board less than 2 cells tall");
		}
		gameMatrix = new boolean[boardWidth][boardHeight];
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
	}

	/**
	 * Convenience method to obtain the width of board.
	 * 
	 * @return	int representing the width of the game board
	 */
	public int getBoardWidth() { return boardWidth; }

	/**
	 * Convenience method to obtain the height of board.
	 * 
	 * @return	int representing the height of the game board
	 */
	public int getBoardHeight() { return boardHeight; }
	
	/**
	 * Toggles a particular cell's current state, either from living to dead or dead to living.
	 * 
	 * @param xIndex	index of the column to toggle, where 0 represents the first cell
	 * @param yIndex	index of the row to toggle, where 0 represents the first cell
	 * @throws IllegalArgumentException		when an invalid cell index is provided
	 * @return			the new state of the toggled cell
	 */
	public boolean toggleCell(int xIndex, int yIndex) {
		if(xIndex < 0 || xIndex >= boardWidth) {
			throw new IllegalArgumentException("Cannot toggle life/death status of a cell that doesn't exist "
					+ "(received xIndex of " + xIndex + ", expected value to be between 0 and " + (boardWidth - 1) + " inclusive)");
		} else if(yIndex < 0 || yIndex >= boardHeight) {
			throw new IllegalArgumentException("Cannot toggle life/death status of a cell that doesn't exist "
					+ "(received yIndex of " + yIndex + ", expected value to be between 0 and " + (boardHeight - 1) + " inclusive)");
		}
		return gameMatrix[xIndex][yIndex] = !gameMatrix[xIndex][yIndex];
	}
	
	/**
	 * Randomly generates the state of each cell of the current board configuration.
	 */
	public void randomizeFirstGeneration() {
		for(int xIndex = 0; xIndex < boardWidth; xIndex++) {
			for(int yIndex = 0; yIndex < boardHeight; yIndex++) {
				gameMatrix[xIndex][yIndex] = ThreadLocalRandom.current().nextBoolean();
			}
		}
	}
	
	/**
	 * Calculates the next generation of the current board configuration using the following rules:
	 * <ul>
	 * 	<li>Any live cell with fewer than two live neighbors dies (as if by under-population)</li>
	 * 	<li>Any live cell with more than three live neighbors dies (as if by overcrowding)</li>
	 * 	<li>Any live cell with two or three live neighbors lives on the next generation</li>
	 * 	<li>Any dead cell with exactly three neighbors becomes a live cell</li>
	 * 	<li>A cell's neighbors are those cells which are horizontally, vertically, or diagonally adjacent.</li>
	 * </ul>
	 */
	public void calculateNextGeneration() {
		boolean[][] newGameMatrix = new boolean[boardWidth][boardHeight];
		for(int xIndex = 0; xIndex < boardWidth; xIndex++) {
			for(int yIndex = 0; yIndex < boardHeight; yIndex++) {
				if(gameMatrix[xIndex][yIndex]) {	// Cell is Living
					newGameMatrix[xIndex][yIndex] = deathCheck(xIndex, yIndex);
				} else {							// Cell is Dead
					newGameMatrix[xIndex][yIndex] = birthCheck(xIndex, yIndex);
				}
			}
		}
		gameMatrix = newGameMatrix.clone();
	}
	
	private int getNeighborCount(int xIndex, int yIndex) {
		int neighbors = 0;
		for(int targetX = xIndex - 1; targetX <= xIndex + 1; targetX++) {
			for(int targetY = yIndex - 1; targetY <= yIndex + 1; targetY++) {
				// Skip check if targeting outside of game matrix
				if(targetX < 0 || targetY < 0 || targetX >= boardWidth || targetY >= boardHeight) continue;
				// Skip check if targeting current cell
				if(targetX == xIndex && targetY == yIndex) continue;
				if(gameMatrix[targetX][targetY]) neighbors++;
			}
		}
		return neighbors;
	}
	
	private boolean deathCheck(int xIndex, int yIndex) {
		int neighbors = getNeighborCount(xIndex, yIndex);
		if(neighbors < 2 || neighbors > 3) return false;
		return true;
	}
	
	private boolean birthCheck(int xIndex, int yIndex) {
		if(getNeighborCount(xIndex, yIndex) == 3) return true;
		return false;
	}
}