package towerdefense;

import java.util.*;

import towerdefense.boards.*;
import towerdefense.game.*;
import towerdefense.strategy.*;
import towerdefense.towers.towerwitheffect.*;
import towerdefense.towers.towerwithprojectiles.*;
import towerdefense.util.Position;

/**
 * Test class to verify the game's functionality.
 */
public class debugTowerDefenseMain {

    /** Create a Tower Defense game to test the features */
    public debugTowerDefenseMain(){
        
    }

    /**
    * Entry point for the debug execution.
    * @param args command line arguments
    */
    public static void main(String[] args) {
        int rows = 10;
        int cols = 10;
        Board board = new ClassicalBoard(rows, cols);
        BloonTowerDefenseGame game = new RandomGame(board, true, false);
        Position[] positions = {
            new Position(1, 1), new Position(1, 3), new Position(1, 5),
            new Position(3, 1), new Position(3, 3), new Position(3, 5),
            new Position(5, 1)
        };
        List<towerdefense.towers.Tower> allTowers = new ArrayList<>();
        
        // Projectiles towers

        //allTowers.add(new MonkeyDartTower(positions[0], null));
        allTowers.add(new BombTower(positions[1], null));
        allTowers.add(new GorrillaTower(positions[2], null));
        allTowers.add(new NeedleTower(positions[3], null));
        //allTowers.add(new SniperMonkeyTower(positions[4], null));
        
        // Effect Towers
        // --- 1. Initialisation des Tours ---
        FreezeTower freeze = new FreezeTower(positions[5], null);
        SlowTower slow = new SlowTower(positions[6], null);
        
        // --- 2. Configuration des Évolutions (Test) ---
        
        
        // RangeStatModifier bigRange = new RangeStatModifier("range", 100, false, 50);
        // freeze.addAllowedUpgrade(bigRange);
        // freeze.addUpgrade(bigRange); 


        // PowerStatModifier superPower = new PowerStatModifier("damage", 1.0, false, 100);
        // slow.addAllowedUpgrade(superPower);
        // slow.addUpgrade(superPower);
        
        // CadenceStatModifier fastSlow = new CadenceStatModifier("cadence", 5, false, 30);
        // slow.addAllowedUpgrade(fastSlow);
        // slow.addUpgrade(fastSlow);

        allTowers.add(freeze);
        allTowers.add(slow);

        /*System.out.println("--- Report of evolutions ---");
        for (towerdefense.towers.Tower t : allTowers) {
            System.out.println("Tower : " + t.getClass().getSimpleName());
            System.out.println(" > Real Range  : " + t.getRange());
            System.out.println(" > Real Damage  : " + t.getDamage());
            System.out.println(" > Real Cadence  : " + t.getCadence());
        }
        */
        System.out.println("--- Initialisation des Tours ---");
        for (towerdefense.towers.Tower t : allTowers) {
            Strategy strat = new FirstOutStrategy(t, board);
            t.setLastShootTime(-t.getCadence());
            t.setStrategy(strat);
            board.addTower(t);
            System.out.println("added : " + t.getSymbol() + " in " + t.getPosition());
        }

        System.out.println("\n--- Lancement du Test de tir/effets ---");
        game.play();
    }
}