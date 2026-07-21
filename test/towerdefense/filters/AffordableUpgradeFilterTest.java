package towerdefense.filters ;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.statmodifier.* ;

public class AffordableUpgradeFilterTest {

    private AffordableUpgradeFilter filter;
    private Player player;
    private List<StatModifier> upgrades;

    @BeforeEach
    public void before() {

        player = new Player();
        filter = new AffordableUpgradeFilter(player);

        upgrades = List.of(
            new CadenceStatModifier("cadence1", 2.5 , true, 20),
            new CadenceStatModifier("cadence2", 2.5 , true, 200),
            new CadenceStatModifier("cadence3", 2.5 , true, 60)
        );
    }

    @Test
    void testNoAffordableUpgrades() {
        this.player.setMoney(0);
        List<StatModifier> result = filter.applyFilter(upgrades);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAllUpgradesAffordable() {
        this.player.setMoney(1000);
        List<StatModifier> result = filter.applyFilter(upgrades);
        assertEquals(3, result.size());
    }

    @Test
    void testMixedUpgrades() {
        this.player.setMoney(100);
        List<StatModifier> result = filter.applyFilter(upgrades);
        assertEquals(2, result.size());
    }
}