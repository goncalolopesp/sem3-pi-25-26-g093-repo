package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseControllerTest {

    private WarehouseController controller;

    @BeforeEach
    void setup() {
        controller = new WarehouseController();
        controller.addWarehouse(new Warehouse(1, List.of(
                new Aisle(1, List.of(
                        new Bay(1, 5),
                        new Bay(2, 5)
                ))
        )));
    }

    @Test
    void testStoreCargoFEFOPlacesInEmptyBay() {
        Cargo cargo = new Cargo("B1", "SKU1", 10, null, "2025-10-10T10:00:00");
        boolean result = controller.storeCargoFEFO(cargo);

        assertTrue(result);
        assertTrue(controller.skuExists("SKU1"));
    }

    @Test
    void testSkuExistsFalseWhenNoCargo() {
        assertFalse(controller.skuExists("SKU-XYZ"));
    }
}
