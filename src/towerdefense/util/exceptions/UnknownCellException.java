package towerdefense.util.exceptions;

/**
 *  throws an exception if the cell is Unknown 
 */
public class UnknownCellException extends Exception {

    /**
     * constructor without message
     */
    public UnknownCellException() {
        super();
    }

    /**
     * constructor with message
     * 
     * @param message the message that will be displayed if an exception is thrown
     */
    public UnknownCellException(String message) {
        super(message);
    }
}
