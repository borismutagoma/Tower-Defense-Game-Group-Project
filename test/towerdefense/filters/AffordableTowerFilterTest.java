package towerdefense.filters ; 

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import towerdefense.player.Player;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.*;


public class AffordableTowerFilterTest {

    private AffordableTowerFilter filter;
    private Player player;
    private List<Tower> towers;

    @BeforeEach
    public void before() {

        player = new Player();
        filter = new AffordableTowerFilter(player);

        towers = List.of(
            new MonkeyDartSpec().create(null, null),
            new BombSpec().create(null, null),
            new SlowSpec().create(null, null)
        );
    }

    @Test
    void testNoAffordableTowers() {
        this.player.setMoney(0);
        List<Tower> result = filter.applyFilter(towers);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAllTowersAffordable() {
        this.player.setMoney(1000);
        List<Tower> result = filter.applyFilter(towers);
        assertEquals(3, result.size());
    }

    @Test
    void testMixedTowers() {
        this.player.setMoney(550);
        List<Tower> result = filter.applyFilter(towers);
        assertEquals(2, result.size());
    }
}

