package towerdefense.projectiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.DebugBoard;
import towerdefense.path.Path;
import towerdefense.util.Position;

public class ProjectileTest {

    private DartProjectile dart;
    private BombProjectile bomb;
    private Balloon cumulus;

    @BeforeEach
    public void setUp() {
        dart = new DartProjectile();
        
        
        DebugBoard board = new DebugBoard();
        bomb = new BombProjectile(board);
        Path path = new Path();
        int midRow = Math.round(board.getRows() / 2);
        for (int col = 0; col < board.getColumns(); col++) {
            path.addCell(board.getCell(new Position(midRow, col)));
        }
        
        cumulus = new CumulusBalloon(path);
    }

    @Test
    public void testDartDamageInit() {
        assertEquals(1.0, dart.getDamage());
    }

    @Test
    public void testBombDamageInit() {
        assertEquals(2.0, bomb.getDamage());
        assertEquals(1, bomb.damageZone());
    }

    @Test
    public void testIncreaseDamage() {
        double initialDamage = bomb.getDamage();
        bomb.increaseDamage(0.5);
        assertEquals(initialDamage + (initialDamage * 0.5), bomb.getDamage());
    }

    @Test
    public void testHitBalloon() {
        double initialLife = cumulus.getLife();
        dart.hit(cumulus);
        assertEquals(initialLife - dart.getDamage(), cumulus.getLife());
    }
}