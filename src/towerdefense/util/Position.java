package towerdefense.util;

/**
 * Represent a position in a 2D grid
 */
public class Position{

  /** attribute for the x */
  private double   x ;

  /** attribute for the y */
  private double  y ;

  /**
  * constructor for the position
  * @param  x the x position
  * @param  y the y position
  */
  public Position(double  x, double  y){
    this.x = x ;
    this.y = y ;
  }

  /**
   * getter for the x position
   * @return the x position
   */
  public double  getX(){
    return this.x ;
  }

  /**
   * getter for the y position
   * @return the y position
   */
  public double  getY(){
    return this.y ; 
  }

  /**
   * Reresentate a position in a 2D grid
   * @return a representation of a position in a 2D grid
   */
  public String toString(){
    return "(x:"+ this.getX() + ",y:" + this.getY() +")";
  }

  /**
   * Check if the other object is a position and if its coordinates (x,y) are equal to those of this Position (x,y).
   * @param o the other object to compare
   * @return <code>True</code> if the other object is a position and if its coordinates (x,y) are equal to those of this, <code>False</code> otherwise.
   */
  public boolean equals(Object o){
      if(this == o) {return true;};
      if (o == null || this.getClass() != o.getClass()) {return false;};
      Position other = (Position) o;
      return (this.getX() == other.getX()) && (this.getY() == other.getY());
  }
  
}  