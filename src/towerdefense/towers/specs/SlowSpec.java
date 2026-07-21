package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwitheffect.SlowTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Slow tower
 */
public class SlowSpec implements TowerSpec {

    /**
     * Built the specific details of a Slow tower, it is used by the tower factory.
     */
    public SlowSpec(){}

    /**
     * Get cost of a Slow Tower
     * @return the cost of a Slow Tower
     */
    @Override
    public int getCost() {
        return GameConstants.SLOW_TOWER_COST;
    }

    /**
     * Create an instance of a Slow Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a Slow Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new SlowTower(pos, null);
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }
    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "SlowTower (Price: " + getCost() + ")";
    }
    
}
