package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {

    @Test
    public void testConstructorParsing() {
        Time time = new Time("13:45:09");
        assertEquals(13, time.getHour());
        assertEquals(45, time.getMinute());
        assertEquals(9, time.getSecond());
    }

    @Test
    public void testToStringFormat() {
        Time time = new Time("07:05:02");
        assertEquals("07:05:02", time.toString());
    }

    @Test
    public void testCompareHours() {
        Time t1 = new Time("08:30:00");
        Time t2 = new Time("10:30:00");

        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
    }

    @Test
    public void testCompareMinutes() {
        Time t1 = new Time("10:15:00");
        Time t2 = new Time("10:45:00");

        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
    }

    @Test
    public void testCompareSeconds() {
        Time t1 = new Time("12:10:30");
        Time t2 = new Time("12:10:40");

        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
    }

    @Test
    public void testEqualTimes() {
        Time t1 = new Time("00:00:00");
        Time t2 = new Time("00:00:00");

        assertEquals(0, t1.compareTo(t2));
    }
}
