package towerdefense.towers.tools.evolutions.statmodifier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PowerStatModifierTest {

    private PowerStatModifier powerAdd;
    private PowerStatModifier powerMult;

    @BeforeEach
    public void before() {
        
        powerAdd = new PowerStatModifier("damage", 1.0, false, 40);
        powerMult = new PowerStatModifier("damage", 2.0, true, 100);
    }

    @Test
    public void testPowerValuesAndType() {
        
        assertEquals("Power", powerAdd.getType());
        assertEquals(1.0, powerAdd.getValue(), 0.001);
        assertFalse(powerAdd.isMultiplier());
        assertEquals(40, powerAdd.getCost());
    }

    @Test
    public void testToStringFormatting() {
        
        String expectedAdd = "Type: Power | Upgrade: +1.0 damage | Cost: 40";
        String expectedMult = "Type: Power | Upgrade: damage x2.0 | Cost: 100";
        
        assertEquals(expectedAdd, powerAdd.toString());
        assertEquals(expectedMult, powerMult.toString());
    }
}