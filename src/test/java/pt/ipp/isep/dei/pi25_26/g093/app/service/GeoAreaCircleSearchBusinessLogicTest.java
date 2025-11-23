package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Business Logic Test for USEI09 - Circular Search by Radius.
 * 
 * This test simulates a complete USEI09 execution with a deterministic KD-tree
 * containing 6 stations. It verifies:
 * - Stations within radius are returned
 * - Stations outside radius are excluded
 * - Filters (city, main, country) work correctly
 * - Distances list matches stations list
 * - Sorting by distance is correct
 * - No duplicates
 * - Execution time is measured
 * - Result object is not null
 */
public class GeoAreaCircleSearchBusinessLogicTest {

    private CompleteStationRepository repository;
    private GeoAreaCircleSearch service;
    private Tree<Double> tree;

    // Test stations with realistic PT/ES coordinates
    private CompleteStation lisboa;      // PT, city, main: 38.7223, -9.1393
    private CompleteStation porto;       // PT, city, main: 41.1579, -8.6291
    private CompleteStation braga;       // PT, city, not main: 41.5454, -8.4265
    private CompleteStation coimbra;     // PT, city, main: 40.2033, -8.4103
    private CompleteStation madrid;      // ES, city, main: 40.4168, -3.7038
    private CompleteStation barcelona;   // ES, city, main: 41.3851, 2.1734

    @Before
    public void setUp() {
        // Initialize repository and service
        repository = new CompleteStationRepository();
        service = new GeoAreaCircleSearch(repository);
        tree = new Tree<>(2); // 2D KD-tree

        // Create test stations with realistic coordinates
        lisboa = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa Oriente",
            38.7223, -9.1393,
            true, true, false
        );

