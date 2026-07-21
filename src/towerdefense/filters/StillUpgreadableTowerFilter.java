package towerdefense.filters;

import towerdefense.towers.tools.evolutions.Upgradeable;

/**A class defining a filter to retrieve towers for which upgrades can still be purchased.
 * 
 * Note: It does not verify that the player has the funds to purchase them.
 */
public class StillUpgreadableTowerFilter extends FiltersTools<Upgradeable>{

    

    /** 
     * A contructor that creates a new StillUpgreadableTowerFilter Object. 
    */
    public StillUpgreadableTowerFilter(){
        super(null);
    };

    

    /**
     * Returns <code>True</code> only if the given tower still has upgrades available for purchase
     *
     * @param tower the given tower to check if it still has purchasable upgrades
     * 
     * @return True only if the given tower still has upgrades available for purchase
     */
    @Override
    public boolean matches(Upgradeable tower){
        return !tower.getPurchasableUpgrades().isEmpty();
    }

}
