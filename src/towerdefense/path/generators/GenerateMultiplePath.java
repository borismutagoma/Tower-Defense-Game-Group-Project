package towerdefense.path.generators;

import java.util.Random;

import towerdefense.boards.Board;
import towerdefense.cell.Cell;
import towerdefense.path.Path;

/**
 * Generates multiple straight paths on a board.
 * The paths are horizontal or vertical; it is possible to have several paths
 * that overlap.
 */
public class GenerateMultiplePath extends GeneratePath {

    /**
     * Creates a path generator for a board.
     *
     * @param board the board on which the paths will be generated
     */
    public GenerateMultiplePath(Board board) {
        super(board);
    }

    /**
     * Generates multiple straight paths on the board.
     * @see GeneratePath#generate()
     */
    @Override
    public void generate() {
        Random rand = new Random();

        // For each path to generate
        for (int i = 0; i < board.getNbPath(); i++) {
            Path currentPath = new Path();
            boolean isHorizontal = rand.nextBoolean();
            boolean isReversed = rand.nextBoolean();

            // Horizontal Path
            if (isHorizontal) {
                int row = rand.nextInt(board.getRows());
                // Define the start, the end and the step depending of isReversed
                int start, end, step;
                if (!(isReversed)) {
                    start = 0; 
                    end = board.getColumns();
                    step = 1;
                } else {
                    start = board.getColumns() - 1;
                    end = -1; // -1 due to the for loop below
                    step = -1;
                }
                for (int col = start; col != end; col += step) {
                    this.addToPath(currentPath, row, col);
                }
            }

            // Vertical Path
            else {
                int column = rand.nextInt(board.getColumns());
                int start, end, step;
                if (!(isReversed)) {
                    start = 0;
                    end = board.getRows();
                    step = 1;
                } else {
                    start = board.getRows() - 1;
                    end = -1;
                    step = -1;
                }
                for (int row = start; row != end; row += step) {
                    this.addToPath(currentPath, row, column);
                }
            }

            // Set path into the board
            board.setPath(i, currentPath);

        }
    }

    /**
     * Add the cell on (row,column) position to the selected path
     * 
     * @param path the selected path
     * @param r    the row of the cell
     * @param c    the column of the cell
     */
    private void addToPath(Path path, int r, int c) {
        Cell cell = board.getGrid()[r][c];
        path.addCell(cell);
    }

}
