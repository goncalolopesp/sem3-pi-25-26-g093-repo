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
 * Business Logic Tests for USEI09 - Nearest-N Proximity Search.
 * 
 * These tests validate complex scenarios and algorithm correctness:
 * - KD-tree structure and traversal
 * - Max-heap behavior with N=1, N=large
 * - Pruning effectiveness
 * - Multiple stations at same coordinates
 * - Real-world coordinate scenarios (Lisbon, Porto, Madrid, etc.)
 * - Filter combinations
 */
public class NearestNSearchBusinessLogicTest {

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

    private CompleteStation createStation(String name, double lat, double lon) {
        return new CompleteStation("PT", "Europe/Lisbon", "WET", name, lat, lon, false, false, false);
    }

    private CompleteStation createStation(String country, String timeZone, String timeZoneGroup,
                                          String name, double lat, double lon,
                                          boolean isCity, boolean isMainStation) {
        return new CompleteStation(country, timeZone, timeZoneGroup, name, lat, lon, isCity, isMainStation, false);
    }

    private void insertStation(CompleteStation station) {
        if (station.getLatitude() != null && station.getLongitude() != null) {
            ArrayList<Double> coords = new ArrayList<>();
            coords.add(station.getLatitude());
            coords.add(station.getLongitude());
            tree.Insert(coords, station);
        }
    }

    private void setupTree() {
        repository.setTree(tree);
    }

    // ==================== REAL-WORLD COORDINATE TESTS ====================

    @Test
    public void testNearestN_LisbonToPorto_ReturnsCorrectOrder() {
        // Arrange - Real coordinates
        // Lisbon: 38.7223°N, 9.1393°W
        // Porto: 41.1579°N, 8.6291°W
        // Coimbra: 40.2033°N, 8.4103°W
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        CompleteStation coimbra = createStation("Coimbra", 40.2033, -8.4103);
        
        insertStation(lisbon);
        insertStation(porto);
        insertStation(coimbra);
        setupTree();

        // Query from Lisbon
        double centerLat = 38.7223;
        double centerLon = -9.1393;
        int n = 3;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(3, result.getStations().size());
        // First should be Lisbon (distance ~0)
        assertEquals("Lisbon", result.getStations().get(0).getStation());
        assertEquals(0.0, result.getDistances().get(0), 1.0);
        
        // Coimbra should be closer than Porto
        int coimbraIndex = result.getStations().indexOf(coimbra);
        int portoIndex = result.getStations().indexOf(porto);
        assertTrue(coimbraIndex < portoIndex, "Coimbra should be closer to Lisbon than Porto");
    }

    @Test
    public void testNearestN_MadridToSpanishStations_ReturnsOnlySpanish() {
        // Arrange
        // Madrid: 40.4168°N, 3.7038°W
        CompleteStation madrid = createStation("ES", "Europe/Madrid", "CET", "Madrid", 40.4168, -3.7038, true, true);
        CompleteStation barcelona = createStation("ES", "Europe/Madrid", "CET", "Barcelona", 41.3851, 2.1734, true, true);
        CompleteStation valencia = createStation("ES", "Europe/Madrid", "CET", "Valencia", 39.4699, -0.3763, true, true);
        CompleteStation lisbon = createStation("PT", "Europe/Lisbon", "WET", "Lisbon", 38.7223, -9.1393, true, true);
        
        insertStation(madrid);
        insertStation(barcelona);
        insertStation(valencia);
        insertStation(lisbon);
        setupTree();

        // Query from Madrid
        double centerLat = 40.4168;
        double centerLon = -3.7038;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, "ES", null, null);

