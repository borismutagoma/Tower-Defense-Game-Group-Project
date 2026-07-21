package towerdefense.util;

import java.util.Random;

import towerdefense.util.listchooser.Input;

/**
 * Class to manage entries
 * to simulate user inputs
 */
public class RandomInputManager extends Input {

    /**
     * Build an Input Wrapper
     */
    public RandomInputManager(){}
    
    /* attribut for the random instance  */
    private static final Random random = new Random();

    /**
     * Return a random integer between 0 and bound (excluded)
     * @param bound the upper bound
     * @return random int
     */
    public static int getRandomInt(int bound) {
        if (bound <= 0) return 0;
        return random.nextInt(bound);
    }

    /**
     * Return a random double between 0 and bound (excluded)
     * @param bound the upper bound
     * @return random double generated
     */
    public static double getRandomDouble(double bound) {
        if (bound <= 0) return 0.0;
        return random.nextDouble() * bound;
    }
}