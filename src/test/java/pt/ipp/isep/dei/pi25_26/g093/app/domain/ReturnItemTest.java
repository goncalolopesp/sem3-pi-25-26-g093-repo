package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.*;
public class ReturnItemTest {

    @Test
    public void testReturnItemConstructor() {
        LocalDateTime timestamp = LocalDateTime.now();
        LocalDate expiryDate = LocalDate.of(2025, 12, 31);

        ReturnItem item = new ReturnItem("R001", "SKU123", 10, "damaged", timestamp, expiryDate);

        assertEquals("R001", item.getReturnId());
        assertEquals("SKU123", item.getSku());
        assertEquals(10, item.getQuantity());
        assertEquals("damaged", item.getReason());
        assertEquals(timestamp, item.getTimestamp());
        assertEquals(expiryDate, item.getExpiryDate());
    }

    @Test
    public void testCompareTo_withEqualTimestamps() {
        // Timestamps iguais, comparação deve ser feita por returnID
        LocalDateTime timestamp = LocalDateTime.of(2025, 1, 1, 10, 0);

        ReturnItem item1 = new ReturnItem("R001", "SKU123", 10, "damaged", timestamp, null);
        ReturnItem item2 = new ReturnItem("R002", "SKU456", 5, "expired", timestamp, null);

        // item1 vem antes de item2, porque o returnID "R001" é lexicograficamente menor que "R002"
        assertTrue(item1.compareTo(item2) < 0); // item1 deve vir antes de item2
        assertTrue(item2.compareTo(item1) > 0); // item2 deve vir depois de item1
    }



    @Test
    public void testFromCSV() {
        Map<String, String> data = Map.of(
                "returnId", "R001",
                "sku", "SKU123",
                "qty", "10",
                "reason", "damaged",
                "timestamp", "2025-01-01T10:00:00",
                "expiryDate", "2025-12-31"
        );

        ReturnItem item = ReturnItem.fromCSV(data);

        assertEquals("R001", item.getReturnId());
        assertEquals("SKU123", item.getSku());
        assertEquals(10, item.getQuantity());
        assertEquals("damaged", item.getReason());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), item.getTimestamp());
        assertEquals(LocalDate.of(2025, 12, 31), item.getExpiryDate());
    }
}
