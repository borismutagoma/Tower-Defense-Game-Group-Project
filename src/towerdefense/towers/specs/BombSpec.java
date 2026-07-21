package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.game.GameConstants;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwithprojectiles.BombTower;
import towerdefense.util.Position;

/**
 * Define the specific characteristics of a Bomb tower
 */
public class BombSpec implements TowerSpec {

    /**
     * Built the specific details of a bomb tower, it is used by the tower factory.
     */
    public BombSpec(){}

    /**
     * Get cost of a Bomb Tower
     * @return the cost of a Bomb Tower
     */
    @Override
    public int getCost() {
        return GameConstants.BOMB_TOWER_COST;
    }

    /**
     * Create an instance of a Bomb Tower
     * @param pos the position on which the tower will be placed
     * @param board the board on which the game is
     * @return an instance of a Bomb Tower
     */
    @Override
    public Tower create(Position pos, Board board) {
        Tower t = new BombTower(pos, new FirstOutStrategy(null, board)); // Dummy strategy to give the bomb projectile a board
        t.setStrategy(new FirstOutStrategy(t, board));
        return t;
    }

    /**
     * Returns the name of the tower for display in menus.
     * @return the name of the tower
     */
    @Override
    public String toString() {
        return "BombTower (Price: " + getCost() + ")";
    }
    
}
