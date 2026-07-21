package towerdefense.util;

/**
 * Representate an effect applied on a balloon in Bloon Tower Defense Game
 */
public enum Effect {

    /** Slow down a balloon */
    SLOWED(10,0.5),
    /** Completely blocks (freezes) the ballon */
    FREEZE(10,1.0),
    /** No effect is applied (return to normal) */
    NONE(0,1.0);

    /** Duration of the effect */
    private final int duration;
    /** Effect on the speed of the balloon */
    private final double speedEffect;

    /**
     * constructor of Effect
     * @param duration the time the effect will last for
     * @param speedEffect the speed effect that will be applied on the ballon
     */
    Effect(int duration, double speedEffect){
        this.duration = duration;
        this.speedEffect = speedEffect; 
    }

    /**
     * getter for the duration of the effect
     * @return the duration of this effect
     */
    public int getDuration(){ return this.duration;}

    /**
     * getter for the speedEffect
     * @return speedEffect
     */
    public double getSpeedEffect(){ return this.speedEffect;}
}


