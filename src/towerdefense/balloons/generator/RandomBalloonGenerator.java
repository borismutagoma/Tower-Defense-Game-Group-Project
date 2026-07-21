package towerdefense.balloons.generator;

import java.util.*;
import java.util.function.Function;

import towerdefense.balloons.*;
import towerdefense.boards.Board;
import towerdefense.path.Path;

/**
 * Generates balloons of random types and places them on random paths.
 */
public class RandomBalloonGenerator implements BalloonGenerator {

    /**
     * Generates balloons randomly of the following types:
     * - ZephyrBalloon
     * - StratusBalloon
     * - CumulusBalloon
     */
    public RandomBalloonGenerator() {
    }


    /**
     * Randomly generate a number of balloons and place them on the path(s) of the
     * selected board. (does not depend on the current level of the game)
     * 
     * @param nbBalloon number of balloons that will be generated
     * @param board     the selected board where the balloons will be placed
     * @param gameLevel The game level at the start of the round
     */
    @Override
    public void generateBalloons(int nbBalloon, Board board, int gameLevel) {
        Random random = new Random();
        List<Function<Path, Balloon>> factories = this.getRecipes(); 
        for (int i = 0; i < nbBalloon; i++){
            // Select a random path from those on the board.
            Path randomPath = board.getPathOfIndex(random.nextInt(board.getNbPath()));
            // Select a random "receipe" from those in the list
            Function<Path,Balloon> randomFactory = factories.get(random.nextInt(factories.size()));
            // Create the balloon <=> Balloon b =  new ....Balloon(randomPath)
            Balloon b = randomFactory.apply(randomPath);
            board.getActiveBalloons().add(b);
        }
    }


    /**
     * Get the basic balloon recipes
     * 
     * @see BalloonGenerator#getRecipes()
     */
    @Override
    public List<Function<Path, Balloon>> getRecipes() {
        return List.of(
            CumulusBalloon::new,
            ZephyrBalloon::new,
            StratusBalloon::new
        );
    }
}


