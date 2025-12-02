package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.service.GeoRadiusSearchService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GeoRadiusSearchControllerTest {

    private GeoRadiusSearchController controller;

    @Before
    public void setUp() {
        List<CompleteStation> mockStations = new ArrayList<>();
        mockStations.add(new CompleteStation("Portugal", "GMT+0", "Europe/Lisbon", "Porto",
                41.15, -8.61, true, true, false));
        mockStations.add(new CompleteStation("Portugal", "GMT+0", "Europe/Lisbon", "Matosinhos",
                41.20, -8.60, false, false, false));
        mockStations.add(new CompleteStation("Spain", "GMT+1", "Europe/Madrid", "Madrid",
                40.00, -3.70, true, true, false));

        GeoRadiusSearchService service = new GeoRadiusSearchService();
        service.injectMockStations(mockStations);

        controller = new GeoRadiusSearchController(service);
    }

    @Test
    public void testExecuteInsideRadius() {
        GeoRadiusSearchService.Result result = controller.execute(41.15, -8.61, 10);

        assertEquals(2, result.getStations().size());
        assertEquals(Integer.valueOf(2), result.getCountryCounts().get("Portugal"));
        assertEquals(1, result.getCityCount());
        assertEquals(1, result.getNonCityCount());
    }

    @Test
    public void testExecuteNothingFound() {
        GeoRadiusSearchService.Result result = controller.execute(0, 0, 1);

        assertTrue(result.getStations().isEmpty());
        assertEquals(0, result.getCityCount());
        assertEquals(0, result.getNonCityCount());
    }
}
