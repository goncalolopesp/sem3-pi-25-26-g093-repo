package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Wagon;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Cargo;

import static org.junit.Assert.*;

public class WagonRepositoryTest {

    private WagonRepository repo;

    @Before
    public void setUp() {
        repo = new WagonRepository();
    }

    @Test
    public void testInitiallyEmpty() {
        assertTrue(repo.getWagons().isEmpty());
    }

    @Test
    public void testAddWagon() {
        Wagon w = new Wagon(1, new Cargo(10, 5, 2, "2025-09-10", "2025-09-01T10:00:00"));
        repo.add(w);

        assertEquals(1, repo.getWagons().size());
        assertEquals(w, repo.getWagons().get(0));
    }
}
