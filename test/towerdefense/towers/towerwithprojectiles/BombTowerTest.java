package towerdefense.towers.towerwithprojectiles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.balloons.*;
import towerdefense.boards.*;
import towerdefense.game.GameConstants;
import towerdefense.projectiles.BombProjectile;
import towerdefense.strategy.*;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.BombSpec;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;
import towerdefense.path.Path;

public class BombTowerTest {
    private BombTower bombTower;
    private Position position;
    private Board board;
    private Strategy strategy;

    @BeforeEach
    public void before() {
        this.board = new DebugBoard();
        this.position = new Position(4, 0);
        this.bombTower = (BombTower) new BombSpec().create(position, board); // Force the tower to be a Bomb Tower
        this.strategy = new FirstOutStrategy(this.bombTower, this.board);
        this.board.addTower(bombTower);
    }

    @Test
    public void testGetCost() {
        assertEquals(GameConstants.BOMB_TOWER_COST, this.bombTower.getCost());
    }

    @Test
    public void testGetRange() {
        assertEquals(GameConstants.BOMB_TOWER_RANGE, this.bombTower.getRange());
    }

    @Test
    public void testGetCadence() {
        assertEquals(GameConstants.BOMB_TOWER_CADENCE, this.bombTower.getCadence());
    }

    @Test
    public void testGetLastShootTime() {
        assertEquals(0, this.bombTower.getLastShootTime());
    }

    @Test
    public void testGetPosition() {
        assertEquals(this.position, this.bombTower.getPosition());
    }

    @Test
    public void testGetStrategyReturnsStrategy() {
        assertEquals(this.strategy.getClass().getName(), this.bombTower.getStrategy().getClass().getName());
        assertEquals(this.strategy.getBoard(), this.bombTower.getStrategy().getBoard());
    }

    @Test
    public void testGetStrategyReturnsNullWhenThereIsNoStrategy() {
        this.bombTower.setStrategy(null);
        assertNull(this.bombTower.getStrategy());
    }

    @Test
    public void testGetProjectileReturnsProjectile() {
        assertSame(BombProjectile.class, this.bombTower.getProjectile().getClass());
    }

    @Test
    public void testHasInfiniteRangeReturnsFalse() {
        assertFalse(this.bombTower.hasInfiniteRange());
    }

    @Test
    public void testCanShootReturnsTrue() {
        assertTrue(this.bombTower.canShoot(GameConstants.BOMB_TOWER_CADENCE));
    }

    @Test
    public void testCanShootReturnsFalse() {
        this.bombTower.setLastShootTime(1);
        assertFalse(this.bombTower.canShoot(GameConstants.BOMB_TOWER_CADENCE - 1));
    }

    @Test
    public void testShootOnCumulusBalloon() {

        Balloon balloon = new CumulusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.bombTower.act(this.bombTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnStratusBalloon() {

        Balloon balloon = new StratusBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.bombTower.act(this.bombTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

    @Test
    public void testShootOnZephyrBalloon() {

        Balloon balloon = new ZephyrBalloon(this.board.getPathOfIndex(0));
        this.board.addBalloon(balloon);
        this.bombTower.act(this.bombTower.getCadence());
        assertEquals(0, balloon.getLife());
    }

   @Test
    public void testBombTowerDamageAOE() {
        this.strategy = new FirstOutStrategy(this.bombTower, this.board);  
        this.bombTower.setStrategy(this.strategy);
        
        Path path = this.board.getPathOfIndex(0);
        Balloon balloonA = new CumulusBalloon(path);
        Balloon balloonB = new CumulusBalloon(path);
        this.board.addBalloon(balloonA);
        this.board.addBalloon(balloonB);
        if (path != null && path.getStart() != null) {
            this.bombTower = new BombTower(path.getStart().getPosition(), this.strategy);
        }
        this.board.addTower(bombTower);
        this.bombTower.act(this.bombTower.getCadence());

    
        assertEquals(0, balloonA.getLife(), 0.001, "The tower failed to shoot balloonA");
        assertEquals(0, balloonB.getLife(), 0.001, "The tower failed to shoot balloonB");
    }
    @Test
    public void testEvolutionRange() {
        int initialRange = this.bombTower.getRange();
        RangeStatModifier upgrade = new RangeStatModifier("range", 50, false, 100);
        
        this.bombTower.addAllowedUpgrade(upgrade);
        this.bombTower.addUpgrade(upgrade);

        assertEquals(initialRange + 50, this.bombTower.getRange());
    }

    @Test
    public void testEvolutionDamage() {
        int initialDamage = this.bombTower.getDamage();
        
        PowerStatModifier upgrade = new PowerStatModifier("damage", 2.0, false, 150);
        
        this.bombTower.addAllowedUpgrade(upgrade);
        this.bombTower.addUpgrade(upgrade);

        assertEquals(initialDamage + 2, this.bombTower.getDamage(), "Les dégâts devraient augmenter de 2");
    }

    @Test
    public void testEvolutionCadenceMultiplier() {
        int initialCadence = this.bombTower.getCadence();
        CadenceStatModifier upgrade = new CadenceStatModifier("cadence", 0.5, true, 200);
        
        this.bombTower.addAllowedUpgrade(upgrade);
        this.bombTower.addUpgrade(upgrade);

        assertEquals((int) Math.round(initialCadence * 0.5), this.bombTower.getCadence());
    }

    @Test
    public void equalsReturnsTrueIfTowersAreIdentic(){
        Tower t2 = new BombTower(this.position, this.strategy);
        assertEquals(this.bombTower, t2);
    }

    // setProjectile missing 
    // @Test
    // public void equalsReturnsFalseIfTowersAreNotIdentic(){}

    
}