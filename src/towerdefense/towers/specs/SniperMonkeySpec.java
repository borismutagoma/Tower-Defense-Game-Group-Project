package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwithprojectiles.SniperMonkeyTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Sniper Monkey tower
 */
public class SniperMonkeySpec implements TowerSpec {

    /**
     * Built the specific details of a Sniper Monkey tower, it is used by the tower factory.
     */
    public SniperMonkeySpec(){}

    /**
     * Get cost of a SniperMonkey Tower
     * @return the cost of a SniperMonkey Tower
     */
    @Override
    public int getCost() {
        return GameConstants.SNIPER_TOWER_COST;
    }

    /**
     * Create an instance of a SniperMonkey Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a SniperMonkey Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new SniperMonkeyTower(pos, null);
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }
    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "SniperMonkeyTower (Price: " + getCost() + ")";
    }
    
}
