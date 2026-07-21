package towerdefense.balloons.generator ;



import towerdefense.boards.* ;



import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;



public class GeneratorZephyrTest{
    private ClassicalBoard board1 ;
    private RandomBoard board2 ;
    private BalloonGenerator generator ;


  @BeforeEach
    public void before(){
        this.board1 = new ClassicalBoard(6,7);
        this.board2 = new RandomBoard(6,7,2);
        this.generator = new GenerateZephyr() ;
    }


    @Test
    public void testGeneratorZephyrWithInvalidNbBalloons(){
        this.generator.generateBalloons(-4,this.board1,1);
        this.generator.generateBalloons(0, this.board2,1);
        assertEquals(0, this.board1.getActiveBalloons().size());
        assertEquals(0, this.board2.getActiveBalloons().size());
    }


    @Test
    public void testGeneratorZephyrWithValidNbBalloons(){
        this.generator.generateBalloons(60,this.board1,1);
        this.generator.generateBalloons(10, this.board2,1);
        assertEquals(60, this.board1.getActiveBalloons().size());
        assertEquals(10, this.board2.getActiveBalloons().size());
    }


}