package towerdefense.livrables;

import towerdefense.boards.*;
import towerdefense.game.*;

/**
 * Final deliverable of the Bloons Tower Defense Game project
 * Create a RandomBoard in interactive mode. 
 */
public class TDGInteractiveB {

    /**
     * Private constructor of the deliverable (usage of main method)
     */
    private TDGInteractiveB(){}

    /**
     * Launch the game in Interactive mode on the Random Board (Multiple straight paths placed randomly)
     * @param args 3 expected arguments: rows columns nbPaths (Excepted dimensions and number of paths for the board)
     */
    public static void main(String[] args) {

        if (args.length < 3) {
            System.err.println("Usage: java TDGInteractiveB <rows> <columns> <nbPaths>");
            return;
        }
        if (args.length > 3) {
            System.out.println("Usage: java TDGInteractiveB <rows> <columns>\n\tWarning: Other elements ignored\n");
        }


        try{
            int rows = Integer.parseInt(args[0]);
            int cols = Integer.parseInt(args[1]);
            int nbPaths = Integer.parseInt(args[2]);

            if (rows <= 0 || cols <= 0) {
                System.err.println("Error: Dimensions must be positive integers.");
                return;
            }

            if(nbPaths <= 0){
                System.err.println("Error: The number of paths must be strictly positive.");
                return;
            }

            // Generate the board
            Board board = new RandomBoard(rows, cols, nbPaths);

            BloonTowerDefenseGame game = new InteractiveGame(board, false,true);
            game.play();

        } catch(NumberFormatException e){
            System.err.println("Usage: java TDGInteractiveB <rows> <columns> <nbPaths>\n\tError: Please enter valid integers.");
            return;
        }
        

        
    }

    


    
    
}
