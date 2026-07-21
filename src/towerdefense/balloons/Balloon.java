package towerdefense.balloons ;


import towerdefense.cell.Cell;
import towerdefense.path.Path;
import towerdefense.util.*;



/**
 * Abstract class representing a balloon in the Tower Defense game.
 * A balloon follows a predefined trajectory and moves according to its speed
 * at each game tick.
 */
public abstract class Balloon {
    /** The trajectory followed by the balloon */
    protected Path trajectory;

    /** Balloon movement speed (cells per game tick) */
    protected final double speed;

    /** Current life points of the balloon */
    protected double life;

    /** Wating time for a ballon */
    protected final int WAITINGTIME = 1;

    /** Index of the current cell in the trajectory */
    protected int currentIndex;

    /** Accumulated waiting time used to manage movement */
    protected double currentWaitingTime;

    /** effect on the ballon au debut at first None because it's not hit */
    protected Effect effect = Effect.NONE;

    /** Remaning duration of the currenteffect */
    protected int effectDuration = 0;

    /**the exacat position of the balloon int the cell */
    protected  Position exactPosition;

    /** Damage level / "danger" level of this balloon */
    protected int damageLevel;

    /**
     * Creates a balloon following a given trajectory.
     *
     * @param trajectory the path the balloon must follow
     */
    public Balloon(Path trajectory) {
        if (trajectory == null) {
            throw new IllegalArgumentException("A balloon needs a trajectory");
        }
        this.trajectory = trajectory;
        this.currentIndex = 0;
        this.currentWaitingTime = 0;
        this.speed = this.getDefaultSpeed();
        this.life = this.getDefaultLife();
        Position startPos = trajectory.getCell(0).getPosition();
        this.exactPosition = new Position(startPos.getX(), startPos.getY());
        this.damageLevel = (int) Math.ceil((this.life * 1.5) + (this.speed * 0.5) - 1); // Level calculation
    }

    /* << GETTERS & SETTERS >> */

    /**
     * Returns the current cell of the balloon.
     *
     * @return the cell where the balloon is currently located
     */
    public Cell getCurrentCell() {
        return this.trajectory.getCell(currentIndex);
    }
    /**
     * Return the exact position of balloon in the cell
     * @return the exact position of the balloon
     */
    public Position getPosition(){
        return this.exactPosition;
    }

    /**
     * gets the effect being applied on the ballon
     * 
     * @return effect
     */
    public Effect getEffect() {
        return this.effect;
    }

    /**
     * return the current index in the trajectory
     * 
     * @return the current index
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * Checks whether the balloon is dead.
     *
     * @return true if life is less than or equal to zero
     */
    public boolean isDead() {
        return life <= 0;
    }

    /**
     * Checks whether the balloon has reached the end of its trajectory.
     *
     * @return true if the balloon is at the last cell of the path
     */
    public boolean isFinishedPath() {
        return currentIndex >= trajectory.size() - 1;
    }

    /**
     * Returns the current life of the balloon.
     *
     * @return the life points
     */
    public double getLife() {
        return this.life;
    }

    /**
     * Returns the speed of the balloon.
     *
     * @return the speed in cells per tick
     */
    public double getSpeed() {
        if (this.effect == Effect.FREEZE) {
        return 0.0;
        }
        if (this.effect == Effect.SLOWED) {
            return this.speed * this.effect.getSpeedEffect();
        }
        return this.speed;
    }

    /**
     * Get the level of damage / "danger level" of this balloon
     * @return the level of damage of this balloon
     */
    public int getDamageLevel(){
        return this.damageLevel;
    }

    /**
     * Returns the default life of the balloon.
     *
     * @return the life points
     */
    public abstract double getDefaultLife();

    /**
     * Returns the default speed of the balloon.
     *
     * @return the speed in cells per tick
     */
    public abstract double getDefaultSpeed();

    /* << SETTERS >> */

    /**
     * change the life of Balloon object
     * 
     * @param life the new life of balloon
     */
    public void setLife(double life) {
        this.life = life;
    }

    /* << TOOLS >> */

    /**
     * applies the effect on the ballons
     * 
     * @param effect the effect we want to apply
     */
    public void applyEffect(Effect effect) {
        this.effect = effect;
        this.effectDuration = effect.getDuration();
    }

    /* << EXPLOITATION >> */

    /**
     * method that updates the cell for ballons, so it move the ballons from one
     * cell to another
     */
    public void update() {
        if (this.isDead() || this.isFinishedPath())
            return;

        /* Managing effects */
        handleEffects();

        double distanceToCover = getSpeed();

        /* Waiting time */
        this.currentWaitingTime += distanceToCover;

        while (this.currentWaitingTime >= 1.0 && ! isFinishedPath()) {
            this.currentIndex++;
            this.currentWaitingTime -= 1.0;

        }
        updateExactPosition();
    }

    /**
     * Updates the precise coordinates of the balloon between its current cell and the next one.
     */
    private void updateExactPosition() {
        Position currentPos = trajectory.getCell(this.getCurrentIndex()).getPosition();
        
        if (isFinishedPath()) {
            this.exactPosition = new Position(currentPos.getX(), currentPos.getY());
        } else {
            Position nextPos = trajectory.getCell(this.getCurrentIndex() + 1).getPosition();
            double realX = currentPos.getX() + (nextPos.getX() - currentPos.getX()) * this.currentWaitingTime;
            double realY = currentPos.getY() + (nextPos.getY() - currentPos.getY()) * this.currentWaitingTime;
            this.exactPosition = new Position(realX, realY);
        }
    }

    /**
     * Manages the remaining duration of the active effect and resets it when finished.
     */
    private void handleEffects() {
        if (this.effectDuration > 0) {
            this.effectDuration--;
        } else {
            this.effect = Effect.NONE;
        }
    }

    /**
     * Remove hit points from the balloon
     * 
     * @param points the number of life points to be removed
     */
    public void shoot(double points) {
        this.life -= points;
        if(this.life <0){
            this.life = 0;
        }
    }

    /* <<< OTHERS >>> */


    /**
     * A textual representation of this balloon
     * @return a textual representation of this balloon
     */
    public String getTextualRepresentation(){
        return "Balloon Type : " + this.getClass().getName() +" / Current Position : " + this.getCurrentCell().getPosition() +  " / Current Speed : " + this.getSpeed() + " / Current Life : " + this.getLife() + " / Current Effect : " + this.getEffect() ; 
    }


    /**
     * A String representation of this balloon
     * @return a representation of this balloon
     */
    public String toString() {
        return "B";
    }



    /**
     * Get the status of this balloon (has exploded, crossed the finish line, or is still on its way)
     * @return the status of this balloon
     */
    public String getStatus() {
      if (this.isDead()){return "💥";}            /* Dead / Popped balloon */
      if (this.isFinishedPath()){return "🏁";}    /* Finished ! The balloon has completed its journey */
      return "🎈";                                /* B = Balloon ;  The balloon has not yet completed its journey and it is not dead*/
    }

}
