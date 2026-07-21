package towerdefense.player.actions;

import java.util.*;

import towerdefense.boards.Board;
import towerdefense.filters.ActionsFilter;
import towerdefense.player.Player;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.TowerSpec;
import towerdefense.towers.tools.TowerManager;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.towers.tools.generator.TowerGenerator;
import towerdefense.util.listchooser.*;

/**
 * The Interaction Manager manages the display of the "main menu" and calls up the "submenus," whether they are interactive or not.
 */
public class InteractionManager {
    /* attribut for the list to initialise player actions */
    private List<PlayerAction> availableActions;
    /** Stores the recipes of all towers that the player can buy. */
    private TowerManager towerManager ;
    /** List chooser from the main menu can be interactive or random (not recommended) */
    private ListChooser<PlayerAction> mainLC;

    /** The action filter used to launch the main menu and only display actions that the player can perform */
    private ActionsFilter actionsFilter;

    /** Choice between text and "graphic" display */
    private boolean isVisualDisplay;


    /**
     * Create the interaction menu for a player, with their "set" of towers purchasable on a certain board.
     * 
     * Note : For optimal gameplay, it's recommended to use an interactive ListChooser parameter to gain some control. 
     *        Submenus can be automated using the `isRandom` attribute,
     *        this option is useful for testing the game's behavior or simply letting the computer play for you.
     * 
     * @param board the game board
     * @param player the player who is interacting
     * @param generator the tower generator used for this game 
     * @param mainLC the desired listchooser to execute the main menu
     * @param isRandom wether a bot who going to play or with user interactions.
     * @param isVisualDisplay the board will be displayed in the main menu either visually or in text.
     */
    public InteractionManager(Board board, Player player, TowerGenerator generator, ListChooser<PlayerAction> mainLC, Boolean isRandom, Boolean isVisualDisplay){
        this.towerManager = new TowerManager(generator, player, board);
        this.mainLC = mainLC;
        this.actionsFilter = new ActionsFilter(player, board);
        this.isVisualDisplay = isVisualDisplay;


        this.availableActions = new ArrayList<>();   
        initAvailableActions(isRandom);

    }

    /**
     * Initialize the list of possible actions.
     * 
     * DEV PURPOSE : Can be overridden in a subclass to modify the list of possible actions.
     * 
     * @param isRandom make the decisions random or not. If isRandom is True, the choices will be random; otherwise, they will be interactive.
     */
    protected void initAvailableActions(boolean isRandom){
        // BuyTowerAction ListChooser
        ListChooser<TowerSpec> towerSpecLC ;
        // BuyUpgrade / SellUpgrade ListChooser
        ListChooser<Upgradeable> upgradeableTowerLC;
        ListChooser<StatModifier> upgradeLC;
        //SellTower ListChooser
        ListChooser<Tower> towerLC; 

        if(isRandom){
            towerSpecLC = new RandomListChooser<>();
            upgradeableTowerLC = new RandomListChooser<>();
            upgradeLC = new RandomListChooser<>();
            towerLC = new RandomListChooser<>();

        }
        else{
            towerSpecLC = new InteractiveListChooser<>();
            upgradeableTowerLC = new InteractiveListChooser<>();
            upgradeLC = new InteractiveListChooser<>();
            towerLC = new InteractiveListChooser<>();

            // Displaying the game board and the sale option is only available for the interactive game.
            this.availableActions.add(new DisplayBoardAction(this.isVisualDisplay));
            this.availableActions.add(new SellTowerAction(towerLC, this.towerManager));
            this.availableActions.add(new SellUpgradeAction(upgradeableTowerLC,upgradeLC, this.towerManager));
        }

        this.availableActions.add(new BuyTowerAction(this.towerManager, towerSpecLC,isRandom));
        this.availableActions.add(new BuyUpgradeAction(upgradeableTowerLC,upgradeLC));
        
    }

    /**
     * This brings up the game's main menu.
     * Choices will be made randomly or interactively depending on the game's implementation.
     * @param player the current player
     * @param board the current board being played on
     * @return boolean value, true if an action was made and false if the action choosen is the quit Menu
     */
    public boolean runMainMenu(Player player, Board board) {

        List<PlayerAction> currentlyAvailableActions = actionsFilter.applyFilter(this.availableActions);


        PlayerAction choosenAction = this.mainLC.choose("/**********************************/\n" + 
                           "/*                                */\n" + 
                           "/*           MAIN MENU            */\n" +
                           "/*                                */\n" +
                           "/**********************************/\n"        
        , currentlyAvailableActions);
        while (true){
            if(choosenAction!=null){
                choosenAction.action(player, board);
                return true;
            }
            return false;
        }
 
    }
}