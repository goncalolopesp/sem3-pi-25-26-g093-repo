package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompleteStationTest {

    @Test
    public void testConstructorAndGetters() {
        CompleteStation cs = new CompleteStation(
                "PT", "Europe/Lisbon", "TZG1", "Lisboa",
                38.7223, -9.1393, true, true, false
        );

        assertEquals("PT", cs.getCountry());
        assertEquals("Europe/Lisbon", cs.getTimeZone());
        assertEquals("TZG1", cs.getTimeZoneGroup());
        assertEquals("Lisboa", cs.getStation());
        assertEquals(Double.valueOf(38.7223), cs.getLatitude());
        assertEquals(Double.valueOf(-9.1393), cs.getLongitude());
        assertTrue(cs.isCity());
        assertTrue(cs.isMainStation());
        assertFalse(cs.isAirport());
    }

    @Test
    public void testToStringContainsKeyFields() {
        CompleteStation cs = new CompleteStation(
                "PT", "Europe/Lisbon", "TZG1", "Lisboa",
                38.0, -9.0, true, false, false
        );

        String s = cs.toString();
        assertTrue(s.contains("Lisboa"));
        assertTrue(s.contains("TZG1"));
        assertTrue(s.contains("PT"));
    }

    @Test
    public void testEqualsSameObject() {
        CompleteStation cs = new CompleteStation(
                "PT", "Europe/Lisbon", "TZG1", "Lisboa",
                38.0, -9.0, true, false, false
        );
        assertEquals(cs, cs);
    }

    @Test
    public void testEqualsDifferentObjectSameData() {
        CompleteStation cs1 = new CompleteStation(
                "PT", "TZ", "G", "Lisboa",
                38.0, -9.0, false, false, false
        );
        CompleteStation cs2 = new CompleteStation(
                "PT", "TZ", "G", "Lisboa",
                38.0, -9.0, true, true, true
        );
        assertEquals(cs1, cs2);
    }

    @Test
    public void testNotEqualsDifferentCoordinates() {
        CompleteStation cs1 = new CompleteStation(
                "PT", "TZ", "G", "Lisboa",
                38.0, -9.0, false, false, false
        );
        CompleteStation cs2 = new CompleteStation(
                "PT", "TZ", "G", "Lisboa",
                39.0, -9.0, false, false, false
        );
        assertNotEquals(cs1, cs2);
    }
}
