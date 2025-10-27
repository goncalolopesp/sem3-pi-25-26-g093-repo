package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Locomotive class (USPL03).
 */
class LocomotiveTest {

    @Test
    void ensureConstructorAndGettersWork() {
        Locomotive loco = new Locomotive("L001", "electric", 200, 1435);
        
        assertEquals("L001", loco.getId());
        assertEquals("electric", loco.getType());
        assertEquals(200, loco.getMaxSpeedKmH());
        assertEquals(1435, loco.getGaugeMm());
    }

    @Test
    void ensureConstructorValidationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Locomotive(null, "electric", 200, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("", "electric", 200, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("   ", "electric", 200, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("L001", null, 200, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("L001", "", 200, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("L001", "electric", 0, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("L001", "electric", -100, 1435));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("L001", "electric", 200, 0));
        assertThrows(IllegalArgumentException.class, () -> new Locomotive("L001", "electric", 200, -50));
    }

    @Test
    void ensureTrimWorks() {
        Locomotive loco = new Locomotive("  L001  ", "  electric  ", 200, 1435);
        
        assertEquals("L001", loco.getId());
        assertEquals("electric", loco.getType());
    }

    @Test
    void ensureTypeIsStoredLowercase() {
        Locomotive loco = new Locomotive("L001", "ELECTRIC", 200, 1435);
        
        assertEquals("electric", loco.getType());
    }

    @Test
    void ensureIsElectricReturnsTrue() {
        Locomotive electricLoco = new Locomotive("L001", "electric", 200, 1435);
        Locomotive dieselLoco = new Locomotive("L002", "diesel", 150, 1435);
        
        assertTrue(electricLoco.isElectric());
        assertFalse(dieselLoco.isElectric());
    }

    @Test
    void ensureIsDieselReturnsTrue() {
        Locomotive dieselLoco = new Locomotive("L001", "diesel", 150, 1435);
        Locomotive electricLoco = new Locomotive("L002", "electric", 200, 1435);
        
        assertTrue(dieselLoco.isDiesel());
        assertFalse(electricLoco.isDiesel());
    }

    @Test
    void ensureGaugeCompatibilityWorks() {
        Locomotive loco = new Locomotive("L001", "electric", 200, 1435);
        
        assertTrue(loco.isGaugeCompatible(1435));
        assertTrue(loco.isGaugeCompatible(1436));
        assertTrue(loco.isGaugeCompatible(1434));
        assertFalse(loco.isGaugeCompatible(1676));
    }

    @Test
    void ensureEqualsWorks() {
        Locomotive l1 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l2 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l3 = new Locomotive("L002", "diesel", 150, 1676);
        
        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
    }

    @Test
    void ensureEqualsIsCaseInsensitiveForId() {
        Locomotive l1 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l2 = new Locomotive("l001", "diesel", 150, 1676);
        
        assertEquals(l1, l2);
    }

    @Test
    void ensureHashCodeWorks() {
        Locomotive l1 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l2 = new Locomotive("L001", "electric", 200, 1435);
        
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    void ensureToStringWorks() {
        Locomotive loco = new Locomotive("L001", "electric", 200, 1435);
        
        String str = loco.toString();
        assertTrue(str.contains("L001"));
        assertTrue(str.contains("electric"));
        assertTrue(str.contains("200"));
        assertTrue(str.contains("1435"));
    }
}

