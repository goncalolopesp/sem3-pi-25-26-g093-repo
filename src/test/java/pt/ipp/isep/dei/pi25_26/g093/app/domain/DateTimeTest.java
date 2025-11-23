package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeTest {

    @Test
    public void testConstructorSplitsDateAndTime() {
        DateTime dt = new DateTime("2024-11-23T10:20:30");
        assertEquals("2024-11-23", dt.getDate().toString());
        assertEquals("10:20:30", dt.getTime().toString());
    }

    @Test
    public void testToStringRebuildsOriginal() {
        DateTime dt = new DateTime("2024-11-23T10:20:30");
        assertEquals("2024-11-23T10:20:30", dt.toString());
    }

    @Test
    public void testCompareToDifferentDates() {
        DateTime dt1 = new DateTime("2024-11-23T10:20:30");
        DateTime dt2 = new DateTime("2024-11-24T09:00:00");
        assertTrue(dt1.compareTo(dt2) < 0);
    }

    @Test
    public void testCompareToSameDateDifferentTimes() {
        DateTime dt1 = new DateTime("2024-11-23T10:20:30");
        DateTime dt2 = new DateTime("2024-11-23T11:00:00");
        assertTrue(dt1.compareTo(dt2) < 0);
    }

    @Test
    public void testCompareToEqual() {
        DateTime dt1 = new DateTime("2024-11-23T10:20:30");
        DateTime dt2 = new DateTime("2024-11-23T10:20:30");
        assertEquals(0, dt1.compareTo(dt2));
    }
}
