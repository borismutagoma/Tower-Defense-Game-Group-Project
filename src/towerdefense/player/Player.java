package towerdefense.player;

import towerdefense.game.GameConstants;

/**
 * Represents a player in the Bloon Tower Defense game
 */
public class Player {
    
    /** Default health points of a player */
    private static final int DEFAULT_LIFE = GameConstants.PLAYER_STARTING_LIFE;
    /** Default money given to the player */
    private static final int DEFAULT_MONEY = GameConstants.PLAYER_STARTING_MONEY;

    /** Health points of a player */
    private int life;
    /** Money of the player */
    private int money;

    /**
     * Constructs a player without parameters, and therefore uses default values
     */
    public Player(){
        this.life = DEFAULT_LIFE;
        this.money = DEFAULT_MONEY;
    }


    /**
     * Creates a player based on the given parameters.
     * @param life the desired life for the player
     * @param money the desired money available for the player
     */
    public Player(int life, int money){
        this.life = life;
        this.money = money;
    }

    /**
     * Get the current life of the player
     * @return the current life of the player
     */
    public int getLife() {
        return this.life;
    }

    /**
     * Get the current money of the player
     * @return the current money of the player
     */
    public int getMoney() {
        return this.money;
    }


    /**
     * Set the life of the player, can be used to remove or add life
     * Use it for player initialization. 
     * It is preferable to use the methods {@link #dealDamage(int)} and {@link #restoreHealth(int)} during gameplay.
     * @param life the number of "life points" given
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Set the money of the player, can be used to remove or add money
     * Use it for player initialization. 
     * It is preferable to use the methods {@link #gainMoney(int)} and {@link #spendMoney(int)} during gameplay.
     * @param money the amount of money given
     */
    public void setMoney(int money) {
        this.money = money;
    }

    
    /**
     * Removes a certain amount of health from the player
     * @param amount the amount of life to be deducted from the player
     */
    public void dealDamage(int amount) {
        this.life -= amount;
    }

    /**
     * Add a certain amount of health to the player
     * @param amount the amount of life to add to the player
     */
    public void restoreHealth(int amount) {
        this.life += amount;
    }

    /**
     * Removes a certain amount of money from the player
     * @param amount the amount of money to be deducted from the player
     */
    public void spendMoney(int amount) {
        this.money -= amount;
    }

    /**
     * Add a certain amount of money to the player
     * @param amount the amount of money to add to the player
     */
    public void gainMoney(int amount) {
        this.money += amount;
    }

    /**
     * Returns true if the player is still alive, returns false otherwise
     * @return true if the player is still alive, returns false otherwise
     */
    public boolean isAlive(){
        return this.life > 0;
    }

}