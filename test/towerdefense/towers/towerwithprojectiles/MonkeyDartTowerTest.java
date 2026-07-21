package towerdefense.towers.towerwithprojectiles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.*;
import towerdefense.game.GameConstants;
import towerdefense.projectiles.*;
import towerdefense.strategy.*;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;

public class MonkeyDartTowerTest {
    private MonkeyDartTower monkeyDartTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.monkeyDartTower = new MonkeyDartTower(this.position, null);
        this.strategy = new FirstOutStrategy(this.monkeyDartTower, this.board);
        this.monkeyDartTower.setStrategy(this.strategy);
        this.board.addTower(monkeyDartTower);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.MONKEY_TOWER_COST, this.monkeyDartTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.MONKEY_TOWER_RANGE, this.monkeyDartTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.MONKEY_TOWER_CADENCE, this.monkeyDartTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.monkeyDartTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.monkeyDartTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy, this.monkeyDartTower.getStrategy());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.monkeyDartTower.setStrategy(null);
        assertNull(this.monkeyDartTower.getStrategy());
    }

    @Test
    public void testGetProjectileReturnsProjectile() {
        assertSame(DartProjectile.class, this.monkeyDartTower.getProjectile().getClass());
    }

    @Test
    public void testHasInfiniteRangeReturnsFalse() {
        assertFalse(this.monkeyDartTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.monkeyDartTower.canShoot(GameConstants.MONKEY_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.monkeyDartTower.setLastShootTime(1);
        assertFalse(this.monkeyDartTower.canShoot(GameConstants.MONKEY_TOWER_CADENCE - 1));
    }

    @Test
    public void testShootOnCumulusBalloon() {

        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.monkeyDartTower.act(this.monkeyDartTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnStratusBalloon() {

        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.monkeyDartTower.act(this.monkeyDartTower.getCadence());
        assertEquals(1, balloon.getLife());
    }

    @Test
    public void testShootOnZephyrBalloon() {

        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.monkeyDartTower.act(this.monkeyDartTower.getCadence());
        assertEquals(0, balloon.getLife());
    }
    @Test
    public void testEvolutionRange() {
        int initialRange = this.monkeyDartTower.getRange();
        
        RangeStatModifier upgrade = new RangeStatModifier("range", 25, false, 50);
        
        this.monkeyDartTower.addAllowedUpgrade(upgrade);
        this.monkeyDartTower.addUpgrade(upgrade);

        assertEquals(initialRange + 25, this.monkeyDartTower.getRange());
    }

    @Test
    public void testEvolutionDamage() {
        int initialDamage = this.monkeyDartTower.getDamage();
        
        PowerStatModifier upgrade = new PowerStatModifier("damage", 1.0, false, 100);
        
        this.monkeyDartTower.addAllowedUpgrade(upgrade);
        this.monkeyDartTower.addUpgrade(upgrade);

        assertEquals(initialDamage + 1, this.monkeyDartTower.getDamage());
    }

    @Test
    public void testEvolutionCadenceMultiplier() {
        int initialCadence = this.monkeyDartTower.getCadence();

        CadenceStatModifier upgrade = new CadenceStatModifier("cadence", 0.5, true, 150);
        
        this.monkeyDartTower.addAllowedUpgrade(upgrade);
        this.monkeyDartTower.addUpgrade(upgrade);

        assertEquals(Math.round(initialCadence * 0.5), this.monkeyDartTower.getCadence());
    } 
    @Test
    public void testRangeUpgradeImpact() {
        
        RangeStatModifier rangeUp = new RangeStatModifier("range", 50.0, false, 50);
        this.monkeyDartTower.addAllowedUpgrade(rangeUp);
        this.monkeyDartTower.addUpgrade(rangeUp);

        assertEquals(150, this.monkeyDartTower.getRange());
    }

    @Test
    public void equalsReturnsTrueIfTowersAreIdentic(){
        Tower t2 = new MonkeyDartTower(this.position, this.strategy);
        assertEquals(this.monkeyDartTower, t2);
    }

    // setProjectile missing 
    // @Test
    // public void equalsReturnsFalseIfTowersAreNotIdentic(){}


}