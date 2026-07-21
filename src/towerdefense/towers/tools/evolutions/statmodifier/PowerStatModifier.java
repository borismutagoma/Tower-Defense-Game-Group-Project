package towerdefense.towers.tools.evolutions.statmodifier;

/** A class that extends from StatModifier and Represents a modification for a tower's stat. */
public class PowerStatModifier extends StatModifier {

    /**
     * Constructor that creats a PowerStatModifier Object.
     *
     * @param statName Name of the stat
     * @param value Value of the modification
     * @param isMultiplier True if multiplier, false if addition
     * @param cost Cost of the modification
     */
    public PowerStatModifier(String statName, double value, boolean isMultiplier, int cost){
        super(statName, value, isMultiplier, cost);
    }

    @Override
    public String getType(){
        return "Power" ;
    }

    /** 
     * An method that privdes a textual representation for A StatModifier Object
     * 
     * @return a textual representation for A StatModifier Object
     */
    @Override
    public String toString(){
        if (this.isMultiplier()) {
        return "Type: " + this.getType()+ " | "+"Upgrade: " + this.getStatName() + " x" + this.getValue() + " | Cost: " + this.getCost() ;
        } 
        return "Type: " + this.getType()+ " | "+"Upgrade: +" + this.getValue() + " " + this.getStatName() + " | Cost: " + this.getCost();
    }

}
