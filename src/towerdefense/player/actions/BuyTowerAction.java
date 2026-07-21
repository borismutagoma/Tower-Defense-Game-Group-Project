package towerdefense.player.actions;

import towerdefense.boards.Board;
import towerdefense.player.Player;
import towerdefense.towers.specs.TowerSpec;
import towerdefense.towers.tools.TowerManager;
import towerdefense.util.RandomInputManager;
import towerdefense.util.listchooser.*;


/**
 *  class for BuyTowerAction that implements PlayerAction
 * 
 * @see PlayerAction
 */
public class BuyTowerAction implements PlayerAction {

    /** Stores the recipes of all towers that the player can buy. */
    private TowerManager towerManager ;

    /**
     * The ListChooser used to select a towerSpec from a list. It can be interactive (user chooses) or  random  
     */
    private ListChooser<TowerSpec> towerSpecLC ;

    /** Do the player's inputs need to be simulated? */
    private boolean isSimulatedInput;

    /** 
     *  Constructor that builds a BuyTowerAction object and initializes the list of available the towerManager and towerSpecLC. 
     * 
     * @param towerManager that stores all the recipes that the player can afford
     * @param towerSpecLC a listChooser that used to choose a towerSpec from a list
     * @param isRandom If set to true, the game will start without the player being able to place their tower (which will be placed randomly).
     */
    public BuyTowerAction(TowerManager towerManager, ListChooser<TowerSpec> towerSpecLC,boolean isRandom){
        this.towerManager = towerManager;
        this.towerSpecLC = towerSpecLC ;
        this.isSimulatedInput = isRandom;
    }



    /**
     * Action that allows a player to buy and place a tower on the board, The player selects coordinates and chooses a tower type by index.
     * 
     * @param player The player who spends money or gains money.
     * @param board The board where we add or remove towers.
     */
    @Override
    public void action(Player player, Board board){
        boolean purchaseSuccessful = false ; 
        int attempts = 0;

        TowerSpec towerSpec = this.towerSpecLC.choose("Choose a tower to Buy:", this.towerManager.getAvailableSpecs());

        while(towerSpec != null && !purchaseSuccessful && attempts < 100){

            int x,y;
            if(this.isSimulatedInput){
                x = RandomInputManager.getRandomInt(board.getRows());
                y = RandomInputManager.getRandomInt(board.getColumns());
            } else {
                System.out.println("enter the coordinate for the row: ");
                x = Input.readIntInRange(0,board.getRows());
                System.out.println("enter the coordinates for the column: ");
                y = Input.readIntInRange(0,board.getColumns());
            }

            

            if(towerManager.buyTower(x, y, towerSpec)){
                purchaseSuccessful = true ;
                System.out.println("Success: Tower placed at (" + x + "," + y + ")");
            } else {
                attempts++;
            }
    
        }
    }

    /**
     * Check whether purchasing at least one tower is feasible or not.
     * 
     * @see PlayerAction#isFeasible(Player, Board)
     * 
     * @return <code>True</code> if the action is feasible given the game conditions, <code>False</code> otherwise
     */
    @Override
    public boolean isFeasible(Player player, Board board){
        return !this.towerManager.getAvailableSpecs().isEmpty();
    }

    /**
     * @see PlayerAction#toString()
     */
    @Override
    public String toString(){
        return "Tower Shop";
    }
}