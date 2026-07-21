package towerdefense.player.actions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.statmodifier.*;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;

public class SellUpgradeActionTest {

    private Player player;
    private MonkeyDartTower tower;

    @BeforeEach
    public void before() {
        this.player = new Player(20, 1000);
        this.tower = new MonkeyDartTower(new Position(0, 0), null);
    }

    @Test
    public void testSellUpgrade() {
        StatModifier upgrade = new PowerStatModifier("damage", 1.0, false, 100);
        tower.addUpgrade(upgrade);
        int moneyBack = (int) (upgrade.getCost() * 0.5);
        tower.removeUpgrade(upgrade);
        player.gainMoney(moneyBack);
        assertEquals(1050, player.getMoney());
        assertFalse(tower.getActiveUpgrades().contains(upgrade));
    }

    @Test
    public void testSellWithoutUpgrade() {
        int moneyBefore = player.getMoney();
        assertEquals(1000, moneyBefore);
    }
}