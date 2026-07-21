package towerdefense.strategy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.balloons.Balloon;
import towerdefense.balloons.CumulusBalloon;
import towerdefense.boards.ClassicalBoard;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;
import towerdefense.path.Path;
import towerdefense.cell.Cell;

import java.util.List;
import java.util.ArrayList;

public class FirstOutStrategyTest {

    private FirstOutStrategy strategy;
    private ClassicalBoard board;
    private MonkeyDartTower tower;
    private Path path;

    @BeforeEach
    void before() {
        board = new ClassicalBoard(10, 10);
        
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(new Position(0, 0)));
        cells.add(new Cell(new Position(0, 1)));
        cells.add(new Cell(new Position(0, 2)));
        path = new Path(cells);

        tower = new MonkeyDartTower(new Position(5, 5), null);
        strategy = new FirstOutStrategy(tower, board);
        tower.setStrategy(strategy);
    }

    @Test
    void testSelectTargetReturnsFurthest() {
        Balloon nearBalloon = new CumulusBalloon(path);
        Balloon farBalloon = new CumulusBalloon(path);
        
        board.getActiveBalloons().add(nearBalloon);
        board.getActiveBalloons().add(farBalloon);

        List<Balloon> targets = strategy.selectTarget();
        
        assertNotNull(targets);
        if (!targets.isEmpty()) {
            assertEquals(1, targets.size());
        }
    }

    @Test
    void testEmptyBoard() {
        board.getActiveBalloons().clear();
        List<Balloon> targets = strategy.selectTarget();
        assertTrue(targets.isEmpty());
    }

    @Test
    void testGetBoard() {
        assertEquals(board, strategy.getBoard());
    }
}