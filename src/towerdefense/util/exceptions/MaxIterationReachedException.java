package towerdefense.util.exceptions;

/**
 * An exception is raised when an iterative process exceeds the maximum number
 * of allowed iterations.
 * 
 * This safeguard prevents infinite loops or excessive resource consumption
 * during divergent calculations.
 */
public class MaxIterationReachedException extends RuntimeException {

    /**
     * Construct a new exception
     * @param message the message that will be displayed if an exception is thrown
     */
    public MaxIterationReachedException(String message) {
        super(message);
    }
}
