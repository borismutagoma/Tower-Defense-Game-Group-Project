package towerdefense.towers.towerwithprojectiles;

import towerdefense.game.GameConstants;
import towerdefense.projectiles.Projectile;
import towerdefense.strategy.Strategy;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;

/**
 * class for MonkeyDartTower that extends from ProjectileTower and implements Upgradeable
 * 
 * @see ProjectileTowerLogic
 * @see Upgradeable
 */
public class MonkeyDartTower extends BaseUpgradeableProjectileTower{

    /** The cost for this type of tower */
    private static final int COST = GameConstants.MONKEY_TOWER_COST;
    /* The range at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final int BASE_RANGE = GameConstants.MONKEY_TOWER_RANGE;
    /* The cadence at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final int BASE_CADENCE = GameConstants.MONKEY_TOWER_CADENCE;
    /* The projectile at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final Projectile BASE_PROJECTILE = GameConstants.MONKEY_TOWER_PROJECTILE;


    /***
     * building a MonkeyDartTower object with cost, range, cadence, position and strategy
     * 
     * @param position the position of MonkeyDartTower object
     * @param strategy the strategy that is used to select the balloons to shoot
     */
    public MonkeyDartTower(Position position, Strategy strategy) {
        super(COST, BASE_RANGE, BASE_CADENCE, position, strategy,BASE_PROJECTILE);
        this.addAllowedUpgrade(new RangeStatModifier("range", 1.25, true, 100));
        this.addAllowedUpgrade(new CadenceStatModifier("cadence", 0.75, true, 150));
        this.addAllowedUpgrade(new PowerStatModifier("damage", 1.0, false, 100));
    }


    /**
     * @see Tower#getSymbol()
     */
    @Override
    public String getSymbol() {
        return "🐒";
    }

}