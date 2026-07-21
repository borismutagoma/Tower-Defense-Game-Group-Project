package towerdefense.player.actions;

import java.util.*;

import towerdefense.boards.Board;
import towerdefense.filters.*;
import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.util.listchooser.ListChooser;

/** class for BuyUpgradeAction that implements PlayerAction
 * 
 * @see PlayerAction
 */
public class BuyUpgradeAction implements PlayerAction {

    /**
     * The ListChooser used to select a tower from a list. It can be interactive (user chooses) or  random  
     */
    private ListChooser<Upgradeable> towerLC ;

    /**
     * The ListChooser used to select an upgrade from a list. It can be interactive (user chooses)or random  
     */
    private ListChooser<StatModifier> upgradeLC ;

    /**
     * Constructor to define which upgrade to buy.
     * 
     * @param towerLC the ListChooser used to choose in which tower an upgrade will be buy 
     * @param upgradeLC the ListChooser used to choose which upgrade to buy
     */
    public BuyUpgradeAction(ListChooser<Upgradeable> towerLC, ListChooser<StatModifier> upgradeLC){
        this.towerLC = towerLC ;
        this.upgradeLC = upgradeLC ;
    }

    /**
     * This method starts the buy upgrade action.                
     * 
     * @param player player who spends money
     * @param board  board on which the active towers are located
     */
    @Override
    public void action(Player player, Board board){
        Upgradeable selectedTower = null;
        StatModifier selectedUpgrade = null;

        List<Filter<Upgradeable>> conditions = new ArrayList<>(List.of(
            new StillUpgreadableTowerFilter(),
            new CanAffordAnyUpgradeFilter(player)
        ));

        List<Upgradeable> towerToDiplay = FiltersTools.applyFiltersList(board.getActiveUpgradableTowers(), conditions);


        while(selectedUpgrade == null){
            // Select a tower
            if(!towerToDiplay.isEmpty()){
                selectedTower = this.towerLC.choose("Choose a tower to upgrade:", towerToDiplay); 
                if(selectedTower == null){
                    // Back to main menu
                    return ;
                }
            }
            else{ // Double security
                System.out.println("This tower can't be upgrade, all the upgrades were purchased.");
                return;
            }
            
            List<StatModifier> purchasableUpgrade = selectedTower.getPurchasableUpgrades();
            if (purchasableUpgrade.isEmpty()) {
                System.out.println("No upgrades available for this specific tower.");
                return;
            }
            // Select an upgrade
            selectedUpgrade = this.upgradeLC.choose("Choose an upgrade for this tower :", purchasableUpgrade);   
        }


        selectedTower.buyUpgrade(player, selectedUpgrade);


        return ;
    }

    /**
     * Check whether purchasing at least one upgrade is feasible or not.
     * 
     * @see PlayerAction#isFeasible(Player, Board)
     * 
     * @return <code>True</code> if the action is feasible given the game conditions, <code>False</code> otherwise
     */
    @Override
    public boolean isFeasible(Player player, Board board){
        List<Filter<Upgradeable>> conditions = new ArrayList<>(List.of(
            new StillUpgreadableTowerFilter(),
            new CanAffordAnyUpgradeFilter(player)
        ));
        return !FiltersTools.applyFiltersList(board.getActiveUpgradableTowers(), conditions).isEmpty();
    }
    
    /**
     * @see PlayerAction#toString()
     */
    @Override
    public String toString(){
        return "Upgrade Shop";
    }
}
