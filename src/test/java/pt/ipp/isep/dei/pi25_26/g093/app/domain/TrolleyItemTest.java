package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrolleyItemTest {

    @Test
    public void testTrolleyItemConstructor() {
        // Criando um TrolleyItem
        TrolleyItem item = new TrolleyItem("O001", "SKU123", 10, 2.5, 3, 4);

        // Verificando se os valores estão corretos
        assertEquals("O001", item.getOrderId());
        assertEquals("SKU123", item.getSku());
        assertEquals(10, item.getQty());
        assertEquals(2.5, item.getWeight(), 0.0);
        assertEquals(3, item.getAisle());
        assertEquals(4, item.getBay());
    }

    @Test
    public void testToString_withDifferentValues() {
         TrolleyItem item = new TrolleyItem("O002", "SKU987", 25, 15.75, 2, 7);

         String expected = "Order O002 | SKU SKU987 | Qty 25 | 15,75 kg | (Aisle 2, Bay 7)";

         assertEquals(expected, item.toString());
    }


}