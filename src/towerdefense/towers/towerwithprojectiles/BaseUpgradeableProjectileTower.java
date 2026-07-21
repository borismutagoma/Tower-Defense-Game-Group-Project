package towerdefense.towers.towerwithprojectiles;

import java.util.List;

import towerdefense.boards.Board;
import towerdefense.player.Player;
import towerdefense.projectiles.Projectile;
import towerdefense.strategy.Strategy;
import towerdefense.towers.Tower;
import towerdefense.towers.tools.evolutions.*;
import towerdefense.towers.tools.evolutions.statmodifier.StatModifier;
import towerdefense.util.Position;

/**
 * Abstract class for towers that can be upgraded and shoot projectiles.
 * It combines ProjectileTower logic and Upgradeable logic.
 */
public abstract class BaseUpgradeableProjectileTower extends BaseProjectileTower implements Upgradeable {

    /** The "improvement logic" that will be used to fulfill the methods requested by the Upgradeable interface */
    protected final UpgradeableLogic upgradeLogic = new UpgradeableLogic();
    
    /** The base cost for this type of tower */
    protected final int baseCost;

    /**
     * Building a BaseUpgradeableProjectileTower object.
     * @param cost the cost for this tower
     * @param range the initial range
     * @param cadence the initial cadence
     * @param position the position on the board
     * @param strategy the shooting strategy
     * @param projectile the projectile used by the tower
     */
    public BaseUpgradeableProjectileTower(int cost, int range, int cadence, Position position, Strategy strategy, Projectile projectile) {
        super(cost, range, cadence, position, strategy, projectile);
        this.baseCost = cost;
    }

    /**********************************/
    /*                                */
    /*       Upgradeable Logic        */
    /*                                */
    /**********************************/


    /**
     * Get the cadence of the tower depending on the active updates on this
     * 
     * @see Tower#getCadence()
     */
    @Override
    public int getCadence() {
        double finalCadence = upgradeLogic.valuesUpgrader(baseCadence, "cadence");
        return Math.max(1, (int) Math.round(finalCadence));
    }

    /**
     * Get the range of the tower depending on the active updates on this
     * 
     * @see Tower#getRange()
     */
    @Override
    public int getRange() {
        double totalRange = upgradeLogic.valuesUpgrader(baseRange, "range");
        return (int)totalRange;
    }


    /**
     * Get the base damage of the tower depending on the active updates on this
     * 
     */
    @Override
    public int getDamage() {
        int baseDamage = (int) projectileLogic.getDamage();
        double totalDamage = upgradeLogic.valuesUpgrader(baseDamage, "damage");
        return (int) Math.round(totalDamage);
    }

    /**
     * Get active upgrades on this
     * @return active upgrades of this
     */
    @Override
    public List<StatModifier> getActiveUpgrades() {
        return upgradeLogic.getActiveUpgrades();
    }

    /**
     * Get allowed upgrades of this
     * @return allowed upgrades of this
     */
    @Override
    public List<StatModifier> getAllowedUpgrades() {
        return upgradeLogic.getAllowedUpgrades();
    }

    /**
     * Get upgrades that the tower doesn't yet have
     * @return the list of upgrades that the tower does not yet have
     */
    public List<StatModifier> getPurchasableUpgrades(){
        return upgradeLogic.getPurchasableUpgrades();
    }

    /**
     * Add to the complete list and the list of upgradeable towers
     * @see Tower#addToBoard(Board)
     */
    @Override
    public void addToBoard(Board board) {
        super.addToBoard(board);
        board.addUpgradableTower(this);
    }

    /**
     * Adds the upgrade from the active list.
     * * @param upgrade The stat modifier to add.
     */
    @Override
    public void addUpgrade(StatModifier upgrade) {
        upgradeLogic.addUpgrade(upgrade);
    }

    /**
     * Remove the upgrade from the active list.
     * * @param upgrade The stat modifier to remove.
     */
    @Override
    public void removeUpgrade(StatModifier upgrade) {
        if (!upgradeLogic.hasUpgrade(upgrade)) {
            System.out.println(upgrade + " doesn't exits on this tower");
        }
        upgradeLogic.removeUpgrade(upgrade);
    }

    /**
     * Checks if this tower already has a specific upgrade.
     * * @param upgrade The stat modifier to check.
     * * @return <code>true</code> if the NeedleTower Object has already the upgrade, <code>false</code> otherwise.
     */
    @Override
    public boolean hasUpgrade(StatModifier upgrade) {
        return upgradeLogic.getActiveUpgrades().contains(upgrade);
    }

    /**
     * Checks if this tower is allowed to have this upgrade and doesn't already own
     * it
     * * @param upgrade The stat modifier to evaluate.
     * @return <code>true</code> if the upgrade can be applied, <code>false</code> otherwise.
     */
    @Override
    public boolean canAcceptUpgrade(StatModifier upgrade) {
        return upgradeLogic.canAcceptUpgrade(upgrade);
    }
    
    /**
     * Adds an upgrade to the list of available evolutions for this tower.
     * @param upgrade the modifier to be made available for purchase
     */
    @Override
    public void addAllowedUpgrade(StatModifier upgrade) {
        upgradeLogic.addAllowedUpgrade(upgrade);
    }

    /**
     * @see Upgradeable#buyUpgrade(Player, StatModifier)
     */
    @Override
    public void buyUpgrade(Player p, StatModifier upgrade){
        upgradeLogic.buyUpgrade(p, upgrade);
    }

    /**
     * @see Upgradeable#sellUpgrade(Player, StatModifier)
     */
    @Override
    public void sellUpgrade(Player p, StatModifier upgrade){
        upgradeLogic.sellUpgrade(p, upgrade);
    }

    /**
     * @see Tower#getTextualRepresentation()
     */
    @Override
    public String getTextualRepresentation(){
        List<StatModifier> activeUpgrades = upgradeLogic.getActiveUpgrades();
        String baseInfo = this.toString();
        if (activeUpgrades.isEmpty()) {
        return baseInfo + " [No upgrades]";
    }
        return baseInfo + " " + activeUpgrades.toString();
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        int count = upgradeLogic.getActiveUpgrades().size();
        String posInfo = "@(" + (int)this.getPosition().getX() + "," + (int)this.getPosition().getY() + ")";
        
        if (count > 0) {
            return name +" " + posInfo + " (Upgrades: " + count + ")";
        }
        return name + " "+ posInfo;
    }

    

    /**
     * Returns 50% of the total gold invested (base cost + all active upgrades).
     * 
     * @param board The board on which the tower is sold
     * @return the refund amount for this tower.
     * 
     * @see Tower#sell(Board)
     */
    @Override
    public int sell(Board board) {
        board.getAllActiveTowers().remove(this);
        board.getActiveUpgradableTowers().remove(this);
        return this.upgradeLogic.calculateSellPrice(this.baseCost);
    }
}