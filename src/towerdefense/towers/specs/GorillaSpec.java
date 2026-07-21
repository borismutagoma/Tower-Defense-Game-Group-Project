package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwithprojectiles.GorrillaTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Gorilla tower
 */
public class GorillaSpec implements TowerSpec {


    /**
     * Built the specific details of a Gorilla tower, it is used by the tower factory.
     */
    public GorillaSpec(){}

    /**
     * Get cost of a Gorilla Tower
     * @return the cost of a Gorilla Tower
     */
    @Override
    public int getCost() {
        return GameConstants.GORILLA_TOWER_COST;
    }

    /**
     * Create an instance of a Gorilla Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a Gorilla Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new GorrillaTower(pos, null);
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }
    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "GorillaTower (Price: " + getCost() + ")";
    }
    
}
