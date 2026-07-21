package towerdefense.livrables;

import towerdefense.boards.*;
import towerdefense.game.*;

/**
 * Final deliverable of the Bloons Tower Defense Game project
 * Create a ClassicalBoard in interactive mode. 
 */
public class TDGInteractiveA {

    /**
     * Private constructor of the deliverable (usage of main method)
     */
    private TDGInteractiveA(){}

    /**
     * Launch the game in Interactive mode on the Classical Board (A single randomly generated path)
     * @param args 2 expected arguments: rows columns (Excepted dimensions for the board)
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: java TDGInteractiveA <rows> <columns>");
            return;
        }
         if (args.length > 2) {
            System.out.println("Usage: java TDGInteractiveA <rows> <columns>\n\tWarning: Other elements ignored\n");
        }


        try{
            int rows = Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);

            if (rows <= 0 || cols <= 0) {
                System.err.println("Error: Dimensions must be positive integers.");
                return;
            }

            // Generate the board
            Board board = new ClassicalBoard(rows, cols);

            BloonTowerDefenseGame game = new InteractiveGame(board, false,true);
            game.play();

        } catch(NumberFormatException e){
            System.err.println("Usage: java TDGInteractiveA <rows> <columns>\n\tError: Please enter valid integers.");
            return;
        }
        

        
    }

    


    
    
}
