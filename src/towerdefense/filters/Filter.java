package towerdefense.filters;

/**
 * Defines the "behavior" of a filter for all types
 * @param <T> A generic type defining the type of filter, and therefore the type of object the filter will sort.
 */
public interface Filter<T> {

    /**
     * Returns <code>True</code> if the item meets the filter condition(s), returns <code>False</code> otherwise
     * @param item the item to check
     * @return true if the item meets the filter condition(s), false otherwise
     */
    boolean matches(T item);
}
