package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Business Logic Test for USEI08 - Search by Geographical Area.
 * 
 * This test simulates a complete USEI08 execution with a deterministic KD-tree
 * containing 5 stations. It verifies:
 * - Rectangle boundary filtering
 * - Filter application (isCity, isMainStation, country)
 * - Sorting correctness
 * - No extra stations
 * - No exceptions thrown
 */
public class GeoAreaSearchBusinessLogicTest {

    private CompleteStationRepository repository;
    private GeoAreaSearch service;
    private Tree<Double> tree;

    // Test stations with realistic PT/ES coordinates
    private CompleteStation lisboa;      // PT, city, main: 38.7223, -9.1393
    private CompleteStation porto;       // PT, city, main: 41.1579, -8.6291
    private CompleteStation braga;       // PT, city, not main: 41.5454, -8.4265
    private CompleteStation madrid;      // ES, city, main: 40.4168, -3.7038
    private CompleteStation barcelona;   // ES, city, main: 41.3851, 2.1734

    @BeforeEach
    public void setUp() {
        // Initialize repository and service
        repository = new CompleteStationRepository();
        service = new GeoAreaSearch(repository);
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
        repository.addStation(madrid);
        repository.addStation(barcelona);

        // Build KD-tree manually by inserting stations
        // Each station is inserted with [latitude, longitude] as key
        ArrayList<Double> coords;

        coords = new ArrayList<>();
        coords.add(lisboa.getLatitude());
        coords.add(lisboa.getLongitude());
        tree.Insert(coords, lisboa);

        coords = new ArrayList<>();
        coords.add(porto.getLatitude());
        coords.add(porto.getLongitude());
        tree.Insert(coords, porto);

        coords = new ArrayList<>();
        coords.add(braga.getLatitude());
        coords.add(braga.getLongitude());
        tree.Insert(coords, braga);

        coords = new ArrayList<>();
        coords.add(madrid.getLatitude());
        coords.add(madrid.getLongitude());
        tree.Insert(coords, madrid);

        coords = new ArrayList<>();
        coords.add(barcelona.getLatitude());
        coords.add(barcelona.getLongitude());
        tree.Insert(coords, barcelona);

        // Set tree in repository
        repository.setTree(tree);
    }

    @Test
    public void testCompleteUSEI08Execution_AllStationsInRectangle_NoFilters() {
        // Arrange: Rectangle covering all Portuguese stations
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return Lisboa, Porto, and Braga (all PT stations in rectangle)
        List<CompleteStation> stations = result.getStations();
        assertEquals(3, stations.size(), "Should return 3 Portuguese stations");

        // Verify exact stations
        assertTrue(stations.contains(lisboa), "Should contain Lisboa");
        assertTrue(stations.contains(porto), "Should contain Porto");
        assertTrue(stations.contains(braga), "Should contain Braga");
        assertFalse(stations.contains(madrid), "Should not contain Madrid (outside rectangle)");
        assertFalse(stations.contains(barcelona), "Should not contain Barcelona (outside rectangle)");

        // Verify sorting: latitude (asc), then longitude (asc), then name (asc)
        // Lisboa: 38.7223, -9.1393
        // Porto: 41.1579, -8.6291
        // Braga: 41.5454, -8.4265
        // Expected order: Lisboa (lowest lat), Porto, Braga (highest lat)
        assertEquals(lisboa, stations.get(0), "First should be Lisboa (lowest latitude)");
        assertEquals(porto, stations.get(1), "Second should be Porto");
        assertEquals(braga, stations.get(2), "Third should be Braga (highest latitude)");

        // Verify execution time was measured
        assertTrue(result.getExecutionTimeNanos() >= 0, "Execution time should be non-negative");

        // Verify no exceptions were thrown
        assertNotNull(result, "Result should not be null");
    }

    @Test
    public void testCompleteUSEI08Execution_FilterByCountryPT() {
        // Arrange: Rectangle covering all stations, filter by PT
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0; // Include Spain
        StationAreaFilter filter = new StationAreaFilter(null, null, "PT");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return only PT stations
        List<CompleteStation> stations = result.getStations();
        assertEquals(3, stations.size(), "Should return 3 Portuguese stations");

        for (CompleteStation station : stations) {
            assertEquals("PT", station.getCountry(), "All stations should be from PT");
        }

        assertFalse(stations.contains(madrid), "Should not contain Madrid (ES)");
        assertFalse(stations.contains(barcelona), "Should not contain Barcelona (ES)");
    }

    @Test
    public void testCompleteUSEI08Execution_FilterByCountryES() {
        // Arrange: Rectangle covering all stations, filter by ES
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ES");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return only ES stations
        List<CompleteStation> stations = result.getStations();
        assertEquals(2, stations.size(), "Should return 2 Spanish stations");

        for (CompleteStation station : stations) {
            assertEquals("ES", station.getCountry(), "All stations should be from ES");
        }

        assertTrue(stations.contains(madrid), "Should contain Madrid");
        assertTrue(stations.contains(barcelona), "Should contain Barcelona");
    }

