package towerdefense.towers.tools;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.boards.DebugBoard;
import towerdefense.player.Player;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.TowerSpec;
import towerdefense.towers.tools.generator.TowerGenerator;

public class TowerManagerTest {

    private DebugBoard board;
    private Player player;
    private TowerGenerator generator;
    private TowerManager manager;

    @BeforeEach
    public void before() {
        board = new DebugBoard();
        player = new Player(1000, 1000);
        generator = new TowerGenerator(board);
        manager = new TowerManager(generator, player, board);
    }

    @Test
    public void testBuyTowerSuccess() {
        if (manager.getAvailableSpecs().isEmpty()) return;

        TowerSpec spec = manager.getAvailableSpecs().get(0);

        boolean result = manager.buyTower(0, 4, spec);

        assertTrue(result);
    }

    @Test
    public void testMoneyDecreasesAfterBuy() {
        if (manager.getAvailableSpecs().isEmpty()) return;

        TowerSpec spec = manager.getAvailableSpecs().get(0);

        double before = player.getMoney();

        manager.buyTower(4, 4, spec);

        assertTrue(player.getMoney() < before);
    }

    @Test
    public void testSellTower() {
        if (manager.getAvailableSpecs().isEmpty()) return;

        TowerSpec spec = manager.getAvailableSpecs().get(0);

        manager.buyTower(0, 4, spec);

        assertFalse(board.getAllActiveTowers().isEmpty());

        Tower tower = board.getAllActiveTowers().get(0);

        double moneyBefore = player.getMoney();
        int refund = (int) tower.sell(board);

        manager.sellTower(board, tower);

        assertEquals(moneyBefore + refund, player.getMoney());
        assertFalse(board.getAllActiveTowers().contains(tower));
    }

    @Test
    public void testBuyTowerFailsIfOutOfBounds() {
        TowerSpec spec = manager.getAvailableSpecs().isEmpty() ? null : manager.getAvailableSpecs().get(0);
        if (spec == null) return;

        boolean result = manager.buyTower(-10, -10, spec);

        assertFalse(result);
    }

    @Test
    public void testBuyTowerFailsIfNotEnoughMoney() {
        Player poor = new Player(100, 0);
        TowerManager m = new TowerManager(generator, poor, board);

        if (m.getAvailableSpecs().isEmpty()) return;

        TowerSpec spec = m.getAvailableSpecs().get(0);

        boolean result = m.buyTower(1, 1, spec);

        assertFalse(result);
    }

    @Test
    public void testGetAvailableSpecsFiltersByMoney() {
        for (TowerSpec spec : manager.getAvailableSpecs()) {
            assertTrue(spec.getCost() <= player.getMoney());
        }
    }
}