package towerdefense.towers.tools;

import java.util.*;

import towerdefense.boards.Board;
import towerdefense.player.Player;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.TowerSpec;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.towers.tools.generator.TowerGenerator;
import towerdefense.util.Position;




/**
 * Manage the towers without building them, generic to all towers
 */
public class TowerManager {

    /** the tower generator used for this game (Contains all available tower types) */
    private TowerGenerator generator; 
    /** the player for whom the towers are managed */
    private Player player;
    /** the game board */
    private Board board;

    /**
     * Constructs a Tower Manager, is initialized only once per player in the Interaction Manager
     * @param generator the tower generator used for this game 
     * @param player the player for whom the towers are managed
     * @param board the game board
     */
    public TowerManager(TowerGenerator generator, Player player, Board board) {
        this.generator = generator;
        this.player = player;
        this.board = board;
    }

   
    /**
     * Filter the towers that the player can buy (sufficient money)
     * @return the towers that the player can buy
     */
    public List<TowerSpec> getAvailableSpecs() {
        List<TowerSpec> affordable = new ArrayList<>();
        for (TowerSpec spec : this.generator.getTowersRecipes()) {
            if (this.player.getMoney() >= spec.getCost()) {
                affordable.add(spec);
            }
        }
        return affordable;
    }

    /**
     * Buys and places a tower on the board at the given position.
     *
     * Checks if the tower type exists, the position is valid, and the player
     * has enough money. If all checks pass, the tower is created, the cost
     * is deducted, and the tower is added to the board.
     *
     * @param x x coordinate for the tower
     * @param y y coordinate for the tower
     * @param choice the tower type to buy
     * 
     * @return <code>true</code> if the buying of tower went succesfully, <code>false</code> otherwise.
     */
    public boolean buyTower(double x, double y, TowerSpec choice){

        if(!this.generator.containsRecipe(choice)){
            System.out.println("Sorry, The choice indicated is not part of the list of recipes.");
            return false ; 
        }

        if(!this.board.inBounds(x, y)){
            System.out.println("the position entered is out the board bounds.");
            return false ;
        }

        Position position = new Position(x, y);

        if(!this.board.getAllowTowersOnPath() && this.board.getCell(position).isOnPath()){
            this.board.visualDisplay(); // Should be dependent on the board itself
            System.out.println("The position entered is a path position.");
            return false ;
        }

        if(choice.getCost() > this.player.getMoney()){
            System.out.println("Sorry, You don't have enough money to buy the tower.");
            return false ;
        }

        

        this.player.spendMoney(choice.getCost());
        Tower BoughtTower = choice.create(position, board); 
        BoughtTower.addToBoard(board);
        System.out.println("The buying of the tower went succesfully !");

        return  true ;
        }


    /**
    * Sells a tower, refunds the player and removes it from the board.
    * @param board The board where the tower is located
    * @param tower The tower to be sold
        */
    public void sellTower(Board board, Tower tower) {
        if (tower == null || board == null) {
            return;
        }
        int refund = (int) (tower.sell(board));
        this.player.gainMoney(refund);
        System.out.println("Tower sold for " + refund + ". New balance: " + this.player.getMoney());
    }

    /**
     * Sells a specific upgrade from an upgradeable tower and refunds the player.
     * @param tower The tower owning the upgrade
     * @param upgrade The stat modifier to remove
     */
    public void sellUpgrade(Upgradeable tower, StatModifier upgrade) {
        if (tower == null || upgrade == null) return;

        int moneyBack = (int) (upgrade.getCost() * 0.5);
        
        tower.removeUpgrade(upgrade);
        this.player.gainMoney(moneyBack);    
        
        System.out.println("Upgrade " + upgrade.getStatName() + " sold. Refund: " + moneyBack);
    }

    /**
     * getter for the tower Generator
     * @return the tower generator
     */

    public TowerGenerator getGenerator(){
        return this.generator;
    }
            
}
