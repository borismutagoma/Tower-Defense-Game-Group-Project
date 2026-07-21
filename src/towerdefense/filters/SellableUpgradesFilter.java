package towerdefense.filters;

import towerdefense.towers.tools.evolutions.Upgradeable;


/**
 * Filter used to retrieve only towers with sellable upgrades
 */
public class SellableUpgradesFilter extends FiltersTools<Upgradeable>{

    

    /** 
     * A contructor that Creates a new SellableUpgradesFilter Object. 
    */
    public SellableUpgradesFilter(){
        super(null);
    };

    /**
     * Returns <code>True</code> only if the given tower has purchased upgrades
     *
     * @param tower the given tower on which the presence of purchased upgrades will be check
     * 
     * @return True only if the given tower has purchased upgrades.
     */
    @Override
    public boolean matches(Upgradeable tower){
        return tower.getActiveUpgrades().isEmpty();
    }
    
    
}
