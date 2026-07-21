package towerdefense.player.actions ;

import towerdefense.boards.Board;
import towerdefense.player.Player ;

/**
 * This interface is for actions in the game.
 * It helps to buy, sell, upgrade towers and other actions needed for the game.
 */
public interface PlayerAction {

    /**
     * This method starts the action.
     * 
     * @param player The player on whom the action will be played
     * @param board  The board on which the action will be played
     */
    public void action(Player player, Board board);

    /**
     * Checks if the action is feasible.
     * This method determines whether or not the action is displayed in the main menu.
     * @param player the player for whom this filter is used
     * @param board the game board on which the game is played, and therefore this filter applies
     * Note : The answer from this method depends on one, both, or neither of these two parameters.
     * @return <code>True</code> if the action is feasible given the game conditions, <code>False</code> otherwise
     */
    public boolean isFeasible(Player player, Board board);

    /**
     * Get a textual representation of this action
     * @return a textual representation of this action
     */
    public String toString();
    
}
