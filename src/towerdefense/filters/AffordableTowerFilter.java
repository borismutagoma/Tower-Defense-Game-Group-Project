package towerdefense.filters;

import towerdefense.player.Player;
import towerdefense.towers.Tower;

/** Filter used to keep only towers that the player can afford. */
public class AffordableTowerFilter extends FiltersTools<Tower>{

    /** 
     * A constructor that creates a new AffordableTowerFilter Object. 
     * @param player the player whose information will affect the filter's response
    */
    public AffordableTowerFilter(Player player){
        super(player);
    };

    /**
     * Returns only the towers that cost less or equal to the available money.
     *
     * @param tower the given tower to check with the player's wallet
     * 
     * @return True only if the given tower costs less or equal to the player's money.
     */
    @Override
    public boolean matches(Tower tower) {
        return tower.getCost() <= this.player.getMoney();
    }
}