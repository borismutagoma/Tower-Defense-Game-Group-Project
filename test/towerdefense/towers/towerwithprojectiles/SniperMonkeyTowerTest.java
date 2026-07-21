package towerdefense.towers.towerwithprojectiles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.*;
import towerdefense.game.GameConstants;
import towerdefense.projectiles.VerySharpDartProjectile;
import towerdefense.strategy.*;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;

public class SniperMonkeyTowerTest {
    private SniperMonkeyTower sniperMonkeyTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.sniperMonkeyTower = new SniperMonkeyTower(this.position, null);
        this.strategy = new FirstOutStrategy(this.sniperMonkeyTower, this.board);
        this.sniperMonkeyTower.setStrategy(this.strategy);
        this.board.addTower(sniperMonkeyTower);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.SNIPER_TOWER_COST, this.sniperMonkeyTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.SNIPER_TOWER_RANGE, this.sniperMonkeyTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.SNIPER_TOWER_CADENCE, this.sniperMonkeyTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.sniperMonkeyTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.sniperMonkeyTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy, this.sniperMonkeyTower.getStrategy());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.sniperMonkeyTower.setStrategy(null);
        assertNull(this.sniperMonkeyTower.getStrategy());
    }

    @Test
    public void testGetProjectileReturnsProjectile() {
        assertSame(VerySharpDartProjectile.class, this.sniperMonkeyTower.getProjectile().getClass());
    }

    @Test
    public void testHasInfiniteRangeReturnsTrue() {
        assertTrue(this.sniperMonkeyTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.sniperMonkeyTower.canShoot(GameConstants.SNIPER_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.sniperMonkeyTower.setLastShootTime(1);
        assertFalse(this.sniperMonkeyTower.canShoot(GameConstants.SNIPER_TOWER_CADENCE - 1));
    }

    @Test
    public void testShootOnCumulusBalloon() {

        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.sniperMonkeyTower.act(this.sniperMonkeyTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnStratusBalloon() {

        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.sniperMonkeyTower.act(this.sniperMonkeyTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnZephyrBalloon() {

        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.sniperMonkeyTower.act(this.sniperMonkeyTower.getCadence());
        assertEquals(0, balloon.getLife());
    }
    @Test
    public void testEvolutionDamage() {
        int initialDamage = this.sniperMonkeyTower.getDamage();
        
        PowerStatModifier upgrade = new PowerStatModifier("damage", 5.0, false, 200);
        
        this.sniperMonkeyTower.addAllowedUpgrade(upgrade);
        this.sniperMonkeyTower.addUpgrade(upgrade);

        assertEquals(initialDamage + 5, this.sniperMonkeyTower.getDamage(), "Les dégâts du Sniper devraient être augmentés de 5");
    }

    @Test
    public void testEvolutionCadence() {
        int initialCadence = this.sniperMonkeyTower.getCadence();

        CadenceStatModifier upgrade = new CadenceStatModifier("cadence", -3, false, 150);
        
        this.sniperMonkeyTower.addAllowedUpgrade(upgrade);
        this.sniperMonkeyTower.addUpgrade(upgrade);

        assertEquals(initialCadence - 3, this.sniperMonkeyTower.getCadence(), "Le Sniper devrait tirer plus vite (délai 7)");
    }

    @Test
    public void testCadenceUpgradeImpact() {
        this.sniperMonkeyTower.setLastShootTime(GameConstants.SNIPER_TOWER_CADENCE);
        
        assertFalse(this.sniperMonkeyTower.canShoot(GameConstants.SNIPER_TOWER_CADENCE + 1));

        CadenceStatModifier speedUp = new CadenceStatModifier("cadence", -GameConstants.SNIPER_TOWER_CADENCE , false, 200);
        this.sniperMonkeyTower.addAllowedUpgrade(speedUp);
        this.sniperMonkeyTower.addUpgrade(speedUp);

        assertTrue(this.sniperMonkeyTower.canShoot(GameConstants.SNIPER_TOWER_CADENCE + 1));
    }

    @Test
    public void equalsReturnsTrueIfTowersAreIdentic(){
        Tower t2 = new SniperMonkeyTower(this.position, this.strategy);
        assertEquals(this.sniperMonkeyTower, t2);
        assertTrue(this.sniperMonkeyTower.equals(t2));
    }

    // setProjectile missing 
    // @Test
    // public void equalsReturnsFalseIfTowersAreNotIdentic(){}

}