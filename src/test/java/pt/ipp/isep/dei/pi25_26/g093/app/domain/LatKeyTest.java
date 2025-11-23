package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class LatKeyTest {

    private CompleteStation cs(String name, String country, Double lat) {
        return new CompleteStation(country, "TZ", "G", name, lat, 0.0, false, false, false);
    }

    @Test
    public void testGetStationAndLatitude() {
        CompleteStation cs = cs("Lisboa", "PT", 10.0);
        LatKey key = new LatKey(cs);

        assertEquals(cs, key.getStation());
        assertEquals(Double.valueOf(10.0), key.getLatitude());
    }

    @Test
    public void testCompareTo_OrdersByLatitude() {
        LatKey k1 = new LatKey(cs("Lisboa", "PT", 10.0));
        LatKey k2 = new LatKey(cs("Madrid", "ES", 20.0));

        assertTrue(k1.compareTo(k2) < 0);
        assertTrue(k2.compareTo(k1) > 0);
    }

    @Test
    public void testCompareTo_HandlesNullLatitudes() {
        LatKey k1 = new LatKey(cs("Lisboa", "PT", null));
        LatKey k2 = new LatKey(cs("Madrid", "ES", 20.0));

        // null latitude is considered "greater" => comes last
        assertTrue(k1.compareTo(k2) > 0);
        assertTrue(k2.compareTo(k1) < 0);
    }

    @Test
    public void testCompareTo_SameLatitudeUsesStationThenCountry() {
        LatKey k1 = new LatKey(cs("Lisboa", "PT", 10.0));
        LatKey k2 = new LatKey(cs("Porto", "PT", 10.0));

        assertTrue(k1.compareTo(k2) < 0); // Lisboa < Porto
    }
}
