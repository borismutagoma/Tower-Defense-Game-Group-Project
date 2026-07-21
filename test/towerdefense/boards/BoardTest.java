package towerdefense.boards;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import towerdefense.cell.*;
import towerdefense.balloons.* ;


/**
 * Test class for Board
 */
public class BoardTest {


    private  Board board;
    private Balloon balloon1 ; 
    private Balloon balloon2 ; 
    private Balloon balloon3 ;
    

    /**
     * Test if the number of rows is correctly initialized
     */
    @BeforeEach
    public void before(){
        this.board = new ClassicalBoard(10, 20);
        this.balloon1 = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.balloon2 = new StratusBalloon(this.board.getPathOfIndex(0));
        this.balloon3 = new ZephyrBalloon(this.board.getPathOfIndex(0));
    }


    /**
     * test the getter of nb  rows
     */
    @Test
    public  void testGetRows(){
        assertEquals(10, board.getRows());
    }
    /**
     * test the getter of nb columns
     */
    @Test 
    public  void testGetComlumns(){
        assertEquals(20,board.getColumns());
    }

    /**
     * Tests that the grid is created and not null
     */
    @Test
    public void testGridNotNull(){
        Cell[][] grid = board.getGrid();
        assertNotNull(grid);
        assertEquals(10, grid.length);
        assertEquals(20, grid[0].length);
    }


    @Test 
    public void testAddBalloonWhenThereIsNoBalloon(){
        assertEquals(0, this.board.getActiveBalloons().size());
    }

    @Test 
    public void testAddBalloonWhenThereIsBalloons(){
        this.board.addBalloon(this.balloon1);
        assertEquals(1, this.board.getActiveBalloons().size());

        this.board.addBalloon(this.balloon2);
        this.board.addBalloon(this.balloon3);
        assertEquals(3, this.board.getActiveBalloons().size());
    }

    @Test 
    public void testGetNbPath(){
        assertEquals(1, this.board.getNbPath());
    }


    /**
     * Tests that each cell in the grid is initialized.
     */
    @Test
    public void testGridCellsInitialized() {
        Cell[][] grid = board.getGrid();

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                assertNotNull(grid[i][j]);
            }
        }
    }



    
}
