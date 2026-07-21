package towerdefense.player.actions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.boards.ClassicalBoard;
import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;
import towerdefense.util.listchooser.RandomListChooser;
import towerdefense.strategy.FirstOutStrategy;

public class BuyUpgradeActionTest {

    private BuyUpgradeAction upgradeAction;
    private Player player;
    private ClassicalBoard board;
    private MonkeyDartTower tower;

    @BeforeEach
    void before() {
        board = new ClassicalBoard(5, 5);
        player = new Player(100, 1000);
        Position pos = new Position(0, 0);
        tower = new MonkeyDartTower(pos, null); 
        FirstOutStrategy strategy = new FirstOutStrategy(tower, board);
        tower.setStrategy(strategy); 
        
        board.getAllActiveTowers().add(tower);
        
        RandomListChooser<Upgradeable> towerLC = new RandomListChooser<>();
        RandomListChooser<StatModifier> upgradeLC = new RandomListChooser<>();
        
        upgradeAction = new BuyUpgradeAction(towerLC, upgradeLC);
    }

    @Test
    void testIsFeasible() {
        upgradeAction.isFeasible(player, board);
    }

    @Test
    void testActionExecution() {
        if (upgradeAction.isFeasible(player, board)) {
            upgradeAction.action(player, board);
        }
        assertNotNull(upgradeAction);
    }

    @Test
    void testToString() {
        assertEquals("Upgrade Shop", upgradeAction.toString());
    }
}