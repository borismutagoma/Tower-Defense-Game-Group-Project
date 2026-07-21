package towerdefense.towers.towerwithprojectiles;

import towerdefense.game.GameConstants;
import towerdefense.projectiles.*;
import towerdefense.strategy.Strategy;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;

/**
 * class for BombTower that extends from ProjectileTower and implements
 * Upgradeable
 * 
 * @see ProjectileTowerLogic
 * @see Upgradeable
 */
public class BombTower extends BaseUpgradeableProjectileTower{

    /** The cost for this type of tower */
    private static final int COST = GameConstants.BOMB_TOWER_COST;
    /* The range at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final int BASE_RANGE = GameConstants.BOMB_TOWER_RANGE;
    /* The cadence at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final int BASE_CADENCE = GameConstants.BOMB_TOWER_CADENCE;
    /* The projectile at the intialization for this type of tower (Can be improved with Upgrade feature) */
    private static final Projectile BASE_PROJECTILE = GameConstants.BOMB_TOWER_PROJECTILE;


    /**
     * building a BombeTower object with cost, range, cadence, position and
     * strategy.
     * 
     * @param position the position of BombeTower object
     * @param strategy the strategy that is used to select the balloons to shoot
     */
    public BombTower(Position position, Strategy strategy) {
        super(COST, BASE_RANGE, BASE_CADENCE, position, strategy,BASE_PROJECTILE);
        this.setProjectile(new BombProjectile(this.strategy.getBoard()));
        this.addAllowedUpgrade(new PowerStatModifier("damage", 1.0, false, 200));
        this.addAllowedUpgrade(new RangeStatModifier("range", 1.50, true, 250));
        this.addAllowedUpgrade(new CadenceStatModifier("cadence", 0.75, true, 300));
        this.addAllowedUpgrade(new ProjectileStatModifier("projectile", 400, new ExtraBombProjectile(strategy.getBoard())));
    }


    /**
     * @see Tower#getSymbol()
     */
    @Override
    public String getSymbol() {
        return "💣";
    }

}