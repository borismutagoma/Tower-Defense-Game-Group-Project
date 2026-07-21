package towerdefense.towers.tools.evolutions.statmodifier;


/** An abstract class that Represents a modification for a tower's stat. */
public abstract class StatModifier {

    /** Name of the stat to modify for example "range", "cadence" */
    private String statName;

    /** Value of the modification */
    private double value;

    /** True if the modification is a multiplier, false if addition */
    private boolean isMultiplier;

    /** Cost to apply this modification */
    private int cost;
    

    /**
     * Constructor that creats a StatModifier Object.
     *
     * @param statName Name of the stat
     * @param value Value of the modification
     * @param isMultiplier True if multiplier, false if addition
     * @param cost Cost of the modification
     */
    public StatModifier(String statName, double value, boolean isMultiplier, int cost){
        this.statName = statName;
        this.value = value;
        this.isMultiplier = isMultiplier;
        this.cost = cost;
    }

    /**
     *  Returns the name of the statModifier Object
     * 
     * @return statName the staName of StatModifier Object
     */
    public String getStatName() {
        return this.statName;
    }

    /** Returns the value of the modification
     * 
     * @return value the value of StatModifier Object
     */
    public double getValue() {
        return this.value;
    }

    /** Returns true if this modification is a multiplier
     * 
     * @return <code>true</code> if the modification is multiple, <code>false</code> otherwise
     */
    public boolean isMultiplier() {
        return this.isMultiplier;
    }

    /** Returns the cost of the modification
     * 
     * @return cost the cost of StatModifier Object 
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Returns <code>true</code> if the other's name matches the name of this, <code>false</code> otherwise.
     * Case is not taken into account.
     * @param o The other StateModifier to compare
     * @return <code>true</code> if the other's name matches the name of this, <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object o){
        StatModifier other = (StatModifier) o;
        return this.getType().equals(other.getType()) 
               && (this.statName.equalsIgnoreCase(other.statName)) 
               && (this.getValue() == other.getValue()) 
               && (this.isMultiplier() == other.isMultiplier()) 
               && (this.getCost() == other.getCost());
    }

    /**
     * Returns a String describing the type of upgrade that this is
     * @return the type of upgrade that this is
     */
    protected abstract String getType();


    /** 
     * An method that privdes a textual representation for A StatModifier Object
     * 
     * @return a textual representation for A StatModifier Object
     */
    public abstract String toString();

}