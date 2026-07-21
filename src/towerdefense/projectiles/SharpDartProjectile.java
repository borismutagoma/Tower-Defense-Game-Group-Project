package towerdefense.projectiles;

import towerdefense.game.GameConstants;

/**
 * Generates a "sharp dart"-type projectile, hits only one balloon, has no particular effect
 * This is an improved version of dart-type projectiles (UPGRADE LVL1)
 */
public class SharpDartProjectile extends Projectile {

    /** The default damage of this projectile */
    private static final int DEFAULT_DAMAGE = GameConstants.SHARP_DART_DAMAGE;

    /**
     * constructor for the SharpDartProjectile class
     */
    public SharpDartProjectile() {
        super(DEFAULT_DAMAGE);
    }

}
