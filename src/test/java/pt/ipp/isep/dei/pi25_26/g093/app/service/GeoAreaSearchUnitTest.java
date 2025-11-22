package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.StationAreaFilter;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for USEI08 - Search by Geographical Area.
 * 
 * These tests cover:
 * - Input validation
 * - Filter functionality
 * - KD-tree RangeSearch
 * - Sorting correctness
 * - Edge cases (empty results, boundary values)
 */
public class GeoAreaSearchUnitTest {

    private CompleteStationRepository repository;
    private GeoAreaSearch service;
    private Tree<Double> tree;

    @Before
    public void setUp() {
        repository = new CompleteStationRepository();
        service = new GeoAreaSearch(repository);
        tree = new Tree<>(2);
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    public void testValidation_LatMinGreaterThanLatMax_ThrowsException() {
        // Arrange
        double latMin = 42.0;
        double latMax = 38.0; // Invalid: min > max
        double lonMin = -9.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalArgumentException when latMin > latMax");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention latMin > latMax",
                      e.getMessage().contains("latMin cannot be greater than latMax"));
        }
    }

    @Test
    public void testValidation_LonMinGreaterThanLonMax_ThrowsException() {
        // Arrange
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -8.0;
        double lonMax = -9.0; // Invalid: min > max
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalArgumentException when lonMin > lonMax");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention lonMin > lonMax",
                      e.getMessage().contains("lonMin cannot be greater than lonMax"));
        }
    }

    @Test
    public void testValidation_InvalidLatitudeBelowMin90_ThrowsException() {
        // Arrange
        double latMin = -91.0; // Invalid: < -90
        double latMax = 42.0;
        double lonMin = -9.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalArgumentException for latitude < -90");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention latitude bounds",
                      e.getMessage().contains("Latitude must be within [-90, 90]"));
        }
    }

    @Test
    public void testValidation_InvalidLatitudeAboveMax90_ThrowsException() {
        // Arrange
        double latMin = 38.0;
        double latMax = 91.0; // Invalid: > 90
        double lonMin = -9.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalArgumentException for latitude > 90");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention latitude bounds",
                      e.getMessage().contains("Latitude must be within [-90, 90]"));
        }
    }

    @Test
    public void testValidation_InvalidLongitudeBelowMin180_ThrowsException() {
        // Arrange
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -181.0; // Invalid: < -180
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalArgumentException for longitude < -180");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention longitude bounds",
                      e.getMessage().contains("Longitude must be within [-180, 180]"));
        }
    }

    @Test
    public void testValidation_InvalidLongitudeAboveMax180_ThrowsException() {
        // Arrange
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -9.0;
        double lonMax = 181.0; // Invalid: > 180
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalArgumentException for longitude > 180");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention longitude bounds",
                      e.getMessage().contains("Longitude must be within [-180, 180]"));
        }
    }

    @Test
    public void testValidation_EmptyTree_ThrowsException() {
        // Arrange: Repository with empty tree
        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -9.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act & Assert
        try {
            service.searchByArea(latMin, latMax, lonMin, lonMax, filter);
            fail("Should throw IllegalStateException when tree is empty");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention empty tree",
                      e.getMessage().contains("KD-tree is empty"));
        }
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
            39.0, -9.0, false, false, false
        );

        buildTreeWithStations(cityStation, nonCityStation);

        double latMin = 38.0;
        double latMax = 40.0;
        double lonMin = -10.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(true, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

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
            39.0, -9.0, false, false, false
        );

        buildTreeWithStations(cityStation, nonCityStation);

        double latMin = 38.0;
        double latMax = 40.0;
        double lonMin = -10.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(false, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

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
            "PT", "Europe/Lisbon", "WET", "Porto Campanhã",
            41.1579, -8.6291, true, true, false
        );
        CompleteStation nonMainStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Braga",
            41.5454, -8.4265, true, false, false
        );

        buildTreeWithStations(mainStation, nonMainStation);

        double latMin = 40.0;
        double latMax = 42.0;
        double lonMin = -9.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, true, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

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

        double latMin = 38.0;
        double latMax = 41.0;
        double lonMin = -10.0;
        double lonMax = -3.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "PT");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

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

        double latMin = 38.0;
        double latMax = 41.0;
        double lonMin = -10.0;
        double lonMax = -3.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ES");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

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

        double latMin = 38.0;
        double latMax = 41.0;
        double lonMin = -10.0;
        double lonMax = -3.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return both stations", 2, stations.size());
        assertTrue("Should contain PT station", stations.contains(ptStation));
        assertTrue("Should contain ES station", stations.contains(esStation));
    }

    @Test
    public void testFilter_CombinedFilters_CityAndMain() {
        // Arrange
        CompleteStation cityMain = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation cityNonMain = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Braga",
            41.5454, -8.4265, true, false, false
        );
        CompleteStation nonCityMain = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Rural Main",
            39.0, -9.0, false, true, false
        );

        buildTreeWithStations(cityMain, cityNonMain, nonCityMain);

        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(true, true, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return only city AND main station", 1, stations.size());
        assertEquals("Should be city and main station", cityMain, stations.get(0));
    }

    // ==================== KD-TREE RANGESEARCH TESTS ====================

    @Test
    public void testRangeSearch_StationInsideBounds_ReturnsStation() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        double latMin = 38.0;
        double latMax = 39.0;
        double lonMin = -10.0;
        double lonMax = -9.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return station inside bounds", 1, stations.size());
        assertEquals("Should be the station", station, stations.get(0));
    }

    @Test
    public void testRangeSearch_StationOutsideBounds_ReturnsEmpty() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Rectangle far from station
        double latMin = 40.0;
        double latMax = 41.0;
        double lonMin = -8.0;
        double lonMax = -7.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should return empty for station outside bounds", stations.isEmpty());
    }

    @Test
    public void testRangeSearch_BoundaryValue_ExactlyEqualMin_ReturnsStation() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Rectangle with min exactly at station's latitude
        double latMin = 38.7223;
        double latMax = 39.0;
        double lonMin = -9.1393;
        double lonMax = -9.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return station at boundary", 1, stations.size());
        assertEquals("Should be the station", station, stations.get(0));
    }

    @Test
    public void testRangeSearch_BoundaryValue_ExactlyEqualMax_ReturnsStation() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Rectangle with max exactly at station's coordinates
        double latMin = 38.0;
        double latMax = 38.7223;
        double lonMin = -10.0;
        double lonMax = -9.1393;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return station at boundary", 1, stations.size());
        assertEquals("Should be the station", station, stations.get(0));
    }

    @Test
    public void testRangeSearch_MultipleStations_SomeInsideSomeOutside() {
        // Arrange
        CompleteStation inside1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );
        CompleteStation inside2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Setúbal",
            38.5244, -8.8882, true, false, false
        );
        CompleteStation outside = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Porto",
            41.1579, -8.6291, true, true, false
        );

        buildTreeWithStations(inside1, inside2, outside);

        // Rectangle covering only Lisboa and Setúbal area
        double latMin = 38.0;
        double latMax = 39.0;
        double lonMin = -10.0;
        double lonMax = -8.5;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return 2 stations inside bounds", 2, stations.size());
        assertTrue("Should contain Lisboa", stations.contains(inside1));
        assertTrue("Should contain Setúbal", stations.contains(inside2));
        assertFalse("Should not contain Porto", stations.contains(outside));
    }

    // ==================== SORTING TESTS ====================

    @Test
    public void testSorting_ByLatitude_ThenLongitude_ThenName() {
        // Arrange: Stations with different latitudes
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Aveiro",
            40.6443, -8.6455, true, false, false
        );
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Braga",
            41.5454, -8.4265, true, false, false
        );
        CompleteStation station3 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station1, station2, station3);

        double latMin = 38.0;
        double latMax = 42.0;
        double lonMin = -10.0;
        double lonMax = -8.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should be sorted by latitude (asc)
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return 3 stations", 3, stations.size());

        // Verify order: Lisboa (38.7223), Aveiro (40.6443), Braga (41.5454)
        assertEquals("First should be Lisboa (lowest latitude)", station3, stations.get(0));
        assertEquals("Second should be Aveiro", station1, stations.get(1));
        assertEquals("Third should be Braga (highest latitude)", station2, stations.get(2));
    }

    @Test
    public void testSorting_SameLatitude_ThenByLongitude() {
        // Arrange: Stations with same latitude, different longitudes
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station A",
            40.0, -9.0, true, false, false
        );
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station B",
            40.0, -8.0, true, false, false
        );
        CompleteStation station3 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Station C",
            40.0, -7.0, true, false, false
        );

        buildTreeWithStations(station1, station2, station3);

        double latMin = 39.0;
        double latMax = 41.0;
        double lonMin = -10.0;
        double lonMax = -6.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should be sorted by longitude (asc) when latitude is equal
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return 3 stations", 3, stations.size());

        // Verify order: Station A (-9.0), Station B (-8.0), Station C (-7.0)
        assertEquals("First should be Station A (lowest longitude)", station1, stations.get(0));
        assertEquals("Second should be Station B", station2, stations.get(1));
        assertEquals("Third should be Station C (highest longitude)", station3, stations.get(2));
    }

    @Test
    public void testSorting_SameLatitudeAndLongitude_ThenByName() {
        // Arrange: Stations with same coordinates (different names)
        CompleteStation station1 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Alpha Station",
            40.0, -8.0, true, false, false
        );
        CompleteStation station2 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Beta Station",
            40.0, -8.0, true, false, false
        );
        CompleteStation station3 = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Gamma Station",
            40.0, -8.0, true, false, false
        );

        buildTreeWithStations(station1, station2, station3);

        double latMin = 39.0;
        double latMax = 41.0;
        double lonMin = -9.0;
        double lonMax = -7.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert: Should be sorted by name (asc) when coordinates are equal
        List<CompleteStation> stations = result.getStations();
        assertEquals("Should return 3 stations", 3, stations.size());

        // Verify order: Alpha, Beta, Gamma (alphabetical)
        assertEquals("First should be Alpha Station", station1, stations.get(0));
        assertEquals("Second should be Beta Station", station2, stations.get(1));
        assertEquals("Third should be Gamma Station", station3, stations.get(2));
    }

    // ==================== EMPTY RESULTS TESTS ====================

    @Test
    public void testEmptyResults_NoStationsInRectangle_ReturnsEmptyList() {
        // Arrange
        CompleteStation station = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(station);

        // Rectangle far from station
        double latMin = 50.0;
        double latMax = 51.0;
        double lonMin = 10.0;
        double lonMax = 11.0;
        StationAreaFilter filter = new StationAreaFilter(null, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertTrue("Should return empty list", stations.isEmpty());
        assertEquals("Should have 0 stations", 0, stations.size());
    }

    @Test
    public void testEmptyResults_FilterExcludesAllStations_ReturnsEmptyList() {
        // Arrange
        CompleteStation cityStation = new CompleteStation(
            "PT", "Europe/Lisbon", "WET", "Lisboa",
            38.7223, -9.1393, true, true, false
        );

        buildTreeWithStations(cityStation);

        double latMin = 38.0;
        double latMax = 39.0;
        double lonMin = -10.0;
        double lonMax = -9.0;
        // Filter for non-city stations (but station is a city)
        StationAreaFilter filter = new StationAreaFilter(false, null, "ALL");

        // Act
        GeoAreaSearchResult result = service.searchByArea(latMin, latMax, lonMin, lonMax, filter);

        // Assert
        List<CompleteStation> stations = result.getStations();
        assertTrue(stations.isEmpty(), "Should return empty list when filter excludes all");
    }

    // ==================== HELPER METHODS ====================

    /**
     * Helper method to build a KD-tree with the given stations.
     */
    private void buildTreeWithStations(CompleteStation... stations) {
        Tree<Double> newTree = new Tree<>(2);
        
        for (CompleteStation station : stations) {
            repository.addStation(station);
            
            ArrayList<Double> coords = new ArrayList<>();
            coords.add(station.getLatitude());
            coords.add(station.getLongitude());
            newTree.Insert(coords, station);
        }
        
        repository.setTree(newTree);
    }
}

