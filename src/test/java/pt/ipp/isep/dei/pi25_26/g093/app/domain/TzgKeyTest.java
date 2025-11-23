package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TzgKeyTest {

    private CompleteStation cs(String tzg, String country, String station) {
        return new CompleteStation(country, "TZ", tzg, station, 0.0, 0.0, false, false, false);
    }

    @Test
    public void testGetStationAndAttributes() {
        CompleteStation cs = cs("G1", "PT", "Lisboa");
        TzgKey key = new TzgKey(cs);

        assertEquals(cs, key.getStation());
        assertEquals("G1", key.getTimeZoneGroup());
        assertEquals("PT", key.getCountry());
    }

    @Test
    public void testCompareTo_OrdersByTzgCountryStation() {
        TzgKey k1 = new TzgKey(cs("A", "PT", "Lisboa"));
        TzgKey k2 = new TzgKey(cs("B", "PT", "Lisboa"));
        TzgKey k3 = new TzgKey(cs("A", "ES", "Madrid"));

        assertTrue(k1.compareTo(k2) < 0); // A < B
        assertTrue(k1.compareTo(k3) > 0); // PT > ES
    }
}
