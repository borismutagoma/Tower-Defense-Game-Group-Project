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

public class NeedleTowerTest {
    private NeedleTower needleTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.needleTower = new NeedleTower(this.position, null);
        this.strategy = new FirstOutStrategy(this.needleTower, this.board);
        this.needleTower.setStrategy(this.strategy);
        this.board.addTower(needleTower);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.NEEDLE_TOWER_COST, this.needleTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.NEEDLE_TOWER_RANGE, this.needleTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.NEEDLE_TOWER_CADENCE, this.needleTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.needleTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.needleTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy, this.needleTower.getStrategy());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.needleTower.setStrategy(null);
        assertNull(this.needleTower.getStrategy());
    }

    @Test
    public void testGetProjectileReturnsProjectile() {
        assertSame(NeedleProjectile.class, this.needleTower.getProjectile().getClass());
    }


    @Test
    public void testHasInfiniteRangeReturnsFalse() {
        assertFalse(this.needleTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.needleTower.canShoot(GameConstants.NEEDLE_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.needleTower.setLastShootTime(1);
        assertFalse(this.needleTower.canShoot(GameConstants.NEEDLE_TOWER_CADENCE - 1));
    }

    @Test
    public void testShootOnCumulusBalloon() {

        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.needleTower.act(this.needleTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnStratusBalloon() {

        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.needleTower.act(this.needleTower.getCadence());
        assertEquals(1, balloon.getLife());
    }

    @Test
    public void testShootOnZephyrBalloon() {

        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.needleTower.act(this.needleTower.getCadence());
        assertEquals(0, balloon.getLife());
    }
    @Test
    public void testEvolutionRange() {
        int initialRange = this.needleTower.getRange();
        
        RangeStatModifier upgrade = new RangeStatModifier("range", 40, false, 80);
        
        this.needleTower.addAllowedUpgrade(upgrade);
        this.needleTower.addUpgrade(upgrade);

        assertEquals(initialRange + 40, this.needleTower.getRange());
    }

    @Test
    public void testEvolutionDamage() {
        int initialDamage = this.needleTower.getDamage();
    
        PowerStatModifier upgrade = new PowerStatModifier("damage", 1.0, false, 120);
        
        this.needleTower.addAllowedUpgrade(upgrade);
        this.needleTower.addUpgrade(upgrade);

        assertEquals(initialDamage + 1, this.needleTower.getDamage());
    }

    @Test
    public void testMultipleUpgradesCadence() {
        int initialCadence = this.needleTower.getCadence();
        
        CadenceStatModifier upgrade = new CadenceStatModifier("cadence", +2, false, 60);
        
        this.needleTower.addAllowedUpgrade(upgrade);
        this.needleTower.addUpgrade(upgrade);

        assertEquals(initialCadence + 2, this.needleTower.getCadence());
    }

    @Test
    public void equalsReturnsTrueIfTowersAreIdentic(){
        Tower t2 = new NeedleTower(this.position, this.strategy);
        assertEquals(this.needleTower, t2);
    }

    // setProjectile missing 
    // @Test
    // public void equalsReturnsFalseIfTowersAreNotIdentic(){}
  
}