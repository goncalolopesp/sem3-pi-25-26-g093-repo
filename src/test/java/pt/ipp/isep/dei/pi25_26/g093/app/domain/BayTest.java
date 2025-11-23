package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BayTest {

    @Test
    public void testConstructorAndGetters() {
        Bay bay = new Bay(1, 10);
        assertEquals(1, bay.getId());
        assertEquals(10, bay.getCapacity());
        assertNotNull(bay.getCargo());
        assertTrue(bay.getCargo().isEmpty());
    }

    @Test
    public void testSetCapacity() {
        Bay bay = new Bay(1, 10);
        bay.setCapacity(20);
        assertEquals(20, bay.getCapacity());
    }

    @Test
    public void testAddAndGetCargo() {
        Bay bay = new Bay(1, 10);
        Cargo c = new Cargo(1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");

        bay.addCargo(c);
        assertEquals(1, bay.getCargo().size());
        assertEquals(c, bay.getCargo().get(0));
    }

    @Test
    public void testSetCargo() {
        Bay bay = new Bay(1, 10);
        Cargo c = new Cargo(1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");

        java.util.List<Cargo> list = new java.util.ArrayList<>();
        list.add(c);
        bay.setCargo(list);

        assertEquals(list, bay.getCargo());
    }

    @Test
    public void testRemoveCargo() {
        Bay bay = new Bay(1, 10);
        Cargo c = new Cargo(1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");
        bay.addCargo(c);

        assertTrue(bay.removeCargo(c));
        assertFalse(bay.getCargo().contains(c));
        assertFalse(bay.removeCargo(c)); // já não existe
    }
}
