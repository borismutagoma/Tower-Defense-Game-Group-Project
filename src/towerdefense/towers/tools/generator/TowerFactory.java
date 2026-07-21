package towerdefense.towers.tools.generator;

import towerdefense.towers.Tower;
import towerdefense.util.Position;

/**
 * Encapsulates the tower factory in a type (instead of using Function directly)
 * Also allows to get information about the tower without building it.
 */
public interface TowerFactory {

    // METADATA

    /**
     * Returns the tower's cost without building it, useful for player choices.
     * @return the tower's cost
     */
    int getCost();

    // additional metadata will be added later if necessary


    
    /**
     * Create a tower of a certain type (equivalent to Tower t = new ....Tower(pos,strategy))
     * @param pos the position on which the tower should be placed
     * @return the tower create
     */
    Tower create(Position pos);
}
