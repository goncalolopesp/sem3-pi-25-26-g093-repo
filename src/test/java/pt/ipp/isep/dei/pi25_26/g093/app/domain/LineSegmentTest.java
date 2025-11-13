package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the LineSegment class (USPL03) — JUnit 4 version.
 */
public class LineSegmentTest {

    private Station stationA;
    private Station stationB;
    private Station stationC;

    @Before
    public void setUp() {
        stationA = new Station("S001", "Lisboa", "Lisboa");
        stationB = new Station("S002", "Porto", "Porto");
        stationC = new Station("S003", "Coimbra", "Coimbra");
    }

    @Test
    public void ensureConstructorAndGettersWork() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);

        assertEquals("L001", segment.getId());
        assertEquals(stationA, segment.getOrigin());
        assertEquals(stationB, segment.getDestination());
        assertEquals(300, segment.getLengthKm(), 0.001);
        assertEquals(120, segment.getSpeedLimitKmH(), 0.001);
        assertEquals(1435, segment.getGaugeMm(), 0.001);
        assertTrue(segment.isElectrified());
    }

    @Test
    public void ensureConstructorValidationThrowsException() {
        try { new LineSegment(null, stationA, stationB, 300, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("", stationA, stationB, 300, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", null, stationB, 300, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", stationA, null, 300, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", stationA, stationA, 300, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", stationA, stationB, 0, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", stationA, stationB, -100, 120, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", stationA, stationB, 300, 0, 1435, true); fail(); } catch (IllegalArgumentException ignored) {}
        try { new LineSegment("L001", stationA, stationB, 300, 120, 0, true); fail(); } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void ensureTrimWorks() {
        LineSegment segment = new LineSegment("  L001  ", stationA, stationB, 300, 120, 1435, true);
        assertEquals("L001", segment.getId());
    }

    @Test
    public void ensureConnectsWorks() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);

        assertTrue(segment.connects(stationA, stationB));
        assertTrue(segment.connects(stationB, stationA));
        assertFalse(segment.connects(stationA, stationC));
    }

    @Test
    public void ensureEqualsWorks() {
        LineSegment s1 = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        LineSegment s2 = new LineSegment("L001", stationA, stationC, 200, 100, 1676, false);
        LineSegment s3 = new LineSegment("L002", stationA, stationB, 300, 120, 1435, true);

        assertEquals(s1, s2); // Same ID (case-insensitive)
        assertNotEquals(s1, s3);
    }

    @Test
    public void ensureEqualsIsCaseInsensitiveForId() {
        LineSegment s1 = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        LineSegment s2 = new LineSegment("l001", stationA, stationC, 200, 100, 1676, false);

        assertEquals(s1, s2);
    }

    @Test
    public void ensureHashCodeWorks() {
        LineSegment s1 = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        LineSegment s2 = new LineSegment("L001", stationA, stationC, 200, 100, 1676, false);

        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void ensureToStringWorks() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, true);
        String str = segment.toString();

        assertTrue(str.contains("L001"));
        assertTrue(str.contains("Lisboa"));
        assertTrue(str.contains("Porto"));
        assertTrue(str.contains("300"));
        assertTrue(str.contains("120"));
        assertTrue(str.contains("1435"));
        assertTrue(str.contains("electrified"));
    }

    @Test
    public void ensureToStringShowsNonElectrified() {
        LineSegment segment = new LineSegment("L001", stationA, stationB, 300, 120, 1435, false);
        String str = segment.toString();

        assertTrue(str.contains("non-electrified"));
    }
}
