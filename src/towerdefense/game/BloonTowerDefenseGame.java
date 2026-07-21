package towerdefense.game;


import java.util.*;

import towerdefense.balloons.Balloon;
import towerdefense.balloons.generator.BalloonGenerator;
import towerdefense.boards.Board;
import towerdefense.player.Player;
import towerdefense.projectiles.ExtraBombProjectile;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.TowerManager;
import towerdefense.towers.tools.evolutions.statmodifier.*;


/**
 * Representate the Bloon Tower Defense Game
 */
public abstract class BloonTowerDefenseGame {
    /** The game board */
    protected Board board;
    /** Does debug is needed */
    protected boolean debug;
    /** Current round of the game */
    protected int currentRound;
    /** Current time / "tick" of the game */
    protected int currentTick;
    /** Number of balloons required for a certain round */
    protected int balloonNumber;
    /** List of all upgrades available */
    protected List<StatModifier> upgradesAvailable;
    /** Attribute for the player */
    protected Player player;
    /** Attribute for the towerManager */
    protected TowerManager towerManager;
    /** Balloon generator */
    protected BalloonGenerator balloonGenerator;
    /** The current level of the game (Balloons with a level lower than or equal to this value may appear) */
    protected int gameLevel;
    /** Random used to generate a number of balloons per round */
    protected Random random;
    /** <code>true</code> board display with emoji activated, <code>false</code> textual display enabled */
    protected boolean isVisualDisplay;

    /**
     * Initialize the Bloon Tower Defense game
     * 
     * @param board         the board on which we play
     * @param debug         if debug = <code>True</code> then debug information will be displayed
     * @param isVisualDisplay <code>true</code> board display with emoji activated, <code>false</code> textual display enabled
     */
    public BloonTowerDefenseGame(Board board, boolean debug, boolean isVisualDisplay) {
        this.board = board;
        this.debug = debug;
        this.currentRound = 0;
        this.currentTick = 0;
        this.balloonNumber = 0; 
        this.gameLevel = 1;
        this.player = new Player(GameConstants.PLAYER_STARTING_LIFE, GameConstants.PLAYER_STARTING_MONEY);
        this.random = new Random();
        this.isVisualDisplay = isVisualDisplay;

        // Initialization of all available upgrades
        this.initUpgrades();
    }

    /**
     * Get the Board on which the game wil be played
     * @return the Board on which the game wil be played
     */
    public Board getBoard(){
        return this.board;
    }

    /**
     * Run the Bloon Tower Defense game loop
     */
    public void play() {
        
        // Setup generators
        setupGame();
        
        if(this.debug){
            System.out.println(board.getTextualRepresentation());
        }
        while (this.player.isAlive()) {
            
            /* Actions before starting the round */
            preRoundActions();
        
            /* Start the round */
            this.currentRound += 1;
            

            /* Generate balloons */
            nbBalloonsPerRound();
            gameLevelEnhancer();
            this.balloonGenerator.generateBalloons(this.balloonNumber, this.board,this.gameLevel);
            

            /* ------- INITIAL BOARD [Before Round] ------- */
            /* Debug ? */
            if (this.debug) {
                System.out.println(allInformationsDebug());
            }
            /* Visual display? */
            if(this.isVisualDisplay){
                board.visualDisplay();
            } else {
                board.display();
            }
            

            /* Balloons travels the path */
            while (!(board.getActiveBalloons().isEmpty()) && this.player.getLife() > 0) {

                /* Balloons moving and towers shooting on balloons */
                tick();
                
                /* Debug ? */
                if (this.debug) {
                    System.out.println(allInformationsDebug());
                }
                /* Visual display? */
                if(this.isVisualDisplay){
                    board.visualDisplay();
                } else {
                    board.display();
                }
                this.removeInactiveBalloons();
                
            }
        }

        System.out.println("The game is finished ! You have reached round n°" + this.getCurrentRound());

    }


    /**
     * Advances the game by one tick.
     * - Moves all balloons
     * - Triggers the action of the towers
     */
    public void tick() {
        for (Tower t : board.getAllActiveTowers()){
            t.act(currentTick);
        }

        for (Balloon balloon : board.getActiveBalloons()){
            balloon.update();
        }

        this.currentTick +=1;
    }


