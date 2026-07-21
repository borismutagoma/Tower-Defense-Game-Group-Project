package towerdefense.projectiles;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;

/**
 * This class generates extra bombs; by definition, the projectile will hit
 * multiple targets in an area. The extra bomb is a more powerful version of the
 * bomb.
 */
public class ExtraBombProjectile extends DamageZoneProjectile {

    /** Default damage constant for balloons in this area */
    private static final int DEFAULT_DAMAGE_ZONE = GameConstants.EXTRA_BOMB_DAMAGE_ZONE;
    /** Default damage constant for balloons in this area */
    private static final int DEFAULT_DAMAGE = GameConstants.EXTRA_BOMB_DAMAGE;

    /**
     * constructor for the ExtraBombProjectile class
     * 
     * @param board the board on which the projectile is played
     */
    public ExtraBombProjectile(Board board) {
        super(DEFAULT_DAMAGE,DEFAULT_DAMAGE_ZONE,board);
    }

    

}
