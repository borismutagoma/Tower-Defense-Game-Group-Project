package towerdefense.player.actions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import towerdefense.boards.ClassicalBoard;
import towerdefense.player.Player;

public class DisplayBoardActionTest {

    private Player player;
    private ClassicalBoard board;
    private DisplayBoardAction visualAction;
    private DisplayBoardAction textualAction;

    @BeforeEach
    void before() {
        board = new ClassicalBoard(5, 5);
        player = new Player(100, 1000);
    
        visualAction = new DisplayBoardAction(true);
        textualAction = new DisplayBoardAction(false);
    }

    @Test
    void testIsFeasible() {
        assertTrue(visualAction.isFeasible(player, board));
        assertTrue(textualAction.isFeasible(player, board));
    }

    @Test
    void testActionDoesNotCrash() {
        assertDoesNotThrow(() -> visualAction.action(player, board));
            
        assertDoesNotThrow(() -> textualAction.action(player, board));
    }
}