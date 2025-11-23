package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class LonKeyTest {

    private CompleteStation cs(String name, String country, Double lon) {
        return new CompleteStation(country, "TZ", "G", name, 0.0, lon, false, false, false);
    }

    @Test
    public void testCompareTo_OrdersByLongitude() {
        LonKey k1 = new LonKey(cs("Lisboa", "PT", -9.0));
        LonKey k2 = new LonKey(cs("Madrid", "ES", -3.0));

        assertTrue(k1.compareTo(k2) < 0);
        assertTrue(k2.compareTo(k1) > 0);
    }

    @Test
    public void testCompareTo_HandlesNullLongitudes() {
        LonKey k1 = new LonKey(cs("Lisboa", "PT", null));
        LonKey k2 = new LonKey(cs("Madrid", "ES", 10.0));

        assertTrue(k1.compareTo(k2) > 0); // null treated as greater
    }

    @Test
    public void testCompareTo_SameLongitudeUsesStationThenCountry() {
        LonKey k1 = new LonKey(cs("Lisboa", "PT", 10.0));
        LonKey k2 = new LonKey(cs("Porto", "PT", 10.0));

        assertTrue(k1.compareTo(k2) < 0);
    }
}
