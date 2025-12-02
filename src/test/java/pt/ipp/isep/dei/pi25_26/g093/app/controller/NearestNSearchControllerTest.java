package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchResult;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for NearestNSearchController (JUnit 4, sem Mockito).
 */
public class NearestNSearchControllerTest {

    private NearestNSearchController controller;
    private StubNearestNSearchService stubService;

    @Before
    public void setUp() {
        stubService = new StubNearestNSearchService();
        controller = new NearestNSearchController(stubService);
    }

    // ==================== DELEGATION TESTS ====================

    @Test
    public void testSearchNearestN_DelegatesToService() {
        double lat = 40.0, lon = -8.0;
        NearestNSearchResult result = controller.searchNearestN(lat, lon, 5,
                true, false, "PT", "Europe/Lisbon", "WET");

        assertNotNull(result);
        assertEquals("PT", result.getStations().get(0).getCountry());
        assertTrue(stubService.wasCalled);
    }

    @Test
    public void testSearchNearestN_AllNullFilters_DelegatesCorrectly() {
        NearestNSearchResult result = controller.searchNearestN(40.0, -8.0, 10,
                null, null, null, null, null);

        assertNotNull(result);
        assertTrue(stubService.wasCalled);
    }

    // ==================== EXCEPTION PROPAGATION TESTS ====================

    @Test(expected = IllegalArgumentException.class)
    public void testSearchNearestN_InvalidLatitude_PropagatesException() {
        controller.searchNearestN(91.0, -8.0, 5, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchNearestN_InvalidLongitude_PropagatesException() {
        controller.searchNearestN(40.0, 181.0, 5, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchNearestN_InvalidN_PropagatesException() {
        controller.searchNearestN(40.0, -8.0, 0, null, null, null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSearchNearestN_EmptyTree_PropagatesException() {
        stubService.throwEmptyTree = true;
        controller.searchNearestN(40.0, -8.0, 5, null, null, null, null, null);
    }

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    public void testConstructor_DefaultConstructor_CreatesService() {
        NearestNSearchController defaultController = new NearestNSearchController();
        assertNotNull(defaultController);
    }

    @Test
    public void testConstructor_CustomService_UsesProvidedService() {
        NearestNSearchController customController = new NearestNSearchController(stubService);
        NearestNSearchResult result = customController.searchNearestN(40.0, -8.0, 5,
                null, null, null, null, null);
        assertNotNull(result);
    }

    // ==================== PARAMETER FORWARDING TESTS ====================

    @Test
    public void testSearchNearestN_AllParametersForwarded() {
        controller.searchNearestN(38.7223, -9.1393, 10,
                true, true, "PT", "Europe/Lisbon", "WET");

        assertEquals(38.7223, stubService.lastLat, 0.0001);
        assertEquals(-9.1393, stubService.lastLon, 0.0001);
        assertEquals(10, stubService.lastN);
        assertEquals(Boolean.TRUE, stubService.lastIsCity);
        assertEquals(Boolean.TRUE, stubService.lastIsMainStation);
        assertEquals("PT", stubService.lastCountry);
        assertEquals("Europe/Lisbon", stubService.lastTimeZone);
        assertEquals("WET", stubService.lastTimeZoneGroup);
    }

    // ==================== HELPER STUB ====================

    private static class StubNearestNSearchService extends NearestNSearchService {
        boolean wasCalled = false;
        boolean throwEmptyTree = false;

        double lastLat;
        double lastLon;
        int lastN;
        Boolean lastIsCity;
        Boolean lastIsMainStation;
        String lastCountry;
        String lastTimeZone;
        String lastTimeZoneGroup;

        @Override
        public NearestNSearchResult searchNearestN(double centerLat, double centerLon, int n,
                                                   Boolean isCity, Boolean isMainStation,
                                                   String country, String timeZone, String timeZoneGroup) {
            wasCalled = true;
            lastLat = centerLat;
            lastLon = centerLon;
            lastN = n;
            lastIsCity = isCity;
            lastIsMainStation = isMainStation;
            lastCountry = country;
            lastTimeZone = timeZone;
            lastTimeZoneGroup = timeZoneGroup;

            if (centerLat < -90 || centerLat > 90) {
                throw new IllegalArgumentException("Latitude must be within [-90, 90].");
            }
            if (centerLon < -180 || centerLon > 180) {
                throw new IllegalArgumentException("Longitude must be within [-180, 180].");
            }
            if (n <= 0) {
                throw new IllegalArgumentException("N must be greater than 0.");
            }
            if (throwEmptyTree) {
                throw new IllegalStateException("KD-tree is empty. Build it before querying.");
            }

            List<CompleteStation> stations = new ArrayList<>();
            stations.add(new CompleteStation("PT", "Europe/Lisbon", "WET", "Test",
                    40.0, -8.0, false, false, false));
            List<Double> distances = new ArrayList<>();
            distances.add(0.0);
            return new NearestNSearchResult(stations, distances, 1000L, new ArrayList<>());
        }
    }
}
