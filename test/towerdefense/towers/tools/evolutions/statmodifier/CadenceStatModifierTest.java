package towerdefense.towers.tools.evolutions.statmodifier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CadenceStatModifierTest {

    private CadenceStatModifier cadenceAdd;
    private CadenceStatModifier cadenceMult;

    @BeforeEach
    public void before() {
        
        cadenceAdd = new CadenceStatModifier("fire_rate", 2.0, false, 50);
        cadenceMult = new CadenceStatModifier("fire_rate", 1.5, true, 120);
    }

    @Test
    public void testCadenceValuesAndType() {
        
        assertEquals("Cadence", cadenceAdd.getType());
        assertEquals(2.0, cadenceAdd.getValue(), 0.001);
        assertFalse(cadenceAdd.isMultiplier());
        assertEquals(50, cadenceAdd.getCost());
        assertEquals("fire_rate", cadenceAdd.getStatName());
    }

    @Test
    public void testToStringFormatting() {
        String expectedAdd = "Type: Cadence | Upgrade: +2.0 fire_rate | Cost: 50";
        String expectedMult = "Type: Cadence | Upgrade: fire_rate x1.5 | Cost: 120";
        
        assertEquals(expectedAdd, cadenceAdd.toString());
        assertEquals(expectedMult, cadenceMult.toString());
    }
}