package towerdefense.boards;

import towerdefense.path.generators.*;

/**
 * Generates a debug board to verify the proper functioning of the various game
 * elements
 */
public class DebugBoard extends Board {

    /**
     * Create a debug board and generates a single straight path through the middle of the board
     */
    public DebugBoard() {
        super(10, 10, 1, false);
        this.generatePath();
    }

    /**
     * Generates a single straight path through the middle of the board
     * 
     * @see Board#generatePath()
     */
    @Override
    public void generatePath() {
        GeneratePath generator = new GenerateDebugPath(this);
        generator.generate(); // In-place modification
    }

}
