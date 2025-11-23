package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;

import java.util.List;

import static org.junit.Assert.*;

public class WagonRepositoryTest {

    @Test
    public void testAddAndGetWagons() {
        WagonRepository repo = new WagonRepository();
        Cargo c = new Cargo(1, 100, 2, "2025-01-01", "2024-11-23T10:00:00");
        Wagon w = new Wagon(10, c);

        repo.add(w);
        List<Wagon> list = repo.getWagons();

        assertEquals(1, list.size());
        assertEquals(w, list.get(0));
    }

    @Test
    public void testGetWagonsReturnsMutableList() {
        WagonRepository repo = new WagonRepository();
        List<Wagon> list = repo.getWagons();
        assertNotNull(list);
        list.clear(); // deve ser possível limpar sem exceptions
    }
}
