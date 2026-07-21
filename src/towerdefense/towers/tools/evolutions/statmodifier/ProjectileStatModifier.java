package towerdefense.towers.tools.evolutions.statmodifier;

import towerdefense.projectiles.Projectile ;


/**
 * A stat modifier that changes the type of projectile used by a tower.
 */
public class ProjectileStatModifier extends StatModifier {

    private Projectile projectile ;

    /**
     * Constructs a new ProjectileStatModifier.
     * @param statName   The name of the statistic being modified
     * @param cost       The cost of this upgrade
     * @param projectile The new projectile to be used by the tower
     */
    public ProjectileStatModifier(String statName, int cost, Projectile projectile){
        super(statName, -1, false, cost);
        this.projectile = projectile ;
    }


    /**
     * Returns a String describing the type of upgrade that this is
     * @return the type of upgrade that this is
     */
    @Override
    public String getType(){
        return "Projectile" ;
    }

    /**
     * Returns the projectile associated with this modifier.
     * @return the projectile object
     */
    public Projectile getProjectile(){
        return this.projectile ;
    }

    /** 
     * An method that privdes a textual representation for A StatModifier Object
     * 
     * @return a textual representation for A StatModifier Object
     */
    @Override
    public String toString() {
        return "Type: " + this.getType() + " | Upgrade value: " + this.getValue() + " | Projectile: " + this.getProjectile() + " | Stat: " + this.getStatName() + " | Cost: " + this.getCost();
}
}