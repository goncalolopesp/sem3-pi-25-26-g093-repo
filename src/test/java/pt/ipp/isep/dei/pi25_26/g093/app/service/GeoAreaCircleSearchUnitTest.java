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
 * Unit tests for USEI09 - Circular Search by Radius.
 * 
 * These tests cover:
 * - Input validation
 * - Haversine distance calculation
 * - KD-tree bounding box
 * - StationAreaFilter functionality
 * - Sorting correctness
 * - Edge cases (empty results, exact radius)
 */
public class GeoAreaCircleSearchUnitTest {

    private CompleteStationRepository repository;
    private GeoAreaCircleSearch service;
    private Tree<Double> tree;

    @Before
    public void setUp() {
        repository = new CompleteStationRepository();
        service = new GeoAreaCircleSearch(repository);
        tree = new Tree<>(2);
    }

    // ==================== VALIDATION TESTS ====================

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLatitudeBelowMin90() {
        // Arrange
        double centerLat = -91.0; // Invalid: < -90
        double centerLon = 0.0;
        double radiusKm = 10.0;

        // Act & Assert: Should throw IllegalArgumentException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLatitudeAboveMax90() {
        // Arrange
        double centerLat = 91.0; // Invalid: > 90
        double centerLon = 0.0;
        double radiusKm = 10.0;

        // Act & Assert: Should throw IllegalArgumentException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLongitudeBelowMin180() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -181.0; // Invalid: < -180
        double radiusKm = 10.0;

        // Act & Assert: Should throw IllegalArgumentException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_InvalidLongitudeAboveMax180() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = 181.0; // Invalid: > 180
        double radiusKm = 10.0;

        // Act & Assert: Should throw IllegalArgumentException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_RadiusZero() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -8.0;
        double radiusKm = 0.0; // Invalid: <= 0

        // Act & Assert: Should throw IllegalArgumentException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidation_RadiusNegative() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -8.0;
        double radiusKm = -10.0; // Invalid: < 0

        // Act & Assert: Should throw IllegalArgumentException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    @Test(expected = IllegalStateException.class)
    public void testValidation_EmptyTree() {
        // Arrange: Repository with empty tree
        double centerLat = 40.0;
        double centerLon = -8.0;
        double radiusKm = 10.0;

        // Act & Assert: Should throw IllegalStateException
        service.searchCircle(centerLat, centerLon, radiusKm, null, null, "ALL");
    }

    // ==================== HAVERSINE DISTANCE TESTS ====================

    @Test
    public void testHaversineDistance_PortoToLisboa() {
        // Arrange: Known distance Porto ↔ Lisboa ≈ 274 km
        double portoLat = 41.1579;
        double portoLon = -8.6291;
        double lisboaLat = 38.7223;
        double lisboaLon = -9.1393;

        // Build tree with both stations
        CompleteStation porto = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Porto",
            portoLat, portoLon, true, true, false
        );
        CompleteStation lisboa = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            lisboaLat, lisboaLon, true, true, false
        );

        buildTreeWithStations(porto, lisboa);

        // Act: Search from Porto with radius 300km (should include Lisboa)
        GeoAreaCircleSearchResult result = service.searchCircle(
            portoLat, portoLon, 300.0, null, null, "ALL"
        );

        // Assert: Lisboa should be in results
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain Lisboa", stations.contains(lisboa));

        // Verify distance is approximately 274 km
        int lisboaIndex = stations.indexOf(lisboa);
        List<Double> distances = result.getDistances();
        double distance = distances.get(lisboaIndex);
        
