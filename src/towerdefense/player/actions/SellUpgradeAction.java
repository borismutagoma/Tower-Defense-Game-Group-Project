package towerdefense.player.actions ;

import java.util.List;

import towerdefense.boards.Board;
import towerdefense.filters.SellableUpgradesFilter;
import towerdefense.player.Player ;
import towerdefense.towers.tools.TowerManager;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.util.listchooser.ListChooser;



/** class for SellUpgradeAction that implements PlayerAction
 * 
 * @see PlayerAction
 */
public class SellUpgradeAction implements PlayerAction {

    /**
     * The ListChooser used to select an upgradeable tower from a list. It can be interactive (user chooses) or  random  
     */
    private ListChooser<Upgradeable> upgradeableChooser ; 

    /**
     * The ListChooser used to select an upgrade from a list. It can be interactive (user chooses) or  random  
     */
    private ListChooser<StatModifier> statModifierChooser ; 


    /** Handles selling an upgrade from the board */    
    private TowerManager towerManager ;

    /** Filter used to retrieve only towers with sellable upgrades */
    private final SellableUpgradesFilter sellableUpgradesFilter = new SellableUpgradesFilter();

    /**
     * Constructor for selling an upgrade.
     * 
     * @param upgradeableChooser the ListChooser used to choose which tower that has an upgrade 
     * @param statModifierChooser the ListChooser used to choose which upgrade to sell 
     * @param towerManager the towerManage used for selling upgrades
     */
    public SellUpgradeAction(ListChooser<Upgradeable> upgradeableChooser, ListChooser<StatModifier> statModifierChooser, TowerManager towerManager){
        this.upgradeableChooser = upgradeableChooser ; 
        this.statModifierChooser = statModifierChooser ; 
        this.towerManager = towerManager ;
    };

    /**
     * This method starts the action.
     * 
     * @param player The player who spends money or gains money.
     * @param board  The board where we add or remove towers.
     */
    @Override
    public void action(Player player, Board board){
        List<Upgradeable> upgradableTowers = board.getActiveUpgradableTowers();

        if (upgradableTowers.isEmpty()) {
            System.out.println("No upgradable towers on the board!");
            return;
        }
        List<Upgradeable> towersWithUpgradesToSell = sellableUpgradesFilter.applyFilter(upgradableTowers);
        if(towersWithUpgradesToSell.isEmpty()){
            System.out.println("No towers with upgrade to sell");
        }


        Upgradeable selectedTower = this.upgradeableChooser.choose("From which tower do you want to sell an upgrade?", upgradableTowers);
        if (selectedTower == null) return;
        List<StatModifier> upgrades = selectedTower.getActiveUpgrades();
        
        if (upgrades.isEmpty()) {
            System.out.println("This tower has no upgrades to sell!");
            return;
        }

        StatModifier choice = statModifierChooser.choose("Which upgrade to sell?", upgrades);
        if (choice != null) {
            this.towerManager.sellUpgrade(selectedTower, choice);
        }
    }

    /**
     * Verify that the action of selling an upgrade is feasible at least once
     * 
     * @see PlayerAction#isFeasible(Player, Board)
     * 
     * @return <code>True</code> if the action is feasible given the game conditions, <code>False</code> otherwise
     */
    @Override
    public boolean isFeasible(Player player, Board board){
        for(Upgradeable tower : board.getActiveUpgradableTowers()){
            if(!tower.getActiveUpgrades().isEmpty()){
                return true;
            }
        }
        return false;

    }

    /**
     * @see PlayerAction#toString()
     */
    @Override
    public String toString(){
        return "Upgrade Seller";
    }
    
}
