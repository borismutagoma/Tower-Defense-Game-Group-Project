package towerdefense.util.listchooser;

import java.util.List;

/**
 * Defines the methods used by a ListChooser
 * @param <T> A generic type defining the type of of the list that the list chooser will process
 */
public interface ListChooser<T> {

	/**
	 * Allows one to choose an item from a list of items of type T. 
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item.
	 */
	public T choose(String msg, List<? extends T> list);

	/**
	 * Asks for or generates an integer within a specified range.
	 * This method abstracts the input process, allowing for both 
	 * interactive user input and automated random generation.
	 *
	 * @param message The prompt to display (in interactive mode)
	 * @param bound The upper limit for the integer (exclusive)
	 * @return An integer between 0 (inclusive) and bound (exclusive)
	 */
	int chooseInt(String message, int bound);
} 
