package towerdefense.towers.tools.evolutions ; 

import java.util.List;

import towerdefense.boards.Board;
import towerdefense.player.Player;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;

/**
 * Interface representing an object that can be improved using stat modifiers.
 * Any class implementing this interface must manage a list of active upgrades
 * and a set of allowed upgrades.
 */
public interface Upgradeable{

    /**
     * Get the cadence of the tower depending on the active updates on this
     * 
     * @see Tower#getCadence()
     * @return the cadence of the tower depending on the active updates on this
     */
    public int getCadence();

    /**
     * Get the range of the tower depending on the active updates on this
     * 
     * @see Tower#getRange()
     * @return the range of the tower depending on the active updates on this
     */
    
    public int getRange();



    /**
     * Add to the complete list and the list of upgradeable towers
     * @see Tower#addToBoard(Board)
     * @param board the board that has the lists of towers
     */
    public void addToBoard(Board board);

    /**
     * Get active upgrades on this
     * @return active upgrades of this
     */
    public List<StatModifier> getActiveUpgrades();

    /**
     * Get allowed upgrades of this
     * @return allowed upgrades of this
     */
    public List<StatModifier> getAllowedUpgrades();

    /**
     * Get upgrades that the tower doesn't yet have
     * @return the list of upgrades that the tower does not yet have
     */
    public List<StatModifier> getPurchasableUpgrades();
    

    /**
     * Adds an upgrade to the object to modify its statistics.
     *  @param upgrade the stat modifier to be applied
     */
    public void addUpgrade(StatModifier upgrade);

    /**
     * Removes a previously applied upgrade from the object.
     *  @param upgrade the stat modifier to be removed
     */
    public void removeUpgrade(StatModifier upgrade);

    /**
     * Checks if a specific upgrade is already applied to the object.
     *  @param upgrade the stat modifier to check
     * @return true if the upgrade is present, false otherwise
     */
    public boolean hasUpgrade(StatModifier upgrade);

    /**
     * Determines if the object is eligible to receive a specific upgrade.
     * Usually checks if the upgrade is in the allowed list and if requirements are met.
     *  @param upgrade the stat modifier to evaluate
     * @return true if the upgrade can be accepted, false otherwise
     */
    public boolean canAcceptUpgrade(StatModifier upgrade);

    /**
     * Adds an upgrade to the list of authorized improvements for this object.
     *  @param upgrade the stat modifier to allow
     */
    public void addAllowedUpgrade(StatModifier upgrade);


    /**
     * Purchase an upgrade for this tower if the tower accepts this upgrade and the player has the money to buy it
     * @param p the player who is buying the upgrade
     * @param upgrade the stat modifier to apply to the tower
     */
    public void buyUpgrade(Player p, StatModifier upgrade);

    /**
     * Selling an upgrade from this tower.
     * @param p the player who will receive the refund
     * @param upgrade the stat modifier to be sold
     */
    public void sellUpgrade(Player p, StatModifier upgrade);


    /**
     * Adding informations about upgradable towers
     * 
     * @see Tower#getTextualRepresentation()
     * @return a representation of this tower
     */
    public String getTextualRepresentation();

    /**
     * Return the specific symbol of this tower 
     * (Used to acces to it despite the implementation of Upgradable)
     * 
     * @see Tower#getSymbol()
     * @return the symbol of this tower
     */
    public String getSymbol();
  
}