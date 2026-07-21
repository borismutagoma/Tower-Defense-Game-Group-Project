package towerdefense.towers.tools.evolutions;

import java.util.*;

import towerdefense.player.Player;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;

/**
 * Interface imposing the "contract" for a tower to be an upgradeable tower
 */
public class UpgradeableLogic {

    /** The upgrades the tower currently has */
    protected List<StatModifier> activeUpgrades;
    /** A list of upgrades this tower is allowed to buy */
    protected List<StatModifier> allowedUpgrades;

    /**
     * Create the upgradable logic for a tower
     */
    public UpgradeableLogic(){
        this.activeUpgrades = new ArrayList<>();
        this.allowedUpgrades = new ArrayList<>();
    };


    /**
     * Returns an improved value according to the type of upgrade we want to apply; 
     * The desired upgrade must be among the upgrades already purchased, 
     * otherwise the method does nothing.
     * 
     * @param baseValue the value that will be improved
     * @param upgradeType The type of upgrade that will be applied to the value
     * @return the improved value if the desired upgrade was found
     */
    public double valuesUpgrader(int baseValue,String upgradeType){
        double finalValue = baseValue;
        for(StatModifier mod : activeUpgrades){
            if (mod.getStatName().equalsIgnoreCase(upgradeType)) {
                if (mod.isMultiplier()) {
                    finalValue *= mod.getValue();
                } else {
                    finalValue += mod.getValue();
                }
            }
        }
        return finalValue;
    }

    /**
     * Get active upgrades on this
     * @return active upgrades of this
     */
    public List<StatModifier> getActiveUpgrades() {
        return this.activeUpgrades;
    }

    /**
     * Get allowed upgrades of this
     * @return allowed upgrades of this
     */
    public List<StatModifier> getAllowedUpgrades() {
        return this.allowedUpgrades;
    }

    /**
     * Get upgrades that the tower doesn't yet have
     * @return the list of upgrades that the tower does not yet have
     */
    public List<StatModifier> getPurchasableUpgrades(){
        List<StatModifier> purchasableUpgrades = new ArrayList<>();
        for(StatModifier upgrade: this.allowedUpgrades){
            if(!this.hasUpgrade(upgrade)){
                purchasableUpgrades.add(upgrade);
            }
        }
        return purchasableUpgrades;
    }

    /**
     * Adds the upgrade from the active list.
     * 
     * @param upgrade The stat modifier to add.
     */
    public void addUpgrade(StatModifier upgrade) {
        this.activeUpgrades.add(upgrade);
    }

    /**
     * Remove the upgrade from the active list.
     * 
     * @param upgrade The stat modifier to remove.
     */
    public void removeUpgrade(StatModifier upgrade) {
        if (!this.activeUpgrades.contains(upgrade)) {
            System.out.println(upgrade + " doesn't exits on this tower");
        }
        this.activeUpgrades.remove(upgrade);
    }

    /**
     * Checks if the tower already has a specific upgrade.
     * 
     * @param upgrade The stat modifier to check.
     * 
     * @return <code>true</code> if the tower has already the upgrade, <code>false</code> otherwise.
     */
    public boolean hasUpgrade(StatModifier upgrade) {
        return this.activeUpgrades.contains(upgrade);
    }

    /**
     * Checks if the tower is allowed to have this upgrade and doesn't already own
     * it
     * 
     * @param upgrade The stat modifier to evaluate.
     * @return <code>true</code> if the upgrade can be applied, <code>false</code> otherwise.
     */
    public boolean canAcceptUpgrade(StatModifier upgrade) {
        return this.allowedUpgrades.contains(upgrade) && (!(this.hasUpgrade(upgrade)));
    }

    /**
     * Adds an upgrade to the list of available evolutions for this tower.
     * @param upgrade the modifier to be made available for purchase
     */
    public void addAllowedUpgrade(StatModifier upgrade) {
        if (upgrade != null && !this.allowedUpgrades.contains(upgrade)) {
            this.allowedUpgrades.add(upgrade);
        }
    }

    /**
     * Purchase an upgrade for this tower if the tower accepts this upgrade and the player has the money to buy it
     * @param p the player who is buying the upgrade
     * @param upgrade the stat modifier to apply to the tower
     * 
     * @see Upgradeable#buyUpgrade(Player, StatModifier)
     */
    public void buyUpgrade(Player p, StatModifier upgrade){
        int price = upgrade.getCost();

        if(!this.canAcceptUpgrade(upgrade)){
            System.out.println("The tower " + this + " cannot have the upgrade \n" + upgrade);
        }

        if(this.hasUpgrade(upgrade)){
            System.out.println("The upgrade " + this + " -> " + upgrade + "is already acquired\n");
        }

        if(p.getMoney() >= price){
            p.setMoney(p.getMoney() - price);
            this.addUpgrade(upgrade);
            System.out.println("The upgrade " + upgrade + " was added to the tower \n" + this);
        }   
        else{
            System.out.println("Sorry, you don't have the funds to buy the upgrade \n" + upgrade);
        }
    }

    /**
     * Selling an upgrade from this tower.
     * @param p the player who will receive the refund
     * @param upgrade the stat modifier to be sold
     * 
     * @see Upgradeable#sellUpgrade(Player, StatModifier)
     */
    public void sellUpgrade(Player p, StatModifier upgrade){
        if (!this.hasUpgrade(upgrade)) {
            System.out.println("Error: The tower does not have the upgrade: " + upgrade.getStatName());
            return; 
        }

        int sellPrice = (int) (upgrade.getCost() * 0.5);
        this.removeUpgrade(upgrade);
        p.setMoney(p.getMoney() + sellPrice);

        System.out.println("Success! Sold " + upgrade.getStatName() + " for " + sellPrice + "$.");
        System.out.println("New balance: " + p.getMoney());
    }
    /**
     * Calculates the total refund amount for the tower (50% of base cost + upgrades).
     * @param baseCost The initial purchase price of the tower
     * @return the refund amount
     */
    public int calculateSellPrice(int baseCost) {
        int totalInvested = baseCost;
        for (StatModifier mod : this.activeUpgrades) {
            totalInvested += mod.getCost();
        }
        return (int) (totalInvested * 0.5);
    }
    
    
}
