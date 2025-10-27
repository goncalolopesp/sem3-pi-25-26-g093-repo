package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BayTest {

    @Test
    public void testConstructorAndGetters() {
        Bay bay = new Bay(10, 50);
        assertEquals(10, bay.getId());
        assertEquals(50, bay.getCapacity());
        assertNotNull(bay.getCargo());
        assertTrue(bay.getCargo().isEmpty());
    }

    @Test
    public void testSetCapacity() {
        Bay bay = new Bay(5, 20);
        bay.setCapacity(99);
        assertEquals(99, bay.getCapacity());
    }

    @Test
    public void testSetCargo() {
        Bay bay = new Bay(1, 5);

        List<Cargo> cargos = new ArrayList<>();
        cargos.add(new Cargo(1, 7, 10, "2025-12-01", "2025-07-09T13:43:00"));
        cargos.add(new Cargo(2, 3, 5, "", "2025-05-02T10:30:00"));

        bay.setCargo(cargos);

        assertEquals(cargos, bay.getCargo());
        assertEquals(2, bay.getCargo().size());
    }

    @Test
    public void testAddCargo() {
        Bay bay = new Bay(2, 10);
        Cargo c = new Cargo(5, 12, 3, "2026-01-05", "2025-03-21T11:20:30");

        bay.addCargo(c);

        assertEquals(1, bay.getCargo().size());
        assertEquals(c, bay.getCargo().get(0));
    }

    @Test
    public void testGetCargoReturnsNonNullList() {
        Bay bay = new Bay(3, 15);
        assertNotNull(bay.getCargo());
    }
}
