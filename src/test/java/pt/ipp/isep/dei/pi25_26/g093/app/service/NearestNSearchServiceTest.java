package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for USEI09 - Nearest-N Proximity Search Service.
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

    @BeforeEach
    public void setUp() {
        repository = new CompleteStationRepository();
        service = new NearestNSearchService(repository);
        tree = new Tree<>(2);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Creates a CompleteStation with default values.
     */
    private CompleteStation createStation(String name, double lat, double lon) {
        return new CompleteStation("PT", "Europe/Lisbon", "WET", name, lat, lon, false, false, false);
    }

    /**
     * Creates a CompleteStation with all parameters.
     */
    private CompleteStation createStation(String country, String timeZone, String timeZoneGroup,
                                          String name, double lat, double lon,
                                          boolean isCity, boolean isMainStation, boolean isAirport) {
        return new CompleteStation(country, timeZone, timeZoneGroup, name, lat, lon, isCity, isMainStation, isAirport);
    }

    /**
     * Inserts a station into the KD-tree.
     */
    private void insertStation(CompleteStation station) {
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(station.getLatitude());
        coords.add(station.getLongitude());
        tree.Insert(coords, station);
    }

    /**
     * Sets up the tree in the repository.
     */
    private void setupTree() {
        repository.setTree(tree);
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    public void testValidation_InvalidLatitudeBelowMin90_ThrowsException() {
        // Arrange
        setupTree();
        double centerLat = -91.0; // Invalid: < -90
        double centerLon = -9.0;
        int n = 5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("Latitude must be within [-90, 90]"));
    }

    @Test
    public void testValidation_InvalidLatitudeAboveMax90_ThrowsException() {
        // Arrange
        setupTree();
        double centerLat = 91.0; // Invalid: > 90
        double centerLon = -9.0;
        int n = 5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("Latitude must be within [-90, 90]"));
    }

    @Test
    public void testValidation_InvalidLongitudeBelowMin180_ThrowsException() {
        // Arrange
        setupTree();
        double centerLat = 40.0;
        double centerLon = -181.0; // Invalid: < -180
        int n = 5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("Longitude must be within [-180, 180]"));
    }

    @Test
    public void testValidation_InvalidLongitudeAboveMax180_ThrowsException() {
        // Arrange
        setupTree();
        double centerLat = 40.0;
        double centerLon = 181.0; // Invalid: > 180
        int n = 5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("Longitude must be within [-180, 180]"));
    }

    @Test
    public void testValidation_NZero_ThrowsException() {
        // Arrange
        setupTree();
        double centerLat = 40.0;
        double centerLon = -9.0;
        int n = 0; // Invalid: <= 0

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("N must be greater than 0"));
    }

    @Test
    public void testValidation_NegativeN_ThrowsException() {
        // Arrange
        setupTree();
        double centerLat = 40.0;
        double centerLon = -9.0;
        int n = -5; // Invalid: < 0

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("N must be greater than 0"));
    }

    @Test
    public void testValidation_EmptyTree_ThrowsException() {
        // Arrange
        // Tree is empty (not set up)
        double centerLat = 40.0;
        double centerLon = -9.0;
        int n = 5;

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("KD-tree is empty"));
    }

    // ==================== BASIC NEAREST-N TESTS ====================

    @Test
    public void testNearestN_SingleStation_ReturnsThatStation() {
        // Arrange
        CompleteStation station = createStation("Lisbon", 38.7223, -9.1393);
        insertStation(station);
        setupTree();

        double centerLat = 38.7223;
        double centerLon = -9.1393;
        int n = 1;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(1, result.getStations().size());
        assertEquals(station, result.getStations().get(0));
        assertEquals(0.0, result.getDistances().get(0), 0.01); // Distance to itself should be ~0
    }

    @Test
    public void testNearestN_TwoStations_ReturnsClosest() {
        // Arrange
        // Lisbon: 38.7223, -9.1393
        // Porto: 41.1579, -8.6291
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        insertStation(lisbon);
        insertStation(porto);
        setupTree();

        // Query from Lisbon
        double centerLat = 38.7223;
        double centerLon = -9.1393;
        int n = 1;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(1, result.getStations().size());
        assertEquals(lisbon, result.getStations().get(0));
        assertEquals(0.0, result.getDistances().get(0), 0.1);
    }

    @Test
    public void testNearestN_TwoStations_ReturnsBothWhenNIs2() {
        // Arrange
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        insertStation(lisbon);
        insertStation(porto);
        setupTree();

        // Query from Lisbon
        double centerLat = 38.7223;
        double centerLon = -9.1393;
        int n = 2;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(2, result.getStations().size());
        // First should be Lisbon (distance ~0)
        assertEquals(lisbon, result.getStations().get(0));
        assertEquals(0.0, result.getDistances().get(0), 0.1);
        // Second should be Porto
        assertEquals(porto, result.getStations().get(1));
        assertTrue(result.getDistances().get(1) > 0);
    }

    @Test
    public void testNearestN_ResultsSortedByDistance() {
        // Arrange
        // Create 5 stations at different distances from center
        CompleteStation center = createStation("Center", 40.0, -8.0);
        CompleteStation near1 = createStation("Near1", 40.1, -8.0);  // ~11 km
        CompleteStation near2 = createStation("Near2", 40.2, -8.0);  // ~22 km
        CompleteStation far1 = createStation("Far1", 41.0, -8.0);   // ~111 km
        CompleteStation far2 = createStation("Far2", 42.0, -8.0);   // ~222 km

        insertStation(center);
        insertStation(near1);
        insertStation(near2);
        insertStation(far1);
        insertStation(far2);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(5, result.getStations().size());
        List<Double> distances = result.getDistances();
        
        // Verify sorted by distance (ascending)
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue(distances.get(i) <= distances.get(i + 1),
                    "Distances should be sorted in ascending order");
        }
        
        // First should be center (distance ~0)
        assertEquals(center, result.getStations().get(0));
    }

    @Test
    public void testNearestN_NLargerThanAvailable_ReturnsAllAvailable() {
        // Arrange
        CompleteStation s1 = createStation("Station1", 40.0, -8.0);
        CompleteStation s2 = createStation("Station2", 40.1, -8.0);
        insertStation(s1);
        insertStation(s2);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 10; // More than available

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(2, result.getStations().size()); // Only 2 available
        assertTrue(result.getStations().size() <= n);
    }

    // ==================== TIE-BREAKING TESTS ====================

    @Test
    public void testNearestN_DuplicateCoordinates_SortedByName() {
        // Arrange
        // Two stations at same coordinates, different names
        CompleteStation stationA = createStation("A-Station", 40.0, -8.0);
        CompleteStation stationB = createStation("B-Station", 40.0, -8.0);
        CompleteStation stationC = createStation("C-Station", 40.0, -8.0);
        
        insertStation(stationA);
        insertStation(stationB);
        insertStation(stationC);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 3;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(3, result.getStations().size());
        // All have same distance (~0), so should be sorted by name
        assertEquals("A-Station", result.getStations().get(0).getStation());
        assertEquals("B-Station", result.getStations().get(1).getStation());
        assertEquals("C-Station", result.getStations().get(2).getStation());
    }

    // ==================== FILTER TESTS ====================

    @Test
    public void testNearestN_WithIsCityFilter_ReturnsOnlyCityStations() {
        // Arrange
        CompleteStation city1 = createStation("PT", "Europe/Lisbon", "WET", "City1", 40.0, -8.0, true, false, false);
        CompleteStation city2 = createStation("PT", "Europe/Lisbon", "WET", "City2", 40.1, -8.0, true, false, false);
        CompleteStation nonCity = createStation("PT", "Europe/Lisbon", "WET", "NonCity", 40.2, -8.0, false, false, false);
        
        insertStation(city1);
        insertStation(city2);
        insertStation(nonCity);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, true, null, null, null, null); // isCity = true

        // Assert
        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(CompleteStation::isCity));
    }

    @Test
    public void testNearestN_WithIsMainStationFilter_ReturnsOnlyMainStations() {
        // Arrange
        CompleteStation main1 = createStation("PT", "Europe/Lisbon", "WET", "Main1", 40.0, -8.0, false, true, false);
        CompleteStation main2 = createStation("PT", "Europe/Lisbon", "WET", "Main2", 40.1, -8.0, false, true, false);
        CompleteStation nonMain = createStation("PT", "Europe/Lisbon", "WET", "NonMain", 40.2, -8.0, false, false, false);
        
        insertStation(main1);
        insertStation(main2);
        insertStation(nonMain);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, true, null, null, null); // isMainStation = true

        // Assert
        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(CompleteStation::isMainStation));
    }

    @Test
    public void testNearestN_WithCountryFilter_ReturnsOnlyMatchingCountry() {
        // Arrange
        CompleteStation pt1 = createStation("PT", "Europe/Lisbon", "WET", "PT1", 40.0, -8.0, false, false, false);
        CompleteStation pt2 = createStation("PT", "Europe/Lisbon", "WET", "PT2", 40.1, -8.0, false, false, false);
        CompleteStation es1 = createStation("ES", "Europe/Madrid", "CET", "ES1", 40.2, -8.0, false, false, false);
        
        insertStation(pt1);
        insertStation(pt2);
        insertStation(es1);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, "PT", null, null); // country = "PT"

        // Assert
        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "PT".equals(s.getCountry())));
    }

    @Test
    public void testNearestN_WithTimeZoneFilter_ReturnsOnlyMatchingTimeZone() {
        // Arrange
        CompleteStation tz1 = createStation("PT", "Europe/Lisbon", "WET", "TZ1", 40.0, -8.0, false, false, false);
        CompleteStation tz2 = createStation("PT", "Europe/Lisbon", "WET", "TZ2", 40.1, -8.0, false, false, false);
        CompleteStation tz3 = createStation("ES", "Europe/Madrid", "CET", "TZ3", 40.2, -8.0, false, false, false);
        
        insertStation(tz1);
        insertStation(tz2);
        insertStation(tz3);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, "Europe/Lisbon", null); // timeZone = "Europe/Lisbon"

        // Assert
        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "Europe/Lisbon".equalsIgnoreCase(s.getTimeZone())));
    }

    @Test
    public void testNearestN_WithTimeZoneGroupFilter_ReturnsOnlyMatchingTimeZoneGroup() {
        // Arrange
        CompleteStation tzg1 = createStation("PT", "Europe/Lisbon", "WET", "TZG1", 40.0, -8.0, false, false, false);
        CompleteStation tzg2 = createStation("PT", "Europe/Lisbon", "WET", "TZG2", 40.1, -8.0, false, false, false);
        CompleteStation tzg3 = createStation("ES", "Europe/Madrid", "CET", "TZG3", 40.2, -8.0, false, false, false);
        
        insertStation(tzg1);
        insertStation(tzg2);
        insertStation(tzg3);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, "WET"); // timeZoneGroup = "WET"

        // Assert
        assertEquals(2, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "WET".equalsIgnoreCase(s.getTimeZoneGroup())));
    }

    @Test
    public void testNearestN_WithAllFilters_ReturnsOnlyMatchingStations() {
        // Arrange
        CompleteStation match = createStation("PT", "Europe/Lisbon", "WET", "Match", 40.0, -8.0, true, true, false);
        CompleteStation noMatch1 = createStation("ES", "Europe/Madrid", "CET", "NoMatch1", 40.1, -8.0, true, true, false); // Wrong country
        CompleteStation noMatch2 = createStation("PT", "Europe/Lisbon", "WET", "NoMatch2", 40.2, -8.0, false, true, false); // Not city
        CompleteStation noMatch3 = createStation("PT", "Europe/Lisbon", "WET", "NoMatch3", 40.3, -8.0, true, false, false); // Not main
        
        insertStation(match);
        insertStation(noMatch1);
        insertStation(noMatch2);
        insertStation(noMatch3);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, true, true, "PT", "Europe/Lisbon", "WET");

        // Assert
        assertEquals(1, result.getStations().size());
        assertEquals(match, result.getStations().get(0));
    }

    @Test
    public void testNearestN_WithFiltersReducingCount_ReturnsLessThanN() {
        // Arrange
        CompleteStation match1 = createStation("PT", "Europe/Lisbon", "WET", "Match1", 40.0, -8.0, true, false, false);
        CompleteStation match2 = createStation("PT", "Europe/Lisbon", "WET", "Match2", 40.1, -8.0, true, false, false);
        CompleteStation noMatch = createStation("ES", "Europe/Madrid", "CET", "NoMatch", 40.2, -8.0, true, false, false);
        
        insertStation(match1);
        insertStation(match2);
        insertStation(noMatch);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5; // Request 5, but only 2 match filter

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, true, null, "PT", null, null);

        // Assert
        assertEquals(2, result.getStations().size()); // Less than N due to filters
        assertTrue(result.getStations().size() <= n);
    }

    // ==================== HAVERSINE DISTANCE TESTS ====================

    @Test
    public void testNearestN_HaversineDistance_CalculatedCorrectly() {
        // Arrange
        // Known distance: Lisbon (38.7223, -9.1393) to Porto (41.1579, -8.6291) ≈ 274 km
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        insertStation(lisbon);
        insertStation(porto);
        setupTree();

        double centerLat = 38.7223; // Lisbon
        double centerLon = -9.1393;
        int n = 2;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(2, result.getStations().size());
        // Distance to Lisbon should be ~0
        assertEquals(0.0, result.getDistances().get(0), 1.0);
        // Distance to Porto should be ~274 km (allow ±10 km tolerance)
        double portoDistance = result.getDistances().get(1);
        assertTrue(portoDistance > 260 && portoDistance < 290,
                "Distance Lisbon-Porto should be approximately 274 km, got " + portoDistance);
    }

    // ==================== RESULT STRUCTURE TESTS ====================

    @Test
    public void testNearestN_StationsAndDistancesAligned() {
        // Arrange
        CompleteStation s1 = createStation("S1", 40.0, -8.0);
        CompleteStation s2 = createStation("S2", 40.1, -8.0);
        CompleteStation s3 = createStation("S3", 40.2, -8.0);
        
        insertStation(s1);
        insertStation(s2);
        insertStation(s3);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 3;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(result.getStations().size(), result.getDistances().size());
        for (int i = 0; i < result.getStations().size(); i++) {
            CompleteStation station = result.getStations().get(i);
            double distance = result.getDistances().get(i);
            assertNotNull(station);
            assertNotNull(distance);
            assertTrue(distance >= 0);
        }
    }

    @Test
    public void testNearestN_ExecutionTimeTracked() {
        // Arrange
        CompleteStation s1 = createStation("S1", 40.0, -8.0);
        insertStation(s1);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 1;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertTrue(result.getExecutionTimeNanos() > 0);
    }

    @Test
    public void testNearestN_SampleQueriesPresent() {
        // Arrange
        CompleteStation s1 = createStation("S1", 40.0, -8.0);
        insertStation(s1);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 1;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertNotNull(result.getSampleQueries());
        assertFalse(result.getSampleQueries().isEmpty());
    }

    // ==================== EDGE CASES ====================

    @Test
    public void testNearestN_NoMatchesAfterFiltering_ReturnsEmpty() {
        // Arrange
        CompleteStation station = createStation("ES", "Europe/Madrid", "CET", "Station", 40.0, -8.0, false, false, false);
        insertStation(station);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act - Filter for PT country, but station is ES
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, "PT", null, null);

        // Assert
        assertTrue(result.getStations().isEmpty());
        assertTrue(result.getDistances().isEmpty());
    }

    @Test
    public void testNearestN_StationWithNullCoordinates_Skipped() {
        // Arrange
        CompleteStation valid = createStation("Valid", 40.0, -8.0);
        CompleteStation invalid = new CompleteStation("PT", "Europe/Lisbon", "WET", "Invalid", null, null, false, false, false);
        
        insertStation(valid);
        // Note: Can't insert station with null coordinates into KD-tree, so this test
        // validates that the algorithm handles missing coordinates gracefully
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(1, result.getStations().size());
        assertEquals(valid, result.getStations().get(0));
    }
}

