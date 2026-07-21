package towerdefense.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.DebugBoard;
import towerdefense.path.Path;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;

public class StrategyTest {

    private DebugBoard board;
    private MonkeyDartTower monkeyTower;
    private Balloon balloonStart;
    private Balloon balloonFar;
    private Balloon balloonAdjacentToFar;

    @BeforeEach
    public void setUp() {
        board = new DebugBoard();
        
        Path path = new Path();
        int midRow = Math.round(board.getRows() / 2);
        for (int col = 0; col < board.getColumns(); col++) {
            path.addCell(board.getCell(new Position(midRow, col)));
        }
        board.setPath(0, path); 

        
        monkeyTower = new MonkeyDartTower(new Position(midRow, 5), null);
        board.addTower(monkeyTower);

        balloonStart = new CumulusBalloon(path);          
        balloonFar = new CumulusBalloon(path);            
        balloonAdjacentToFar = new CumulusBalloon(path);

        for (int i = 0; i < 3; i++) {
            balloonFar.update();
        }
        
        for (int i = 0; i < 2; i++) {
            balloonAdjacentToFar.update();
        }

        board.addBalloon(balloonStart);
        board.addBalloon(balloonFar);
        board.addBalloon(balloonAdjacentToFar);
    }

    @Test
    public void testFirstOutStrategy() {
        FirstOutStrategy strategy = new FirstOutStrategy(monkeyTower, board);
        monkeyTower.setStrategy(strategy);

        List<Balloon> targets = strategy.selectTarget();
        
        assertFalse(targets.isEmpty());
        assertEquals(1, targets.size());
        assertEquals(balloonFar, targets.get(0));
    }
}