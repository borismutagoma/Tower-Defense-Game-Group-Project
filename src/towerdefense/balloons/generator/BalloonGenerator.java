package towerdefense.balloons.generator;

import java.util.List;
import java.util.function.Function;

import towerdefense.balloons.Balloon;
import towerdefense.boards.Board;
import towerdefense.path.Path;

/** An interface for BalloonGenerator */
public interface BalloonGenerator {

  /**
   * abstract method that serve to generate balloons on a given board
   * 
   * @param nbBalloon number of balloons that will be generated
   * @param board     the selected board where the balloons will be placed
   * @param gameLevel The game level at the start of the game (can be ignored by static generators).
   */
  public void generateBalloons(int nbBalloon, Board board, int gameLevel);

  /**
   * Returns the list of balloon recipes.
   * Can be overridden to add new balloon types (/!\ They're sorted by level /!\ ).
   * @return the list of balloon recipes
   */
  public List<Function<Path,Balloon>> getRecipes();

}