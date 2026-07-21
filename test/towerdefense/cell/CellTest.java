package towerdefense.cell;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import towerdefense.util.*;


/**
 * Test class for Cell
 */
public class CellTest {

    /**
     * Test setPath and isOnPath - cell transitions from non-path to path
     */
    @Test
    public void testSetPathAndIsOnPath() {
        Cell cell = new Cell(new Position(5, 10));
        
        assertFalse(cell.isOnPath());
        cell.setPath();
        assertTrue(cell.isOnPath());
    }

    /**
     * Test equals method - two cells with same position should be equal
     */
    @Test
    public void testEqualsSamePosition() {
        Cell cell1 = new Cell(new Position(5, 10));
        Cell cell2 = new Cell(new Position(5, 10));
        Cell cell3 = new Cell(new Position(3, 7));
        
        assertTrue(cell1.equals(cell2), "Cells with same position should be equal");
        assertFalse(cell1.equals(cell3), "Cells with different positions should not be equal");
    }

    /**
     * Test equals ignores path state and handles non-Cell objects
     */
    @Test
    public void testEqualsIgnoresPathAndNonCell() {
        Cell cell1 = new Cell(new Position(5, 10));
        Cell cell2 = new Cell(new Position(5, 10));
        
        cell1.setPath();
        assertTrue(cell1.equals(cell2));
        assertFalse(cell1.equals("not a cell"));
    }
}

