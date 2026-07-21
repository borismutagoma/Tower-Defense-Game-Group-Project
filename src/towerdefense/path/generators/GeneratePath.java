package towerdefense.path.generators;

import towerdefense.boards.Board;
import towerdefense.path.Path;

/**
 * Abstract class responsible for generating a path on a board.
 */
public abstract class GeneratePath {

    /** Board for which the path is generated */
    protected Board board;

    /** Ordered list of cells forming the path */
    protected Path path;

    /**
     * Constructs a path generator for a given board.
     *
     * @param board the board on which the path will be generated
     */
    public GeneratePath(Board board) {
        this.board = board;
        this.path = new Path();
    }

    /**
     * Returns the board associated with this path generator.
     *
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the generated path.
     *
     * @return the path as a list of cells
     */
    public Path getPath() {
        return this.path;
    }

    /**
     * Generates the path on the board.
     */
    public abstract void generate();
}

