package towerdefense.towers.tools.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import towerdefense.boards.*;
import towerdefense.towers.specs.*;

public class TowerGeneratorTest {

    private Board board1;
    private TowerGenerator tGenerator;

    @BeforeEach
    public void before(){
        board1 = new DebugBoard();
        tGenerator = new TowerGenerator(board1);
    }

    @Test
    public void initializationIsCorrect(){
        assertEquals(tGenerator.getBoard(),board1);
        assertTrue(tGenerator.getTowersRecipes().size() == 7); // Number of towers by default
    }

    @Test
    public void settingATowersReceipesIsAvailable(){
        List<TowerSpec> newTowersReceipes = new ArrayList<>();
        newTowersReceipes.add(new BombSpec());

        tGenerator.setTowersRecipes(newTowersReceipes);
        assertTrue(tGenerator.getTowersRecipes().size() == 1); 
    } 

    @Test
    public void containsReceipeReturnsTrueIfRecipeInTheList(){
        assertTrue(tGenerator.containsRecipe(tGenerator.getRecipe(0)));
    }
    

    // NEED TO BE FIX AND ENHANCE


    // @Test
    // public void containsReceipeReturnsFalseIfRecipeIsNotInTheList(){
    //     Board board2 = new RandomBoard(10, 10, 5);

    //     TowerSpec recipe1Bis= new BombSpec() ;

    //     TowerFactory recipe2 = p -> {
    //             Tower t = new BombTower(p, null);
    //             t.setStrategy(new BombStrategy(t, board2));
    //             return t;
    //         };


    //     assertFalse(tGenerator.containsRecipe(recipe1Bis));
    //     assertFalse(tGenerator.containsRecipe(recipe2));
    // }

    // @Test
    // public void addRecipeAddsARecipeAtTheEnd(){
    //     List<TowerFactory> emptyList = new ArrayList<>();

    //     tGenerator.setTowersRecipes(emptyList);
    //     tGenerator.addRecipe(recipe1);

    //     assertTrue(tGenerator.containsRecipe(recipe1));
    // }

    // @Test
    // public void removeRecipeRemovesTheCorrectRecipe(){ 
    //     TowerFactory recipe = tGenerator.getRecipe(0);
    //     tGenerator.removeRecipe(0);
    //     assertFalse(tGenerator.containsRecipe(recipe));
        
    // }

    // @Test
    // public void placeTowerGeneratesATowerOnTheBoard(){
    //     TowerFactory recipe = this.tGenerator.getRecipe(0);

    //     assertTrue(board1.getAllActiveTowers().isEmpty());

    //     tGenerator.placeTower(recipe, new Position(0, 0));

    //     assertFalse(board1.getAllActiveTowers().isEmpty());

    //     Tower tower1 = board1.getAllActiveTowers().get(0);
    //     Tower tower1_bis = recipe.create(new Position(0, 0));

    //     assertEquals(tower1,tower1_bis);
    // }
}

