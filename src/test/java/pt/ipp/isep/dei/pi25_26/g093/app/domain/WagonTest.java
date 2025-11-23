package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class WagonTest {

    @Test
    public void testConstructorWithCargo() {
        Cargo c = new Cargo(1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");
        Wagon w = new Wagon(10, c);

        assertEquals(10, w.getwagonID());
        assertEquals(c, w.getCargo());
    }

    @Test
    public void testConstructorWithPrimitiveParamsCreatesCargo() {
        Wagon w = new Wagon(10, 1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");

        assertEquals(10, w.getwagonID());
        assertNotNull(w.getCargo());
        assertEquals(1, w.getCargo().getBoxID());
        assertEquals(100, w.getCargo().getSku());
    }

    @Test
    public void testToString() {
        Wagon w = new Wagon(10, 1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");
        assertEquals("Wagon 10", w.toString());
    }
}
