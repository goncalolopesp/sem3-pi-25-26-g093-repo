package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeTest {

    @Test
    public void testConstructorParsing() {
        DateTime dt = new DateTime("2025-12-01T13:43:00");

        assertEquals(2025, dt.getDate().getYear());
        assertEquals(12, dt.getDate().getMonth());
        assertEquals(1, dt.getDate().getDay());
        assertEquals(13, dt.getTime().getHour());
        assertEquals(43, dt.getTime().getMinute());
        assertEquals(0, dt.getTime().getSecond());
    }

    @Test
    public void testToStringFormat() {
        DateTime dt = new DateTime("2024-11-06T09:05:07");
        assertEquals("2024-11-06T09:05:07", dt.toString());
    }

    @Test
    public void testCompareToDifferentDates() {
        DateTime dt1 = new DateTime("2024-05-10T10:15:30");
        DateTime dt2 = new DateTime("2025-05-10T10:15:30");

        assertTrue(dt1.compareTo(dt2) < 0);
        assertTrue(dt2.compareTo(dt1) > 0);
    }

    @Test
    public void testCompareToSameDateDifferentTimes() {
        DateTime dt1 = new DateTime("2025-03-10T08:00:00");
        DateTime dt2 = new DateTime("2025-03-10T09:00:00");

        assertTrue(dt1.compareTo(dt2) < 0);
        assertTrue(dt2.compareTo(dt1) > 0);
    }

    @Test
    public void testCompareToEqual() {
        DateTime dt1 = new DateTime("2026-01-01T00:00:00");
        DateTime dt2 = new DateTime("2026-01-01T00:00:00");

        assertEquals(0, dt1.compareTo(dt2));
    }
}
