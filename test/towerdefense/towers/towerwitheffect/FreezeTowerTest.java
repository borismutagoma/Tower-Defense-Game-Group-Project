package towerdefense.towers.towerwitheffect;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.*;
import towerdefense.game.GameConstants;
import towerdefense.strategy.*;
import towerdefense.util.*;


public class FreezeTowerTest {

    private FreezeTower freezeTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.freezeTower = new FreezeTower(this.position, null);
        this.strategy = new FirstOutStrategy(this.freezeTower, this.board);
        this.freezeTower.setStrategy(this.strategy);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.FREEZE_TOWER_COST, this.freezeTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.FREEZE_TOWER_RANGE, this.freezeTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.FREEZE_TOWER_CADENCE, this.freezeTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.freezeTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.freezeTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy, this.freezeTower.getStrategy());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.freezeTower.setStrategy(null);
        assertNull(this.freezeTower.getStrategy());
    }

    @Test
    public void testGetEffect() {
        assertEquals(Effect.FREEZE, this.freezeTower.getEffect());
    }

    @Test
    public void testHasInfiniteRangeReturnsFalse() {
        assertFalse(this.freezeTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.freezeTower.canShoot(GameConstants.FREEZE_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.freezeTower.setLastShootTime(1);
        assertFalse(this.freezeTower.canShoot(GameConstants.FREEZE_TOWER_CADENCE-1));
    }

    @Test
    public void testApplyEffectOnCumulusBalloons() {
        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.getActiveBalloons().add(balloon);
        
        this.freezeTower.act(GameConstants.FREEZE_TOWER_CADENCE);

        assertEquals(Effect.FREEZE, balloon.getEffect());
    }

    @Test
    public void testApplyEffectOnStratusBalloons() {
        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.getActiveBalloons().add(balloon);

        this.freezeTower.act(1600);

        assertEquals(Effect.FREEZE, balloon.getEffect());
    }

    @Test
    public void testApplyEffectOnZephyrBalloons() {
        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.getActiveBalloons().add(balloon);

        this.freezeTower.act(1600);

        assertEquals(Effect.FREEZE, balloon.getEffect());
    }

}