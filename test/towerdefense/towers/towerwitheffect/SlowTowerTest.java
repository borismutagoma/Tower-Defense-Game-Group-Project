package towerdefense.towers.towerwitheffect;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.*;
import towerdefense.game.GameConstants;
import towerdefense.strategy.*;
import towerdefense.util.*;


public class SlowTowerTest {

    private SlowTower slowTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.slowTower = new SlowTower(this.position, null);
        this.strategy = new FirstOutStrategy(this.slowTower, this.board);
        this.slowTower.setStrategy(this.strategy);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.SLOW_TOWER_COST, this.slowTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.SLOW_TOWER_RANGE, this.slowTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.SLOW_TOWER_CADENCE, this.slowTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.slowTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.slowTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy, this.slowTower.getStrategy());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.slowTower.setStrategy(null);
        assertNull(this.slowTower.getStrategy());
    }

    @Test
    public void testGetEffect() {
        assertEquals(Effect.SLOWED, this.slowTower.getEffect());
    }

    @Test
    public void testHasInfiniteRangeReturnsFalse() {
        assertFalse(this.slowTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.slowTower.canShoot(GameConstants.SLOW_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.slowTower.setLastShootTime(GameConstants.SLOW_TOWER_CADENCE);
        assertFalse(this.slowTower.canShoot(GameConstants.SLOW_TOWER_CADENCE-1));
    }

    @Test
    public void testApplyEffectOnCumulusBalloons() {
        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.getActiveBalloons().add(balloon);
        
        this.slowTower.act(1600);

        assertEquals(Effect.SLOWED, balloon.getEffect());
    }

    @Test
    public void testApplyEffectOnStratusBalloons() {
        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.getActiveBalloons().add(balloon);

        this.slowTower.act(1600);

        assertEquals(Effect.SLOWED, balloon.getEffect());
    }

    @Test
    public void testApplyEffectOnZephyrBalloons() {
        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.getActiveBalloons().add(balloon);

        this.slowTower.act(1600);

        assertEquals(Effect.SLOWED, balloon.getEffect());
    }
    }