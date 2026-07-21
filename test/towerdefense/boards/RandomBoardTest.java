package towerdefense.boards;

import org.junit.jupiter.api.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import towerdefense.cell.*;

public class RandomBoardTest {
    
    private Board board;

    /**
     * Initializes a random board before each test.
     */
    @BeforeEach
    public void before() {
        board = new RandomBoard(10, 10,4);
        
    }

    /** 
     * Tests that the grid is correctly initialized.
     */
    @Test
    public void testGridInitialization() {
        Cell[][] grid = board.getGrid();
        assertNotNull(grid);
        assertEquals(10, grid.length);
        assertEquals(10, grid[0].length);
        assertEquals(10, 10);
    }
}