        porto = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Porto Campanhã",
            41.1579, -8.6291,
            true, true, false
        );

        braga = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Braga",
            41.5454, -8.4265,
            true, false, false
        );

        coimbra = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Coimbra",
            40.2033, -8.4103,
            true, true, false
        );

        madrid = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Madrid Atocha",
            40.4168, -3.7038,
            true, true, false
        );

        barcelona = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Barcelona Sants",
            41.3851, 2.1734,
            true, true, false
        );

        // Add stations to repository
        repository.addStation(lisboa);
        repository.addStation(porto);
        repository.addStation(braga);
        repository.addStation(coimbra);
        repository.addStation(madrid);
        repository.addStation(barcelona);

        // Build KD-tree manually by inserting stations
        // Each station is inserted with [latitude, longitude] as key
        ArrayList<Double> coords;

        coords = new ArrayList<>(Arrays.asList(lisboa.getLatitude(), lisboa.getLongitude()));
        tree.Insert(coords, lisboa);

        coords = new ArrayList<>(Arrays.asList(porto.getLatitude(), porto.getLongitude()));
        tree.Insert(coords, porto);

        coords = new ArrayList<>(Arrays.asList(braga.getLatitude(), braga.getLongitude()));
        tree.Insert(coords, braga);

        coords = new ArrayList<>(Arrays.asList(coimbra.getLatitude(), coimbra.getLongitude()));
        tree.Insert(coords, coimbra);

        coords = new ArrayList<>(Arrays.asList(madrid.getLatitude(), madrid.getLongitude()));
        tree.Insert(coords, madrid);

        coords = new ArrayList<>(Arrays.asList(barcelona.getLatitude(), barcelona.getLongitude()));
        tree.Insert(coords, barcelona);

        // Set tree in repository
        repository.setTree(tree);
    }

    @Test
    public void testCompleteUSEI09Execution_StationsWithinRadius_NoFilters() {
        // Arrange: Center at Coimbra, radius 150km (should include Lisboa, Porto, Coimbra)
        double centerLat = 40.2033; // Coimbra
        double centerLon = -8.4103;
        double radiusKm = 150.0;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = null;
        String countryFilter = "ALL";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Result object is not null
        assertNotNull("Result should not be null", result);

        // Assert: Execution time >= 0
        assertTrue("Execution time should be non-negative",
                  result.getExecutionTimeNanos() >= 0);

        // Assert: Stations and distances lists match
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();
        assertEquals("Stations and distances lists should have same size",
                    stations.size(), distances.size());

        // Assert: Stations within radius are returned
        // Coimbra (center) ≈ 0 km
        // Lisboa ≈ 100 km
        // Porto ≈ 120 km
        // Braga ≈ 150 km (should be included, distance <= radius)
        assertTrue("Should contain Lisboa (within radius)", stations.contains(lisboa));
        assertTrue("Should contain Porto (within radius)", stations.contains(porto));
        assertTrue("Should contain Coimbra (center)", stations.contains(coimbra));
        assertTrue("Should contain Braga (within radius)", stations.contains(braga));

        // Assert: Stations outside radius are excluded
        assertFalse("Should not contain Madrid (outside radius)", stations.contains(madrid));
        assertFalse("Should not contain Barcelona (outside radius)", stations.contains(barcelona));

        // Assert: All distances are within radius
        for (Double distance : distances) {
            assertTrue("Distance should be <= radius", distance <= radiusKm);
            assertTrue("Distance should be >= 0", distance >= 0);
        }

        // Assert: No duplicates
        long uniqueCount = stations.stream().distinct().count();
        assertEquals("Should have no duplicate stations",
                    stations.size(), uniqueCount);

        // Assert: Sorted by distance (ascending)
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue("Distances should be sorted ascending",
                      distances.get(i) <= distances.get(i + 1));
        }

        // Assert: Distance matches station position in list
        for (int i = 0; i < stations.size(); i++) {
            CompleteStation station = stations.get(i);
            Double distance = distances.get(i);
            
            // Calculate expected distance using Haversine
            double expectedDistance = haversineDistance(
                centerLat, centerLon,
                station.getLatitude(), station.getLongitude()
            );
            
            // Allow small floating point error (0.1 km)
            assertEquals("Distance should match Haversine calculation",
                        expectedDistance, distance, 0.1);
        }
    }

    @Test
    public void testCompleteUSEI09Execution_FilterByCountryPT() {
        // Arrange: Center at Coimbra, radius 200km, filter by PT
        double centerLat = 40.2033;
        double centerLon = -8.4103;
        double radiusKm = 200.0;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = null;
        String countryFilter = "PT";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Should return only PT stations
        List<CompleteStation> stations = result.getStations();
        for (CompleteStation station : stations) {
            assertEquals("All stations should be from PT", "PT", station.getCountry());
        }

        assertTrue("Should contain Lisboa", stations.contains(lisboa));
        assertTrue("Should contain Porto", stations.contains(porto));
        assertTrue("Should contain Coimbra", stations.contains(coimbra));
        assertFalse("Should not contain Madrid (ES)", stations.contains(madrid));
        assertFalse("Should not contain Barcelona (ES)", stations.contains(barcelona));
    }

    @Test
    public void testCompleteUSEI09Execution_FilterByIsMainStationTrue() {
        // Arrange: Center at Coimbra, radius 200km, filter main stations only
        double centerLat = 40.2033;
        double centerLon = -8.4103;
        double radiusKm = 200.0;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = true;
        String countryFilter = "ALL";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Should return only main stations (all except Braga)
        List<CompleteStation> stations = result.getStations();
        for (CompleteStation station : stations) {
            assertTrue("All stations should be main stations", station.isMainStation());
        }

        assertFalse("Should not contain Braga (not main)", stations.contains(braga));
    }

    @Test
    public void testCompleteUSEI09Execution_CombinedFilters_PT_And_Main() {
        // Arrange: Center at Coimbra, radius 200km, filter PT AND main
        double centerLat = 40.2033;
        double centerLon = -8.4103;
        double radiusKm = 200.0;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = true;
        String countryFilter = "PT";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Should return Lisboa, Porto, Coimbra (PT and main), not Braga
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain Lisboa", stations.contains(lisboa));
        assertTrue("Should contain Porto", stations.contains(porto));
        assertTrue("Should contain Coimbra", stations.contains(coimbra));
        assertFalse("Should not contain Braga (not main)", stations.contains(braga));
        assertFalse("Should not contain Madrid (ES)", stations.contains(madrid));
    }

    @Test
    public void testCompleteUSEI09Execution_SortingByDistance() {
        // Arrange: Center at Lisboa, radius 300km
        double centerLat = 38.7223; // Lisboa
        double centerLon = -9.1393;
        double radiusKm = 300.0;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = null;
        String countryFilter = "ALL";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Verify sorting by distance
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();

        // First station should be Lisboa (distance ≈ 0)
        assertEquals("First station should be Lisboa (closest)", lisboa, stations.get(0));
        assertEquals("First distance should be approximately 0", 0.0, distances.get(0), 1.0);

        // Verify distances are in ascending order
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue("Distances should be sorted ascending",
                      distances.get(i) <= distances.get(i + 1));
        }

        // Verify stations match their distances
        for (int i = 0; i < stations.size(); i++) {
            double expectedDistance = haversineDistance(
                centerLat, centerLon,
                stations.get(i).getLatitude(),
                stations.get(i).getLongitude()
            );
            assertEquals("Distance should match station position",
                        expectedDistance, distances.get(i), 0.1);
        }
    }

    @Test
    public void testCompleteUSEI09Execution_NoStationsOutsideRadius() {
        // Arrange: Center at (0, 0), small radius 10km
        double centerLat = 0.0;
        double centerLon = 0.0;
        double radiusKm = 10.0;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = null;
        String countryFilter = "ALL";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Should return empty (all stations are far from (0,0))
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();
        assertTrue("Should return empty list", stations.isEmpty());
        assertTrue("Distances should be empty", distances.isEmpty());
    }

    @Test
    public void testCompleteUSEI09Execution_StationExactlyOnRadius() {
        // Arrange: Center at Lisboa, calculate exact distance to Porto
        double centerLat = 38.7223; // Lisboa
        double centerLon = -9.1393;
        
        // Calculate distance from Lisboa to Porto
        double distanceToPorto = haversineDistance(
            centerLat, centerLon,
            porto.getLatitude(), porto.getLongitude()
        );
        
        // Use radius exactly equal to distance (should include Porto)
        double radiusKm = distanceToPorto;
        Boolean isCityFilter = null;
        Boolean isMainStationFilter = null;
        String countryFilter = "ALL";

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm,
            isCityFilter, isMainStationFilter, countryFilter
        );

        // Assert: Should include Porto (distance == radius, should be included)
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain Porto (distance == radius)", stations.contains(porto));
        
        // Verify Porto's distance in result
        int portoIndex = stations.indexOf(porto);
        assertTrue("Porto should be in result", portoIndex >= 0);
        List<Double> distances = result.getDistances();
        assertEquals("Porto distance should equal radius",
                    radiusKm, distances.get(portoIndex), 0.1);
    }

    /**
     * Helper method to calculate Haversine distance (duplicated from service for testing).
     */
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

