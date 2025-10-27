package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class CargoTest {

    @Test
    public void testConstructorWithValidExpiry() {
        Cargo cargo = new Cargo(10, 5, 100, "2025-12-01", "2025-07-09T13:43:00");

        assertEquals(10, cargo.getBoxID());
        assertEquals(5, cargo.getSku());
        assertEquals(100, cargo.getQuantity());
        assertEquals("2025-12-01", cargo.getExpiryDate());
        assertEquals("2025-07-09T13:43:00", cargo.getReceivedAt());
        assertTrue(cargo.hasExpiry());
    }

    @Test
    public void testConstructorWithNoExpiry() {
        Cargo cargo = new Cargo(55, 7, 20, "", "2025-06-10T10:00:00");

        assertEquals(55, cargo.getBoxID());
        assertFalse(cargo.hasExpiry());
        assertEquals("9999-12-31", cargo.getExpiryDate());
    }

    @Test
    public void testConstructorStringVersionWithDigitsExtraction() {
        Cargo cargo = new Cargo("BOX-77", "SKU-888", 10, "2026-01-05", "2025-02-20T14:22:11");

        assertEquals(77, cargo.getBoxID());
        assertEquals(888, cargo.getSku());
        assertEquals(10, cargo.getQuantity());
        assertTrue(cargo.hasExpiry());
    }

    @Test
    public void testDateObjectsNotNull() {
        Cargo cargo = new Cargo(15, 3, 50, "2024-11-05", "2024-10-30T06:12:33");

        assertNotNull(cargo.getExpiryDateObj());
        assertNotNull(cargo.getReceivedAtObj());
    }

    @Test
    public void testGetExpiryDateStringFormat() {
        Cargo cargo = new Cargo(20, 9, 10, "2027-09-15", "2027-09-10T08:00:00");
        assertEquals("2027-09-15", cargo.getExpiryDate());
    }

    @Test
    public void testGetReceivedAtStringFormat() {
        Cargo cargo = new Cargo(30, 11, 5, "2024-12-25", "2024-12-24T23:59:59");
        assertEquals("2024-12-24T23:59:59", cargo.getReceivedAt());
    }
}
