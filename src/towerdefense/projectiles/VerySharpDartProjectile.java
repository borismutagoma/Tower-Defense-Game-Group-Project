package towerdefense.projectiles;

import towerdefense.game.GameConstants;

/**
 * Generates a "very sharp dart"-type projectile, hits only one balloon, has no particular effect
 * This is an improved version of dart-type projectiles (UPGRADE LVL2)
 */
public class VerySharpDartProjectile extends Projectile {

    /** The default damage of this projectile */
    private static final int DEFAULT_DAMAGE = GameConstants.VERY_SHARP_DART_DAMAGE;

    /**
     * constructor for the VerySharpDartProjectile class
     */
    public VerySharpDartProjectile(){
        super(DEFAULT_DAMAGE);
    }
    

}
