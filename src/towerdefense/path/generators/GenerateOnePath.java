package towerdefense.path.generators;

import java.util.*;

import towerdefense.boards.Board;
import towerdefense.cell.Cell;
import towerdefense.path.Path;
import towerdefense.util.exceptions.MaxIterationReachedException;

/**
 * Generates a single valid path on a board.
 * The generated path starts on the left border of the board 
 */
public class GenerateOnePath extends GeneratePath {

    /** Additional number of cells for the path 
     * (The path is a minimum size equal to the number of columns by default) */
    protected int minAdditionalCell;

    /**
     * Creates a path generator for a board.
     *
     * @param board the board on which the path will be generated
     */
    public GenerateOnePath(Board board) {
        super(board);
        this.minAdditionalCell = 2; //An arbitrary number, must be less than the total number of cells
    }

    /**
     * Generates one valid path for the board.
     * The method randomly selects a starting cell on the left edge of the board, 
     * then chooses one of the neighboring cells that is not yet part of the path. 
     * If the path is invalid (e.g., the chosen cell is surrounded by cells that are already part of the path), 
     * the generation process restarts.
     * @throws MaxIterationReachedException in the event that no path has been found after a certain number of attempts
     * 
     * @see GeneratePath#generate()
     */
    @Override
    public void generate() {
        boolean validPath = false;
        int attempts = 0;
        int MAX_ATTEMPTS = 1000;

        while (!validPath && attempts < MAX_ATTEMPTS) {

            this.path = new Path();
            attempts ++;
            
        
            try{
                // Starting left
                int startRow = (int) (Math.random() * board.getRows());
                Cell currCell = board.getGrid()[startRow][0];
                this.path.addCell(currCell);

                // Ending right
                while(currCell.getPosition().getY() < this.board.getColumns() - 1){
                    Cell newCell = getNextCellPath(currCell);
                    this.path.addCell(newCell);
                    currCell = newCell;
                }

                if(this.path.size() >= board.getColumns() + this.minAdditionalCell){
                    validPath = true;
                } else{
                    this.path.clear(); // Remove the cells from the path and take away their status  
                } 
            } catch (IllegalStateException e) { // If getNextCellPath isn't finding a new cell
                this.path.clear(); 
            }
        }

        if(!validPath){
            throw new MaxIterationReachedException("Unable to generate a valid path after " + MAX_ATTEMPTS +  " iterations.\n Hint: Try changing the board or generation settings.");
        }

        // Add to the board
        board.setPath(0, this.path);
    }

    /**
     * Returns a randomly selected next cell among the valid neighbours of the current cell.
     * A valid neighboring cell is a cell above, below or to the right of the current cell that is not one of the neighbors.
     * The neighboring cell is obviously on the board.
     *
     * @param currentCell the current cell in the path
     * @return a valid neighbouring cell
     * @throws IllegalStateException if no valid neighbouring cell exists
     */
    private Cell getNextCellPath(Cell currentCell) {
        List<Cell> potentialNeighbours = new ArrayList<>();
        List<Cell> adjacentCells = board.getAdjacentCells(currentCell,1,false);  
        double currentY = currentCell.getPosition().getY();

        for (Cell cell : adjacentCells) {
            // Not on path and not on the left
            double cellY = cell.getPosition().getY();
            if (!cell.isOnPath() && cellY >= currentY) {

                // Favor cell on the right
                if(cellY > currentY){
                    potentialNeighbours.add(cell);
                    potentialNeighbours.add(cell);
                } else{
                    potentialNeighbours.add(cell);
                }
            }
        }

        if (potentialNeighbours.isEmpty()) {
            throw new IllegalStateException("No neighbors available to continue the path from " + currentCell);
        }

        return potentialNeighbours.get((int) (Math.random() * potentialNeighbours.size()));
    }
}


