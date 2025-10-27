package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WarehouseControllerTest {

    private WarehouseController controller;

    @Before
    public void resetController() {
        Repositories repos = Repositories.getInstance();
        repos.getWarehouseRepository().getWarehouseList().clear();

        controller = new WarehouseController();

        List<Bay> bays = new ArrayList<>();
        bays.add(new Bay(1, 2));
        bays.add(new Bay(2, 2));
        bays.add(new Bay(3, 2));

        List<Aisle> aisles = new ArrayList<>();
        aisles.add(new Aisle(1, bays));

        controller.addWarehouse(new Warehouse(1, aisles));
    }

    @Test
    public void testAddAndRetrieveWarehouse() {
        assertEquals(1, controller.getWarehouses().size());
        assertNotNull(controller.getWarehouseById(1));
    }

    @Test
    public void testGetAisleById() {
        Warehouse wh = controller.getWarehouseById(1);
        Aisle aisle = controller.getAisleById(wh, 1);
        assertNotNull(aisle);
        assertEquals(1, aisle.getId());
    }

    @Test
    public void testGetBayById() {
        Warehouse wh = controller.getWarehouseById(1);
        Aisle aisle = controller.getAisleById(wh, 1);
        Bay bay = controller.getBayById(aisle, 1);
        assertNotNull(bay);
        assertEquals(1, bay.getId());
    }
}
