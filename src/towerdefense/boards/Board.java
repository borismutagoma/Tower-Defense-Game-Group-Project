package towerdefense.boards;

import java.util.*;

import towerdefense.balloons.Balloon;
import towerdefense.cell.Cell;
import towerdefense.path.Path;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.Upgradeable;
import towerdefense.util.Position;

/**
 * Representate a board for the Bloons Tower Defense Game
 */
public abstract class Board {

    /* The 2-dimensional grid containing cells */
    private Cell[][] grid;
    /* Number of rows in this */
    private int rows;
    /* Number of columns in this */
    private int columns;
    /* List of all active balloons on the board */
    private List<Balloon> activeBalloonsList;

    /** Represent the number of paths */
    private int nbPath;

    /** The list of paths generated on the board */
    private HashMap<Integer, Path> pathsList;

    /** Attribut for the Path */
    protected Path path;

    /* List of all active towers on the board */
    private List<Tower> allActiveTowersList;
    /* List of active upgradeable towers */
    private List<Upgradeable> activeUpgradeables;
    /** check wether placing towers on the path is allowed. */
    protected boolean allowTowersOnPath ;

    /**
     * Construct a board with the desired number of rows and columns
     * 
     * @param rows    desired number of rows
     * @param columns desired number of columns
     * @param nbPath  desired number of paths
     * @param allowTowersOnPath wether is allowed to place towers on the path on not.
     */
    public Board(int rows, int columns, int nbPath, boolean allowTowersOnPath) {
        this.rows = rows;
        this.columns = columns;
        this.activeBalloonsList = new ArrayList<>();
        this.nbPath = nbPath;
        this.pathsList = new HashMap<>();
        this.allActiveTowersList = new ArrayList<>();
        this.activeUpgradeables = new ArrayList<>();
        this.grid = new Cell[rows][columns];
        this.allowTowersOnPath = allowTowersOnPath ;
        this.generateGrid();
    }

    // ### GETTERS ###

    /**
     * Get the number of rows in this
     * 
     * @return the number of rows in this
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Get the number of columns in this
     * 
     * @return the number of columns in this
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Get the full grid of this
     * 
     * @return the full grid of this
     */
    public Cell[][] getGrid() {
        return this.grid;
    }

    /**
     * Get the list of all active balloons
     * 
     * @return the list of all active balloons
     */
    public List<Balloon> getActiveBalloons() {
        return this.activeBalloonsList;
    }

    /**
     * Get the list of all active towers
     * 
     * @return the list of all active towers
     */
    public List<Tower> getAllActiveTowers() {
        return this.allActiveTowersList;
    }

    /**
     * Get the list of upgradable active towers
     * 
     * @return the list of all active towers
     */
    public List<Upgradeable> getActiveUpgradableTowers() {
        return this.activeUpgradeables;
    }

    /**
     * return nbPath the number of path of the board
     * 
     * @return nbPath the number of path of the board
     */
    public int getNbPath() {
        return this.nbPath;
    }

    /**
     * return A path by providing the index of the path
     * 
     * @param index the key of the path list
     * @return the list of cells that represent a path
     */
    public Path getPathOfIndex(int index) {
        return this.pathsList.get(index);
    }

    /**
     * Return the number of cells marked as a path in the board
     * 
     * @return the number of cells marked as a path in the board
     */
    public int getNumberOfPathsCells() {
        int size = 0;
        for (Path path : this.pathsList.values()) {
            size += path.size();
        }
        return size;
    }

    /**
     * return the cell on the board if present
     * 
     * @param position of the cell
     * @return a cell on the board
     */
    public Cell getCell(Position position) {
        int x = (int) position.getX();
        int y = (int) position.getY();
        if (!inBounds(x, y)) {
        return null; 
        }
        return this.grid[x][y];
    }

    /**
     * check if the board does allow placing towers on the path. 
     * 
     * @return <code>true</code> if the board does allow placing towers on the path, <code>false</code> otherwise.
     */
    public boolean getAllowTowersOnPath(){
        return this.allowTowersOnPath ;
    }



    // ### SETTERS ###

