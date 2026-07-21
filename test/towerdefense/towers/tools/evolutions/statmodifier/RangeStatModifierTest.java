package towerdefense.towers.tools.evolutions.statmodifier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RangeStatModifierTest {

    private RangeStatModifier rangeAdd;
    private RangeStatModifier rangeMult;

    @BeforeEach
    public void before() {
    
        rangeAdd = new RangeStatModifier("range", 20.0, false, 60);
        rangeMult = new RangeStatModifier("range", 1.2, true, 150);
    }

    @Test
    public void testRangeValuesAndType() {
        
        assertEquals("Range", rangeAdd.getType());
        assertEquals(20.0, rangeAdd.getValue(), 0.001);
        assertFalse(rangeAdd.isMultiplier());
        assertEquals(60, rangeAdd.getCost());
    }

    @Test
    public void testToStringFormatting() {
        
        String expectedAdd = "Type: Range | Upgrade: +20.0 range | Cost: 60";
        String expectedMult = "Type: Range | Upgrade: range x1.2 | Cost: 150";
        assertEquals(expectedAdd, rangeAdd.toString());
        assertEquals(expectedMult, rangeMult.toString());
    }
}