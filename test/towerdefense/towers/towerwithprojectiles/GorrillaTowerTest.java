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

public class GorrillaTowerTest {
    private GorrillaTower gorrillaTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.gorrillaTower = new GorrillaTower(this.position, null);
        this.strategy = new FirstOutStrategy(this.gorrillaTower, this.board);
        this.gorrillaTower.setStrategy(this.strategy);
        this.board.addTower(gorrillaTower);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.GORILLA_TOWER_COST, this.gorrillaTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.GORILLA_TOWER_RANGE, this.gorrillaTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.GORILLA_TOWER_CADENCE, this.gorrillaTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.gorrillaTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.gorrillaTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy, this.gorrillaTower.getStrategy());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.gorrillaTower.setStrategy(null);
        assertNull(this.gorrillaTower.getStrategy());
    }

    @Test
    public void testGetProjectileReturnsProjectile() {
        assertSame(SharpDartProjectile.class, this.gorrillaTower.getProjectile().getClass());
    }
    

    @Test
    public void testHasInfiniteRangeReturnsFalse() {
        assertFalse(this.gorrillaTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.gorrillaTower.canShoot(GameConstants.GORILLA_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.gorrillaTower.setLastShootTime(1);
        assertFalse(this.gorrillaTower.canShoot(GameConstants.GORILLA_TOWER_CADENCE - 1));
    }

    @Test
    public void testShootOnCumulusBalloon() {

        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.gorrillaTower.act(this.gorrillaTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnStratusBalloon() {

        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.gorrillaTower.act(this.gorrillaTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnZephyrBalloon() {

        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.gorrillaTower.act(this.gorrillaTower.getCadence());
        assertEquals(0, balloon.getLife());
    }
    @Test
    public void testEvolutionRange() {
        int initialRange = this.gorrillaTower.getRange();
        RangeStatModifier upgrade = new RangeStatModifier("range", 100, false, 300);
        
        this.gorrillaTower.addAllowedUpgrade(upgrade);
        this.gorrillaTower.addUpgrade(upgrade);

        assertEquals(initialRange + 100, this.gorrillaTower.getRange(), "La portée devrait être de 300 après l'upgrade");
    }

    @Test
    public void testEvolutionDamageMultiplier() {
        int initialDamage = this.gorrillaTower.getDamage();
        
        PowerStatModifier upgrade = new PowerStatModifier("damage", 2.0, true, 500);
        
        this.gorrillaTower.addAllowedUpgrade(upgrade);
        this.gorrillaTower.addUpgrade(upgrade);

        assertEquals(initialDamage * 2, this.gorrillaTower.getDamage(), "Les dégâts devraient avoir doublé");
    }

    @Test
    public void testMultipleUpgradesCadence() {
        int initialCadence = this.gorrillaTower.getCadence();
        
        CadenceStatModifier up1 = new CadenceStatModifier("cadence", +2, false, 100);
        CadenceStatModifier up2 = new CadenceStatModifier("cadence", +2, false, 100);
        
        this.gorrillaTower.addAllowedUpgrade(up1);
        this.gorrillaTower.addAllowedUpgrade(up2);
        this.gorrillaTower.addUpgrade(up1);
        this.gorrillaTower.addUpgrade(up2);

        assertEquals(initialCadence + 4, this.gorrillaTower.getCadence());
    }

    @Test
    public void equalsReturnsTrueIfTowersAreIdentic(){
        Tower t2 = new GorrillaTower(this.position, this.strategy);
        assertEquals(this.gorrillaTower, t2);
    }

    // setProjectile missing 
    // @Test
    // public void equalsReturnsFalseIfTowersAreNotIdentic(){}


}