package towerdefense.balloons.generator;

import java.util.*;
import java.util.function.Function;

import towerdefense.balloons.Balloon;
import towerdefense.boards.Board;
import towerdefense.path.Path;

/**
 * Generates balloons from among the available types based on the current game level. 
 * The higher the level, the fewer low-level balloons appear, until they stop appearing altogether.
 */
public class EvolvingBalloonGenerator extends RandomBalloonGenerator {
    
    /**
     * Create an evolving balloon generator
     */
    public EvolvingBalloonGenerator(){}

    /**
     * Generates balloons in ways that evolve according to the current level of play.
     * 
     * Balloon types are generated randomly but depend on the accepted game level.
     * For example, in round 10 :
     * - level 1 balloons are no longer accepted
     * - levels 2 to 4 are available.
     * 
     * See the Game Design file for more infos (Spec_BloonTDG.md)
     * @param nbBalloon number of balloons that will be generated
     * @param board     the selected board where the balloons will be placed
     * @param gameLevel The game level at the start of the game
     */
    @Override
    public void generateBalloons(int nbBalloon, Board board, int gameLevel){
        Random random = new Random();
        List<Function<Path, Balloon>> factories = this.getRecipes(); 
        int totalTypes = factories.size();
        
        
        // Limit it to the current game level (-1 to have an index) or to the number of types availabe (if the level goes higher).
        int maxIndex = Math.min(gameLevel - 1, totalTypes - 1);
        // We decide that every 3 levels up, the lowest level no longer appears
        int minIndex = Math.min(gameLevel / 3, maxIndex);


        for (int i = 0; i < nbBalloon; i++){
            // Select a random path from those on the board.
            Path randomPath = board.getPathOfIndex(random.nextInt(board.getNbPath()));
            // Select a "receipe" from those in the list
            Function<Path,Balloon> factory = factories.get(random.nextInt(maxIndex - minIndex + 1) + minIndex);
            // Create the balloon <=> Balloon b =  new ....Balloon(randomPath)
            Balloon b = factory.apply(randomPath);
            board.getActiveBalloons().add(b);
        }
    }
}
