package towerdefense.filters ; 

import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;

/** Filter used to keep only upgrades the player can afford. */
public class AffordableUpgradeFilter extends FiltersTools<StatModifier>{

    

    /** 
     * A contructor that creates a new AffordableUpgradeFilter Object. 
     * @param player the player whose information will affect the filter's response
    */
    public AffordableUpgradeFilter(Player player){
        super(player);
    };

    

    /**
     * Returns <code>True</code> only if the given upgrade costs less or equal to the player's money.
     *
     * @param upgrade the given upgrade to check with the player's wallet
     * 
     * @return True only if the given upgrade costs less or equal to the player's money.
     */
    @Override
    public boolean matches(StatModifier upgrade){
        return upgrade.getCost() <= this.player.getMoney();
    }
}