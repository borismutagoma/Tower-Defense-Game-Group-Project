package towerdefense.towers.tools.generator;

import java.util.*;

import towerdefense.boards.Board;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.*;
import towerdefense.util.Position;

/**
 * Generates Towers instances of the selected type
 */
public class TowerGenerator {

    /** List of recipes for all towers, allowing the generation of Tower-type instances */
    protected List<TowerSpec> towersRecipes;

    /** The board on which the towers will be placed */
    protected Board board;

    /**
     * Generates towers for every type on the board
     * @param board the board on which the towers will be placed
     */
    public TowerGenerator(Board board) {
        this.board = board;
       
        this.towersRecipes = new ArrayList<>();
        initRecipes();
    }


    /**
     * Initialization recipes used by this TowerGenerator
     * Usage: Can be overridden in a subclass of TowerGenerator to create another list
     * (Developer methods are also available to manually modify the list)
     */
    protected void initRecipes(){
        // Single Target Towers
        this.towersRecipes.add(new BombSpec());
        this.towersRecipes.add(new GorillaSpec());
        this.towersRecipes.add(new MonkeyDartSpec());
        this.towersRecipes.add(new NeedleSpec());
        this.towersRecipes.add(new SniperMonkeySpec());
        // Multi-Target Tower
        this.towersRecipes.add(new FreezeSpec());
        this.towersRecipes.add(new SlowSpec());
    }

    /**
     * Get the Board used by this
     * @return the Board used by this
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get all the recipes for the towers 
     * @return all the recipes for the towers 
     */
    public List<TowerSpec> getTowersRecipes() {
        return towersRecipes;
    }

    /**
     * Get the TowerFactory instance corresponding to the given index.
     * @param index the desired TowerFactory instance index
     * @return the TowerFactory instance corresponding to the given index.
     */
    public TowerSpec getRecipe(int index){
        if(index < 0 || index >= this.towersRecipes.size()){
            return null;
        }
        return this.towersRecipes.get(index);
    }


    /**
     * Set the list of recipes for the towers using {@link TowerFactory}
     * Note: A list is already configured by default.
     * @param towersRecipes the list of recipes for the towers
     */
    public void setTowersRecipes(List<TowerSpec> towersRecipes) {
        this.towersRecipes = new ArrayList<TowerSpec>(towersRecipes); // Deep copy
    }

    /**
     * Checks if the recipe is in the list; <code>True</code> if it is, <code>False</code> otherwise
     * @param recipe The recipe to check
     * @return true if the recipe is in the list, false otherwise
     */
    public boolean containsRecipe(TowerSpec recipe){
        return this.towersRecipes.contains(recipe);
    }

    /**
     * DEV USAGE
     * Adds a recipe to the existing list
     * @param recipe the recipe to be added
     */
    public void addRecipe(TowerSpec recipe){
        this.towersRecipes.add(recipe);
    }

    /**
     * DEV USAGE
     * Removes a recipe from the list if it is contained in; otherwise, displays an error.
     * @param i the index of the recipe to be removed
     */
    public void removeRecipe(int i){
        if(i >= 0 && i < this.towersRecipes.size()){
            this.towersRecipes.remove(i);
        } else {
            System.out.println("DEV ERROR :  Please specify a recipe from the recipe list \n" + 
            "Hint : Use the getter to get information about the current status of the list");
        }
    }

    /**
     * Create a tower instance and place it on the board if possible.
     * @param recipe The recipe that will be transformed into a tower
     * @param coordinates The coordinates on which the tower will be placed
     */
    public void placeTower(TowerSpec recipe,Position coordinates){

        if (!this.containsRecipe(recipe)) {
            System.out.println("Sorry, either you don't have this recipe or it doesn't exist.");
            return;
        }


        // NOTE : Double security with the buyTower method of the Player class
        if(!board.getAllowTowersOnPath() && board.getCell(coordinates).isOnPath()){
            System.out.println("Sorry, you cannot place the tower on this coordinates : " + coordinates);
            return;
        }

        Tower tower = recipe.create(coordinates,this.board);
        tower.addToBoard(this.board);  

    }
}
