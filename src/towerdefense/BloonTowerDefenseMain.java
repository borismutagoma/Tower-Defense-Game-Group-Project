package towerdefense;
import java.io.IOException;

import towerdefense.boards.*;
import towerdefense.game.*;
import towerdefense.util.listchooser.Input;

/**
 * A class that creates an interactive Classical Board based on dimensions specified by the player in the console.
 * (Alternative to deliverables)
 */
public class BloonTowerDefenseMain {

    /**
     * Private Constructor of BloonTowerDefenseMain (Use main)
     */
    private BloonTowerDefenseMain(){}


    /**
     * Start the ClassicalBoard customization and then launch it
     * @param args No arguments are expected.
     */
    public static void main(String[] args) {
        int width = 10;
        int height = 10;

        System.out.println("=== WELCOME TO THE TOWER DEFENSE GAME ===");
        try {
            System.out.print("Width ? : ");
            width = Input.readInt();
            System.out.print("Height ? : ");
            height = Input.readInt();
        } catch (IOException e) {
            System.out.println("Invalid Input.");
        }

        Board board = new ClassicalBoard(width, height);
        
        BloonTowerDefenseGame myGame = new InteractiveGame(board, false, false);
        myGame.play();
    }
}