    /**
     * add a balloon to the list of active balloons
     * 
     * @param balloon the balloon to be added
     */
    public void addBalloon(Balloon balloon) {
        this.activeBalloonsList.add(balloon);
    }

    /**
     * Add the selected tower to the list of active towers
     * 
     * @param tower the tower to be added
     */
    public void addTower(Tower tower) {
        this.allActiveTowersList.add(tower);
    }

    /**
     * Add the selected tower to the list of active upgradeable towers
     * 
     * @param tower the tower to be added
     */
    public void addUpgradableTower(Upgradeable tower) {
        this.activeUpgradeables.add(tower);
    }

    /**
     * Set the desired path to a certain index
     * 
     * @param index the index where the path will be placed
     * @param path  the desired path
     */
    public void setPath(int index, Path path) {
        this.pathsList.put(index, path);
    }

    // ### TOOLS ###

    /**
     * remove a balloon from the liste of active balloons
     * 
     * @param balloon the balloon to be removed
     */
    public void removeBalloon(Balloon balloon) {
        this.activeBalloonsList.remove(balloon);
    }

    /**
     * Indicates whether the given position is within the limits of the board
     * 
     * @param x the x value
     * @param y the y value
     * @return true if (x,y) is within the board
     */
    public boolean inBounds(double  x, double  y) {
        return x >= 0 && x < this.getRows() && y >= 0 && y < this.getColumns();
    }

    /**
     * Indicates whether the given cell is on the edges of the board
     * 
     * @param cell the cell to check
     * @return true if the cell is on the border , false otherwise.
     */
    public boolean isOnBorder(Cell cell) {
        double x = cell.getPosition().getX();
        double y = cell.getPosition().getY();
        return x == 0 || x == this.getRows() - 1 ||
                y == 0 || y == this.getColumns() - 1;
    }

    /**
     * return the neighboors of a cell, if the boolean is true we have all the 
     * neighbours even the diagonals one but if false it exclude the diagonals cells
     * 
     * @param currentCell the cell we are starting from
     * @param range the range of neighboors we want
     * @param isDiagonal boolean true if we want diagonal neighbours false if not
     * @return the list of cells found in those directions within range
     */

