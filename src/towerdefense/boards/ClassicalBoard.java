package towerdefense.boards;

import towerdefense.path.generators.*;

/**
 * Represent a classical borad where all ballons follow the same predefined path
 */
public class ClassicalBoard extends Board {

    /**
     * Create a classical board and generates its path
     * 
     * @param rows    number of rows
     * @param columns number of columns
     */
    public ClassicalBoard(int rows, int columns) {
        super(rows, columns, 1, false);
        this.allowTowersOnPath = false ;
        this.generatePath();
    }


    /**
     * Generates a single path with a random shape on the board
     * 
     * @see Board#generatePath()
     */
    @Override
    public void generatePath() {
        GeneratePath generator = new GenerateOnePath(this);
        generator.generate(); // In-place modification

    }
}