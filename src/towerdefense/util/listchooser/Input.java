package towerdefense.util.listchooser;


import java.util.*;

/**
 * A utility class for entering strings or integers on standard input
 */
public class Input {
	
	private static Scanner scanner = new Scanner(System.in); 

	/**
	 * Constructs an Input instance to collect what the user writes in the terminal
	 */
	public Input() {}
	
	/**
	 * Allows entry of a string on standard input
	 * 
	 * @return the string entered
	 */
	public static String readString() {
		return scanner.next();
	}

	
	/**
	 * Allows entry of an int on standard input
	 * 
	 * @return the int entered
	 * @throws java.io.IOException exception
	 */
	public static int readInt() throws java.io.IOException {
	   try {
			return scanner.nextInt();
		} catch (Exception e) {
			scanner.next();
			throw new java.io.IOException();
		}
	}

	/**
	 * Reads an integer from standard input and checks that the given element corresponds to an integer.
	 * Displays an error message unless the input is an int
	 * 
	 * @return an integer read from the standard input
	 */
	public static int readIntVerified() {
		int attemps = 0;
		while (attemps < 100) {
			try {
				return scanner.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Error: Please enter an integer.\n");
				scanner.next(); 
				attemps++;
			}
		}
		throw new IllegalArgumentException("\nError: Too many integer input attempts (100), restart the game.\n\t"+
			"Hint 1: There is an input problem.\n\t"+
			"Hint 2: The player fell asleep at the keyboard.\n");
	}

	/**
	 * Requesting an integer within a range
	 * @param min the lower bound of the interval (include)
	 * @param max the upper bound of the interval (non-include)
	 * @return an integer within a range
	 */
	public static int readIntInRange(int min, int max){
		int value = readIntVerified();
		while(value < min || value >= max){
			System.out.println("Please enter an integer in the range ["+ min + "," + (max - 1) +"] : ");
			value = readIntVerified();
		}
		return value;
	}

	/**
	 * Allows entry of a double on standard input
	 * 
	 * @return the double entered
	 * @throws java.io.IOException exception
	 */
	public static double readDouble() throws java.io.IOException {
	   try {
			return scanner.nextDouble();
		} catch (Exception e) {
			scanner.next();
			throw new java.io.IOException();
		}
	}
	
}

//	// Test
//	public static void main(String[] args) {
//		try {
//			System.out.print(" chaine : ? ");
//			String chaineLue = Input.readString();
//			System.out.println("lue  => " + chaineLue);
//			System.out.print(" int : ? ");
//			int intLu = Input.readInt();
//			System.out.println("lue  => " + intLu);
//		} catch (java.io.IOException e) {
//		}
//	}