    @Test
    public void testCompleteUSEI08Execution_FilterByIsCityTrue() {
        // Arrange: All stations are cities, so should return all
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0;
        StationAreaFilter filter = new StationAreaFilter(true, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: All stations are cities
        List<CompleteStation> stations = result.getStations();
        assertEquals(5, stations.size(), "Should return all 5 stations (all are cities)");

        for (CompleteStation station : stations) {
            assertTrue(station.isCity(), "All stations should be cities");
        }
    }

    @Test
    public void testCompleteUSEI08Execution_FilterByIsMainStationTrue() {
        // Arrange: Filter for main stations only
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0;
        StationAreaFilter filter = new StationAreaFilter(null, true, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return only main stations (all except Braga)
        List<CompleteStation> stations = result.getStations();
        assertEquals(4, stations.size(), "Should return 4 main stations");

        for (CompleteStation station : stations) {
            assertTrue(station.isMainStation(), "All stations should be main stations");
        }

        assertFalse(stations.contains(braga), "Should not contain Braga (not main)");
    }

    @Test
    public void testCompleteUSEI08Execution_FilterByIsMainStationFalse() {
        // Arrange: Filter for non-main stations
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0;
        StationAreaFilter filter = new StationAreaFilter(null, false, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return only Braga
        List<CompleteStation> stations = result.getStations();
        assertEquals(1, stations.size(), "Should return only Braga");
        assertEquals(braga, stations.get(0), "Should be Braga");
    }

    @Test
    public void testCompleteUSEI08Execution_CombinedFilters_PT_And_Main() {
        // Arrange: Filter for PT AND main stations
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0;
        StationAreaFilter filter = new StationAreaFilter(null, true, "PT");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return Lisboa and Porto (PT and main), not Braga (not main)
        List<CompleteStation> stations = result.getStations();
        assertEquals(2, stations.size(), "Should return 2 stations (Lisboa and Porto)");

        assertTrue(stations.contains(lisboa), "Should contain Lisboa");
        assertTrue(stations.contains(porto), "Should contain Porto");
        assertFalse(stations.contains(braga), "Should not contain Braga (not main)");
        assertFalse(stations.contains(madrid), "Should not contain Madrid (ES)");
        assertFalse(stations.contains(barcelona), "Should not contain Barcelona (ES)");
    }

    @Test
    public void testCompleteUSEI08Execution_RectangleBoundaries_ExactMatch() {
        // Arrange: Rectangle exactly matching Lisboa's coordinates
        double latMin = 38.7223;
        double latMax = 38.7223;
        double lonMin = -9.1393;
        double lonMax = -9.1393;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return only Lisboa
        List<CompleteStation> stations = result.getStations();
        assertEquals(1, stations.size(), "Should return only Lisboa");
        assertEquals(lisboa, stations.get(0), "Should be Lisboa");
    }

    @Test
    public void testCompleteUSEI08Execution_RectangleBoundaries_NoMatches() {
        // Arrange: Rectangle in empty area (North of Portugal)
        double latMin = 42.0;
        double latMax = 43.0;
        double lonMin = -9.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return empty list
        List<CompleteStation> stations = result.getStations();
        assertTrue(stations.isEmpty(), "Should return empty list");
        assertEquals(0, stations.size(), "Should have 0 stations");
    }

    @Test
    public void testCompleteUSEI08Execution_SortingCorrectness_MultipleStations() {
        // Arrange: Rectangle covering all PT stations
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "PT");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Verify sorting order
        List<CompleteStation> stations = result.getStations();
        assertEquals(3, stations.size(), "Should return 3 stations");

        // Check sorting: latitude (asc), then longitude (asc), then name (asc)
        // Lisboa: 38.7223, -9.1393
        // Porto: 41.1579, -8.6291
        // Braga: 41.5454, -8.4265

        CompleteStation first = stations.get(0);
        CompleteStation second = stations.get(1);
        CompleteStation third = stations.get(2);

        // First should have lowest latitude
        assertTrue(first.getLatitude() <= second.getLatitude(), 
                  "First station should have latitude <= second");
        assertTrue(second.getLatitude() <= third.getLatitude(), 
                  "Second station should have latitude <= third");

        // Verify exact order
        assertEquals(lisboa, first, "First should be Lisboa (lowest latitude: 38.7223)");
        assertEquals(porto, second, "Second should be Porto (latitude: 41.1579)");
        assertEquals(braga, third, "Third should be Braga (highest latitude: 41.5454)");
    }

    @Test
    public void testCompleteUSEI08Execution_NoExtraStations() {
        // Arrange: Small rectangle around Lisboa
        double latMin = 38.5;
        double latMax = 39.0;
        double lonMin = -9.5;
        double lonMax = -9.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should return exactly Lisboa, no more, no less
        List<CompleteStation> stations = result.getStations();
        assertEquals(1, stations.size(), "Should return exactly 1 station");
        assertEquals(lisboa, stations.get(0), "Should be exactly Lisboa");
        
        // Verify no other stations leaked in
        assertFalse(stations.contains(porto), "Should not contain Porto");
        assertFalse(stations.contains(braga), "Should not contain Braga");
        assertFalse(stations.contains(madrid), "Should not contain Madrid");
        assertFalse(stations.contains(barcelona), "Should not contain Barcelona");
    }

    @Test
    public void testCompleteUSEI08Execution_NoExceptionsThrown() {
        // Arrange: Various valid inputs
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = 3.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert: Should not throw any exceptions
        assertDoesNotThrow(() -> {
            GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            assertNotNull(result, "Result should not be null");
            assertNotNull(result.getStations(), "Stations list should not be null");
        }, "Should not throw any exceptions");
    }
}

