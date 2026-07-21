package towerdefense.towers.towerwitheffect;

import towerdefense.game.GameConstants;
import towerdefense.strategy.Strategy;
import towerdefense.towers.Tower;
import towerdefense.util.*;

/**
 * class for SlowTower that implements EffectTowerLogic
 * 
 * @see EffectTowerLogic
 */
public class SlowTower extends Tower implements EffectTower {

    /** The cost for this type of tower */
    private static final int COST = GameConstants.SLOW_TOWER_COST;
    /* The range at the intialization for this type of tower  */
    private static final int BASE_RANGE = GameConstants.SLOW_TOWER_RANGE;
    /* The cadence at the intialization for this type of tower */
    private static final int BASE_CADENCE = GameConstants.SLOW_TOWER_CADENCE;
    /* The effect at the intialization for this type of tower */
    private static final Effect BASE_EFFECT = GameConstants.SLOW_TOWER_EFFECT;

    /** The "Effect logic" that will be used to fulfill the methods requested by EffectTower interface */
    private final EffectTowerLogic effectLogic = new EffectTowerLogic(BASE_EFFECT);


    /***
     * building a SlowTower object with cost, range, cadence, position and strategy
     * 
     * @param position the position of SlowTower object
     * @param strategy the strategy that is used to select the balloons to shoot
     */
    public SlowTower(Position position, Strategy strategy) {
        super(COST, BASE_RANGE, BASE_CADENCE, position, strategy);
    }

    /**********************************/
    /*                                */
    /*        Projectile Logic        */
    /*                                */
    /**********************************/

    /**
     * return effect the effect of EffectTower on Balloons
     * 
     * @return effect the effect of EffectTower on Balloons
     */
    public Effect getEffect() {
        return effectLogic.getEffect();
    }


    /**
     * Apply damage to balloons if the tower can shoot
     * 
     * @see Tower#act
     */
    @Override
    public void act(int currentTick) {
        effectLogic.act(this.getTargets(), this.canShoot(currentTick));
        this.setLastShootTime(currentTick);
    }

    /**
     * @see Tower#getSymbol()
     */
    @Override
    public String getSymbol() {
        return "🐌";
    }
}