package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WarehouseRepositoryTest {

    private WarehouseRepository repo;

    @Before
    public void init() {
        repo = new WarehouseRepository();
    }

    @Test
    public void testWarehouseRepoStartsEmpty() {
        assertTrue(repo.getWarehouseList().isEmpty());
    }

    @Test
    public void testAddWarehouse() {
        Warehouse wh = new Warehouse(1, new ArrayList<>());
        repo.add(wh);

        assertEquals(1, repo.getWarehouseList().size());
        assertEquals(wh, repo.getWarehouseList().get(0));
    }
}
