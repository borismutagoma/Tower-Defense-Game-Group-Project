package towerdefense.projectiles;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;

/**
 *  This class generates bomb-type projectiles; by definition, its purpose is to hit multiple balloons at once.
 */
public class BombProjectile extends DamageZoneProjectile {
    /** Default damage constant for balloons in this area */
    private static final int DEFAULT_DAMAGE_ZONE = GameConstants.BOMB_DAMAGE_ZONE;
    /** constant of the default Damage */
    private static final int DEFAULT_DAMAGE = GameConstants.BOMB_DAMAGE;
    

    /**
     * constructor for the BombProjectile class
     * 
     * @param board the board on which the projectile is played
     */
    public BombProjectile(Board board) {
        super(DEFAULT_DAMAGE,DEFAULT_DAMAGE_ZONE,board);
    }

    

}