package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Monkey Dart tower
 */
public class MonkeyDartSpec implements TowerSpec {

    /**
     * Built the specific details of a Monkey Dart tower, it is used by the tower factory.
     */
    public MonkeyDartSpec(){}


    /**
     * Get cost of a MonkeyDart Tower
     * @return the cost of a MonkeyDart Tower
     */
    @Override
    public int getCost() {
        return GameConstants.MONKEY_TOWER_COST;
    }

    /**
     * Create an instance of a MonkeyDart Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a MonkeyDart Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new MonkeyDartTower(pos, null);
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }
    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "MonkeyDartTower (Price: " + getCost() + ")";
    }
    
}
