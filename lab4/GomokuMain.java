package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;


/**
 * Runs the game
 * 
 * @author niklas
 *
 */
public class GomokuMain {

    /**
     * Starts the game
     * 
     * @param args
     * 
     * @author niklas
     */
    public static void main(String[] args) {
        //sets a default port
        int port = 9515;
        //should 0 or more than 1 arguments be made then only the default port will be used
        //otherwise the given argument will be used as a port
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        else if(args.length > 1){
            System.out.print("too many arguments");
        }
        GomokuClient client = new GomokuClient(port);
        GomokuGameState state = new GomokuGameState(client);
        GomokuGUI gui = new GomokuGUI(state, client);
    }
}