    /**
     * Remove balloons that are no longer active : 
     * - shut-down balloons (Give money to the player)
     * - balloons that have reached the finish line (Remove some of the player's health)
     * 
     * See the Game Design file for more infos (Spec_BloonTDG.md)
     */
    public void removeInactiveBalloons(){
        for(Balloon b: board.getActiveBalloons()){
            if(b.isDead()){
                this.player.gainMoney((int)(b.getDefaultLife() * 20));
            }
            if(b.isFinishedPath()){
                this.player.dealDamage((int)b.getDefaultLife());
            }
        }
        board.getActiveBalloons().removeIf(balloon -> balloon.isDead() || balloon.isFinishedPath());
    }

    

    /**
     * Get the current round of the game
     * 
     * @return the current round of the game
     */
    public int getCurrentRound() {
        return this.currentRound;
    }

    /**
     * Get the current tick of the game
     * 
     * @return the current tick of the game
     */
    public int getCurrentTick() {
        return this.currentTick;
    }

    /**
     * Setting up the game by initializing instances of generators and tools that do not change during gameplay.
     */
    protected abstract void setupGame();

    /**
     * manages all of the prerounds Actions
     */
    protected abstract void preRoundActions();



    /**********************************/
    /*       Upgrades Methods         */
    /**********************************/
    
    
    /**
     * Initializes the list of all upgrades available in the game.
     * <p> This method is designed to be overridden by subclasses or specialized 
     * game modes to define a custom set of available upgrades.</p>
     */
    protected void initUpgrades() {
        this.upgradesAvailable = new ArrayList<>(List.of(
            // Bomb Tower
            new PowerStatModifier("damage", 1.0, false, 200),
            new RangeStatModifier("range", 1.50, true, 250),
            new CadenceStatModifier("cadence", 0.75, true, 300),
            new ProjectileStatModifier("projectile", 400, new ExtraBombProjectile(this.board)),
            
            // Gorilla Tower
            new RangeStatModifier("range", 1.50, true, 400),
            new PowerStatModifier("damage", 2.0, false, 600),
            new CadenceStatModifier("cadence", 0.30, true, 1000),
            
            // MonkeyDartTower
            new RangeStatModifier("range", 1.25, true, 100),
            new CadenceStatModifier("cadence", 0.75, true, 150),
            new PowerStatModifier("damage", 1.0, false, 100),
            
            // NeedleTower
            new RangeStatModifier("range", 1.20, true, 150),
            new CadenceStatModifier("cadence", 0.75, true, 200), 
            
            // SniperMonkeyTower
            new CadenceStatModifier("cadence", 0.75, true, 200),
            new PowerStatModifier("damage", 2.0, false, 300)
        ));
    }

    /**
     * [DEV PURPOSE]
     * Sets the list of available upgrades for the game.
     * This allows developers to completely replace the default upgrade set.
     * @param upgrades the new list of StatModifier to be available
     */
    public void setUpgradesAvailable(List<StatModifier> upgrades) {
        this.upgradesAvailable = new ArrayList<>(upgrades);
    }

    /**
     * [DEV PURPOSE]
     * Adds a single upgrade to the existing list of available upgrades.
     * Useful for developers who want to extend the default set without replacing it.
     * @param upgrade the StatModifier to add to the game
     */
    public void addUpgradeAvailable(StatModifier upgrade) {
        if (this.upgradesAvailable == null) {
            this.upgradesAvailable = new ArrayList<>();
           }
        this.upgradesAvailable.add(upgrade);
    }

    
    /**********************************/
    /*     Balloons Wave Methods      */
    /**********************************/

    /**
     * Calculate the number of balloons to deploy for the round
     * 
     * See the Game Design file for more infos (Spec_BloonTDG.md)
     */
    protected void nbBalloonsPerRound(){
        this.balloonNumber = 15 + this.currentRound  + this.random.nextInt(6);
    }

    /**
     * Increases the game level after a certain number of rounds
     * 
     * See the Game Design file for more infos (Spec_BloonTDG.md)
     */
    public void gameLevelEnhancer(){
        if(this.currentRound % 3 == 0){
            this.gameLevel += 1;
        }
    }


    /**********************************/
    /*         Debug Methods          */
    /**********************************/

    /**
     * [DEBUG PURPOSE]
     * Use to return all debug infos about the game :
     * - Balloon Status and Description
     * - Current round and time
     * 
     * @return all debug infos about the game
     */
    private String allInformationsDebug() {
        String debugResult = "---------- DEBUG ----------";
        debugResult += "\n Round : " + this.currentRound + " Tick : " + this.currentTick;
        debugResult += "\n" + board.getTowersInformation();
        debugResult += "\n" + board.getBalloonsStillAlive();

        debugResult += "\n\n---------- END DEBUG ----------";
        return debugResult;
    }

}