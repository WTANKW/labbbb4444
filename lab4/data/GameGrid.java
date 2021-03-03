package lab4.data;

import java.util.Observable;

/**
 * Represents the 2-d game grid
 * 
 * @author daniel
 */

public class GameGrid extends Observable{

	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	int INROW = 5;
	int hori = 0;
	int vert = 0;
	public int[][] nodes;
	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 * 
	 * @author daniel
	 */
	public GameGrid(int size) {
		//makes a 2 dimentional array using the given size
		nodes = new int[size][size];
		//gives each element in the array a value
		for(int i = 0; i < size; i++) {
			for(int n = 0; n < size; n++) {
				nodes[i][n]=EMPTY;
			}
		}
	}
	
	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 * 
	 * @author daniel
	 */
	public int getLocation(int x, int y) {
		//goes through every element in the array 1 by 1
		for(int i = 0; i < nodes.length; i++) {
			for(int n = 0; n < nodes.length; n++) {
				//checks if the given x and y corresponds with any of the elements indexes.
				if(x == i && y == n) {
					hori = i;
					vert = n;
					//returns the value of the element corresponding with the coordinates given. 
					return nodes[i][n];
				}
			}
		}
		//If no squares were found then hori is assigned nodes.length so that other methods know
		//that the location wasn't within the grid. 
		hori = nodes.length;
		return EMPTY;
	}
	
	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 * 
	 * @author daniel
	 */
	public int getSize() {
		return nodes.length;
	}
	
	/**
	 * Enters a move in the game grid
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 * 
	 * @author daniel
	 */
	public boolean move(int x, int y, int player) {
		//checks if the square is occupied or not
		if(getLocation(x, y) == EMPTY) {
			//hori is assigned the value of nodes.length in getLocation method
			//if the move couldn't be made i.e. out of bounds for any box. 
			if(hori < nodes.length) {
				nodes[hori][vert] = player;
				this.setChanged();
				this.notifyObservers();
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid() {
		//goes through each node 1 by 1 and sets to empty
		for(int i = 0; i < nodes.length; i++) {
			for(int n = 0; n < nodes.length; n++) {
				nodes[i][n]=EMPTY;
			}
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 * 
	 * @author niklas
	 */
	public boolean isWinner(int player) {
		//makes a boolean for later use
		boolean check;
		//loops through every node in the grid to check if any of them are a winning square
		for(int i = 0; i < nodes.length; i++) {
			for(int n = 0; n < nodes.length; n++) {
				//first checks whether the current node cannot have a full line to the right
				if(i+INROW<=nodes.length) {
					//if any of the nodes in the line to the right isn't one of the players nodes
					//then the inner loop is broken and we move on to the next potential line. 
					check=true;
					for(int row = INROW-1; row > -1; row--) {
						if(nodes[i+row][n]!=player) {
							check = false;
							break;
						}
					}
					if (check) {
						return true;
					}
				}
				//same as last except down
				if (n+INROW<=nodes.length) {
					check=true;
					for(int row = INROW-1; row > -1; row--) {
						if(nodes[i][n+row]!=player) {
							check = false;
							break;
						}
					}
					if (check) {
						return true;
					}
				}
				//same as last except down and to the right
				if (n+INROW<=nodes.length && i+INROW<=nodes.length) {
					check=true;
					for(int row = INROW-1; row > -1; row--) {
						if(nodes[i+row][n+row]!=player) {
							check = false;
							break;
						}
					}
					if (check) {
						return true;
					}
				}
				//same as last except down and to the left
				if (n+INROW<=nodes.length && i-(INROW-1)>=0) {
					check=true;
					for(int row = INROW-1; row > -1; row--) {
						if(nodes[i-row][n+row]!=player) {
							check = false;
							break;
						}
					}
					if (check) {
						return true;
					}
				}
			}
		}
		//if no square was a winning square then false is returned. 
		return false;
	}
	
	
}