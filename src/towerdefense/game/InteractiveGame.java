package towerdefense.game;
import towerdefense.balloons.generator.EvolvingBalloonGenerator;
import towerdefense.boards.Board;
import towerdefense.player.actions.*;
import towerdefense.towers.tools.TowerManager;
import towerdefense.towers.tools.generator.TowerGenerator;
import towerdefense.util.listchooser.InteractiveListChooser;


/**
 * A Tower Defense game with interactive actions.
 * It inherits from BloonTowerDefenseGame and and lets the player make choices during gameplay.
 *
 * @see BloonTowerDefenseGame
 */
public class InteractiveGame extends BloonTowerDefenseGame {


    /** Creates An interactive game with a board, debug mode, and number of balloons.
     * @param board the board used to play the game
     * @param debug <code>true</code> debug mode activated, <code>false</code>otherwise
     * @param isVisualDisplay <code>true</code> board display with emoji activated, <code>false</code> textual display enabled
     */    
    public  InteractiveGame(Board board, boolean debug, boolean isVisualDisplay) {
        super(board, debug, isVisualDisplay);
    }

    /** Sets up the game at the beginning: creates the player and prepares the towers */
    @Override
    protected void setupGame() {
        TowerGenerator towerGenerator = new TowerGenerator(this.board);
        this.towerManager = new TowerManager(towerGenerator, this.player, this.board);
        this.balloonGenerator = new EvolvingBalloonGenerator();
        
        System.out.println("Round Started ! You have " + player.getMoney() + " Gold.");
    }


    /**
     * Happens before each round: shows Player info, lets the player manage towers and starts the wave
     */
    @Override
    protected void preRoundActions() {
        System.out.println("\n=== PREPARATION - ROUND " + (this.currentRound + 1) + " ===");
        
        InteractiveListChooser<PlayerAction> mainLC = new InteractiveListChooser<>();
        InteractionManager interactionManager = new InteractionManager(
            this.board, this.player, this.towerManager.getGenerator(), mainLC, false, this.isVisualDisplay
        );

        System.out.println("Manage your Towers. Choose '0 - back' in the menu to START the game.");
        
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nMoney : " + player.getMoney() + " | HP : " + player.getLife());
            inMenu = interactionManager.runMainMenu(this.player, this.board);
        }
        
        System.out.println("The wave of balloons is about to begin! Get ready! 🔥\n\t________________________\n");
    }

}
