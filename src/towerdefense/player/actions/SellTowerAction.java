package towerdefense.player.actions ;

import java.util.List;

import towerdefense.boards.Board;
import towerdefense.player.Player ;
import  towerdefense.towers.Tower;
import towerdefense.towers.tools.TowerManager;
import towerdefense.util.listchooser.ListChooser;


/** class for SellTowerAction that implements PlayerAction
 * 
 * @see PlayerAction
 */
public class SellTowerAction implements PlayerAction {

    /**
     * The ListChooser used to select a tower from a list. It can be interactive (user chooses) or  random  
     */
    private ListChooser<Tower> towerChooser ;


    /** Handles selling a tower from the board */    
    private TowerManager towerManager ;


    /***
     * Constructor to define which tower to sell.
     * 
     * @param towerChooser the ListChooser used to choose which tower to sell 
     * @param towerManager the towerManage used for selling towers
     */
    public SellTowerAction(ListChooser<Tower> towerChooser, TowerManager towerManager){
        this.towerChooser = towerChooser ; 
        this.towerManager = towerManager ;
    }

    /**
     * This method starts the action.
     * 
     * @param player The player who spends money or gains money.
     * @param board  The board where we add or remove towers.
     */
    @Override
    public void action(Player player, Board board){
        List<Tower> towers = board.getAllActiveTowers();
        if(towers.isEmpty()){
            System.out.println("No towers to sell"); // Double security
            return;
        }
        
        Tower selected = this.towerChooser.choose("Select a tower to sell:", towers);
        if (selected != null) {
            this.towerManager.sellTower(board, selected);
        }
    }

    /**
     * Verify that the action of selling a tower is feasible at least once
     * 
     * @see PlayerAction#isFeasible(Player, Board)
     * 
     * @return <code>True</code> if the action is feasible given the game conditions, <code>False</code> otherwise
     */
    @Override
    public boolean isFeasible(Player player, Board board){
        return !board.getAllActiveTowers().isEmpty();
    }

    /**
     * @see PlayerAction#toString()
     */
    @Override
    public String toString(){
        return "Tower Seller";
    }
    
}
