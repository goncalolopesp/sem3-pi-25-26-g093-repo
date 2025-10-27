package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WarehouseTest {

    @Test
    public void testConstructorAndGetters() {
        List<Bay> bays1 = new ArrayList<>();
        bays1.add(new Bay(1, 10));
        Aisle aisle1 = new Aisle(100, bays1);

        List<Aisle> aisles = new ArrayList<>();
        aisles.add(aisle1);

        Warehouse warehouse = new Warehouse(1, aisles);

        assertEquals(1, warehouse.getId());
        assertEquals(aisles, warehouse.getAisles());
    }

    @Test
    public void testSetAisles() {
        List<Aisle> initialList = new ArrayList<>();
        initialList.add(new Aisle(1, new ArrayList<>()));

        Warehouse warehouse = new Warehouse(2, initialList);

        List<Aisle> newList = new ArrayList<>();
        newList.add(new Aisle(2, new ArrayList<>()));
        newList.add(new Aisle(3, new ArrayList<>()));

        warehouse.setAisles(newList);

        assertEquals(newList, warehouse.getAisles());
        assertEquals(2, warehouse.getAisles().size());
    }

    @Test
    public void testGetId() {
        Warehouse warehouse = new Warehouse(99, new ArrayList<>());
        assertEquals(99, warehouse.getId());
    }
}
