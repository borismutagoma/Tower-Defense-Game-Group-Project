package towerdefense.towers.tools.evolutions.statmodifier;

import towerdefense.projectiles.* ;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


public class StatModifierTest {

    private StatModifier rangeStatModifier ; 
    private StatModifier powerStatModifier ; 
    private StatModifier cadenceStatModifier ; 
    private ProjectileStatModifier projectileStatModifier ; 
    private Projectile extraBombProjectile ; 


    @BeforeEach
    public void before(){
        this.rangeStatModifier = new RangeStatModifier("range", 25, false, 100) ;
        this.powerStatModifier = new PowerStatModifier("power", 1, false, 250) ;
        this.cadenceStatModifier = new CadenceStatModifier("cadence", 25, false, 150) ;
        this.extraBombProjectile = new ExtraBombProjectile(null) ;
        this.projectileStatModifier = new ProjectileStatModifier("projectile", 400, this.extraBombProjectile ) ;
    }

    @Test
    public void testGetStatName() {
        assertEquals("range", this.rangeStatModifier.getStatName());
        assertEquals("power", this.powerStatModifier.getStatName());
        assertEquals("cadence", this.cadenceStatModifier.getStatName());
        assertEquals("projectile", this.projectileStatModifier.getStatName());
    }

    @Test
    public void testGetValue() {
        assertEquals(25, this.rangeStatModifier.getValue());
        assertEquals(1, this.powerStatModifier.getValue());
        assertEquals(25, this.cadenceStatModifier.getValue());
        assertEquals(-1, this.projectileStatModifier.getValue());
    }

    @Test
    public void testIsMultiplierReturnsFalse() {
        assertFalse(this.rangeStatModifier.isMultiplier());
        assertFalse(this.powerStatModifier.isMultiplier());
        assertFalse(this.cadenceStatModifier.isMultiplier());
        assertFalse(this.projectileStatModifier.isMultiplier());
    }

    @Test
    public void testGetCost() {
        assertEquals(100, this.rangeStatModifier.getCost());
        assertEquals(250, this.powerStatModifier.getCost());
        assertEquals(150, this.cadenceStatModifier.getCost());
        assertEquals(400, this.projectileStatModifier.getCost());
    }


    @Test
    public void testGetType() {
        assertEquals("Range", this.rangeStatModifier.getType());
        assertEquals("Power", this.powerStatModifier.getType());
        assertEquals("Cadence", this.cadenceStatModifier.getType());
        assertEquals("Projectile", this.projectileStatModifier.getType());
    }

    @Test
    public void testGetProjectile() {
        assertEquals(this.extraBombProjectile, this.projectileStatModifier.getProjectile());
    }
}
