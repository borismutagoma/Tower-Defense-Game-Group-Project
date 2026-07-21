package towerdefense.towers.towerwithprojectiles;

import towerdefense.projectiles.Projectile;
import towerdefense.towers.Tower;

/**
 * Interface imposing the "contract" for a tower to be a tower throwing projectiles
 */
public interface ProjectileTower {
    
    /**
     * return the projectile of ProjectileTower
     * 
     * @return projectile the projectile of ProjectileTower
     */
    public Projectile getProjectile();

    /**
     * get the base damage of the tower
     * 
     * @return the base damage of the tower
     */
    public int getDamage();

    /**
     * applies the damage on the target
     */
    public void applyDamage();

    /**
     * Apply damage to balloons if the tower can shoot
     * 
     * @param currentTick the current tick of the game
     * @see Tower#act
     */
    public void act(int currentTick);

    /**
     * Override the Tower's method to check if the two objects have the same projectile.
     * @see Tower#equals(Object)
     */
    public boolean equals(Object o);

}
