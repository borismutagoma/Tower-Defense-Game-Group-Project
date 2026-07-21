package towerdefense.projectiles;

import towerdefense.game.GameConstants;

/**
 * Generates a dart-type projectile, hits only one balloon, has no particular effect
 */
public class DartProjectile extends Projectile {

    /** The default damage of this projectile */
    private static final int DEFAULT_DAMAGE = GameConstants.DART_DAMAGE;

    /**
     * constructor for the DartProjectile class
     */
    public DartProjectile(){
        super(DEFAULT_DAMAGE);
    }
    

}