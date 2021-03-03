package lab4.gui;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;

/**
 * The GUI class
 * 
 * @author niklas
 */

public class GomokuGUI implements Observer{

	private GomokuClient client;
	private GomokuGameState gamestate;
	private JFrame frame;
	private GamePanel gameGridPanel;
	private JButton connectButton;
	private JButton newGameButton;
	private JButton disconnectButton;
	private JLabel messageLabel;
	private SpringLayout lay;
	private Container pane;
	private MListener mL = new MListener();
	private CListener cL = new CListener();
	private NGListener ngL = new NGListener();
	private DCListener dcL = new DCListener();

	
	/**
	 * The constructor
	 * 
	 * @param g   The game state that the GUI will visualize
	 * @param c   The client that is responsible for the communication
	 * 
	 * @author niklas
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c){
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);
		
		//makes a frame for placing components in and a pane to use for layout
		frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane = frame.getContentPane();
		frame.setContentPane(pane);
		
		//makes componenents
		gameGridPanel = new GamePanel(g.getGameGrid());
		messageLabel = new JLabel("Welcome");
		connectButton = new JButton();
		connectButton.setText("CONNECT");
		newGameButton = new JButton();
		newGameButton.setText("NEW GAME");
		disconnectButton = new JButton();
		disconnectButton.setText("DISCONNECT");
		lay = new SpringLayout();
		
		//makes the pane use the layout
		pane.setLayout(lay);
		//assigns constraints to objects so that they are laid out as we want
		lay.putConstraint(lay.SOUTH, messageLabel, -15, lay.SOUTH, pane);
		lay.putConstraint(lay.HORIZONTAL_CENTER, messageLabel, 0, lay.HORIZONTAL_CENTER, pane);
		lay.putConstraint(lay.NORTH, gameGridPanel, 5, lay.NORTH, pane);
		lay.putConstraint(lay.WEST, gameGridPanel, 5, lay.WEST, pane);
		lay.putConstraint(lay.NORTH, connectButton, 5, lay.SOUTH, newGameButton);
		lay.putConstraint(lay.HORIZONTAL_CENTER, connectButton, 0, lay.HORIZONTAL_CENTER, gameGridPanel);
		lay.putConstraint(lay.HORIZONTAL_CENTER, newGameButton, 0, lay.HORIZONTAL_CENTER, gameGridPanel);
		lay.putConstraint(lay.NORTH, newGameButton, 5, lay.SOUTH, gameGridPanel);
		lay.putConstraint(lay.HORIZONTAL_CENTER, disconnectButton, 0, lay.HORIZONTAL_CENTER, gameGridPanel);
		lay.putConstraint(lay.NORTH, disconnectButton, 5, lay.SOUTH, connectButton);
		
		//adds the components to the frame so that they will appear
		frame.add(gameGridPanel);
		frame.add(messageLabel);
		frame.add(connectButton);
		frame.add(newGameButton);
		frame.add(disconnectButton);
		
		//makes the frame and it's components appear
		frame.setVisible(true);
		
		//assigns listeners to each respective component
		gameGridPanel.addMouseListener(mL);
		connectButton.addActionListener(cL);
		newGameButton.addActionListener(ngL);
		disconnectButton.addActionListener(dcL);
	}
	
	/**
	 * Listener that listens for mouseclicks on the grid
	 * 
	 * 
	 * @author niklas
	 *
	 */
	class MListener extends MouseAdapter {
		//when a square in the grid is clicked, calls the method in the gameGridPanel using 
		//the appropriate coordinates so that we can send the corresponding square to gamestate
		public void mouseClicked(MouseEvent e) {
			int[] cords = gameGridPanel.getGridPosition(e.getX()-5, e.getY()-5);
			gamestate.move(cords[0], cords[1]);
		}
	}
	
	/**
	 * Listener that connects player upon button press
	 * 
	 * @author niklas
	 *
	 */
	class CListener implements ActionListener {
		//When the connect button is clicked, makes a connection window
		public void actionPerformed(ActionEvent e) {
			ConnectionWindow cw = new ConnectionWindow(client);
		}
	}
	
	/**
	 * Listener that starts a new game upon button press
	 * 
	 * @author niklas
	 *
	 */
	class NGListener implements ActionListener {
		//when the new game button is clicked, calls the newGame method in gamestate
		public void actionPerformed(ActionEvent e) {
			gamestate.newGame();
		}
	}
	
	/**
	 * Listener that disconnects player upon button press
	 * 
	 * @author niklas
	 *
	 */
	class DCListener implements ActionListener {
		//when the disconnect button is clicked, calls the disconnect method in gamestate
		public void actionPerformed(ActionEvent e) {
			gamestate.disconnect();
		}
	}
	
	
	/**
	 * Updates the buttons upon status change
	 * 
	 * @author håkan
	 * 
	 */
	public void update(Observable arg0, Object arg1) {
		
		// Update the buttons if the connection status has changed
		if(arg0 == client){
			if(client.getConnectionStatus() == GomokuClient.UNCONNECTED){
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			}else{
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}
		
		// Update the status text if the gamestate has changed
		if(arg0 == gamestate){
			messageLabel.setText(gamestate.getMessageString());
		}
	}
	
}