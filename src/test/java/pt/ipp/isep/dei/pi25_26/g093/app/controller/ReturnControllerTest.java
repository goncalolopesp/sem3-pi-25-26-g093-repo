package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.controller.ReturnController;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.ReturnItem;

import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class ReturnControllerTest {

    private ReturnController controller;
    private List<ReturnItem> returns;

    @Before
    public void setUp() {
        controller = new ReturnController();

         returns = new ArrayList<>(Arrays.asList(
                new ReturnItem("R001", "SKU123", 10, "damaged", LocalDateTime.of(2025, 1, 1, 10, 0), LocalDate.of(2025, 12, 31)),
                new ReturnItem("R002", "SKU456", 5, "customer-remorse", LocalDateTime.of(2025, 1, 2, 10, 0), null),
                new ReturnItem("R003", "SKU789", 3, "cycle-count", LocalDateTime.of(2025, 1, 3, 10, 0), null)
        ));
    }

    @Test
    public void testProcessReturns() {
        controller.processReturns(returns);

        // Verificar se os itens foram corretamente classificados
        assertEquals(1, controller.getDiscarded().size());
        assertEquals(1, controller.getRestocked().size());
        assertEquals(1, controller.getQuarantine().size());
    }

    @Test
    public void testPopQuarantineItem() {
        controller.processReturns(returns);
        ReturnItem quarantinedItem = controller.popQuarantineItem();

        assertEquals("R003", quarantinedItem.getReturnId());
    }

    @Test
    public void testInspectItem() {
        controller.processReturns(returns);
        ReturnItem quarantinedItem = controller.popQuarantineItem();

        String action = controller.inspectItem(quarantinedItem, true);

        assertEquals("Restocked", action);
        assertTrue(controller.getRestocked().contains(quarantinedItem));
    }

    @Test
    public void testLoadReturnItems() throws Exception {

        List<ReturnItem> loadedItems = controller.loadReturnItems();

        assertNotNull(loadedItems);
        assertTrue(loadedItems.size() > 0);
    }
}