        // Assert
        assertEquals(3, result.getStations().size()); // Only Spanish stations
        assertTrue(result.getStations().stream().allMatch(s -> "ES".equals(s.getCountry())));
        assertFalse(result.getStations().contains(lisbon));
    }

    // ==================== MAX-HEAP BEHAVIOR TESTS ====================

    @Test
    public void testNearestN_NEquals1_ReturnsSingleClosest() {
        // Arrange - Create many stations
        List<CompleteStation> stations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CompleteStation s = createStation("Station" + i, 40.0 + i * 0.1, -8.0);
            stations.add(s);
            insertStation(s);
        }
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 1;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(1, result.getStations().size());
        // Should be the station closest to (40.0, -8.0)
        CompleteStation closest = result.getStations().get(0);
        double minDistance = Double.MAX_VALUE;
        for (CompleteStation s : stations) {
            double dist = haversineDistance(40.0, -8.0, s.getLatitude(), s.getLongitude());
            if (dist < minDistance) {
                minDistance = dist;
            }
        }
        assertEquals(minDistance, result.getDistances().get(0), 0.1);
    }

    @Test
    public void testNearestN_NEqualsLarge_ReturnsAllAvailable() {
        // Arrange
        List<CompleteStation> stations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompleteStation s = createStation("Station" + i, 40.0 + i * 0.1, -8.0);
            stations.add(s);
            insertStation(s);
        }
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 100; // Much larger than available

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(5, result.getStations().size()); // All available
        assertTrue(result.getStations().size() <= n);
    }

    // ==================== MULTIPLE STATIONS AT SAME COORDINATES ====================

    @Test
    public void testNearestN_MultipleStationsSameCoordinates_AllReturnedAndSortedByName() {
        // Arrange
        // Insert multiple stations at same coordinates
        CompleteStation a = createStation("A-Station", 40.0, -8.0);
        CompleteStation b = createStation("B-Station", 40.0, -8.0);
        CompleteStation c = createStation("C-Station", 40.0, -8.0);
        CompleteStation d = createStation("D-Station", 40.0, -8.0);
        
        insertStation(a);
        insertStation(b);
        insertStation(c);
        insertStation(d);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 10;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(4, result.getStations().size());
        // All should have same distance (~0)
        for (double dist : result.getDistances()) {
            assertEquals(0.0, dist, 0.1);
        }
        // Should be sorted by name
        assertEquals("A-Station", result.getStations().get(0).getStation());
        assertEquals("B-Station", result.getStations().get(1).getStation());
        assertEquals("C-Station", result.getStations().get(2).getStation());
        assertEquals("D-Station", result.getStations().get(3).getStation());
    }

    // ==================== FILTER COMBINATION TESTS ====================

    @Test
    public void testNearestN_MultipleFilters_AllMustMatch() {
        // Arrange
        CompleteStation match = createStation("PT", "Europe/Lisbon", "WET", "Match", 40.0, -8.0, true, true);
        CompleteStation wrongCountry = createStation("ES", "Europe/Lisbon", "WET", "WrongCountry", 40.1, -8.0, true, true);
        CompleteStation wrongCity = createStation("PT", "Europe/Lisbon", "WET", "WrongCity", 40.2, -8.0, false, true);
        CompleteStation wrongMain = createStation("PT", "Europe/Lisbon", "WET", "WrongMain", 40.3, -8.0, true, false);
        CompleteStation wrongTZ = createStation("PT", "Europe/Madrid", "CET", "WrongTZ", 40.4, -8.0, true, true);
        CompleteStation wrongTZG = createStation("PT", "Europe/Lisbon", "CET", "WrongTZG", 40.5, -8.0, true, true);
        
        insertStation(match);
        insertStation(wrongCountry);
        insertStation(wrongCity);
        insertStation(wrongMain);
        insertStation(wrongTZ);
        insertStation(wrongTZG);
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 10;

        // Act - All filters active
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, true, true, "PT", "Europe/Lisbon", "WET");

        // Assert
        assertEquals(1, result.getStations().size());
        assertEquals(match, result.getStations().get(0));
    }

    @Test
    public void testNearestN_NoFilters_ReturnsAllMatchingDistance() {
        // Arrange
        List<CompleteStation> stations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CompleteStation s = createStation("Station" + i, 40.0 + i * 0.1, -8.0);
            stations.add(s);
            insertStation(s);
        }
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act - No filters
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(5, result.getStations().size());
        // Should be sorted by distance
        List<Double> distances = result.getDistances();
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue(distances.get(i) <= distances.get(i + 1));
        }
    }

    // ==================== KD-TREE STRUCTURE TESTS ====================

    @Test
    public void testNearestN_TreeWithDepth_HandlesCorrectly() {
        // Arrange - Create a tree with multiple levels
        // Root at (40.0, -8.0)
        // Left child at (39.0, -8.0) - smaller latitude
        // Right child at (41.0, -8.0) - larger latitude
        // Left-left at (38.0, -8.0)
        // Left-right at (39.0, -7.0) - same lat, different lon
        CompleteStation root = createStation("Root", 40.0, -8.0);
        CompleteStation left = createStation("Left", 39.0, -8.0);
        CompleteStation right = createStation("Right", 41.0, -8.0);
        CompleteStation leftLeft = createStation("LeftLeft", 38.0, -8.0);
        CompleteStation leftRight = createStation("LeftRight", 39.0, -7.0);
        
        insertStation(root);
        insertStation(left);
        insertStation(right);
        insertStation(leftLeft);
        insertStation(leftRight);
        setupTree();

        // Query from root
        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(5, result.getStations().size());
        // Root should be first (distance ~0)
        assertEquals(root, result.getStations().get(0));
        assertEquals(0.0, result.getDistances().get(0), 0.1);
    }

    // ==================== PRUNING EFFECTIVENESS TESTS ====================

    @Test
    public void testNearestN_FarStations_PrunedCorrectly() {
        // Arrange
        // Create stations: 5 close, 5 very far
        List<CompleteStation> closeStations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompleteStation s = createStation("Close" + i, 40.0 + i * 0.01, -8.0);
            closeStations.add(s);
            insertStation(s);
        }
        
        List<CompleteStation> farStations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompleteStation s = createStation("Far" + i, 50.0 + i * 0.1, -8.0); // ~1110 km away
            farStations.add(s);
            insertStation(s);
        }
        setupTree();

        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        // Act
        NearestNSearchResult result = service.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertEquals(5, result.getStations().size());
        // All should be from close stations
        for (CompleteStation station : result.getStations()) {
            assertTrue(closeStations.contains(station), 
                    "Result should only contain close stations, got: " + station.getStation());
        }
        // All distances should be small
        for (double dist : result.getDistances()) {
            assertTrue(dist < 100, "Distance should be small (< 100 km), got: " + dist);
        }
    }

    // ==================== HELPER METHOD FOR DISTANCE ====================

    /**
     * Calculates Haversine distance (same as in service).
     */
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

