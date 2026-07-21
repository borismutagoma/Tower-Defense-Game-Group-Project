package towerdefense.towers;
import static org.junit.Assert.*;
import org.junit.Test;
import towerdefense.boards.*;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;
import towerdefense.cell.Cell;
import java.util.List;

public class RangeTest {
    @Test
    public void testRangeScalingSmallBoard() {
        Board smallBoard = new ClassicalBoard(10, 10);
        MonkeyDartTower tower = new MonkeyDartTower(new Position(5, 5), null);
        
        List<Cell> cells = tower.getCellsInRange(smallBoard);
        assertEquals(25, cells.size() + 1); // ici rayon de 2 venant du centre
    }

    @Test
    public void testRangeScalingLargeBoard() {
        Board largeBoard = new ClassicalBoard(50, 50);
        MonkeyDartTower tower = new MonkeyDartTower(new Position(25, 25), null);
        List<Cell> cells = tower.getCellsInRange(largeBoard);
        assertTrue(cells.size() >= 280); // rayon de 17 alors 17 x 17 
    }    
}
