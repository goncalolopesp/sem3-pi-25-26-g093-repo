package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WagonControllerTest {

    private WagonController wagonController;
    private WarehouseController warehouseController;

    @Before
    public void setup() {
        Repositories repos = Repositories.getInstance();
        repos.getWarehouseRepository().getWarehouseList().clear();
        repos.getWagonRepository().getWagons().clear();

        wagonController = new WagonController();
        warehouseController = new WarehouseController();

        List<Bay> bays = new ArrayList<>();
        bays.add(new Bay(1, 2));
        bays.add(new Bay(2, 2));
        List<Aisle> aisles = new ArrayList<>();
        aisles.add(new Aisle(1, bays));
        warehouseController.addWarehouse(new Warehouse(1, aisles));
    }

    @Test
    public void testAddWagonToRepository() {
        Wagon w = new Wagon(1, 10, 1, 5, "2025-12-01", "2025-07-10T10:00:00");
        wagonController.addWagon(w);

        assertEquals(1, wagonController.getWagons().size());
        assertEquals(w, wagonController.getWagons().get(0));
    }

    @Test
    public void testStoreCargoFromWagons_FEFO_FIFO_OrderAndStorage() {
        Wagon w1 = new Wagon(1, 10, 1, 5, "2025-01-01", "2025-09-01T09:00:00"); // expira primeiro
        Wagon w2 = new Wagon(2, 20, 1, 5, "", "2025-01-02T10:00:00"); // sem validade
        Wagon w3 = new Wagon(3, 5, 1, 5, "2025-01-01", "2025-09-01T11:00:00"); // mesma validade mas menor BoxID

        wagonController.addWagon(w1);
        wagonController.addWagon(w2);
        wagonController.addWagon(w3);

        boolean success = wagonController.storeAllCargoInWarehouses();
        assertTrue(success);

        Bay bay1 = warehouseController.getBayById(
                warehouseController.getAisleById(
                        warehouseController.getWarehouseById(1), 1), 1);

        List<Cargo> stored = bay1.getCargo();
        assertEquals(2, stored.size());
        assertEquals(5, stored.get(0).getBoxID());
        assertEquals(10, stored.get(1).getBoxID());

        Bay bay2 = warehouseController.getBayById(
                warehouseController.getAisleById(
                        warehouseController.getWarehouseById(1), 1), 2);

        assertEquals(1, bay2.getCargo().size());
        assertEquals(20, bay2.getCargo().get(0).getBoxID());
    }
}
