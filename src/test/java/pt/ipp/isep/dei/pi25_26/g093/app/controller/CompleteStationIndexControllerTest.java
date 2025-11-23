package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CompleteStationIndexControllerTest {

    private CompleteStationIndexController controller;
    private CompleteStationRepository repo;

    @Before
    public void setUp() {
        repo = Repositories.getInstance().getCompleteStationRepository();
        repo.getStations().clear();
        controller = new CompleteStationIndexController();
    }

    /**
     * Helper para testes que precisam de latitude/longitude.
     */
    private CompleteStation csLatLon(String country, String tzg, String station,
                                     Double lat, Double lon) {
        return new CompleteStation(
                country, "TZ", tzg, station,
                lat, lon, false, false, false
        );
    }

    /**
     * Helper para testes que precisam de timeZoneGroup (TZG).
     */
    private CompleteStation csGroup(String country, String timeZoneGroup,
                                    String station, Double latitude) {
        return new CompleteStation(
                country, "TZ", timeZoneGroup, station,
                latitude, 0.0, false, false, false
        );
    }

    // ---------- getByLatitudeRange ----------

    @Test
    public void testGetByLatitudeRange_ReturnsStationsWithinRangeSorted() {
        CompleteStation s1 = csLatLon("PT", "A", "Lisboa", 10.0, 0.0);
        CompleteStation s2 = csLatLon("ES", "A", "Madrid", 5.0, 0.0);
        CompleteStation s3 = csLatLon("FR", "A", "Paris", 20.0, 0.0);
        repo.addStation(s1);
        repo.addStation(s2);
        repo.addStation(s3);

        Map<Double, List<CompleteStation>> result = controller.getByLatitudeRange(6.0, 20.0);
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(2, list.size());
        assertEquals(s1, list.get(0)); // 10.0
        assertEquals(s3, list.get(1)); // 20.0
    }

    @Test
    public void testGetByLatitudeRange_MinGreaterThanMax_IsSwapped() {
        CompleteStation s1 = csLatLon("PT", "A", "Lisboa", 10.0, 0.0);
        repo.addStation(s1);

        Map<Double, List<CompleteStation>> result = controller.getByLatitudeRange(20.0, 0.0);
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(1, list.size());
        assertEquals(s1, list.get(0));
    }

    @Test
    public void testGetByLatitudeRange_NoStationsLoaded_ReturnsNull() {
        Map<Double, List<CompleteStation>> result = controller.getByLatitudeRange(0.0, 100.0);
        assertNull(result);
    }

    @Test
    public void testGetByLatitudeRange_IgnoresNullLatitude() {
        CompleteStation s1 = csLatLon("PT", "A", "Lisboa", null, 0.0);
        CompleteStation s2 = csLatLon("ES", "A", "Madrid", 10.0, 0.0);
        repo.addStation(s1);
        repo.addStation(s2);

        Map<Double, List<CompleteStation>> result = controller.getByLatitudeRange(0.0, 20.0);
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(1, list.size());
        assertEquals(s2, list.get(0));
    }

    // ---------- getByLongitudeRange ----------

    @Test
    public void testGetByLongitudeRange_Basic() {
        CompleteStation s1 = csLatLon("PT", "A", "Lisboa", 0.0, -9.1);
        CompleteStation s2 = csLatLon("ES", "A", "Madrid", 0.0, -3.7);
        CompleteStation s3 = csLatLon("FR", "A", "Paris", 0.0, 2.3);
        repo.addStation(s1);
        repo.addStation(s2);
        repo.addStation(s3);

        Map<Double, List<CompleteStation>> result = controller.getByLongitudeRange(-4.0, 3.0);
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(2, list.size());
        assertEquals(s2, list.get(0)); // -3.7
        assertEquals(s3, list.get(1)); // 2.3
    }

    @Test
    public void testGetByLongitudeRange_MinGreaterThanMax_IsSwapped() {
        CompleteStation s1 = csLatLon("PT", "A", "Lisboa", 0.0, 1.0);
        repo.addStation(s1);

        Map<Double, List<CompleteStation>> result = controller.getByLongitudeRange(5.0, -5.0);
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(1, list.size());
        assertEquals(s1, list.get(0));
    }

    @Test
    public void testGetByLongitudeRange_IgnoresNullLongitude() {
        CompleteStation s1 = csLatLon("PT", "A", "Lisboa", 0.0, null);
        CompleteStation s2 = csLatLon("ES", "A", "Madrid", 0.0, 2.0);
        repo.addStation(s1);
        repo.addStation(s2);

        Map<Double, List<CompleteStation>> result = controller.getByLongitudeRange(-10.0, 10.0);
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(1, list.size());
        assertEquals(s2, list.get(0));
    }

    // ---------- getByTimeZoneGroup ----------

    @Test
    public void testGetByTimeZoneGroup_BasicCaseInsensitive() {
        // timeZoneGroup = "TZG1"/"TZG2"
        CompleteStation s1 = csGroup("PT", "TZG1", "Lisboa", 0.0);
        CompleteStation s2 = csGroup("ES", "tzg1", "Madrid", 0.0);
        CompleteStation s3 = csGroup("FR", "TZG2", "Paris", 0.0);
        repo.addStation(s1);
        repo.addStation(s2);
        repo.addStation(s3);

        Map<Double, List<CompleteStation>> result = controller.getByTimeZoneGroup(" tZg1 ");
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(2, list.size());
        // sorted by country then station
        assertEquals(s1, list.get(0));
        assertEquals(s2, list.get(1));
    }

    @Test
    public void testGetByTimeZoneGroup_NullOrBlank_ReturnsEmptyMap() {
        CompleteStation s1 = csGroup("PT", "TZG1", "Lisboa", 0.0);
        repo.addStation(s1);

        assertTrue(controller.getByTimeZoneGroup(null).isEmpty());
        assertTrue(controller.getByTimeZoneGroup("   ").isEmpty());
    }

    // ---------- getByTimeZoneGroupWindow ----------

    @Test
    public void testGetByTimeZoneGroupWindow_InclusiveAndSorted() {
        CompleteStation s1 = csGroup("PT", "B", "Lisboa", 0.0);
        CompleteStation s2 = csGroup("ES", "A", "Madrid", 0.0);
        CompleteStation s3 = csGroup("FR", "C", "Paris", 0.0);
        repo.addStation(s1);
        repo.addStation(s2);
        repo.addStation(s3);

        Map<Double, List<CompleteStation>> result = controller.getByTimeZoneGroupWindow("A", "B");
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(2, list.size());
        // sorted by timeZoneGroup, then country, then station
        assertEquals(s2, list.get(0)); // A
        assertEquals(s1, list.get(1)); // B
    }

    @Test
    public void testGetByTimeZoneGroupWindow_SwapsStartEndIfNeeded() {
        CompleteStation s1 = csGroup("PT", "B", "Lisboa", 0.0);
        repo.addStation(s1);

        Map<Double, List<CompleteStation>> result = controller.getByTimeZoneGroupWindow("Z", "A");
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(1, list.size());
        assertEquals(s1, list.get(0));
    }

    @Test
    public void testGetByTimeZoneGroupWindow_NullArgs_ReturnsEmptyMap() {
        assertTrue(controller.getByTimeZoneGroupWindow(null, "B").isEmpty());
        assertTrue(controller.getByTimeZoneGroupWindow("A", null).isEmpty());
    }

    // ---------- getByTimeZoneGroupAndCountry ----------

    @Test
    public void testGetByTimeZoneGroupAndCountry_BasicCaseInsensitive() {
        CompleteStation s1 = csGroup("PT", "G1", "Lisboa", 0.0);
        CompleteStation s2 = csGroup("pt", "G1", "Porto", 0.0);
        CompleteStation s3 = csGroup("ES", "G1", "Madrid", 0.0);
        repo.addStation(s1);
        repo.addStation(s2);
        repo.addStation(s3);

        Map<Double, List<CompleteStation>> result =
                controller.getByTimeZoneGroupAndCountry("g1", "pt");
        assertNotNull(result);
        List<CompleteStation> list = result.values().iterator().next();

        assertEquals(2, list.size());
        // sorted by station name
        assertEquals(s1, list.get(0)); // Lisboa
        assertEquals(s2, list.get(1)); // Porto
    }

    @Test
    public void testGetByTimeZoneGroupAndCountry_NullArgs_ReturnsEmptyMap() {
        assertTrue(controller.getByTimeZoneGroupAndCountry(null, "PT").isEmpty());
        assertTrue(controller.getByTimeZoneGroupAndCountry("G1", null).isEmpty());
    }
}
