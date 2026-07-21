package towerdefense.towers.towerwithprojectiles;

import towerdefense.game.GameConstants;
import towerdefense.projectiles.Projectile;
import towerdefense.strategy.Strategy;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;

/**
 * class for NeedleTower that extends from ProjectileTower and implements Upgradeable
 * 
 * @see ProjectileTowerLogic
 * @see Upgradeable
 */
public class NeedleTower extends BaseUpgradeableProjectileTower {

    /** The cost for this type of tower */
    private static final int COST = GameConstants.NEEDLE_TOWER_COST;
    /* The range at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final int BASE_RANGE = GameConstants.NEEDLE_TOWER_RANGE;
    /* The cadence at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final int BASE_CADENCE = GameConstants.NEEDLE_TOWER_CADENCE;
    /* The projectile at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final Projectile BASE_PROJECTILE = GameConstants.NEEDLE_TOWER_PROJECTILE;


    /**
     * building a NeedleTower object with cost, range, cadence, position and strategy
     * 
     * @param position the position of NeedleTower object
     * @param strategy the strategy that is used to select the balloons to shoot
     * 
     */
    public NeedleTower(Position position, Strategy strategy) {
        
        super(COST, BASE_RANGE, BASE_CADENCE, position, strategy,BASE_PROJECTILE);
        this.addAllowedUpgrade(new RangeStatModifier("range", 1.20, true, 150));
        this.addAllowedUpgrade(new CadenceStatModifier("cadence", 0.75, true, 200));
    }
    
    
    /**
     * @see Tower#getSymbol()
     */
    @Override
    public String getSymbol() {
        return "🪡";
    }
    
}