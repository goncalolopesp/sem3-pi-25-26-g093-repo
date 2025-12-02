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
 * Business Logic Tests for USEI09 - Nearest-N Proximity Search (JUnit 4).
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
        CompleteStation lisbon = createStation("Lisbon", 38.7223, -9.1393);
        CompleteStation porto = createStation("Porto", 41.1579, -8.6291);
        CompleteStation coimbra = createStation("Coimbra", 40.2033, -8.4103);

        insertStation(lisbon);
        insertStation(porto);
        insertStation(coimbra);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                38.7223, -9.1393, 3, null, null, null, null, null);

        assertEquals(3, result.getStations().size());
        assertEquals("Lisbon", result.getStations().get(0).getStation());
        assertEquals(0.0, result.getDistances().get(0), 1.0);

        int coimbraIndex = result.getStations().indexOf(coimbra);
        int portoIndex = result.getStations().indexOf(porto);
        assertTrue(coimbraIndex < portoIndex);
    }

    @Test
    public void testNearestN_MadridToSpanishStations_ReturnsOnlySpanish() {
        CompleteStation madrid = createStation("ES", "Europe/Madrid", "CET", "Madrid", 40.4168, -3.7038, true, true);
        CompleteStation barcelona = createStation("ES", "Europe/Madrid", "CET", "Barcelona", 41.3851, 2.1734, true, true);
        CompleteStation valencia = createStation("ES", "Europe/Madrid", "CET", "Valencia", 39.4699, -0.3763, true, true);
        CompleteStation lisbon = createStation("PT", "Europe/Lisbon", "WET", "Lisbon", 38.7223, -9.1393, true, true);

        insertStation(madrid);
        insertStation(barcelona);
        insertStation(valencia);
        insertStation(lisbon);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                40.4168, -3.7038, 5, null, null, "ES", null, null);

        assertEquals(3, result.getStations().size());
        assertTrue(result.getStations().stream().allMatch(s -> "ES".equals(s.getCountry())));
        assertFalse(result.getStations().contains(lisbon));
    }

    // ==================== MAX-HEAP BEHAVIOR TESTS ====================

    @Test
    public void testNearestN_NEquals1_ReturnsSingleClosest() {
        List<CompleteStation> stations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CompleteStation s = createStation("Station" + i, 40.0 + i * 0.1, -8.0);
            stations.add(s);
            insertStation(s);
        }
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 1, null, null, null, null, null);

        assertEquals(1, result.getStations().size());

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
        List<CompleteStation> stations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompleteStation s = createStation("Station" + i, 40.0 + i * 0.1, -8.0);
            stations.add(s);
            insertStation(s);
        }
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 100, null, null, null, null, null);

        assertEquals(5, result.getStations().size());
        assertTrue(result.getStations().size() <= 100);
    }

    // ==================== MULTIPLE STATIONS AT SAME COORDINATES ====================

    @Test
    public void testNearestN_MultipleStationsSameCoordinates_AllReturnedAndSortedByName() {
        CompleteStation a = createStation("A-Station", 40.0, -8.0);
        CompleteStation b = createStation("B-Station", 40.0, -8.0);
        CompleteStation c = createStation("C-Station", 40.0, -8.0);
        CompleteStation d = createStation("D-Station", 40.0, -8.0);

        insertStation(a);
        insertStation(b);
        insertStation(c);
        insertStation(d);
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 10, null, null, null, null, null);

        assertEquals(4, result.getStations().size());
        for (double dist : result.getDistances()) {
            assertEquals(0.0, dist, 0.1);
        }
        assertEquals("A-Station", result.getStations().get(0).getStation());
        assertEquals("B-Station", result.getStations().get(1).getStation());
        assertEquals("C-Station", result.getStations().get(2).getStation());
        assertEquals("D-Station", result.getStations().get(3).getStation());
    }

    // ==================== FILTER COMBINATION TESTS ====================

    @Test
    public void testNearestN_MultipleFilters_AllMustMatch() {
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

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 10, true, true, "PT", "Europe/Lisbon", "WET");

        assertEquals(1, result.getStations().size());
        assertEquals(match, result.getStations().get(0));
    }

    @Test
    public void testNearestN_NoFilters_ReturnsAllMatchingDistance() {
        List<CompleteStation> stations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CompleteStation s = createStation("Station" + i, 40.0 + i * 0.1, -8.0);
            stations.add(s);
            insertStation(s);
        }
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 5, null, null, null, null, null);

        assertEquals(5, result.getStations().size());
        List<Double> distances = result.getDistances();
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue(distances.get(i) <= distances.get(i + 1));
        }
    }

    // ==================== KD-TREE STRUCTURE TESTS ====================

    @Test
    public void testNearestN_TreeWithDepth_HandlesCorrectly() {
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

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 5, null, null, null, null, null);

        assertEquals(5, result.getStations().size());
        assertEquals(root, result.getStations().get(0));
        assertEquals(0.0, result.getDistances().get(0), 0.1);
    }

    // ==================== PRUNING EFFECTIVENESS TESTS ====================

    @Test
    public void testNearestN_FarStations_PrunedCorrectly() {
        List<CompleteStation> closeStations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompleteStation s = createStation("Close" + i, 40.0 + i * 0.01, -8.0);
            closeStations.add(s);
            insertStation(s);
        }

        List<CompleteStation> farStations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompleteStation s = createStation("Far" + i, 50.0 + i * 0.1, -8.0);
            farStations.add(s);
            insertStation(s);
        }
        setupTree();

        NearestNSearchResult result = service.searchNearestN(
                40.0, -8.0, 5, null, null, null, null, null);

        assertEquals(5, result.getStations().size());
        for (CompleteStation station : result.getStations()) {
            assertTrue(closeStations.contains(station));
        }
        for (double dist : result.getDistances()) {
            assertTrue(dist < 100);
        }
    }

    // ==================== HELPER METHOD FOR DISTANCE ====================

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
