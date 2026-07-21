package towerdefense.projectiles;

import towerdefense.game.GameConstants;

/**
 * Generates a needle-type projectile, hits only one balloon, has no particular effect
 */
public class NeedleProjectile extends Projectile {

    /** The default damage of this projectile */
    private static final int DEFAULT_DAMAGE = GameConstants.NEEDLE_DAMAGE;

    /**
     * constructor for the NeedleProjectile class
     */
    public NeedleProjectile(){
        super(DEFAULT_DAMAGE);
    }
    

}
