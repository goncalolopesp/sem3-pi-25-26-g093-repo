package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void testConstructorValidDate() {
        Date date = new Date("2025-12-01");
        assertEquals(2025, date.getYear());
        assertEquals(12, date.getMonth());
        assertEquals(1, date.getDay());
    }

    @Test
    public void testConstructorWithDateTimeFormat() {
        Date date = new Date("2024-04-15T10:30:45");
        assertEquals(2024, date.getYear());
        assertEquals(4, date.getMonth());
        assertEquals(15, date.getDay());
    }

    @Test
    public void testConstructorWithNullSetsDefaultExpiry() {
        Date date = new Date(null);
        assertEquals(9999, date.getYear());
        assertEquals(12, date.getMonth());
        assertEquals(31, date.getDay());
    }

    @Test
    public void testConstructorWithEmptyStringSetsDefaultExpiry() {
        Date date = new Date("");
        assertEquals("9999-12-31", date.toString());
    }

    @Test
    public void testToStringFormat() {
        Date date = new Date("2025-07-09");
        assertEquals("2025-07-09", date.toString());
    }

    @Test
    public void testCompareToDifferentYears() {
        Date d1 = new Date("2024-05-10");
        Date d2 = new Date("2025-05-10");

        assertTrue(d1.compareTo(d2) < 0);
        assertTrue(d2.compareTo(d1) > 0);
    }

    @Test
    public void testCompareToDifferentMonths() {
        Date d1 = new Date("2025-03-10");
        Date d2 = new Date("2025-04-10");

        assertTrue(d1.compareTo(d2) < 0);
        assertTrue(d2.compareTo(d1) > 0);
    }

    @Test
    public void testCompareToDifferentDays() {
        Date d1 = new Date("2025-03-10");
        Date d2 = new Date("2025-03-15");

        assertTrue(d1.compareTo(d2) < 0);
        assertTrue(d2.compareTo(d1) > 0);
    }

    @Test
    public void testCompareToEqualDates() {
        Date d1 = new Date("2025-09-09");
        Date d2 = new Date("2025-09-09");

        assertEquals(0, d1.compareTo(d2));
    }
}
