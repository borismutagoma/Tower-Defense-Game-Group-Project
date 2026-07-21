package towerdefense.boards;

import towerdefense.path.generators.*;

/**
 * Represent a random board where all ballons will move horizontally or vertically
 * depending on their spawn position
 */
public class RandomBoard extends Board {


    /**
     * Create a random board and initialize its grid
     * 
     * @param rows    the number of rows
     * @param columns the number of columns
     * @param nbPath  the number of path which will be generated
     */
    public RandomBoard(int rows, int columns, int nbPath) {
        super(rows, columns, nbPath, true);
        this.allowTowersOnPath = true ;
        this.generatePath();
    }



    /**
     * Generates random horizontal, vertical paths on the board.
     * Each path is stored in the Hashmap map and marked as a path cell.
     * 
     * @see Board#generatePath()
     */
    @Override
    protected void generatePath() {
        GeneratePath generator = new GenerateMultiplePath(this);
        generator.generate(); // In-place modification

    }
}