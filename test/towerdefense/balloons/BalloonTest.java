package towerdefense.balloons;


import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


import towerdefense.cell.*;
import towerdefense.path.Path;
import towerdefense.util.*;
;

/**Test class for Balloon */
public class  BalloonTest{

    private  Balloon balloon;
    private Path trajectory;

    /**
     * Initializes a simple trajectory and a balloon before each test.
     */
    @BeforeEach
    public void before(){
        trajectory = new Path();
        trajectory.addCell(new Cell(new Position(0, 0)));
        trajectory.addCell(new Cell(new Position(0, 1)));
        trajectory.addCell(new Cell(new Position(0, 2)));

        balloon = new CumulusBalloon(trajectory);

    }

    @Test
    public void initializationIsValid(){
        assertEquals(balloon.getPosition(), trajectory.getCell(0).getPosition());
        int damageLevel = (int) Math.ceil((balloon.getLife() * 1.5) + (balloon.getSpeed() * 0.5) - 1);
        assertEquals(balloon.getDamageLevel(), damageLevel);
        assertEquals(balloon.getEffect(), Effect.NONE);

        
        assertEquals(balloon.toString(), "B");
        String textualRepresentation = "Balloon Type : towerdefense.balloons.CumulusBalloon / Current Position : (x:0.0,y:0.0) / Current Speed : 1.0 / Current Life : 1.0 / Current Effect : NONE";
        assertEquals(balloon.getTextualRepresentation(), textualRepresentation);
    
        balloon.setLife(15.0);
        assertEquals(balloon.getLife(), 15.0);
    
    }

    @Test
    public void testInitialCell() {
        assertEquals(trajectory.getCell(0), balloon.getCurrentCell());
    }

    @Test
    public void testNotDeadInitially() {
        assertFalse(balloon.isDead());
    }

    @Test
    public void testNotFinishedInitially() {
        assertFalse(balloon.isFinishedPath());
    }

    @Test
    public void testBalloonMoves() {
        balloon.update();
        assertEquals(trajectory.getCell(1), balloon.getCurrentCell());
    }

    @Test
    public void testFreezeEffectStopsMovement() {
        balloon.applyEffect(Effect.FREEZE);
        balloon.update();
        assertEquals(trajectory.getCell(0), balloon.getCurrentCell());
    }

    @Test
    public void testFinishPath() {
        balloon.update();
        balloon.update();
        assertTrue(balloon.isFinishedPath());
    }

    /*@Test
    public void testCorrectInformations(){
        assertEquals("Current Position : (x:0,y:0) / Current Speed : 1.0 / Current Life : 1.0", balloon.toString());
        balloon.update();
        assertEquals("Current Position : (x:0,y:1) / Current Speed : 1.0 / Current Life : 1.0", balloon.toString());
        balloon.shoot(1); 
        assertEquals("Current Position : (x:0,y:1) / Current Speed : 1.0 / Current Life : 0.0", balloon.toString());

    }*/

    @Test
    public void testCorrectStatus(){
        Balloon balloon2 = new CumulusBalloon(trajectory);
        assertEquals("🎈", balloon.getStatus());
        balloon.update();
        balloon.update();
        assertEquals("🏁", balloon.getStatus());
        balloon2.shoot(1);
        assertEquals("💥", balloon2.getStatus());
    }


}


