package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for the TravelTimeService class (USPL03).
 */
public class TravelTimeServiceTest {

    private TravelTimeService service;
    private Station stationA;
    private Station stationB;
    private Locomotive loco;

    @Before
    public void setUp() {
        service = new TravelTimeService();
        stationA = new Station("S001", "Lisboa", "Lisboa");
        stationB = new Station("S002", "Porto", "Porto");
        loco = new Locomotive("L001", "electric", 200, 1435);
    }

    @Test
    public void ensureFindStationByIdWorks() {
        service.getStationsById().put("S001", stationA);

        Optional<Station> found = service.findStationByIdOrName("S001");
        assertTrue(found.isPresent());
        assertEquals(stationA, found.get());
    }

    @Test
    public void ensureFindStationByNameWorks() {
        service.getStationsByNameLower().put("lisboa", stationA);

        Optional<Station> found = service.findStationByIdOrName("Lisboa");
        assertTrue(found.isPresent());
        assertEquals(stationA, found.get());
    }

    @Test
    public void ensureFindStationReturnsEmptyForNotFound() {
        Optional<Station> found = service.findStationByIdOrName("UNKNOWN");
        assertTrue(found.isEmpty());
    }

    @Test
    public void ensureFindStationReturnsEmptyForNull() {
        Optional<Station> found = service.findStationByIdOrName(null);
        assertTrue(found.isEmpty());
    }

    @Test
    public void ensureFindLocoByIdWorks() {
        service.getLocomotivesById().put("L001", loco);

        Optional<Locomotive> found = service.findLocoById("L001");
        assertTrue(found.isPresent());
        assertEquals(loco, found.get());
    }

    @Test
    public void ensureFindLocoReturnsEmptyForNotFound() {
        Optional<Locomotive> found = service.findLocoById("UNKNOWN");
        assertTrue(found.isEmpty());
    }

    @Test
    public void ensureFindDirectSegmentWorks() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        service.getSegments().add(segment);

        Optional<LineSegment> found = service.findDirectSegment(stationA, stationB);
        assertTrue(found.isPresent());
        assertEquals(segment, found.get());
    }

    @Test
    public void ensureFindDirectSegmentWorksInReverse() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        service.getSegments().add(segment);

        Optional<LineSegment> found = service.findDirectSegment(stationB, stationA);
        assertTrue(found.isPresent());
    }

    @Test
    public void ensureFindDirectSegmentReturnsEmptyForNoConnection() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        service.getSegments().add(segment);

        Station stationC = new Station("S003", "Coimbra", "Coimbra");
        Optional<LineSegment> found = service.findDirectSegment(stationA, stationC);
        assertTrue(found.isEmpty());
    }

    @Test
    public void ensureCalculateTravelTimeWorks() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 100, 120, 1435, true);

        double hours = service.calculateTravelTimeHours(stationA, stationB, loco, segment);
        // Expected: 100 km / min(200 km/h, 120 km/h) = 100 / 120 = 0.833... hours
        assertEquals(100.0 / 120.0, hours, 0.001);
    }

    @Test
    public void ensureCalculateTravelTimeUsesLocomotiveSpeedWhenLower() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 100, 250, 1435, true);
        Locomotive slowLoco = new Locomotive("L002", "diesel", 100, 1435);

        double hours = service.calculateTravelTimeHours(stationA, stationB, slowLoco, segment);
        // Expected: 100 km / min(100 km/h, 250 km/h) = 100 / 100 = 1.0 hours
        assertEquals(1.0, hours, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureCalculateTravelTimeThrowsExceptionOnGaugeMismatch() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 100, 120, 1676, true);
        service.calculateTravelTimeHours(stationA, stationB, loco, segment);
    }

    @Test
    public void ensureFormatHWorks() {
        assertEquals("1h", TravelTimeService.formatH(1.0));
        assertEquals("30m", TravelTimeService.formatH(0.5));
        assertEquals("1h 30m", TravelTimeService.formatH(1.5));
        assertEquals("2h", TravelTimeService.formatH(2.0));
        assertEquals("0m", TravelTimeService.formatH(-1.0));
    }

    @Test
    public void ensureFormatHRoundsCorrectly() {
        String result = TravelTimeService.formatH(1.99);
        assertTrue(result.contains("1h"));
    }
}
