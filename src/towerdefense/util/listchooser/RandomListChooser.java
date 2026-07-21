package towerdefense.util.listchooser;
import java.util.*;

import towerdefense.util.RandomInputManager;

/**
 * Random answer selector, chooses a random answer from a given list of possibilities
 *  @param <T> A generic type defining the type of of the list that the list chooser will process
 */
public class RandomListChooser<T> implements ListChooser<T> {

	/**
	 * Constructs a RandomListChooser
	 */
	public RandomListChooser(){}
	
	private Random random = new Random();	
	
	/**
	 * Allows one to choose a random item from a list of items of type T.
	 * If the list of items is empty, null is returned.
	 * The list of items is presented as numbers followed by the 
	 * string representation of the item. 
	 * The possibility not to make a choice is automatically added (choice number 0), 
	 * in this case, null is returned.
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item. null if the list of items is empty or if no choice has been made
	 */
	public T choose(String msg, List<? extends T> list) {
		if (list.isEmpty()) {
			return null;
		}

		System.out.println(msg);
		System.out.print("[0 - none");
		int index = 1;
		for (T element : list) {
			System.out.print(", " + (index++) + " - " + element);
		}
		System.out.println("]");

		int choice = random.nextInt(list.size() + 1);

		if (choice == 0) {
			System.out.println("The element 0 (none) was chosen\n");
			return null;
		}

		System.out.println("The element with index " + choice + " was chosen\n");
		return list.get(choice-1);
	}
	/**
	 * Automatically generates a random integer without user intervention.
	 * Useful for automated simulations (like Livrable 5).
	 * @param message Ignored in this implementation
	 * @param bound The upper bound for the random generation (exclusive)
	 * @return A random integer between 0 and bound
	 */
	@Override
	public int chooseInt(String message, int bound) {
		return RandomInputManager.getRandomInt(bound);
	}

}
