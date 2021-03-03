/*
 * Created on 2007 feb 8
 */
package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game
 * 
 * @author daniel
 */

public class GomokuGameState extends Observable implements Observer{

   // Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;
	
    //Possible game states
	private final int NOT_STARTED = 0;
	private final int MY_TURN = gameGrid.ME;
	private final int OTHER_TURN = gameGrid.OTHER;
	private final int FINISHED = 3;
	private int currentState;
	
	private GomokuClient client;
	
	private String message;
	
	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 * 
	 * @author daniel
	 */
	public GomokuGameState(GomokuClient gc){
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}
	

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 * 
	 * @author daniel
	 */
	public String getMessageString() {
		return message;
	}
	
	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 * 
	 * @author daniel
	 */
	public GameGrid getGameGrid() {
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @author niklas
	 */
	public void move(int x, int y) {
		//first checks whether or not it's the players turn and tells the player if otherwise
		if (currentState == MY_TURN) {
			//checks if move can be made on clicked square and tells otherwise. If the move can be made
			//then the move is made
			if (gameGrid.move(x, y, currentState)) {
				//sets the message
				message = "\"me\" made a move on the square (" + gameGrid.hori + ", " + gameGrid.vert + ")";
				//sends the move to other player
				client.sendMoveMessage(x, y);
				//sets the state
				currentState = OTHER_TURN;
				//checks if the move won the game
				if(gameGrid.isWinner(gameGrid.ME)) {
					currentState = FINISHED;
					message = "\"me\" won";
				}
				//notifies observers
				this.setChanged();
				this.notifyObservers();
			}
			else {
				message = "not a valid square";
				this.setChanged();
				this.notifyObservers();
			}
		}
		else {
			message = "not your turn";
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Starts a new game with the current client
	 * 
	 * @author daniel
	 */
	public void newGame() {
		//sets the message
		message = "\"me\" initiated a new game";
		//clears the grid
		gameGrid.clearGrid();
		//sets the state
		currentState=OTHER_TURN;
		//sends to other player
		client.sendNewGameMessage();
		//notifies observers of changes
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Other player has requested a new game, so the 
	 * game state is changed accordingly
	 * 
	 * @author daniel
	 */
	public void receivedNewGame() {
		//sets the message
		message = "\"other\" initiated a new game";
		//clears the grid
		gameGrid.clearGrid();
		//sets the state
		currentState=MY_TURN;
		//notifies observers of changes
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * The connection to the other player is lost, 
	 * so the game is interrupted
	 * 
	 * @author daniel
	 */
	public void otherGuyLeft() {
		//sets message
		message = "player left";
		//clears the grid
		gameGrid.clearGrid();
		//sets the state
		currentState=FINISHED;
		//sends disconnection to client
		client.disconnect();
		//notifies the observers
		this.setChanged();
		this.notifyObservers();
		
	}
	
	/**
	 * The player disconnects from the client
	 * 
	 * @author daniel
	 */
	public void disconnect() {
		//sets the message
		message = "player left";
		//clears the grid
		gameGrid.clearGrid();
		//sets the state
		currentState=FINISHED;
		//sends disconnection to client
		client.disconnect();
		//notifies observers
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 * 
	 * @author daniel
	 */
	public void receivedMove(int x, int y) {
		//sends the made move to the gameGrid, as the move method checks if the move was able to be made
		//this method doesn't need to check again
		gameGrid.move(x, y, gameGrid.OTHER);
		//sets the state
		currentState = MY_TURN;
		//sets the message
		message = "\"other\" made a move on the square (" + gameGrid.hori + ", " + gameGrid.vert + ")";
		//checks if the latest move resulted in a win
		if(gameGrid.isWinner(gameGrid.OTHER)) {
			currentState = FINISHED;
			message = "\"other\" won";
		}
		//notifies observers
		this.setChanged();
		this.notifyObservers();
	}
	
	
	/**
	 * Updates the state when a game is started
	 * 
	 * @param observable
	 * @param object
	 * 
	 * @author håkan
	 */
	public void update(Observable o, Object arg) {
		
		switch(client.getConnectionStatus()){
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHER_TURN;
			break;
		}
		setChanged();
		notifyObservers();
		
		
	}
	
}