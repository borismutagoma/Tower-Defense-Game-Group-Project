package towerdefense.util;

/**
 * Represents a cardinal direction in a 2D grid.
 */
public enum Direction {

    /** Upward direction */
    UP(0, -1),

    /** Downward direction */
    DOWN(0, 1),

    /** Left direction */
    LEFT(-1, 0),

    /** Right direction */
    RIGHT(1, 0);

    /** Horizontal displacement */
    private final int dx;
    /** Vertical displacement */
    private final int dy;
    
    /**
     * constructor for the direction
     * @param dx
     * @param dy
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the x direction
     * @return the x direction
     */
    public int getDx() { return dx; }
    /**
     * Get the y direction
     * @return the y direction
     */
    public int getDy() { return dy; }

}
