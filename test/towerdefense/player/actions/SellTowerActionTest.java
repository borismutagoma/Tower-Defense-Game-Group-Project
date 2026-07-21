package towerdefense.player.actions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.boards.*;
import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.statmodifier.PowerStatModifier;
import towerdefense.towers.towerwithprojectiles.MonkeyDartTower;
import towerdefense.util.Position;

public class SellTowerActionTest {

    private Player player;
    private Board board;
    private MonkeyDartTower tower;

    @BeforeEach
    public void before() {
        this.board = new ClassicalBoard(10, 10);
        this.player = new Player(20, 1000);
        this.tower = new MonkeyDartTower(new Position(0, 0), null);
        board.getAllActiveTowers().add(tower);
    }

    @Test
    public void testSellTowerRemovesItFromBoard() {
        var towers = board.getAllActiveTowers();
        var selected = towers.get(0); 
        int refund = selected.sell(board);
        player.gainMoney(refund);
        board.getAllActiveTowers().remove(selected);
        assertFalse(board.getAllActiveTowers().contains(tower));                
        assertEquals(0, board.getAllActiveTowers().size());
    }

    @Test
    public void testSellPriceIncludesUpgrades() {
        PowerStatModifier upgrade = new PowerStatModifier("damage", 1.0, false, 100);
        tower.addUpgrade(upgrade); 
        int moneyBefore = player.getMoney();
        var selected = board.getAllActiveTowers().get(0);
        int refund = selected.sell(board);
        player.gainMoney(refund);
        board.getAllActiveTowers().remove(selected);
        assertEquals(moneyBefore + 150, player.getMoney());
    }
}