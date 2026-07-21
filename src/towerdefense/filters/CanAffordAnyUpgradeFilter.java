package towerdefense.filters;

import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;

/**
 * This filter allows to checks if it's possible to buy at least one upgrade for a tower from the upgrades not yet purchased.
 */
public class CanAffordAnyUpgradeFilter extends FiltersTools<Upgradeable>{

    

    /** 
     * A contructor that creates a new CanAffordAnyUpgradeFilter Object. 
     * @param player the player whose information will affect the filter's response
    */
    public CanAffordAnyUpgradeFilter(Player player){
        super(player);
    };

    

    /**
     * Returns <code>True</code> only if there is at least one upgrade that can be purchased for this tower
     *
     * @param tower the given tower to check with the player's wallet
     * 
     * @return True only if there is at least one upgrade that can be purchased for this tower
     */
    @Override
    public boolean matches(Upgradeable tower){
        for (StatModifier upgrade : tower.getPurchasableUpgrades()) {
            if (upgrade.getCost() <= this.player.getMoney()){
                return true;
            } 
        }
        return false;
    }

}
