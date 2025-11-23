package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class CargoTest {

    @Test
    public void testConstructorWithExpiry() {
        Cargo c = new Cargo(1, 100, 5, "2025-01-01", "2024-11-23T10:00:00");
        assertEquals(1, c.getBoxID());
        assertEquals(100, c.getSku());
        assertEquals(5, c.getQuantity());
        assertTrue(c.hasExpiry());
        assertEquals("2025-01-01", c.getExpiryDate());
    }

    @Test
    public void testConstructorWithoutExpiry() {
        Cargo c = new Cargo(1, 100, 5, "", "2024-11-23T10:00:00");
        assertFalse(c.hasExpiry());
        assertEquals("9999-12-31", c.getExpiryDate());
    }

    @Test
    public void testReturnedGoodsConstructor_ValidNumericStrings() {
        Cargo c = new Cargo("BOX-10", "SKU-200", 3, "2025-01-01", "2024-11-23T10:00:00");
        assertEquals(10, c.getBoxID());
        assertEquals(200, c.getSku());
        assertEquals(3, c.getQuantity());
        assertTrue(c.hasExpiry());
    }

    @Test
    public void testReturnedGoodsConstructor_InvalidStrings() {
        Cargo c = new Cargo("X", "Y", 1, "2025-01-01", "2024-11-23T10:00:00");
        assertEquals(-1, c.getBoxID());
        assertEquals(-1, c.getSku());
    }

    @Test
    public void testToStringContainsKeyFields() {
        Cargo c = new Cargo(1, 100, 5, "2025-01-01", "2024-11-23T10:00:00");
        String s = c.toString();
        assertTrue(s.contains("1"));
        assertTrue(s.contains("100"));
    }
}
