package towerdefense.projectiles;

import java.util.List;

import towerdefense.balloons.Balloon;
import towerdefense.boards.Board;
import towerdefense.cell.Cell;

/**
 * Defines the projectiles that cause area damage
 */
public abstract class DamageZoneProjectile extends Projectile {

    /** The board on which the projectile will impact */
    private Board board;

    /** Damage zone by the bomb */
    protected int damageZone;

    /**
     * Creates a Projectile instance with area-of-effect damage.
     * Note: This is an abstract class; please create a projectile that implements this class.
     * 
     * @param damage Damage that the projectile inflicts on the targeted balloon
     * @param damageZone Projectile impact zone (will cause the same damage as the projectile on the target ball)
     * @param board Board on which the projectile will have an impact
     */
    public DamageZoneProjectile(double damage,int damageZone, Board board) {
        super(damage);
        this.board = board;
        this.damageZone = damageZone;
    }

    /**
     * Hit the balloons in the target balloon's impact zone
     * 
     * @see Projectile#hit(Balloon)
     */
    @Override
    public void hit(Balloon target){
        Cell currentCell = target.getCurrentCell();
        List<Cell> inDamageZoneCells = this.board.getAdjacentCells(currentCell,this.damageZone,true);
        inDamageZoneCells.add(currentCell); // Miss the current cell of the balloon otherwise
        for (Balloon b : this.board.getActiveBalloons()){
            if(inDamageZoneCells.contains(b.getCurrentCell())){
                b.shoot(this.damage);
            }
        }

    }

    /**
     * Getter for the damage zone
     * 
     * @return the damage zone
     */
    public int damageZone() {
        return this.damageZone;
    }

    /**
     * Textual representation of the class (Returns the class name)
     * 
     * @see Projectile#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getName();
    }
    
}
