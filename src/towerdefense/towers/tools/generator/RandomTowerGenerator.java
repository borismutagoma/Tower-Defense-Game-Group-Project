package towerdefense.towers.tools.generator;

import java.util.Random;

import towerdefense.boards.Board;
import towerdefense.cell.Cell;
import towerdefense.towers.Tower;
import towerdefense.towers.specs.TowerSpec;
import towerdefense.util.Position;

/**
 * Generates Towers of random types
 */
public class RandomTowerGenerator extends TowerGenerator {

    /**
     * Generates towers randomly for every type on the board
     * @param board the board on which the towers will be placed
     */
    public RandomTowerGenerator(Board board) {
        super(board);
    }

    /**
     * Create and place a certain number of towers of a certain type on the board.
     * 
     * @param factory     the type of tower we will place on the board
     * @param nbToCreate  the number of towers that will be built
     * @param allowOnPath allow the placement of towers on the path(s)
     */
    private void placeTowersRandomly(TowerSpec factory, int nbToCreate, boolean allowOnPath) {
        Random rand = new Random();
        int placed = 0;

        while (placed < nbToCreate) {
            Position position = new Position(rand.nextInt(board.getRows()), rand.nextInt(board.getColumns()));
            Cell cell = board.getCell(position);

            // "Lazy operator", if we allow it, there's no need to check if the cell is a
            // path
            boolean canPlace = allowOnPath || !(cell.isOnPath());

            if (canPlace) {
                Tower tower = factory.create(position,this.board);
                tower.addToBoard(board);
                placed++;
            }
        }
    }

    /**
     * Deliverable3 objective: Generate all types of towers, twice, at random positions on the board.
     */
    public void towerGeneration() {
        boolean allowOnPath = this.board.getAllowTowersOnPath();
        for (TowerSpec type : this.towersRecipes) {
            placeTowersRandomly(type, 2, allowOnPath);
        }
    }

    

}