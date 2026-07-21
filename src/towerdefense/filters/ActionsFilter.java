package towerdefense.filters;

import towerdefense.boards.Board;
import towerdefense.player.Player;
import towerdefense.player.actions.PlayerAction;

/**
 * Class allowing filtering of actions based on whether they are feasible for the player in the current state of the game or not.
 */
public class ActionsFilter extends FiltersTools<PlayerAction>{

    /** The board on which the action takes place */
    private Board board;

    /** 
     * A constructor that creates a new ActionsFilter Object. 
     * @param player the player whose information will affect the filter's response
     * @param board  the selected board on which the actions take place. (Note: This must be the same board as the one defined for the Interaction Manager)
    */
    public ActionsFilter(Player player,Board board){
        super(player);
        this.board = board;
    };

    /**
     * Returns <code>true</code> if the action is feasible, and <code>false</code> otherwise (the action defines itself if it is feasible)
     *
     * @param action the given action to check if its feasible or not
     * 
     * @return true if the action is feasible, false otherwise
     */
    @Override
    public boolean matches(PlayerAction action) {
        return action.isFeasible(this.player, this.board);
    }
}