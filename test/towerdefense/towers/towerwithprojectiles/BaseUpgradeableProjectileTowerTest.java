package towerdefense.towers.towerwithprojectiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import towerdefense.boards.ClassicalBoard;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.util.Position;

public class BaseUpgradeableProjectileTowerTest {

    private MonkeyDartTower tower; 
    private ClassicalBoard board;


    @BeforeEach
    void before() {
        board = new ClassicalBoard(10, 10);
        Position pos = new Position(5, 5);
        tower = new MonkeyDartTower(pos, null); 
        
    }

    @Test
    void testStatsCalculationWithUpgrades() {
        int initialDamage = tower.getDamage();
        StatModifier damageBoost = new PowerStatModifier("damage", 5.0, false, 50);
        tower.addUpgrade(damageBoost);
        
        assertEquals(initialDamage + 5, tower.getDamage());
    }

    @Test
    void testGetRange() {
        int initialRange = tower.getRange();
        StatModifier rangeBoost = new PowerStatModifier("range", 2.0, false, 50);
        tower.addUpgrade(rangeBoost);
        
        assertEquals(initialRange + 2, tower.getRange());
    }

    @Test
    void testSellPriceCalculation() {
        int costBase = tower.getCost();
        int upgradeCost = 100;
        tower.addUpgrade(new PowerStatModifier("power", 1.0, false, upgradeCost));
        
        int expectedSellPrice = (int) ((costBase + upgradeCost) * 0.5);
        assertEquals(expectedSellPrice, tower.sell(board));
    }
}