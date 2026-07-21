package towerdefense.player.actions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.*;

import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.boards.ClassicalBoard;
import towerdefense.game.GameConstants;
import towerdefense.player.Player;
import towerdefense.towers.tools.generator.TowerGenerator;
import towerdefense.util.listchooser.RandomListChooser;

public class InteractionManagerTest {

    private InteractionManager interactionManager;
    private Player player;
    private ClassicalBoard board;
    private TowerGenerator generator;

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

   // Interaction Manager Tests

    @BeforeEach
    void before() {
        board = new ClassicalBoard(5, 5);
        player = new Player(GameConstants.PLAYER_STARTING_LIFE, GameConstants.PLAYER_STARTING_MONEY);
        generator = new TowerGenerator(board);
        
        interactionManager = new InteractionManager(
            board, 
            player, 
            generator, 
            new RandomListChooser<PlayerAction>(), 
            false, 
            false
        );
    }

    @Test
    void testInitialization() {
        assertNotNull(interactionManager);
    }

    @Test
    void testRunMainMenuReturnsFalseOnNull() {
        RandomListChooser<PlayerAction> chooser = new RandomListChooser<>();
        
        InteractionManager managerWithNoActions = new InteractionManager(
            board, player, generator, 
            chooser, 
            true, false
        );
        assertNotNull(managerWithNoActions);
    }

    @Test
    void testToString() {
        assertNotNull(interactionManager.toString());
    }
}