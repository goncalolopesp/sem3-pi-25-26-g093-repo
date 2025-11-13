package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Locomotive class (USPL03) — JUnit 4 version.
 */
public class LocomotiveTest {

    private Locomotive loco;

    @Before
    public void setUp() {
        loco = new Locomotive("L001", "electric", 200, 1435);
    }

    @Test
    public void ensureConstructorAndGettersWork() {
        assertEquals("L001", loco.getId());
        assertEquals("electric", loco.getType());
        assertEquals(200, loco.getMaxSpeedKmH(), 0.001);
        assertEquals(1435, loco.getGaugeMm(), 0.001);
    }

    @Test
    public void ensureConstructorValidationThrowsException() {
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
    public void ensureTrimWorks() {
        Locomotive trimmed = new Locomotive("  L001  ", "  electric  ", 200, 1435);
        assertEquals("L001", trimmed.getId());
        assertEquals("electric", trimmed.getType());
    }

    @Test
    public void ensureTypeIsStoredLowercase() {
        Locomotive upperType = new Locomotive("L001", "ELECTRIC", 200, 1435);
        assertEquals("electric", upperType.getType());
    }

    @Test
    public void ensureIsElectricReturnsTrue() {
        Locomotive electricLoco = new Locomotive("L001", "electric", 200, 1435);
        Locomotive dieselLoco = new Locomotive("L002", "diesel", 150, 1435);

        assertTrue(electricLoco.isElectric());
        assertFalse(dieselLoco.isElectric());
    }

    @Test
    public void ensureIsDieselReturnsTrue() {
        Locomotive dieselLoco = new Locomotive("L001", "diesel", 150, 1435);
        Locomotive electricLoco = new Locomotive("L002", "electric", 200, 1435);

        assertTrue(dieselLoco.isDiesel());
        assertFalse(electricLoco.isDiesel());
    }

    @Test
    public void ensureGaugeCompatibilityWorks() {
        Locomotive loco = new Locomotive("L001", "electric", 200, 1435);

        assertTrue(loco.isGaugeCompatible(1435));
        assertTrue(loco.isGaugeCompatible(1436));
        assertTrue(loco.isGaugeCompatible(1434));
        assertFalse(loco.isGaugeCompatible(1676));
    }

    @Test
    public void ensureEqualsWorks() {
        Locomotive l1 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l2 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l3 = new Locomotive("L002", "diesel", 150, 1676);

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
    }

    @Test
    public void ensureEqualsIsCaseInsensitiveForId() {
        Locomotive l1 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l2 = new Locomotive("l001", "diesel", 150, 1676);

        assertEquals(l1, l2);
    }

    @Test
    public void ensureHashCodeWorks() {
        Locomotive l1 = new Locomotive("L001", "electric", 200, 1435);
        Locomotive l2 = new Locomotive("L001", "electric", 200, 1435);

        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    public void ensureToStringWorks() {
        String str = loco.toString();
        assertTrue(str.contains("L001"));
        assertTrue(str.contains("electric"));
        assertTrue(str.contains("200"));
        assertTrue(str.contains("1435"));
    }
}
