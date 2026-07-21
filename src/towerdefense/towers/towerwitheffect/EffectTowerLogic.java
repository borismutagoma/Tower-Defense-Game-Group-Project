package towerdefense.towers.towerwitheffect;


import java.util.List;

import towerdefense.balloons.Balloon;
import towerdefense.towers.Tower;
import towerdefense.util.Effect;

/**
 * class for EffectTower that extends from Tower
 * 
 * @see Tower
 */
public class EffectTowerLogic {

    // peut etre effect on le fait comme final pourquoi pas

    /** EFFECT the effect of tower on balloons */
    private final Effect EFFECT;

    /***
     * building a EffectTower object with cost, range, cadence, position, strategy
     * and effect
     * 
     * @param effect   the effect that is used on balloons like freezing them of
     *                 slowing them
     */
    public EffectTowerLogic(Effect effect) {
        this.EFFECT = effect;
    }

    /**
     * return effect the effect of EffectTower on Balloons
     * 
     * @return effect the effect of EffectTower on Balloons
     */
    public Effect getEffect() {
        return this.EFFECT;
    }

    /**
     * Apply effect to a list of balloons
     * @param targets the balloons on which the damage will be applied
     */
    protected void applyEffect(List<Balloon> targets) {
        for (Balloon balloon : targets) {
            balloon.applyEffect(this.getEffect());
        }
    }

    /**
     * Apply damage to balloons if the tower can shoot
     * 
     * @param targets the balloons on which the damage will be applied
     * @param canShoot allows the tower to fire or not
     * 
     * @see Tower#act
     */
    public void act(List<Balloon> targets, boolean canShoot) {
        if (canShoot) {
            this.applyEffect(targets);
        }
    }

}