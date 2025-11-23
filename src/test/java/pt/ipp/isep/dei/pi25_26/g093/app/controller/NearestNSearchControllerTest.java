package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.CompleteStationRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchResult;
import pt.ipp.isep.dei.pi25_26.g093.app.service.NearestNSearchService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NearestNSearchController.
 * 
 * Tests:
 * - Controller correctly delegates to service
 * - All parameters are forwarded correctly
 * - Invalid inputs are handled properly
 * - Service exceptions are propagated
 */
@ExtendWith(MockitoExtension.class)
public class NearestNSearchControllerTest {

    @Mock
    private NearestNSearchService mockService;

    private NearestNSearchController controller;

    @BeforeEach
    public void setUp() {
        controller = new NearestNSearchController(mockService);
    }

    // ==================== DELEGATION TESTS ====================

    @Test
    public void testSearchNearestN_DelegatesToService() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;
        Boolean isCity = true;
        Boolean isMainStation = false;
        String country = "PT";
        String timeZone = "Europe/Lisbon";
        String timeZoneGroup = "WET";

        NearestNSearchResult expectedResult = createMockResult();
        when(mockService.searchNearestN(centerLat, centerLon, n, isCity, isMainStation, 
                country, timeZone, timeZoneGroup))
                .thenReturn(expectedResult);

        // Act
        NearestNSearchResult result = controller.searchNearestN(
                centerLat, centerLon, n, isCity, isMainStation, country, timeZone, timeZoneGroup);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResult, result);
        verify(mockService, times(1)).searchNearestN(
                centerLat, centerLon, n, isCity, isMainStation, country, timeZone, timeZoneGroup);
    }

    @Test
    public void testSearchNearestN_AllNullFilters_DelegatesCorrectly() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 10;

        NearestNSearchResult expectedResult = createMockResult();
        when(mockService.searchNearestN(centerLat, centerLon, n, null, null, null, null, null))
                .thenReturn(expectedResult);

        // Act
        NearestNSearchResult result = controller.searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);

        // Assert
        assertNotNull(result);
        verify(mockService, times(1)).searchNearestN(
                centerLat, centerLon, n, null, null, null, null, null);
    }

    // ==================== EXCEPTION PROPAGATION TESTS ====================

    @Test
    public void testSearchNearestN_InvalidLatitude_PropagatesException() {
        // Arrange
        double centerLat = 91.0; // Invalid
        double centerLon = -8.0;
        int n = 5;

        when(mockService.searchNearestN(centerLat, centerLon, n, null, null, null, null, null))
                .thenThrow(new IllegalArgumentException("Latitude must be within [-90, 90]."));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("Latitude"));
    }

    @Test
    public void testSearchNearestN_InvalidLongitude_PropagatesException() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = 181.0; // Invalid
        int n = 5;

        when(mockService.searchNearestN(centerLat, centerLon, n, null, null, null, null, null))
                .thenThrow(new IllegalArgumentException("Longitude must be within [-180, 180]."));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("Longitude"));
    }

    @Test
    public void testSearchNearestN_InvalidN_PropagatesException() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 0; // Invalid

        when(mockService.searchNearestN(centerLat, centerLon, n, null, null, null, null, null))
                .thenThrow(new IllegalArgumentException("N must be greater than 0."));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("N must be greater than 0"));
    }

    @Test
    public void testSearchNearestN_EmptyTree_PropagatesException() {
        // Arrange
        double centerLat = 40.0;
        double centerLon = -8.0;
        int n = 5;

        when(mockService.searchNearestN(centerLat, centerLon, n, null, null, null, null, null))
                .thenThrow(new IllegalStateException("KD-tree is empty. Build it before querying."));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            controller.searchNearestN(centerLat, centerLon, n, null, null, null, null, null);
        });
        assertTrue(exception.getMessage().contains("KD-tree is empty"));
    }

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    public void testConstructor_DefaultConstructor_CreatesService() {
        // Arrange & Act
        NearestNSearchController defaultController = new NearestNSearchController();

        // Assert
        assertNotNull(defaultController);
        // Should not throw when using default constructor
        // (Note: This will use real repository, so tree must be set up)
    }

    @Test
    public void testConstructor_CustomService_UsesProvidedService() {
        // Arrange
        NearestNSearchService customService = mock(NearestNSearchService.class);

        // Act
        NearestNSearchController customController = new NearestNSearchController(customService);

        // Assert
        assertNotNull(customController);
        // Verify it uses the provided service
        NearestNSearchResult mockResult = createMockResult();
        when(customService.searchNearestN(anyDouble(), anyDouble(), anyInt(), 
                any(), any(), any(), any(), any())).thenReturn(mockResult);

        NearestNSearchResult result = customController.searchNearestN(
                40.0, -8.0, 5, null, null, null, null, null);

        assertNotNull(result);
        verify(customService, times(1)).searchNearestN(anyDouble(), anyDouble(), anyInt(),
                any(), any(), any(), any(), any());
    }

    // ==================== PARAMETER FORWARDING TESTS ====================

    @Test
    public void testSearchNearestN_AllParametersForwarded() {
        // Arrange
        double centerLat = 38.7223;
        double centerLon = -9.1393;
        int n = 10;
        Boolean isCity = true;
        Boolean isMainStation = true;
        String country = "PT";
        String timeZone = "Europe/Lisbon";
        String timeZoneGroup = "WET";

        NearestNSearchResult expectedResult = createMockResult();
        when(mockService.searchNearestN(eq(centerLat), eq(centerLon), eq(n),
                eq(isCity), eq(isMainStation), eq(country), eq(timeZone), eq(timeZoneGroup)))
                .thenReturn(expectedResult);

        // Act
        controller.searchNearestN(centerLat, centerLon, n, isCity, isMainStation, 
                country, timeZone, timeZoneGroup);

        // Assert
        verify(mockService, times(1)).searchNearestN(
                eq(centerLat), eq(centerLon), eq(n),
                eq(isCity), eq(isMainStation), eq(country), eq(timeZone), eq(timeZoneGroup));
    }

    // ==================== HELPER METHODS ====================

    private NearestNSearchResult createMockResult() {
        List<CompleteStation> stations = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        stations.add(new CompleteStation("PT", "Europe/Lisbon", "WET", "Test", 40.0, -8.0, false, false, false));
        distances.add(0.0);
        return new NearestNSearchResult(stations, distances, 1000L, new ArrayList<>());
    }
}

