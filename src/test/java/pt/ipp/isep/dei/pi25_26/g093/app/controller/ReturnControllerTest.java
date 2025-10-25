package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReturnControllerTest {

    private ReturnController controller;
    private WarehouseController warehouseController;
    private WagonController wagonController;

    @BeforeEach
    void setUp() {
        wagonController = new WagonController();
        warehouseController = new WarehouseController();
        controller = new ReturnController(wagonController, warehouseController);
    }

    private ReturnItem createItem(String id, String sku, int qty, String reason, int daysOffset) {
        return new ReturnItem(
                id, sku, qty, reason,
                LocalDateTime.now().plusDays(daysOffset),
                LocalDate.now().plusDays(daysOffset)
        );
    }

    @Test
    void expiredItemGoesDirectlyToDiscard() {
        ReturnItem item = createItem("R01", "SKU1", 5, "expired", -5);

        controller.processReturns(List.of(item));

        assertEquals(1, controller.getDiscarded().size());
        assertEquals(0, controller.getQuarantine().size());
    }

    @Test
    void damagedItemGoesDirectlyToDiscard() {
        ReturnItem item = createItem("R02", "SKU1", 5, "damaged", 3);

        controller.processReturns(List.of(item));

        assertEquals(1, controller.getDiscarded().size());
    }

    @Test
    void customerRemorseGoesToQuarantine() {
        ReturnItem item = createItem("R03", "SKU2", 3, "customer remorse", 2);

        controller.processReturns(List.of(item));

        assertEquals(1, controller.getQuarantine().size());
    }

    @Test
    void cycleCountGoesToQuarantine() {
        ReturnItem item = createItem("R04", "SKU3", 2, "cycle Count", 1);

        controller.processReturns(List.of(item));

        assertEquals(1, controller.getQuarantine().size());
    }

    @Test
    void quarantineOrdersLatestFirst() {
        ReturnItem older = createItem("R10","SKU5",2,"customer-remorse", -3);
        ReturnItem newer = createItem("R11","SKU5",2,"customer-remorse", 0);

        controller.processReturns(List.of(older, newer));

        ReturnItem first = controller.getQuarantine().poll();

        assertEquals("R11", first.getReturnId());
    }

    @Test
    void restockIfSkuExists() {
        // Add existing cargo first (SKU match required)
        Bay bay = new Bay(1, 10);
        bay.addCargo(new Cargo("INIT", "X1", 5, null, "2025-01-01T10:00:00"));
        Aisle aisle = new Aisle(1, List.of(bay));
        warehouseController.addWarehouse(new Warehouse(1, List.of(aisle)));

        ReturnItem item = createItem("R50", "X1", 5, "customer remorse", 0);

        controller.processReturns(List.of(item));
        controller.inspectQuarantine();

        assertEquals(1, controller.getRestocked().size());
        assertEquals(0, controller.getDiscarded().size());
    }

    @Test
    void discardWhenSkuNotExists() {
        ReturnItem item = createItem("R60", "NOPE", 4, "customer remorse", 0);

        controller.processReturns(List.of(item));
        controller.inspectQuarantine();

        assertEquals(1, controller.getDiscarded().size());
        assertEquals(0, controller.getRestocked().size());
    }

    @Test
    void unknownReasonLeadsToDiscard() {
        ReturnItem item = createItem("R70","SKU7",3,"gremlins", 1);

        controller.processReturns(List.of(item));

        assertEquals(1, controller.getDiscarded().size());
    }
}
