package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Aisle;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Bay;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Warehouse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WarehouseRepositoryTest {

    @Test
    public void testAddAndGetWarehouses() {
        WarehouseRepository repo = new WarehouseRepository();
        List<Aisle> aisles = new ArrayList<>();
        aisles.add(new Aisle(1, new ArrayList<Bay>()));
        Warehouse wh = new Warehouse(1, aisles);

        repo.add(wh);
        List<Warehouse> list = repo.getWarehouseList();

        assertEquals(1, list.size());
        assertEquals(wh, list.get(0));
    }

    @Test
    public void testGetWarehouseListNotNull() {
        WarehouseRepository repo = new WarehouseRepository();
        assertNotNull(repo.getWarehouseList());
    }
}
