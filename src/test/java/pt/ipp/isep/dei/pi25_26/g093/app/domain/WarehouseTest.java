package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WarehouseTest {

    @Test
    public void testConstructorAndGetters() {
        Warehouse wh = new Warehouse(1, new ArrayList<Aisle>());
        assertEquals(1, wh.getId());
        assertNotNull(wh.getAisles());
    }

    @Test
    public void testSetAisles() {
        Warehouse wh = new Warehouse(1, new ArrayList<Aisle>());
        java.util.List<Aisle> aisles = new java.util.ArrayList<>();
        aisles.add(new Aisle(1, new ArrayList<Bay>()));

        wh.setAisles(aisles);
        assertEquals(aisles, wh.getAisles());
    }
}
