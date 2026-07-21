package towerdefense.cell;

import towerdefense.util.Position;

/** class for Cell */
public class Cell {
  /** position the position of the Cell object */
  private Position position;
  /** attribut for the path */
  private boolean isPath;

  /**
   * constructor for Cell
   * 
   * @param position the position of the Cell object
   */
  public Cell(Position position) {
    this.position = position;
    this.isPath = false;
  }

  /**
   * get the position of the Cell object
   * 
   * @return the position of the Cell object
   */
  public Position getPosition() {
    return this.position;
  }

  /**
   * Indicates if this cell is on the path or not
   * 
   * @return true if position is on the path, false otherwise
   */
  public boolean isOnPath() {
    return this.isPath;
  }

  /**
   * set the cell in path
   */
  public void setPath() {
    this.isPath = true;
  }

  /**
   * unset the cell in path
   */
  public void unsetPath() {
    this.isPath = false;
  }

  /**
   * return true if the cell are identical
   * 
   * @param o other cell
   * @return true if the cell are identical
   */
  public boolean equals(Object o) {
    if (this == o) {return true;};
    if(o == null || this.getClass() != o.getClass()) {return false;};
    Cell otherCell = (Cell) o;
    return this.getPosition().getX() == otherCell.getPosition().getX() &&
        this.getPosition().getY() == otherCell.getPosition().getY();
  }

  /**
   * A String representation of this (Path or not)
   * 
   * @return a representation of this
   */
  public String toString() {
    if (this.isOnPath()) {
      return "P";
    }
    return " ";
  }

  /**
   * A visual representation of this (Path or not)
   * 
   * @return a representation of this
   */
  public String getSymbol() {
    if (this.isOnPath()) {
      return "🟩";
    }
    return "🟫";
  }



}