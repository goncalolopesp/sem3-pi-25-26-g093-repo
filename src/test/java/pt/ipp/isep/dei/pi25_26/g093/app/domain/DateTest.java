package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void testConstructorParsesValidDate() {
        Date d = new Date("2024-11-23");
        assertEquals(2024, d.getYear());
        assertEquals(11, d.getMonth());
        assertEquals(23, d.getDay());
    }

    @Test
    public void testConstructorHandlesNullAsDefault() {
        Date d = new Date(null);
        assertEquals("9999-12-31", d.toString());
    }

    @Test
    public void testConstructorRemovesTimePart() {
        Date d = new Date("2024-11-23T10:00:00");
        assertEquals(2024, d.getYear());
        assertEquals(11, d.getMonth());
        assertEquals(23, d.getDay());
    }

    @Test
    public void testToStringFormat() {
        Date d = new Date("2024-01-02");
        assertEquals("2024-01-02", d.toString());
    }

    @Test
    public void testCompareTo() {
        Date d1 = new Date("2024-01-01");
        Date d2 = new Date("2024-01-02");
        Date d3 = new Date("2023-12-31");

        assertTrue(d1.compareTo(d2) < 0);
        assertTrue(d2.compareTo(d1) > 0);
        assertTrue(d1.compareTo(new Date("2024-01-01")) == 0);
        assertTrue(d3.compareTo(d1) < 0);
    }
}
