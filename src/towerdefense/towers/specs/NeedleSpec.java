package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwithprojectiles.NeedleTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Needle tower
 */
public class NeedleSpec implements TowerSpec {

    /**
     * Built the specific details of a Needle tower, it is used by the tower factory.
     */
    public NeedleSpec(){}

    /**
     * Get cost of a Needle Tower
     * @return the cost of a Needle Tower
     */
    @Override
    public int getCost() {
        return GameConstants.NEEDLE_TOWER_COST;
    }

    /**
     * Create an instance of a Needle Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a Needle Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new NeedleTower(pos, null);
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }
    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "NeedleTower (Price: " + getCost() + ")";
    }
    
}
