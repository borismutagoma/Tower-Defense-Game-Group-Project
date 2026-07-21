package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwitheffect.FreezeTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Freeze tower
 */
public class FreezeSpec implements TowerSpec {

    /**
     * Built the specific details of a Freeze tower, it is used by the tower factory.
     */
    public FreezeSpec(){}

    /**
     * Get cost of a Freeze Tower
     * @return the cost of a Freeze Tower
     */
    @Override
    public int getCost() {
        return GameConstants.FREEZE_TOWER_COST;
    }

    /**
     * Create an instance of a Freeze Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a Freeze Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new FreezeTower(pos, null);
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }
    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "FreezeTower (Price: " + getCost() + ")";
    }
    
}
