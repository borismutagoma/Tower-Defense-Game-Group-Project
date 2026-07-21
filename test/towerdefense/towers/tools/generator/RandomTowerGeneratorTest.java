package towerdefense.towers.tools.generator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import towerdefense.boards.ClassicalBoard;

public class RandomTowerGeneratorTest {

    private RandomTowerGenerator generator;
    private ClassicalBoard board;

    @BeforeEach
    void before() {
        board = new ClassicalBoard(5, 5);
        generator = new RandomTowerGenerator(board);
    }

    @Test
    void testTowerGenerationCount() {
        int recipesCount = generator.towersRecipes.size();
        
        generator.towerGeneration();
    
        assertEquals(recipesCount * 2, board.getAllActiveTowers().size());
    }

    @Test
    void testPlacementConstraints() {
        generator.towerGeneration();
        
        boolean allowOnPath = board.getAllowTowersOnPath();
        
        board.getAllActiveTowers().forEach(tower -> {
            if (!allowOnPath) {
                assertFalse(board.getCell(tower.getPosition()).isOnPath());
            }
        });
    }

    @Test
    void testEmptyRecipes() {
        generator.towersRecipes.clear();
        generator.towerGeneration();
        assertTrue(board.getAllActiveTowers().isEmpty());
    }
}