package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for USEI09 - Nearest-N Proximity Search Service (JUnit 4).
 *
 * These tests validate:
 * - Input validation (coordinates, N parameter)
 * - KD-tree nearest-N algorithm correctness
 * - Max-heap behavior (size N)
 * - Pruning logic
 * - Filter application during search
 * - Result sorting and limiting
 * - Edge cases (empty tree, N > available matches, ties)
 */
public class NearestNSearchServiceTest {

    private CompleteStationRepository repository;
    private NearestNSearchService service;
    private Tree<Double> tree;

    @Before
    public void setUp() {
        repository = new CompleteStationRepository();
        service = new NearestNSearchService(repository);
        tree = new Tree<>(2);
    }

    // ==================== HELPER METHODS ====================

    private CompleteStation createStation(String name, double lat, double lon) {
        return new CompleteStation("PT", "Europe/Lisbon", "WET", name, lat, lon, false, false, false);
    }

    private CompleteStation createStation(String country, String timeZone, String timeZoneGroup,
                                          String name, double lat, double lon,
                                          boolean isCity, boolean isMainStation, boolean isAirport) {
        return new CompleteStation(country, timeZone, timeZoneGroup, name, lat, lon, isCity, isMainStation, isAirport);
    }

    private void insertStation(CompleteStation station) {
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(station.getLatitude());
        coords.add(station.getLongitude());
        tree.Insert(coords, station);
    }

    private void setupTree() {
        repository.setTree(tree);
    }

