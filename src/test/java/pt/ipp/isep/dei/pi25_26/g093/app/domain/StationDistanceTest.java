package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class StationDistanceTest {

    @Test
    public void testConstructorAndGetters() {
        CompleteStation station = new CompleteStation(
                "PT", "GMT", "EU", "Lisboa",
                38.72, -9.13,
                true, true, false
        );

        StationDistance sd = new StationDistance(station, 12.5);

        assertEquals(station, sd.getStation());
        assertEquals(12.5, sd.getDistanceKm(), 0.0001);
    }

    @Test
    public void testCompareTo_DifferentDistances() {
        CompleteStation s = new CompleteStation(
                "PT", "GMT", "EU", "Lisboa",
                38.72, -9.13,
                true, true, false
        );

        StationDistance a = new StationDistance(s, 5.0);
        StationDistance b = new StationDistance(s, 10.0);

        assertTrue(a.compareTo(b) < 0);   // 5 km < 10 km
        assertTrue(b.compareTo(a) > 0);
    }

    @Test
    public void testCompareTo_SameDistanceSortByName() {
        CompleteStation s1 = new CompleteStation(
                "PT", "GMT", "EU", "Lisboa",
                38.72, -9.13,
                true, true, false
        );

        CompleteStation s2 = new CompleteStation(
                "PT", "GMT", "EU", "Porto",
                41.15, -8.61,
                true, true, false
        );

        StationDistance a = new StationDistance(s1, 10.0); // Lisboa
        StationDistance b = new StationDistance(s2, 10.0); // Porto

        // Lisboa < Porto alfabeticamente
        assertTrue(a.compareTo(b) < 0);
    }

    @Test
    public void testToString() {
        CompleteStation s = new CompleteStation(
                "PT", "GMT", "EU", "Lisboa",
                38.72, -9.13,
                true, true, false
        );

        StationDistance sd = new StationDistance(s, 7.23);

        String txt = sd.toString();

        // Verifica que começa com "7.23 km - "
        assertTrue(txt.startsWith("7.23 km - "));

        // Verifica que contém o nome da estação
        assertTrue(txt.contains("Lisboa"));
    }
}
