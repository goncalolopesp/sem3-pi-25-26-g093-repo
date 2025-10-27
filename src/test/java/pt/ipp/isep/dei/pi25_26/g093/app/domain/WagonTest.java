package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class WagonTest {

    @Test
    public void testConstructorWithCargo() {
        Cargo cargo = new Cargo(10, 5, 20, "2025-12-01", "2025-07-09T13:43:00");
        Wagon wagon = new Wagon(1, cargo);

        assertEquals(1, wagon.getwagonID());
        assertEquals(cargo, wagon.getCargo());
    }

    @Test
    public void testConstructorWithParameters() {
        Wagon wagon = new Wagon(3, 22, 9, 15, "2026-01-05", "2025-03-21T11:20:30");
        Cargo cargo = wagon.getCargo();

        assertNotNull(cargo);
        assertEquals(3, wagon.getwagonID());
        assertEquals(22, cargo.getBoxID());
        assertEquals(9, cargo.getSku());
        assertEquals(15, cargo.getQuantity());
        assertEquals("2026-01-05", cargo.getExpiryDate());
        assertEquals("2025-03-21T11:20:30", cargo.getReceivedAt());
    }

    @Test
    public void testToString() {
        Wagon wagon = new Wagon(7, new Cargo(1, 1, 1, "2025-01-01", "2024-01-01T00:00:00"));
        assertEquals("Wagon 7", wagon.toString());
    }
}