        // Allow ±5 km tolerance
        assertEquals("Porto to Lisboa distance should be ~274 km",
                    274.0, distance, 5.0);
    }

    @Test
    public void testHaversineDistance_LisboaToMadrid() {
        // Arrange: Known distance Lisboa ↔ Madrid ≈ 502 km
        double lisboaLat = 38.7223;
        double lisboaLon = -9.1393;
        double madridLat = 40.4168;
        double madridLon = -3.7038;

        CompleteStation lisboa = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            lisboaLat, lisboaLon, true, true, false
        );
        CompleteStation madrid = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Madrid",
            madridLat, madridLon, true, true, false
        );

        buildTreeWithStations(lisboa, madrid);

        // Act: Search from Lisboa with radius 600km
        GeoAreaCircleSearchResult result = service.searchCircle(
            lisboaLat, lisboaLon, 600.0, null, null, "ALL"
        );

        // Assert: Madrid should be in results
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain Madrid", stations.contains(madrid));

        // Verify distance is approximately 502 km
        int madridIndex = stations.indexOf(madrid);
        List<Double> distances = result.getDistances();
        double distance = distances.get(madridIndex);
        
        assertEquals("Lisboa to Madrid distance should be ~502 km",
                    502.0, distance, 10.0);
    }

    @Test
    public void testHaversineDistance_PortoToVigo() {
        // Arrange: Known distance Porto ↔ Vigo ≈ 120 km
        double portoLat = 41.1579;
        double portoLon = -8.6291;
        double vigoLat = 42.2406;
        double vigoLon = -8.7207;

        CompleteStation porto = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Porto",
            portoLat, portoLon, true, true, false
        );
        CompleteStation vigo = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Vigo",
            vigoLat, vigoLon, true, false, false
        );

        buildTreeWithStations(porto, vigo);

        // Act: Search from Porto with radius 150km
        GeoAreaCircleSearchResult result = service.searchCircle(
            portoLat, portoLon, 150.0, null, null, "ALL"
        );

        // Assert: Vigo should be in results
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain Vigo", stations.contains(vigo));

        // Verify distance is approximately 120 km
        int vigoIndex = stations.indexOf(vigo);
        List<Double> distances = result.getDistances();
        double distance = distances.get(vigoIndex);
        
        assertEquals("Porto to Vigo distance should be ~120 km",
                    120.0, distance, 5.0);
    }

    // ==================== KD-TREE BOUNDING TESTS ====================

    @Test
    public void testKdTreeBounding_StationInsideBoundingBox() {
        // Arrange: Station inside bounding box
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Act: Search with center near station, radius 50km
        GeoAreaCircleSearchResult result = service.searchCircle(
            38.7, -9.1, 50.0, null, null, "ALL"
        );

        // Assert: Station should be found
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain station inside bounding box", stations.contains(station));
    }

    @Test
    public void testKdTreeBounding_StationOutsideBoundingBox() {
        // Arrange: Station far from center
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Act: Search with center far from station, small radius
        GeoAreaCircleSearchResult result = service.searchCircle(
            50.0, 10.0, 10.0, null, null, "ALL"
        );

        // Assert: Station should not be found
        List<CompleteStation> stations = result.getStations();
        assertFalse("Should not contain station outside bounding box", stations.contains(station));
    }

    @Test
    public void testKdTreeBounding_StationOnBoundingBoxBoundary() {
        // Arrange: Station exactly on bounding box boundary
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Act: Search with center such that station is on bounding box edge
        // Use small radius to create tight bounding box
        double centerLat = 38.7223;
        double centerLon = -9.1393;
        double radiusKm = 1.0; // Small radius

        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, radiusKm, null, null, "ALL"
        );

        // Assert: Station should be found (on boundary, distance ≈ 0)
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain station on boundary", stations.contains(station));
    }

    // ==================== FILTER TESTS ====================

    @Test
    public void testFilter_IsCityTrue_ReturnsOnlyCityStations() {
        // Arrange
        CompleteStation cityStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation nonCityStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Rural Station",
            38.8, -9.2, false, false, false
        );

        buildTreeWithStations(cityStation, nonCityStation);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            38.7, -9.1, 50.0, true, null, "ALL"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only city station", 1, stations.size());
        assertEquals("Should be city station", cityStation, stations.get(0));
        assertFalse("Should not contain non-city station", stations.contains(nonCityStation));
    }

    @Test
    public void testFilter_IsCityFalse_ReturnsOnlyNonCityStations() {
        // Arrange
        CompleteStation cityStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation nonCityStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Rural Station",
            38.8, -9.2, false, false, false
        );

        buildTreeWithStations(cityStation, nonCityStation);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            38.7, -9.1, 50.0, false, null, "ALL"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only non-city station", 1, stations.size());
        assertEquals("Should be non-city station", nonCityStation, stations.get(0));
        assertFalse("Should not contain city station", stations.contains(cityStation));
    }

    @Test
    public void testFilter_IsMainStationTrue_ReturnsOnlyMainStations() {
        // Arrange
        CompleteStation mainStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Porto",
            41.1579, -8.6291, true, true, false
        );
        CompleteStation nonMainStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Braga",
            41.5454, -8.4265, true, false, false
        );

        buildTreeWithStations(mainStation, nonMainStation);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            41.0, -8.5, 100.0, null, true, "ALL"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only main station", 1, stations.size());
        assertEquals("Should be main station", mainStation, stations.get(0));
        assertFalse("Should not contain non-main station", stations.contains(nonMainStation));
    }

    @Test
    public void testFilter_CountryPT_ReturnsOnlyPTStations() {
        // Arrange
        CompleteStation ptStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation esStation = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Madrid",
            40.4168, -3.7038, true, true, false
        );

        buildTreeWithStations(ptStation, esStation);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            39.0, -6.0, 500.0, null, null, "PT"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only PT station", 1, stations.size());
        assertEquals("Should be PT station", ptStation, stations.get(0));
        assertFalse("Should not contain ES station", stations.contains(esStation));
    }

    @Test
    public void testFilter_CountryES_ReturnsOnlyESStations() {
        // Arrange
        CompleteStation ptStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation esStation = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Madrid",
            40.4168, -3.7038, true, true, false
        );

        buildTreeWithStations(ptStation, esStation);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            39.0, -6.0, 500.0, null, null, "ES"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only ES station", 1, stations.size());
        assertEquals("Should be ES station", esStation, stations.get(0));
        assertFalse("Should not contain PT station", stations.contains(ptStation));
    }

    @Test
    public void testFilter_CountryALL_ReturnsAllStations() {
        // Arrange
        CompleteStation ptStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation esStation = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Madrid",
            40.4168, -3.7038, true, true, false
        );

        buildTreeWithStations(ptStation, esStation);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            39.0, -6.0, 500.0, null, null, "ALL"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return both stations", 2, stations.size());
        assertTrue("Should contain PT station", stations.contains(ptStation));
        assertTrue("Should contain ES station", stations.contains(esStation));
    }

    @Test
    public void testFilter_CombinedFilters_CityAndPT() {
        // Arrange
        CompleteStation cityPT = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation cityES = new CompleteStation(
            "ES", "Europe/Madrid", "CET", "Madrid",
            40.4168, -3.7038, true, true, false
        );
        CompleteStation nonCityPT = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Rural",
            38.8, -9.2, false, false, false
        );

        buildTreeWithStations(cityPT, cityES, nonCityPT);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            39.0, -6.0, 500.0, true, null, "PT"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only city AND PT station", 1, stations.size());
        assertEquals("Should be city and PT station", cityPT, stations.get(0));
    }

    // ==================== SORTING TESTS ====================

    @Test
    public void testSorting_ByDistanceAscending() {
        // Arrange: Create 3 stations at different distances
        // Center at (40.0, -8.0)
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station A",
            40.0, -8.0, true, false, false
        ); // ~0 km (center)
        
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station B",
            40.09, -8.0, true, false, false
        ); // ~10 km
        
        CompleteStation station3 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station C",
            40.27, -8.0, true, false, false
        ); // ~30 km

        buildTreeWithStations(station1, station2, station3);

        // Act
        GeoAreaCircleSearchResult result = service.searchCircle(
            40.0, -8.0, 50.0, null, null, "ALL"
        );

        // Assert: Should be sorted by distance (ascending)
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();
        
        assertEquals("Should return 3 stations", 3, stations.size());
        
        // Verify distances are in ascending order
        for (int i = 0; i < distances.size() - 1; i++) {
            assertTrue("Distances should be sorted ascending",
                      distances.get(i) <= distances.get(i + 1));
        }
        
        // First should be closest (station1 at center)
        assertEquals("First should be Station A (closest)", station1, stations.get(0));
        assertTrue("First distance should be approximately 0",
                  distances.get(0) < 1.0);
    }

    @Test
    public void testSorting_SameDistance_ThenByLatitude() {
        // Arrange: Two stations at same distance but different latitudes
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station A",
            40.0, -8.0, true, false, false
        );
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station B",
            41.0, -8.0, true, false, false
        );

        buildTreeWithStations(station1, station2);

        // Act: Center at (40.5, -8.0) - equal distance to both
        GeoAreaCircleSearchResult result = service.searchCircle(
            40.5, -8.0, 100.0, null, null, "ALL"
        );

        // Assert: Should be sorted by latitude when distances are equal
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();
        
        assertEquals("Should return 2 stations", 2, stations.size());
        
        // Station1 (40.0) should come before Station2 (41.0) - lower latitude first
        assertEquals("First should be Station A (lower latitude)", station1, stations.get(0));
        assertEquals("Second should be Station B (higher latitude)", station2, stations.get(1));
    }

    @Test
    public void testSorting_SameDistanceAndLatitude_ThenByLongitude() {
        // Arrange: Two stations at same distance and latitude, different longitudes
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station A",
            40.0, -9.0, true, false, false
        );
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station B",
            40.0, -8.0, true, false, false
        );

        buildTreeWithStations(station1, station2);

        // Act: Center at (40.0, -8.5) - equal distance to both
        GeoAreaCircleSearchResult result = service.searchCircle(
            40.0, -8.5, 100.0, null, null, "ALL"
        );

        // Assert: Should be sorted by longitude when distance and latitude are equal
        List<CompleteStation> stations = result.getStations();
        
        assertEquals("Should return 2 stations", 2, stations.size());
        
        // Station1 (-9.0) should come before Station2 (-8.0) - lower longitude first
        assertEquals("First should be Station A (lower longitude)", station1, stations.get(0));
        assertEquals("Second should be Station B (higher longitude)", station2, stations.get(1));
    }

    @Test
    public void testSorting_SameDistanceLatitudeLongitude_ThenByName() {
        // Arrange: Two stations at same coordinates, different names
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Alpha Station",
            40.0, -8.0, true, false, false
        );
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Beta Station",
            40.0, -8.0, true, false, false
        );

        buildTreeWithStations(station1, station2);

        // Act: Center at same coordinates
        GeoAreaCircleSearchResult result = service.searchCircle(
            40.0, -8.0, 10.0, null, null, "ALL"
        );

        // Assert: Should be sorted by name when all coordinates are equal
        List<CompleteStation> stations = result.getStations();
        
        assertEquals("Should return 2 stations", 2, stations.size());
        
        // Alpha should come before Beta (alphabetical)
        assertEquals("First should be Alpha Station", station1, stations.get(0));
        assertEquals("Second should be Beta Station", station2, stations.get(1));
    }

    // ==================== EMPTY RESULTS TESTS ====================

    @Test
    public void testEmptyResults_NoStationsInRadius() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Act: Center far from station, small radius
        GeoAreaCircleSearchResult result = service.searchCircle(
            0.0, 0.0, 5.0, null, null, "ALL"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();
        assertTrue("Should return empty list", stations.isEmpty());
        assertTrue("Distances should be empty", distances.isEmpty());
    }

    @Test
    public void testEmptyResults_FilterExcludesAllStations() {
        // Arrange
        CompleteStation cityStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(cityStation);

        // Act: Filter for non-city stations (but station is a city)
        GeoAreaCircleSearchResult result = service.searchCircle(
            38.7, -9.1, 50.0, false, null, "ALL"
        );

        // Assert
        List<CompleteStation> stations = result.getStations();
        List<Double> distances = result.getDistances();
        assertTrue("Should return empty list when filter excludes all", stations.isEmpty());
        assertTrue("Distances should be empty", distances.isEmpty());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    public void testEdgeCase_StationExactlyOnRadius() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Act: Calculate exact distance from a nearby point and use that as radius
        // This ensures station is exactly on the radius boundary
        double centerLat = 38.73;
        double centerLon = -9.14;
        double distance = haversineDistance(centerLat, centerLon, 38.7223, -9.1393);
        
        GeoAreaCircleSearchResult result = service.searchCircle(
            centerLat, centerLon, distance, null, null, "ALL"
        );

        // Assert: Station should be included (distance == radius, should be included)
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should contain station exactly on radius", stations.contains(station));
        
        // Verify distance equals radius (within small tolerance)
        int stationIndex = stations.indexOf(station);
        List<Double> distances = result.getDistances();
        assertEquals("Distance should equal radius",
                    distance, distances.get(stationIndex), 0.01);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Helper method to build a KD-tree with the given stations.
     */
    private void buildTreeWithStations(CompleteStation... stations) {
        Tree<Double> newTree = new Tree<>(2);
        
        for (CompleteStation station : stations) {
            repository.addStation(station);
            
            ArrayList<Double> coords = new ArrayList<>(
                Arrays.asList(station.getLatitude(), station.getLongitude())
            );
            newTree.Insert(coords, station);
        }
        
        repository.setTree(newTree);
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