    // ==================== VALIDATION TESTS ====================

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLatitudeBelowMin90_ThrowsException() {
        setupTree();
        service.searchNearestN(-91.0, -9.0, 5, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLatitudeAboveMax90_ThrowsException() {
        setupTree();
        service.searchNearestN(91.0, -9.0, 5, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLongitudeBelowMin180_ThrowsException() {
        setupTree();
        service.searchNearestN(40.0, -181.0, 5, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLongitudeAboveMax180_ThrowsException() {
        setupTree();
        service.searchNearestN(40.0, 181.0, 5, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_NZero_ThrowsException() {
        setupTree();
        service.searchNearestN(40.0, -9.0, 0, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_NegativeN_ThrowsException() {
        setupTree();
        service.searchNearestN(40.0, -9.0, -5, null, null, null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidation_EmptyTree_ThrowsException() {
        service.searchNearestN(40.0, -9.0, 5, null, null, null, null, null);
    }

    // ==================== BASIC NEAREST-N TESTS ====================

    @Test
    public void testNearestN_SingleStation_ReturnsThatStation() {
        CompleteStation station = createStation("Lisbon", 38.7223, -9.1393);
        insertStation(station);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(38.7223, -9.1393, 1, null, null, null, null, null);

        assertEquals(1, result.getStations().size());
        assertEquals(station, result.getStations().get(0));
        assertEquals(0.0, result.getDistances().get(0), 0.01);
    }

    @Test
    public void testNearestN_TwoStations_ReturnsClosest() {
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        insertStation(lisbon);
        insertStation(porto);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(38.7223, -9.1393, 1, null, null, null, null, null);

        assertEquals(1, result.getStations().size());
        assertEquals(lisbon, result.getStations().get(0));
    }

    @Test
    public void testNearestN_TwoStations_ReturnsBothWhenNIs2() {
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        insertStation(lisbon);
        insertStation(porto);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(38.7223, -9.1393, 2, null, null, null, null, null);

        assertEquals(2, result.getStations().size());
        assertEquals(lisbon, result.getStations().get(0));
        assertEquals(porto, result.getStations().get(1));
    }

    @Test
    public void testNearestN_ResultsSortedByDistance() {
        CompleteStation center = createStation("Center", 40.0, -8.0);
        CompleteStation near1 = createStation("Near1", 40.1, -8.0);
        CompleteStation near2 = createStation("Near2", 40.2, -8.0);
        CompleteStation far1 = createStation("Far1", 41.0, -8.0);
        CompleteStation far2 = createStation("Far2", 42.0, -8.0);

        insertStation(center);
        insertStation(near1);
        insertStation(near2);
        insertStation(far1);
        insertStation(far2);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, null, null, null, null);

        assertEquals(5, result.getStations().size());
        List<Double> distances = result.getDistances();
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue(distances.get(i) <= distances.get(i + 1));
        }
        assertEquals(center, result.getStations().get(0));
    }

    @Test
    public void testNearestN_NLargerThanAvailable_ReturnsAllAvailable() {
        CompleteStation s1 = createStation("Station1", 40.0, -8.0);
        CompleteStation s2 = createStation("Station2", 40.1, -8.0);
        insertStation(s1);
        insertStation(s2);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 10, null, null, null, null, null);

        assertEquals(2, result.getStations().size());
    }

    // ==================== TIE-BREAKING TESTS ====================

    @Test
    public void testNearestN_DuplicateCoordinates_SortedByName() {
        CompleteStation stationA = createStation("A-Station", 40.0, -8.0);
        CompleteStation stationB = createStation("B-Station", 40.0, -8.0);
        CompleteStation stationC = createStation("C-Station", 40.0, -8.0);

        insertStation(stationA);
        insertStation(stationB);
        insertStation(stationC);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 3, null, null, null, null, null);

        assertEquals(3, result.getStations().size());
        assertEquals("A-Station", result.getStations().get(0).getStation());
        assertEquals("B-Station", result.getStations().get(1).getStation());
        assertEquals("C-Station", result.getStations().get(2).getStation());
    }

    // ==================== FILTER TESTS ====================

    @Test
    public void testNearestN_WithIsCityFilter_ReturnsOnlyCityStations() {
        CompleteStation city1 = createStation("PT", "Europe/Lisbon", "WET", "City1", 40.0, -8.0, true, false, false);
        CompleteStation city2 = createStation("PT", "Europe/Lisbon", "WET", "City2", 40.1, -8.0, true, false, false);
        CompleteStation nonCity = createStation("PT", "Europe/Lisbon", "WET", "NonCity", 40.2, -8.0, false, false, false);

        insertStation(city1);
        insertStation(city2);
        insertStation(nonCity);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, true, null, null, null, null);

        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(CompleteStation::isCity));
    }

    @Test
    public void testNearestN_WithIsMainStationFilter_ReturnsOnlyMainStations() {
        CompleteStation main1 = createStation("PT", "Europe/Lisbon", "WET", "Main1", 40.0, -8.0, false, true, false);
        CompleteStation main2 = createStation("PT", "Europe/Lisbon", "WET", "Main2", 40.1, -8.0, false, true, false);
        CompleteStation nonMain = createStation("PT", "Europe/Lisbon", "WET", "NonMain", 40.2, -8.0, false, false, false);

        insertStation(main1);
        insertStation(main2);
        insertStation(nonMain);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, true, null, null, null);

        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(CompleteStation::isMainStation));
    }

    @Test
    public void testNearestN_WithCountryFilter_ReturnsOnlyMatchingCountry() {
        CompleteStation pt1 = createStation("PT", "Europe/Lisbon", "WET", "PT1", 40.0, -8.0, false, false, false);
        CompleteStation pt2 = createStation("PT", "Europe/Lisbon", "WET", "PT2", 40.1, -8.0, false, false, false);
        CompleteStation es1 = createStation("ES", "Europe/Madrid", "CET", "ES1", 40.2, -8.0, false, false, false);

        insertStation(pt1);
        insertStation(pt2);
        insertStation(es1);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, null, "PT", null, null);

        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "PT".equals(s.getCountry())));
    }

    @Test
    public void testNearestN_WithTimeZoneFilter_ReturnsOnlyMatchingTimeZone() {
        CompleteStation tz1 = createStation("PT", "Europe/Lisbon", "WET", "TZ1", 40.0, -8.0, false, false, false);
        CompleteStation tz2 = createStation("PT", "Europe/Lisbon", "WET", "TZ2", 40.1, -8.0, false, false, false);
        CompleteStation tz3 = createStation("ES", "Europe/Madrid", "CET", "TZ3", 40.2, -8.0, false, false, false);

        insertStation(tz1);
        insertStation(tz2);
        insertStation(tz3);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, null, null, "Europe/Lisbon", null);

        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "Europe/Lisbon".equalsIgnoreCase(s.getTimeZone())));
    }

    @Test
    public void testNearestN_WithTimeZoneGroupFilter_ReturnsOnlyMatchingTimeZoneGroup() {
        CompleteStation tzg1 = createStation("PT", "Europe/Lisbon", "WET", "TZG1", 40.0, -8.0, false, false, false);
        CompleteStation tzg2 = createStation("PT", "Europe/Lisbon", "WET", "TZG2", 40.1, -8.0, false, false, false);
        CompleteStation tzg3 = createStation("ES", "Europe/Madrid", "CET", "TZG3", 40.2, -8.0, false, false, false);

        insertStation(tzg1);
        insertStation(tzg2);
        insertStation(tzg3);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, null, null, null, "WET");

        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "WET".equalsIgnoreCase(s.getTimeZoneGroup())));
    }

    @Test
    public void testNearestN_WithAllFilters_ReturnsOnlyMatchingStations() {
        CompleteStation match = createStation("PT", "Europe/Lisbon", "WET", "Match", 40.0, -8.0, true, true, false);
        CompleteStation noMatch1 = createStation("ES", "Europe/Madrid", "CET", "NoMatch1", 40.1, -8.0, true, true, false);
        CompleteStation noMatch2 = createStation("PT", "Europe/Lisbon", "WET", "NoMatch2", 40.2, -8.0, false, true, false);
        CompleteStation noMatch3 = createStation("PT", "Europe/Lisbon", "WET", "NoMatch3", 40.3, -8.0, true, false, false);

        insertStation(match);
        insertStation(noMatch1);
        insertStation(noMatch2);
        insertStation(noMatch3);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, true, true, "PT", "Europe/Lisbon", "WET");

        assertEquals(1, result.getStations().size());
        assertEquals(match, result.getStations().get(0));
    }

    @Test
    public void testNearestN_WithFiltersReducingCount_ReturnsLessThanN() {
        CompleteStation match1 = createStation("PT", "Europe/Lisbon", "WET", "Match1", 40.0, -8.0, true, false, false);
        CompleteStation match2 = createStation("PT", "Europe/Lisbon", "WET", "Match2", 40.1, -8.0, true, false, false);
        CompleteStation noMatch = createStation("ES", "Europe/Madrid", "CET", "NoMatch", 40.2, -8.0, true, false, false);

        insertStation(match1);
        insertStation(match2);
        insertStation(noMatch);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, true, null, "PT", null, null);

        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().size() <= 5);
    }

    // ==================== HAVERSINE DISTANCE TESTS ====================

    @Test
    public void testNearestN_HaversineDistance_CalculatedCorrectly() {
        // Lisbon (38.7223, -9.1393) to Porto (41.1579, -8.6291) ≈ 274 km
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        insertStation(lisbon);
        insertStation(porto);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(38.7223, -9.1393, 2, null, null, null, null, null);

        assertEquals(2, result.getStations().size());
        assertEquals(0.0, result.getDistances().get(0), 1.0);
        double portoDistance = result.getDistances().get(1);
        assertTrue(portoDistance > 260 && portoDistance < 290);
    }

    // ==================== RESULT STRUCTURE TESTS ====================

    @Test
    public void testNearestN_StationsAndDistancesAligned() {
        CompleteStation s1 = createStation("S1", 40.0, -8.0);
        CompleteStation s2 = createStation("S2", 40.1, -8.0);
        CompleteStation s3 = createStation("S3", 40.2, -8.0);

        insertStation(s1);
        insertStation(s2);
        insertStation(s3);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 3, null, null, null, null, null);

        assertEquals(result.getStations().size(), result.getDistances().size());
        for (int i = 0; i < result.getStations().size(); i++) {
            assertNotNull(result.getStations().get(i));
            assertNotNull(result.getDistances().get(i));
            assertTrue(result.getDistances().get(i) >= 0);
        }
    }

    @Test
    public void testNearestN_ExecutionTimeTracked() {
        CompleteStation s1 = createStation("S1", 40.0, -8.0);
        insertStation(s1);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 1, null, null, null, null, null);

        assertTrue(result.getExecutionTimeNanos() > 0);
    }

    @Test
    public void testNearestN_SampleQueriesPresent() {
        CompleteStation s1 = createStation("S1", 40.0, -8.0);
        insertStation(s1);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 1, null, null, null, null, null);

        assertNotNull(result.getSampleQueries());
        assertFalse(result.getSampleQueries().isEmpty());
    }

    // ==================== EDGE CASES ====================

    @Test
    public void testNearestN_NoMatchesAfterFiltering_ReturnsEmpty() {
        CompleteStation station = createStation("ES", "Europe/Madrid", "CET", "Station", 40.0, -8.0, false, false, false);
        insertStation(station);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, null, "PT", null, null);

        assertTrue(result.getStations().isEmpty());
        assertTrue(result.getDistances().isEmpty());
    }

    @Test
    public void testNearestN_StationWithNullCoordinates_Skipped() {
        CompleteStation valid = createStation("Valid", 40.0, -8.0);
        CompleteStation invalid = new CompleteStation("PT", "Europe/Lisbon", "WET", "Invalid", null, null, false, false, false);

        insertStation(valid);
        // invalid não é inserido porque tem coordenadas nulas
        setupTree();

        NearestNSearchResult result = service.searchNearestN(40.0, -8.0, 5, null, null, null, null, null);

        assertEquals(1, result.getStations().size());
        assertEquals(valid, result.getStations().get(0));
    }
}
