package towerdefense.filters;

import java.util.*;

import towerdefense.player.Player;

/**
 * The "tools" used by the filters have been defined; other methods could be implemented later.
 * @param <T> A generic type defining the type of filter, and therefore the type of object the filter will sort.
 */
public abstract class FiltersTools<T> implements Filter<T>{
    

    /** The player influencing the response of the filter used */
    protected Player player;

    /**
     * Filters inherit from this "toolbox". 
     * Therefore, it's not necessary to be able to create an instance of this class.
     * 
     * @param player the player whose information will affect the filter's response
     */
    public FiltersTools(Player player){
        this.player = player;
    };


    /**
     * Set a new player for this filter.
     * Allows to change the player without having to create a new filter instance.
     * @param player another player with whom we want to run the filter
     */
    public void changePlayer(Player player){
        this.player = player;
    }

    /**
     * Returns <code>True</code> if the item meets the filter condition(s), returns <code>False</code> otherwise
     * 
     * @see Filter#matches(Object)
     * 
     */
    public abstract boolean matches(T item);


    /**
     * Applying a filter to a list of objects
     * @param items the list of elements that we will pass through the filter
     * @return the list of items meeting the filter conditions
     */
    public List<T> applyFilter(List<T> items){
        List<T> acceptedItems = new ArrayList<>();
        for(T item: items){
            if(this.matches(item)){
                acceptedItems.add(item);
            }
        }
        return acceptedItems;

    }

    /**
     * Applying a list of filters to a list of objects
     * @param <T> the type of these items
     * @param items the list of elements that we will pass through the filters
     * @param filters the list of filters used to sort these elements
     * @return the list of items meeting the filter conditions
     */
    public static <T> List<T> applyFiltersList(List<T> items, List<Filter<T>> filters){
        List<T> acceptedItems = new ArrayList<>();
        boolean isMatching = true;
        for(T item: items){
            int i = 0;
            
            while(i < filters.size() && isMatching){
                Filter<T> filter = filters.get(i);
                isMatching = filter.matches(item);
                i++;
            }

            if(isMatching){
                acceptedItems.add(item);
            }
        }
        return acceptedItems;
    }

    
}
