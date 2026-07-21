package towerdefense.projectiles;

import towerdefense.balloons.Balloon;

/**
 * Define what a projectile is and the damage it inflicts. A projectile can be
 * activated/deactivated and upgraded.
 */
public abstract class Projectile {

    /** damage of the projectile */
    protected double damage;

    /** boolean to inform us if the projectile is active */
    protected boolean active;

    /**
     * constructor of abstract Class Projectilee
     * 
     * @param damage damage of the projectile
     */
    public Projectile(double damage) {
        this.damage = damage;
        this.active = false;
    }

    // ### GETTERS ###

    /**
     * returns the damage of the projectile
     * 
     * @return the damage the projectile gives
     */

    public double getDamage() {
        return this.damage;
    }

    // TOOLS

    /**
     * activates the projectile
     */
    public void activate() {
        this.active = true;
    }

    /**
     * increase the damage of the projectile
     * 
     * @param increase the multiplier rate (e.g., x1.5, x2...)
     */
    public void increaseDamage(double increase) {
        this.damage += (this.damage * increase);
    }

    /**
     * hit a Balloon using projectile
     * 
     * @param target the balloon to hit
     */
    public void hit(Balloon target) {
        target.shoot(this.damage);
    }

}
