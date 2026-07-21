package towerdefense.game;

import towerdefense.projectiles.*;
import towerdefense.util.Effect;

/**
 * Constants of the different game elements:
 * <ul>
 *  <li> Towers </li>
 *  <li> Projectiles </li>
 *  <li> Balloons </li>
 *  <li> Player</li> 
 * </ul>
 * These data are fixed. Therefore, it is not possible to modify the basic elements of the game.
 */
public final class GameConstants {

    /** Private constructor of GameConstants (final class) */
    private GameConstants(){};

    // Towers
     // Monkey Dart Tower 
     /** MONKEY TOWER COST  */ 
     public static final int MONKEY_TOWER_COST = 200;
     /** MONKEY TOWER RANGE  */ 
     public static final int MONKEY_TOWER_RANGE = 100;
     /** MONKEY TOWER CADENCE  */ 
     public static final int MONKEY_TOWER_CADENCE = 2;
     /** MONKEY TOWER PROJECTILE  */
     public static final Projectile MONKEY_TOWER_PROJECTILE = new DartProjectile();

     // Needle Tower  
     /** NEEDLE TOWER COST  */ 
     public static final int NEEDLE_TOWER_COST = 300;
     /** NEEDLE TOWER RANGE  */ 
     public static final int NEEDLE_TOWER_RANGE = 80;
     /** NEEDLE TOWER CADENCE  */
     public static final int NEEDLE_TOWER_CADENCE = 1;
     /** NEEDLE TOWER PROJECTILE  */
     public static final Projectile NEEDLE_TOWER_PROJECTILE = new NeedleProjectile();

     // Sniper Monkey Tower  
     /** SNIPER TOWER COST  */ 
     public static final int SNIPER_TOWER_COST = 500;
     /** SNIPER TOWER RANGE  */ 
     public static final int SNIPER_TOWER_RANGE = -1;
     /** SNIPER TOWER CADENCE  */
     public static final int SNIPER_TOWER_CADENCE = 5;
     /** SNIPER TOWER PROJECTILE  */
     public static final Projectile SNIPER_TOWER_PROJECTILE = new VerySharpDartProjectile();

     // Bomb Tower  
     /** BOMB TOWER COST  */ 
     public static final int BOMB_TOWER_COST = 600;
     /** BOMB TOWER RANGE  */ 
     public static final int BOMB_TOWER_RANGE = 150;
     /** BOMB TOWER CADENCE  */
     public static final int BOMB_TOWER_CADENCE = 5;
     /** BOMB TOWER PROJECTILE  */
     public static final Projectile BOMB_TOWER_PROJECTILE = null; // /!\ Board must be initialized

     // Gorilla Tower  
     /** GORILLA TOWER COST  */ 
    public static final int GORILLA_TOWER_COST = 300;
    /** GORILLA TOWER RANGE  */ 
    public static final int GORILLA_TOWER_RANGE = 200;
    /** GORILLA TOWER CADENCE  */
    public static final int GORILLA_TOWER_CADENCE = 4;
    /** GORILLA TOWER PROJECTILE  */
    public static final Projectile GORILLA_TOWER_PROJECTILE = new SharpDartProjectile();

    // Slow Tower  
     /** SLOW TOWER COST  */ 
     public static final int SLOW_TOWER_COST = 400;
     /** SLOW TOWER RANGE  */ 
     public static final int SLOW_TOWER_RANGE = 100;
     /** SLOW TOWER CADENCE  */
     public static final int SLOW_TOWER_CADENCE = 3;
     /** SLOW TOWER PROJECTILE  */
     public static final Effect SLOW_TOWER_EFFECT = Effect.SLOWED;

     // Freeze Tower  
     /** FREEZE TOWER COST  */ 
     public static final int FREEZE_TOWER_COST = 500;
     /** FREEZE TOWER RANGE  */ 
     public static final int FREEZE_TOWER_RANGE = 100;
     /** FREEZE TOWER CADENCE  */
     public static final int FREEZE_TOWER_CADENCE = 6;
     /** FREEZE TOWER PROJECTILE  */
     public static final Effect FREEZE_TOWER_EFFECT = Effect.FREEZE;

     

    // Projectiles
    /** DART DAMAGE */
     public static final int DART_DAMAGE = 1;

     /** NEEDLE DAMAGE */
     public static final int NEEDLE_DAMAGE = 1;

     /** SHARP DART DAMAGE */
     public static final int SHARP_DART_DAMAGE = 2;

     /** VERY SHARP DART DAMAGE */
     public static final int VERY_SHARP_DART_DAMAGE = 4;

     /** BOMB DAMAGE */
     public static final int BOMB_DAMAGE = 2;
     /** BOMB DAMAGE ZONE */
     public static final int BOMB_DAMAGE_ZONE = 1;
     
     /** EXTRA BOMB DAMAGE ZONE */
     public static final int EXTRA_BOMB_DAMAGE = 2;
     /** EXTRA BOMB DAMAGE */
     public static final int EXTRA_BOMB_DAMAGE_ZONE = 2;

    // Balloons
     // Cumulus Balloon
     /** CUMULUS LIFE */
     public static final double CUMULUS_LIFE = 1.0;
     /** CUMULUS SPEED */
     public static final double CUMULUS_SPEED = 1.0;

     // Zephyr Balloon
     /** ZEPHYR LIFE */
     public static final double ZEPHYR_LIFE = 0.8;
     /** ZEPHYR SPEED */
     public static final double ZEPHYR_SPEED = 1.5;

     // Stratus Balloon
     /** STRATUS LIFE */
     public static final double STRATUS_LIFE = 2.0;
     /** STRATUS SPEED */
     public static final double STRATUS_SPEED = 0.5;

    
    // Player Stats
    /** PLAYER STRARTING LIFE */
    public static final int PLAYER_STARTING_LIFE = 20;
    /** PLAYER STARTING MONEY */
    public static final int PLAYER_STARTING_MONEY = 800;



    
}
