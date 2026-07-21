package towerdefense.strategy;

import java.util.*;

import towerdefense.balloons.Balloon;
import towerdefense.boards.Board;
import towerdefense.cell.Cell;
import towerdefense.towers.Tower;

/**
 * A class strategy that chooses the balloon closest to the exit; the strategy is
 * applied to a tower.
 */
public class FirstOutStrategy implements Strategy {

    /** the tower that use this fire Strategy */
    private Tower tower;
    /** attribut for the board */
    private Board board;

    /**
     * constructor of the Tower
     * 
     * @param tower the strategie the tower uses
     * @param board the board where the strategy will be applied
     */
    public FirstOutStrategy(Tower tower, Board board) {
        this.tower = tower;
        this.board = board;
    }


    /**
     * getter for the board
     * 
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }


    /**
     * Selects the balloon furthest along its path from those within range.
     * 
     * @return a list containing the target balloon (or an empty list if no balloons
     *         are within range)
     */
    @Override
    public List<Balloon> selectTarget() {
        List<Balloon> targets = new ArrayList<Balloon>();
        Balloon balloon = this.getFurtherBalloon();
        if (balloon != null) {
            targets.add(balloon);
        }
        return targets;
    }

    /**
     * give us the furthest balloons in the path
     * 
     * @see Strategy#getFurtherBalloon()
     * @return the furthest balloon in the path
     */
    public Balloon getFurtherBalloon() {
        List<Balloon> candidates = new ArrayList<>();
        if (this.tower.hasInfiniteRange()) {
            candidates = board.getActiveBalloons();
        } else {
            List<Cell> inRangeCells = this.tower.getCellsInRange(board);
            for (Balloon b : board.getActiveBalloons()) {
                if (inRangeCells.contains(b.getCurrentCell())) {
                    candidates.add(b);
                }
            }
        }

        Balloon furthestBalloon = null;
        int maxIndex = -1;
        for (Balloon b : candidates) {
            if (b.getCurrentIndex() > maxIndex) {
                maxIndex = b.getCurrentIndex();
                furthestBalloon = b;
            }
        }
        return furthestBalloon;
    }
}
