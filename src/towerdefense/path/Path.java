package towerdefense.path;

import java.util.*;

import towerdefense.cell.Cell;

/**
 * Represents a path on the board of Bloons Tower Defense Game 
 */
public class Path {

    /** List of cells composing the path */
    private List<Cell> cells;

    /**
     * Creates an empty path.
     */
    public Path() {
        this.cells = new ArrayList<>();
    }

    /**
     * Creates a path from an existing list of cells.
     * @param cells the list of cells composing the path
     */
    public Path(List<Cell> cells) {
        this.cells = new ArrayList<>(cells); // Deep copy
    }

    /**
     * Adds a cell at the end of the path.
     *
     * @param cell the cell to add
     */
    public void addCell(Cell cell) {
        this.cells.add(cell);
        cell.setPath();
    }

    /**
     * Remove the cell from the path in the list; if the given index does not correspond to any cell, then an exception is thrown.
     * @param index the index of the cell to be deleted
     * @throws IndexOutOfBoundsException if the given index does not correspond to any cell
     */
    public void removeCell(int index){
        this.cells.remove(index);
    }



    /**
     * Returns the cell at the given index in the path.
     *
     * @param index the position in the path
     * @return the cell at the given index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Cell getCell(int index) {
        return this.cells.get(index);
    }

    /**
     * Returns the number of cells in the path.
     *
     * @return the length of the path
     */
    public int size() {
        return this.cells.size();
    }

    /**
     * Indicates whether the path is empty or invalid.
     *
     * @return true if the path contains no usable cells
     */
    public boolean isEmpty() {
        return this.cells.isEmpty();
    }

    /**
     * Returns the first cell of the path.
     *
     * @return the start cell of the path
     */
    public Cell getStart() {
        if (!this.isEmpty()){
            return this.getCell(0);
        }
        return null;
    }

    /**
     * Returns the last cell of the path.
     *
     * @return the end cell of the path
     */
    public Cell getEnd() {
         if (!this.isEmpty()){
            return this.getCell(this.size() - 1);
        }
        return null;
    }

    /**
     * Checks whether the given cell is part of the path.
     *
     * @param cell the cell to check
     * @return true if the cell belongs to the path
     */
    public boolean contains(Cell cell) {
        return this.cells.contains(cell);
    }

    /**
     * Remove all cells from the path by changing their "path state" and removing them from the list.
     */
    public void clear(){
        for (Cell cell: this.cells){
            cell.unsetPath();
        }
        this.cells.clear();
    }

    /**
     * Returns the list of cells that make up the path.
     *
     * @return the list of cells that make up the path.
     */
    public List<Cell> getCells() {
        if (!this.isEmpty()){
            return new ArrayList<>(this.cells);
        }
        return null;
    }
}
