package towerdefense.player ;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import towerdefense.game.GameConstants;


public class PlayerTest {

    private Player player;

    @BeforeEach
    public void before() {
        player = new Player();
    }
    
    @Test
    public void initializationIsCorrect(){
        Player p2 = new Player(10,100);
        assertTrue(player.getLife() == GameConstants.PLAYER_STARTING_LIFE);
        assertTrue(player.getMoney() == GameConstants.PLAYER_STARTING_MONEY);
        assertTrue(p2.getLife() == 10);
        assertTrue(p2.getMoney() == 100);
    }

}

