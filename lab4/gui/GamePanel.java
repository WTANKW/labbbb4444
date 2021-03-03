package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 * 
 * @author niklas
 */

public class GamePanel extends JPanel implements Observer{

	private final int UNIT_SIZE = 20;
	private GameGrid grid;
	
	/**
	 * The constructor
	 * 
	 * @param grid The grid that is to be displayed
	 * 
	 * @author håkan
	 * 
	 */
	public GamePanel(GameGrid grid){
		//method given by teacher
		this.grid = grid;
		grid.addObserver(this);
		Dimension d = new Dimension(grid.getSize()*UNIT_SIZE+1, grid.getSize()*UNIT_SIZE+1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Returns a grid position given pixel coordinates
	 * of the panel
	 * 
	 * @param x the x coordinates
	 * @param y the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 * 
	 * @author niklas
	 * 
	 */
	public int[] getGridPosition(int x, int y) {
		//returns the given coordinates divided by the size of the squares (rounded down because integers)
		//as to match their index values with the returned coordinate values
		int[] pos = {x / UNIT_SIZE, y / UNIT_SIZE};
		return pos;
	}
	
	/**
	 * Calls repaint method
	 * 
	 * @param observable
	 * @param object
	 * 
	 * @author håkan
	 * 
	 */
	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}
	
	/**
	 * Paints grid
	 * 
	 * @param graphics to paint with
	 * 
	 * @author niklas
	 * 
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//loops through every square in the grid
		for(int i = 0; i < grid.nodes.length; i++) {
			for(int n = 0; n < grid.nodes.length; n++) {
				//first a square is made
				g.setColor(Color.black);
				g.drawRect(i*UNIT_SIZE, n*UNIT_SIZE, (i+1)*UNIT_SIZE, (n+1)*UNIT_SIZE);
				//afterwards, the player that owns the square (if any) will have it shown by way of
				//a circle inside of the square. 
				if (grid.nodes[i][n]==grid.ME) {
					g.setColor(Color.blue);
					g.fillOval(i*UNIT_SIZE, n*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
				}
				else if (grid.nodes[i][n]==grid.OTHER) {
					g.setColor(Color.orange);
					g.fillOval(i*UNIT_SIZE, n*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
				}
			}
		}
	}
}