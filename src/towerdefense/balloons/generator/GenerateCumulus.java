package towerdefense.balloons.generator;

import java.util.List;
import java.util.function.Function;

import towerdefense.balloons.*;
import towerdefense.boards.Board;
import towerdefense.path.Path;

/**
 * A class for GenerateCumulus that implements BalloonGenerator
 * 
 * @see BalloonGenerator
 */
public class GenerateCumulus implements BalloonGenerator {

    /**
     * Generate Cumulus-style balloons
     */
    public GenerateCumulus() {
    }

    /**
     * A method that serve to generate balloons on a given board
     * 
     * @param nbBalloon number of balloons that will be generated
     * @param board     the selected board where the balloons will be placed
     * @param gameLevel The game level at the start of the game
     */
    @Override
    public void generateBalloons(int nbBalloon, Board board, int gameLevel) {

        int nbPath = board.getNbPath();

        for (int b = 0; b < nbBalloon; b++) {
            int pathIndex = b % nbPath;
            board.addBalloon(new CumulusBalloon(board.getPathOfIndex(pathIndex)));
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