package towerdefense.strategy;

import java.util.List;

import towerdefense.balloons.Balloon;
import towerdefense.boards.Board;

/**
 * Strategy used by a tower to select a target balloon.
 */
public interface Strategy {

    /**
     * getter for the board
     * 
     * @return the board
     */
    public Board getBoard();

    /**
     * Selects a balloon among all active balloons.
     * 
     * @return the selected balloon or null if none available
     */
    List<Balloon> selectTarget();

    /**
     * Get the balloon that's closest to the exit of its path
     * @return the balloon that's closest to the exit of its path
     */
    Balloon getFurtherBalloon();
}
