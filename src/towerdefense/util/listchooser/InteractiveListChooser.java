package towerdefense.util.listchooser;
import java.util.List;

/**
 * Answer selector, allows the user to provide an answer from a given list of possibilities.
 *  @param <T> A generic type defining the type of of the list that the list chooser will process
 */
public class InteractiveListChooser<T> implements ListChooser<T> {

	/** 
	 * Build an interactive listchooser
	 */
	public InteractiveListChooser(){}


	/**
	 * Allows one to choose an item from a list of items of type T.
	 * If the list of items is empty, null is returned.
	 * The list of items is presented as numbers followed by the 
	 * string representation of the item. 
	 * The possibility not to make a choice is automatically added (choice number 0), 
	 * in this case, null is returned.
	 * 
	 * @param msg The asked question.
	 * @param list The list of items of type T from which one must choose one.
	 * @return The chosen item. null if the list of items is empty or if the user chooses not to make no choice
	 */
	public T choose(String msg, List<? extends T> list) {
		// is there is not possible choice, null is returned
		if (list.isEmpty()) {
			return null;
		}
		// shows the items until the user made a valid choice
		int choice = -1;
		while ((choice < 0) || (choice > list.size())) {
			System.out.println(msg);
			System.out.println("      0 - back");
			int index = 1;
			for (T element : list) {
				System.out.println("      " + (index++) + " - " + element);
			}
			System.out.println("            choice ?");
			try {
				choice = Input.readInt();
			} catch (java.io.IOException e) {
				System.out.println("Please, enter a number between 0 and " + (index-1));
			}
		}
		if (choice == 0) {
				return null;
		}
		return list.get(choice-1);
	}

	/**
	 * Displays a prompt and waits for the user to enter an integer via the console.
	 * @param message The prompt displayed to the user
	 * @param bound The upper limit (not enforced by input, but used for context)
	 * @return The integer entered by the user
	 */
	@Override
    public int chooseInt(String message, int bound) {
        System.out.println(message);
        try {
            return Input.readInt();
        } catch (Exception e) {
            return 0;
        }
    }

}
