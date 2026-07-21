package towerdefense.towers.specs;

import towerdefense.boards.Board;
import towerdefense.towers.Tower;
import towerdefense.util.Position;

/**
 * Collects information from towers, this metadata is useful for player selection 
 * [DEV - Can also store all the "identifying" information of a tower and can be passed as a parameter to Tower]
 */
public interface TowerSpec {

    /**
     * Returns the tower's cost without building it, useful for player choices.
     * @return the tower's cost
     */
    int getCost();


    /**
     * Create a tower of a certain type (equivalent to Tower t = new ....Tower(pos,strategy))
     * @param pos the position on which the tower should be placed
     * @param board the board on which the tower should be placed
     * 
     * @return the tower create
     */
    Tower create(Position pos,Board board);
} 
