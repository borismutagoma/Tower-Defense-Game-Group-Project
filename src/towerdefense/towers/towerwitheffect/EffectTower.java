package towerdefense.towers.towerwitheffect;

import towerdefense.towers.Tower;
import towerdefense.util.Effect;

/**
 * Interface imposing the "contract" for a tower to be a tower applying an effect to balloons
 */
public interface EffectTower {
    
    /**
     * return effect the effect of EffectTower on Balloons
     * 
     * @return effect the effect of EffectTower on Balloons
     */
    public Effect getEffect();


    /**
     * Apply damage to balloons if the tower can shoot
     * @param currentTick the current "state" of the game, also known as tickrate 
     * @see Tower#act
     */
    public void act(int currentTick);
}
