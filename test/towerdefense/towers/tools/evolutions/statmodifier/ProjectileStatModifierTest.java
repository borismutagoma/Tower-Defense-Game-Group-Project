package towerdefense.towers.tools.evolutions.statmodifier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import towerdefense.projectiles.NeedleProjectile;

public class ProjectileStatModifierTest {

    private ProjectileStatModifier projModifier;
    private NeedleProjectile needle;

    @BeforeEach
    public void before() {
        
        needle = new NeedleProjectile(); 
        projModifier = new ProjectileStatModifier("needle_upgrade", 150, needle);
    }

    @Test
    public void testProjectileValuesAndType() {
        
        assertEquals("Projectile", projModifier.getType());
        assertEquals(needle, projModifier.getProjectile());
        assertEquals(-1.0, projModifier.getValue());
        assertEquals(150, projModifier.getCost());
    }

    @Test
    public void testToStringFormatting() {
        String result = projModifier.toString();
        assertTrue(result.contains("Type: Projectile"));
        assertTrue(result.contains("Projectile:"));
        assertTrue(result.contains("Cost: 150"));
    }
}