package towerdefense.towers.towerwithprojectiles;

import towerdefense.projectiles.Projectile;
import towerdefense.strategy.Strategy;
import towerdefense.towers.Tower;
import towerdefense.util.Position;

/**
 * An abstract class that serves as the foundation for all towers that utilize projectiles
 */
public abstract class BaseProjectileTower extends Tower implements ProjectileTower {

    /** 
    * The logic engine that handles projectile behavior, collisions, and damage.
    */
    protected final ProjectileTowerLogic projectileLogic;


    /**
     * Constructs a new BaseProjectileTower.
     * @param cost the cost to purchase the tower
     * @param range the maximum distance the tower can shoot
     * @param cadence the delay between shots (in ticks)
     * @param position the coordinates of the tower on the board
     * @param strategy the targeting strategy used to select balloons
     * @param projectile the type of projectile this tower fires
     */
    public BaseProjectileTower(int cost, int range, int cadence, Position position, Strategy strategy, Projectile projectile) {
        super(cost, range, cadence, position, strategy);
        this.projectileLogic = new ProjectileTowerLogic(projectile);
    }

    /**
     * return the projectile of ProjecileTower
     * 
     * @return projectile the projectile of ProjectileTower
     */
    @Override
    public Projectile getProjectile() {
        return projectileLogic.getProjectile();
    }

    /**
     * Set a new projectile for this tower
     * @param newProjectile the new projectile to use with this tower
     */
    public void setProjectile(Projectile newProjectile){
        projectileLogic.setProjectile(newProjectile);
    }

    /**
     * applies the damage on the target
     */
    @Override
    public void applyDamage() {
        projectileLogic.applyDamage(this.getTargets());
    }

    /**
     * Apply damage to balloons if the tower can shoot
     * @see ProjectileTowerLogic#act(Tower, int)
     */
    @Override
    public void act(int currentTick) {
        projectileLogic.act(this, currentTick);
    }

    /**
     * Override the Tower's method to check if the two objects have the same projectile.
     * @see Tower#equals(Object)
     * 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseProjectileTower other = (BaseProjectileTower) o;
        if (this.getProjectile() == null || other.getProjectile() == null) {
        return this.getProjectile() == other.getProjectile();
    }
    
    return this.getProjectile().getClass().equals(other.getProjectile().getClass());
}
}