package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GeoRadiusSearchServiceTest {

    private GeoRadiusSearchService service;

    @Before
    public void setUp() {
        service = new GeoRadiusSearchService();

        // Criar mocks de estações
        List<CompleteStation> mockStations = new ArrayList<>();
        mockStations.add(new CompleteStation("Portugal", "GMT+0", "Europe/Lisbon", "Porto",
                41.15, -8.61, true, true, false));
        mockStations.add(new CompleteStation("Portugal", "GMT+0", "Europe/Lisbon", "Matosinhos",
                41.20, -8.60, false, false, false));
        mockStations.add(new CompleteStation("Spain", "GMT+1", "Europe/Madrid", "Madrid",
                40.00, -3.70, true, true, false));

        service.injectMockStations(mockStations);
    }

    @Test
    public void testSearchInsideRadius() {
        double lat = 41.15;
        double lon = -8.61;
        double radiusKm = 10;

        GeoRadiusSearchService.Result result = service.search(lat, lon, radiusKm);

        assertEquals(2, result.getStations().size());
        assertEquals(Integer.valueOf(2), result.getCountryCounts().get("Portugal"));
        assertEquals(1, result.getCityCount());
        assertEquals(1, result.getNonCityCount());
    }

    @Test
    public void testSearchNothingFound() {
        GeoRadiusSearchService.Result result = service.search(0, 0, 1);

        assertTrue(result.getStations().isEmpty());
        assertEquals(0, result.getCityCount());
        assertEquals(0, result.getNonCityCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorWhenNoStationsLoaded() {
        GeoRadiusSearchService emptyService = new GeoRadiusSearchService();
        emptyService.injectMockStations(new ArrayList<>());
        emptyService.search(0, 0, 10);
    }
}
