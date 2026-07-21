package towerdefense.path.generators;

import towerdefense.boards.Board;
import towerdefense.cell.Cell;
import towerdefense.path.Path;
import towerdefense.util.Position;

/**
 * Create a path down the center of the board to make debugging features like towers and balloons easier.
 */
public class GenerateDebugPath extends GeneratePath{

    /**
     * Create a horizontal path down the center of the board.
     * @param board the board to debug 
     */
    public GenerateDebugPath(Board board) {
        super(board);
    }

    @Override
    /**
     * Generate an horizontal path down the center of the board
     * 
     * @see GeneratePath#generate()
     */
    public void generate() {
        int midRow = Math.round(board.getRows() / 2);
        Path path = new Path();
        for (int column = 0; column < board.getColumns(); column ++){
            // Set current cell as a path
            Cell currentCell = board.getCell(new Position(midRow, column));
            path.addCell(currentCell);
        }
        board.setPath(0, path);
    }




    



}
