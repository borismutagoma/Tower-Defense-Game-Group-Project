package towerdefense.player.actions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.boards.*;
import towerdefense.player.Player;
import towerdefense.towers.specs.TowerSpec;
import towerdefense.towers.tools.TowerManager;
import towerdefense.towers.tools.generator.TowerGenerator;
import towerdefense.util.listchooser.InteractiveListChooser;

public class BuyTowerActionTest {

    private BuyTowerAction buyAction;
    private Player player;
    private Board board;
    private TowerManager towerManager;
    private InteractiveListChooser<TowerSpec> mockChooser;

    //Simulate Input
    private static InputStream systemIn;
    private static PrintStream systemOut;

    @BeforeClass // Save of System.in and Syetm.out
    public static void changeSystemOut() {
        System.out.println("tests begin");
        systemIn = System.in;
        systemOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));   // diverts System.out
    }
    @AfterClass // restauration of System.in and Syetm.out
    public static void restoreSystemInOut() throws IOException {
        System.setIn(systemIn);
        System.setOut(systemOut);
        System.out.println("tests end");
    }
    

   
   /** allows to simulate keyboard input by providing the string that would be entered
    * @param input the entered string, which will therefore be retrieved by a scanner that reads from Scanner.in 
    */
   public void simulateInput(String input) {   
      InputStream in = new ByteArrayInputStream(input.getBytes()); 
      System.setIn(in);  // diverts System.in to in
   }

   // BuyTowerAction Tests

    @BeforeEach
    void before() {
        board = new DebugBoard();
        player = new Player(100, 1000); 
        TowerGenerator generator = new TowerGenerator(board);
        towerManager = new TowerManager(generator, player, board);
        
        
        mockChooser = new InteractiveListChooser<TowerSpec>();
        buyAction = new BuyTowerAction(towerManager, mockChooser,false); 
    }

    
    @Test
    void testIsFeasibleWithMoney() {
        assertTrue(buyAction.isFeasible(player, board));
    }

    @Test
    void testIsFeasibleWithoutMoney() {
        player.spendMoney(1000); 
        assertFalse(buyAction.isFeasible(player, board));
    }

    
    @Test
    void testActionPlacement() {
        int initialTowers = board.getAllActiveTowers().size();
        assertTrue(initialTowers == 0);
        this.simulateInput("1 0 0");
        buyAction.action(player, board);
        assertTrue(player.getMoney() < 1000 || board.getAllActiveTowers().size() > initialTowers);
    }

    @Test
    void testToString() {
        assertEquals("Tower Shop", buyAction.toString());
    }
}