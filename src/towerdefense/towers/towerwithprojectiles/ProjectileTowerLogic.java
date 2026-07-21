package towerdefense.towers.towerwithprojectiles;

import java.util.List;

import towerdefense.balloons.Balloon;
import towerdefense.projectiles.Projectile;
import towerdefense.towers.Tower;

/**
 * class for ProjectileTower that extends from Tower
 * 
 * @see Tower
 */
public class ProjectileTowerLogic {

    /**  A projectile that represent the weapon to shoot the balloons */
    protected Projectile projectile;

    /***
     * Building a ProjectileTower object with the projectile that is used by the tower
     *
     * @param projectile the projectile that is used to shoot balloons with
     */
    public ProjectileTowerLogic(Projectile projectile) {
        this.projectile = projectile;
    }

    /**
     * return the projectile of ProjectileTower
     * 
     * @return projectile the projectile of ProjectileTower
     */
    public Projectile getProjectile() {
        return this.projectile;
    }

    /**
     * Set a new projectile for this tower
     * @param newProjectile the new projectile to use with this tower
     */
    public void setProjectile(Projectile newProjectile){
        this.projectile = newProjectile;
    }

    /**
     * get the base damage of the tower
     * 
     * @return the base damage of the tower
     */
    public double getDamage() {
        return this.projectile.getDamage();
    }

    /**
     * applies the damage on the target
     * 
     * @param targets the balloons on which the damage will be applied
     */
    protected void applyDamage(List<Balloon> targets) {
        for (Balloon balloon : targets) {
            projectile.hit(balloon);
        }
    }


    /**
     * Apply damage to balloons if the tower can shoot
     * 
     * @see Tower#act
     * 
     * @param t The tower that will launch the projectile
     * @param currentTick the current turn of the game (determines whether the tower can shoot or not)
     */
    public void act(Tower t, int currentTick) {
        List<Balloon> targets = t.getTargets();
        if(t.canShoot(currentTick) && !t.getTargets().isEmpty()){
            applyDamage(targets);
            t.setLastShootTime(currentTick);
        }
        
    }
}