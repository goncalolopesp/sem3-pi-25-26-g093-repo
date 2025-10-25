package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReturnItemTest {

    @Test
    void testCompareToOrdersByTimestampDesc() {
        LocalDateTime now = LocalDateTime.now();
        ReturnItem older = new ReturnItem("R1", "SKU", 1, "reason", now.minusHours(1), null);
        ReturnItem newer = new ReturnItem("R2", "SKU", 1, "reason", now, null);

        assertTrue(newer.compareTo(older) < 0,
                "Newer item should come first in descending timestamp order");
    }

    @Test
    void testCompareToUsesReturnIdAsTiebreaker() {
        LocalDateTime ts = LocalDateTime.now();
        ReturnItem item1 = new ReturnItem("A1", "SKU", 1, "reason", ts, null);
        ReturnItem item2 = new ReturnItem("B1", "SKU", 1, "reason", ts, null);

        assertTrue(item1.compareTo(item2) < 0,
                "Alphabetically lower returnId should come first when timestamps are equal");
    }
}
