package towerdefense.player.actions;

import towerdefense.boards.Board;
import towerdefense.player.Player;

/**
 * Class defining the action of displaying the board
 */
public class DisplayBoardAction implements PlayerAction {

    /** Display the game board visually or textually */
    private boolean isVisualDisplay;

    /**
     * Constructor to manage the display action of the board
     * 
     * @param isVisualDisplay display the game board visually or textually
     */
    public DisplayBoardAction(Boolean isVisualDisplay){
        this.isVisualDisplay = isVisualDisplay;
    }

    /**
     * This method starts the buy upgrade action.                
     * 
     * @param player player who spends money
     * @param board  board on which the active towers are located
     */
    @Override
    public void action(Player player, Board board){
        if(this.isVisualDisplay){
            board.visualDisplay();
        }
        else{
            board.display();
        }        
    }

    /**
     * Verify that the action of displaying the table is possible or not
     * 
     * @see PlayerAction#isFeasible(Player, Board)
     * 
     * @return Always <code>True</code>
     */
    @Override
    public boolean isFeasible(Player player, Board board){
        return true;
    }

    /**
     * @see PlayerAction#toString()
     */
    @Override
    public String toString(){
        return "Display Board";
    }
}