    public List<Cell> getAdjacentCells(Cell currentCell, int range, boolean isDiagonal) {
        List<Cell> neighbours = new ArrayList<>();
        int startX = (int) currentCell.getPosition().getX();
        int startY = (int) currentCell.getPosition().getY();

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                if (i == 0 && j == 0) continue;
                if (!isDiagonal && (i != 0 && j != 0)) {
                    continue;
                }

                int newX = startX + i;
                int newY = startY + j;
                if (inBounds(newX, newY)) {
                    neighbours.add(this.getGrid()[newX][newY]);
                }
            }
        }
        return neighbours;
    }


    // ### PATH GENERATION ### //

    /**
     * Generate a path for the board see the full algorithm in Path Package
     */
    abstract protected void generatePath();

    // ### INITIALIZATION ###

    /**
     * Initialize the board
     */
    public void generateGrid() {
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                this.getGrid()[i][j] = new Cell(new Position(i, j));
            }
        }
    }


    // ### OTHER ###

    /**
     * Display a graphical representation of the board
     */
    public void display() {
        String dataOfALine = "";
        int maxSpace = Integer.toString(this.rows).length();
        String spaceBuffer;
        String displayNumbersAbove = " ".repeat(maxSpace - 1);
        String separator = " ".repeat(maxSpace) + " +---+" + "+---+".repeat(this.grid[0].length - 1);

        for (int k = 0; k < this.grid[0].length; k++) {
            displayNumbersAbove += "    " + k;
        }
        System.out.println(displayNumbersAbove);
        for (int i = 0; i < this.grid.length; i++) {

            

            // Separator
            System.out.println(separator);

            // Display Ballons and Towers
            for (int j = 0; j < this.grid[i].length; j++) {
                Position pos = new Position(i, j);
                Object content = null;



                for (Balloon b : this.activeBalloonsList) {
                    if (b.getCurrentCell().getPosition().equals(pos)){
                        content = b;
                    }
                }
                if (content == null){
                    for (Tower t : this.allActiveTowersList) {
                        if (t.getPosition().equals(pos)){
                            content = t;
                        }
                    }    
                }


                // Adding the content of the cell to the line
                if (content != null) {
                    dataOfALine += ("| " + content.toString() + " |");
                } else {
                    dataOfALine += ("| " + this.grid[i][j] + " |");
                }
            }

            

            spaceBuffer = " ".repeat(maxSpace - Integer.toString(i).length());

            System.out.println((i) + " " + spaceBuffer + dataOfALine);
            dataOfALine = "";
        }
        System.out.println(separator);
        System.out.println("\n\t________________________\n");
    }

    /**
     * Display a graphical representation of the board using emoji (Not working proprely)
     */
    public void visualDisplay() {
        String dataOfALine = "";
        String displayNumbersAbove = "  ";
        int maxSpace = Integer.toString(this.rows).length();
        String spaceBuffer;

        for (int k = 0; k < this.grid[0].length; k++) {
            displayNumbersAbove += " " + k;
        }
        System.out.println(displayNumbersAbove);
        for (int i = 0; i < this.grid.length; i++) {
            
            // Display Ballons and Towers
            for (int j = 0; j < this.grid[i].length; j++) {
                Position pos = new Position(i, j);
                String content = null;



                for (Balloon b : this.activeBalloonsList) {
                    if (b.getCurrentCell().getPosition().equals(pos)){
                        content = b.getStatus();
                    }
                }
                if (content == null){
                    for (Tower t : this.allActiveTowersList) {
                        if (t.getPosition().equals(pos)){
                            content = t.getSymbol();
                        }
                    }    
                }


                // Adding the content of the cell to the line
                if (content != null) {
                    dataOfALine += content;
                } else {
                    dataOfALine += this.grid[i][j].getSymbol();
                }

                
            }

            spaceBuffer = " ".repeat(maxSpace - Integer.toString(i).length());

            System.out.println((i) + " " + spaceBuffer + dataOfALine);
            dataOfALine = "";
        }
        System.out.println("\n\t________________________\n");
    }


    /**
     * [DEBUG PURPOSE]
     * return A Textual representation of the board
     * 
     * @return A Textual representation of the board
     * 
     */
    public String getTextualRepresentation() {

        String description = "A Board of " + this.rows + " rows and " + this.columns + " columns, contains "
                + this.getNumberOfPathsCells() + " cell path, the coordinate of the cells path are : \n";

        for (Path path : this.pathsList.values()) {
            for (Cell c : path.getCells()) {
                description = description + "(x=" + c.getPosition().getX() + ", y=" + c.getPosition().getY() + ")\t";

            }
        }

        return description;
    }

    /**
     * [DEBUG PURPOSE]
     * Get text information about balloons still alive and their position
     * 
     * @return text information about balloons still alive and their position
     */
    public String getBalloonsStillAlive() {
        if (!(this.activeBalloonsList.isEmpty())) {
            String tab_rep = "[";
            String data = "";
            Balloon lastBalloon = this.activeBalloonsList.get(this.activeBalloonsList.size() - 1);
            for (Balloon balloon : this.activeBalloonsList) {
                if (balloon.equals(lastBalloon)) {
                    tab_rep += " " + balloon.getStatus() + " ";
                } else {
                    tab_rep += " " + balloon.getStatus() + " |";
                }
                data += balloon.getTextualRepresentation() + "\n";
            }
            return "\n\n--- Balloons Info ---\n Status : \n" + tab_rep + "]\n Data : \n" + data;
        }
        return "No Balloons active !";

    }

    /**
     * Get text information about towers on the board
     * 
     * @return text information about towers
     */
    public String getTowersInformation() {
        if (this.allActiveTowersList.isEmpty()) {
            return "No Towers active!";
        }

        String data = "\n\n--- Towers Info ---\n";
        for (Tower tower : this.allActiveTowersList) {
            data += tower.getTextualRepresentation() + "\n";
        }
        return data;
    }

}