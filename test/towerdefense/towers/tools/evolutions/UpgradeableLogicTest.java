package towerdefense.towers.tools.evolutions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.towers.tools.evolutions.statmodifier.PowerStatModifier;

public class UpgradeableLogicTest {

    private UpgradeableLogic logic;
    private Player player;
    private StatModifier upgrade;

    @BeforeEach
    void before() {
        logic = new UpgradeableLogic();
        player = new Player(100, 1000); 
        upgrade = new PowerStatModifier("power", 5.0, false, 100); 
    }

    @Test
    void testAddAndGetAllowedUpgrades() {
        logic.addAllowedUpgrade(upgrade);
        assertTrue(logic.getAllowedUpgrades().contains(upgrade));
        assertEquals(1, logic.getPurchasableUpgrades().size());
    }

    @Test
    void testBuyUpgradeSuccess() {
        logic.addAllowedUpgrade(upgrade);
        logic.buyUpgrade(player, upgrade);
        assertTrue(logic.hasUpgrade(upgrade));
        assertEquals(900, player.getMoney());
    }

    @Test
    void testValuesUpgraderAddition() {
        logic.addUpgrade(upgrade);
        double result = logic.valuesUpgrader(10, "power");
        assertEquals(15.0, result, 0.001);
    }

    @Test
    void testValuesUpgraderMultiplier() {
        StatModifier multi = new PowerStatModifier("power", 2.0, true, 200);
        logic.addUpgrade(multi);
        assertEquals(20.0, logic.valuesUpgrader(10, "power"), 0.001);
    }

    @Test
    void testSellUpgrade() {
        logic.addUpgrade(upgrade);
        int initialMoney = player.getMoney();
        logic.sellUpgrade(player, upgrade);
        assertFalse(logic.hasUpgrade(upgrade));
        assertEquals(initialMoney + 50, player.getMoney());
    }

    @Test
    void testCalculateSellPrice() {
        logic.addUpgrade(upgrade);
        int sellPrice = logic.calculateSellPrice(200);
        assertEquals(150, sellPrice);
    }
}