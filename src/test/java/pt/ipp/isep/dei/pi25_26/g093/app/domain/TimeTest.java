package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {

    @Test
    public void testConstructorParsesTime() {
        Time t = new Time("10:20:30");
        assertEquals(10, t.getHour());
        assertEquals(20, t.getMinute());
        assertEquals(30, t.getSecond());
    }

    @Test
    public void testToStringFormat() {
        Time t = new Time("01:02:03");
        assertEquals("01:02:03", t.toString());
    }

    @Test
    public void testCompareToDifferentHours() {
        Time t1 = new Time("10:00:00");
        Time t2 = new Time("11:00:00");
        assertTrue(t1.compareTo(t2) < 0);
    }

    @Test
    public void testCompareToDifferentMinutes() {
        Time t1 = new Time("10:10:00");
        Time t2 = new Time("10:20:00");
        assertTrue(t1.compareTo(t2) < 0);
    }

    @Test
    public void testCompareToDifferentSeconds() {
        Time t1 = new Time("10:10:10");
        Time t2 = new Time("10:10:20");
        assertTrue(t1.compareTo(t2) < 0);
    }
}
