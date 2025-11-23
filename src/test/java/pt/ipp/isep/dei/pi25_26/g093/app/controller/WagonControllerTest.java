package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WagonRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.WarehouseRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WagonControllerTest {

    private WagonController controller;
    private WagonRepository wagonRepo;
    private WarehouseRepository warehouseRepo;

    @Before
    public void setUp() {
        Repositories repos = Repositories.getInstance();
        wagonRepo = repos.getWagonRepository();
        warehouseRepo = repos.getWarehouseRepository();

        wagonRepo.getWagons().clear();
        warehouseRepo.getWarehouseList().clear();

        controller = new WagonController();
    }

    private WarehouseController createWarehouseControllerWithSingleBay(int capacity) {
        Bay bay = new Bay(1, capacity);
        Aisle aisle = new Aisle(1, new ArrayList<>(Arrays.asList(bay)));
        Warehouse wh = new Warehouse(1, new ArrayList<>(Arrays.asList(aisle)));
        warehouseRepo.add(wh);
        return new WarehouseController();
    }

    @Test
    public void testAddAndGetWagons_UsingWagonObject() {
        Wagon w = new Wagon(1, 10, 100, 5, "2025-01-01", "2024-11-23T10:00:00");
        controller.addWagon(w);

        List<Wagon> wagons = controller.getWagons();
        assertEquals(1, wagons.size());
        assertEquals(w, wagons.get(0));
    }

    @Test
    public void testAddWagon_UsingPrimitiveParameters() {
        controller.addWagon(1, 10, 200, 3, "2025-12-31", "2024-11-23T09:00:00");

        List<Wagon> wagons = controller.getWagons();
        assertEquals(1, wagons.size());
        Wagon w = wagons.get(0);

        assertEquals(1, w.getwagonID());
        assertEquals(10, w.getCargo().getBoxID());
        assertEquals(200, w.getCargo().getSku());
    }

    @Test
    public void testStoreCargoDirectly_PlacesCargoInFirstAvailableBay() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(2);
        Cargo c = new Cargo(10, 100, 5, "2025-01-01", "2024-11-23T10:00:00");

        Bay bay = controller.storeCargoDirectly(c, wc);
        assertNotNull(bay);
        assertEquals(1, bay.getCargo().size());
        assertEquals(c, bay.getCargo().get(0));
    }

    @Test
    public void testStoreCargoDirectly_ReturnsNullWhenNoCapacity() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(0);
        Cargo c = new Cargo(10, 100, 5, "2025-01-01", "2024-11-23T10:00:00");

        Bay bay = controller.storeCargoDirectly(c, wc);
        assertNull(bay);
    }

    @Test
    public void testRelocateBox_Success() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(5);
        Warehouse wh = warehouseRepo.getWarehouseList().get(0);
        Bay originBay = wh.getAisles().get(0).getBays().get(0);
        Bay targetBay = new Bay(2, 5);
        wh.getAisles().get(0).getBays().add(targetBay);

        Cargo c = new Cargo(10, 100, 5, "2025-01-01", "2024-11-23T10:00:00");
        originBay.addCargo(c);

        boolean moved = controller.relocateBox(100, 10, wc, targetBay);

        assertTrue(moved);
        assertFalse(originBay.getCargo().contains(c));
        assertTrue(targetBay.getCargo().contains(c));
    }

    @Test
    public void testRelocateBox_NotFoundReturnsFalse() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(5);
        Warehouse wh = warehouseRepo.getWarehouseList().get(0);
        Bay targetBay = wh.getAisles().get(0).getBays().get(0);

        boolean moved = controller.relocateBox(999, 999, wc, targetBay);
        assertFalse(moved);
    }

    @Test
    public void testDispatchCargoFromWagons_RemovesAllCargo() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(3);
        Warehouse wh = warehouseRepo.getWarehouseList().get(0);
        Bay bay = wh.getAisles().get(0).getBays().get(0);

        Cargo c1 = new Cargo(1, 100, 1, "2025-01-01", "2024-11-23T08:00:00");
        Cargo c2 = new Cargo(2, 100, 1, "2025-01-02", "2024-11-23T09:00:00");
        bay.addCargo(c1);
        bay.addCargo(c2);

        List<Cargo> dispatched = controller.dispatchCargoFromWagons();
        assertEquals(2, dispatched.size());
        assertTrue(dispatched.contains(c1));
        assertTrue(dispatched.contains(c2));
        assertTrue(bay.getCargo().isEmpty());
    }

    @Test
    public void testDispatchQuantityCargoFromWagons_LimitsByQuantity() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(5);
        Warehouse wh = warehouseRepo.getWarehouseList().get(0);
        Bay bay = wh.getAisles().get(0).getBays().get(0);

        Cargo c1 = new Cargo(1, 100, 1, "2025-01-01", "2024-11-23T08:00:00");
        Cargo c2 = new Cargo(2, 100, 1, "2025-01-02", "2024-11-23T09:00:00");
        Cargo c3 = new Cargo(3, 100, 1, "2025-01-03", "2024-11-23T10:00:00");
        bay.addCargo(c1);
        bay.addCargo(c2);
        bay.addCargo(c3);

        List<Cargo> dispatched = controller.dispatchQuantityCargoFromWagons(2);
        assertEquals(2, dispatched.size());
        assertEquals(1, bay.getCargo().size());
    }

    @Test
    public void testDispatchQuantityCargoFromWagonsBySku_SelectsCorrectSku() {
        WarehouseController wc = createWarehouseControllerWithSingleBay(5);
        Warehouse wh = warehouseRepo.getWarehouseList().get(0);
        Bay bay = wh.getAisles().get(0).getBays().get(0);

        Cargo c1 = new Cargo(1, 100, 1, "2025-01-01", "2024-11-23T08:00:00");
        Cargo c2 = new Cargo(2, 200, 1, "2025-01-02", "2024-11-23T09:00:00");
        Cargo c3 = new Cargo(3, 100, 1, "2025-01-03", "2024-11-23T10:00:00");
        bay.addCargo(c1);
        bay.addCargo(c2);
        bay.addCargo(c3);

        List<Cargo> dispatched = controller.dispatchQuantityCargoFromWagonsBySku(100, 2);
        assertEquals(2, dispatched.size());
        for (Cargo c : dispatched) {
            assertEquals(100, c.getSku());
        }
        // c2 (sku 200) deve ter ficado
        assertTrue(bay.getCargo().contains(c2));
    }
}
