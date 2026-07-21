package towerdefense.towers;

import java.util.List;

import towerdefense.balloons.Balloon;
import towerdefense.boards.Board;
import towerdefense.cell.Cell;
import towerdefense.strategy.Strategy;
import towerdefense.util.Position;



/**
 * Represent a tower in the Bloons Tower Defense game
 */
public abstract class Tower {

    /** Cost of the tower */
    protected int cost;
    /** Attack range of the tower without any upgrades */
    protected int baseRange;
    /** Number of ticks between two shots without any upgrades */
    protected int baseCadence;
    /** Last tick when the tower fired */
    protected int lastShootTime;
    /** Position of the tower on the board */
    protected Position position;
    /** Strategy used to select a target */
    protected Strategy strategy;
    
    
    /**
     * Create a new tower
     * 
     * @param cost     the cost the player will have to spend to place this tower
     * @param baseRange    the tower's attack range
     * @param cadence  the tower's rate of fire
     * @param position the current position of the tower
     * @param strategy the strategy used by the tower
     */
    public Tower(int cost, int baseRange, int cadence, Position position, Strategy strategy) {
        this.baseCadence = cadence;
        this.cost = cost;
        this.baseRange = baseRange;
        this.lastShootTime = 0;
        this.position = position;
        this.strategy = strategy;
    }

    /// <<< GETTERS & SETTERS >>> //

    /**
     * get the cost of the tower
     * 
     * @return the cost of the tower
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * get the cadence of the tower
     * 
     * @return the cadence of the tower
     */
    public int getCadence() {
        return this.baseCadence;
    }
    
    /**
     * get the range of the tower
     * 
     * @return the range of the tower
     */
    public int getRange() {
        return this.baseRange;
    }

    /**
     * get the last shoot of the tower
     * 
     * @return the last shoot time of the tower
     */
    public int getLastShootTime() {
        return this.lastShootTime;
    }

    /**
     * get the position of the tower
     * 
     * @return the position of the tower
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * return the strategy of the tower object
     * 
     * @return the strategy of the tower object
     */
    public Strategy getStrategy() {
        return this.strategy;
    }

    /**
     * return all the list of selected balloons
     * 
     * @return the list of selected balloons
     */
    public List<Balloon> getTargets() {
        return this.strategy.selectTarget();
    }

    /**
     * Updates the timestamp of the last shot fired by this tower.
     * @param lastShootTime the tick at which the last shot occurred
     */
    public void setLastShootTime(int lastShootTime) {
        this.lastShootTime = lastShootTime;
    }

    /**
     * set a new position for the Tower object
     * 
     * @param position the new position to set.
     */
    public void setPosition(Position position){
        this.position = position ;
    }

    /**
     * Sets the targeting strategy of the tower
     * 
     * @param strategy the strategy to assign to this tower
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /// <<< TOOLS >>> //


    /**
     * Add this tower to the active towers list of the board given as a parameter
     * @param board the board in which this tower will be added to the active towers
     */
    public void addToBoard(Board board) {
        board.getAllActiveTowers().add(this);
    }


    /**
     * return a bolean to see if a Tower object has an infinite range or not
     * 
     * @return <code>true </code> if the tower has an infinite range,
     *         <code>false</code> otherwise.
     */
    public boolean hasInfiniteRange() {
        return this.baseRange == -1;
    }

    /**
     * Returns the list of all board cells that are within the tower's attack range. up/left/right/down/even diagonals
     * Usage of the method in Board getAjdacentCells
     * @param board board the game board
     * @return the list of reachable cells
     */

    public List<Cell> getCellsInRange(Board board) {
        double boardDimension = Math.max(board.getColumns(), board.getRows());
        double rangeFactor = this.getRange()/100.0;
        int cellRange = (int) Math.round(rangeFactor * (boardDimension * 0.15)); // on prend 15 car on assume que pour une tour de 100 porte il couvre 15 pourcent du grid.
        cellRange = Math.max(1, cellRange);
        Cell myCell = board.getCell(this.position);
        return board.getAdjacentCells(myCell, cellRange, true);
    }
    /**
     * Checks whether a balloon is inside the tower range.
     *
     * @param balloon balloon to test
     * @param board the board that contains balloon
     * @return <code>true</code> if balloon is in range, <code>false</code>
     *         otherwise.
     */
    protected boolean isInRange(Balloon balloon, Board board) {
        if (this.hasInfiniteRange()) return true;

        double boardDimension = Math.max(board.getRows(), board.getColumns());
        double maxDistanceInCells = (this.getRange() / 100.0) * (boardDimension * 0.15);
        
        double dx = this.position.getX() - balloon.getPosition().getX();
        double dy = this.position.getY() - balloon.getPosition().getY();
        double currentDistance = Math.sqrt(dx * dx + dy * dy);

        return currentDistance <= maxDistanceInCells;
    }

    /**
     * Checks whether the tower can shoot at the current tick.
     * 
     * @param currentTick current game tick
     * @return true if enough time passed since last shot
     */
    public boolean canShoot(int currentTick) {
        return (lastShootTime == 0) || ((currentTick - lastShootTime) >= getCadence());
    }


    public boolean equals(Object o){
        if (this == o) {return true;};
        if(o == null || this.getClass() != o.getClass()) {return false;};
        Tower other = (Tower) o;
        return (this.position.equals(other.getPosition())) && 
               (this.baseCadence == other.getCadence()) &&
               (this.baseRange == other.getRange());

    }

    /**
     * Returns the amount of money earned when selling the tower.
     * The tower is removed from the board
     * @param board The board on which the tower is sold
     * @return half of the original cost
     */
    public int sell(Board board) {
        board.getAllActiveTowers().remove(this);
        return this.cost / 2;
    }


    // <<< abstract >> //

    /**
     * Makes the tower act, fires an arrow, a bomb, triggers an effect, etc
     * @param  tick the time 
     */
    public abstract void act(int tick);

    // <<< DEBUG >>> //

    /**
     * A String representation of this tower
     * 
     * @return a representation of this tower
     */
    public String getTextualRepresentation() {
        return "Type: " + this.getClass().getSimpleName() +
                " / Position: " + this.getPosition() +
                " / Strategy: " + this.getStrategy().getClass().getSimpleName();  
    }

    /**
     * Get a String representation of this tower
     * 
     * @return a String reprensentation of this tower
     */
    public String toString() {
        return "T";
    }

    /**
     * Get the visual symbol of this tower for the grid
     * 
     * @return the symbol representing the tower
     */
    public String getSymbol() {
        return "🗼 ";
    }

}