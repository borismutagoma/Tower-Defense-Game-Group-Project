package towerdefense.balloons;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import towerdefense.cell.*;
import towerdefense.path.Path;
import towerdefense.util.*;

/**
 * Tests specific to the CumulusBalloon type.
 */
public class StratusBalloonTest {
    private StratusBalloon balloon;
    private Path trajectory ;

    /**
     * Initializes a simple trajectory and a Cumulus balloon before each test.
     */
    @BeforeEach
    public void before(){
        trajectory = new Path();
        trajectory.addCell(new Cell(new Position(0, 0)));
        trajectory.addCell(new Cell(new Position(0, 1)));
        trajectory.addCell(new Cell(new Position(0, 2)));

        balloon = new StratusBalloon(trajectory);
    }

    @Test
    public void testInitialLife() {
        assertEquals(2.0,  balloon.getLife());
    }
    @Test
    public  void testSpeed(){
        assertEquals(0.5, balloon.getSpeed());
    }
    
}

