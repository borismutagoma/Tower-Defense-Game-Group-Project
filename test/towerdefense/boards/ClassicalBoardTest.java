package towerdefense.boards;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;


import towerdefense.cell.*;
import towerdefense.path.Path;


/**
 *tests for the ClassicalBoard 
 */
public class ClassicalBoardTest {

    private ClassicalBoard board;

    /**
     * Initializes a classical board
     */
    @BeforeEach
    public void before() {
        board = new ClassicalBoard(10, 10);
    }

    /**
     * Tests that a path is generated.
     */
    @Test
    public void testPathIsGenerated() {
        assertNotNull(board.getPathOfIndex(0));
        assertTrue(board.getPathOfIndex(0).size() > 0);
    }

    /**
     * Tests that the spawn cell belongs to the path.
     */
    @Test
    public void testSpawnCellOnPath() {
        Path path = board.getPathOfIndex(0);
        assertTrue(path.getStart().isOnPath());
    }

    /**
     * Tests that the end cell belongs to the path.
     */
    @Test
    public void testEndCellOnPath() {
        Path path = board.getPathOfIndex(0);
        assertTrue(path.getEnd().isOnPath());
    }

    /**
     * Tests that all cells in the path are marked as on the path.
     */
    @Test
    public void testAllPathCellsMarked() {
        for (Cell cell : board.getPathOfIndex(0).getCells()) {
            assertTrue(cell.isOnPath());
        }
    }


    
    /**
     *  Tests that the path starts and ends on a border.
     */
    @Test
    public void testPathStartsAndEndsOnBorder() {
        Cell startOfPath = board.getPathOfIndex(0).getStart();
        Cell endOfPath = board.getPathOfIndex(0).getEnd();
        assertTrue(board.isOnBorder(startOfPath));
        assertTrue(board.isOnBorder(endOfPath));
    }

}

