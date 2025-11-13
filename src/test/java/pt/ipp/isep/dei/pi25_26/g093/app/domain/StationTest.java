package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Station class (USPL03) — JUnit 4 version.
 */
public class StationTest {

    @Test
    public void ensureConstructorAndGettersWork() {
        Station station = new Station("S001", "Lisboa Oriente", "Lisboa");

        assertEquals("S001", station.getId());
        assertEquals("Lisboa Oriente", station.getName());
        assertEquals("Lisboa", station.getLocation());
        assertTrue(station.hasLocation());
    }

    @Test
    public void ensureConstructorWithoutLocationWorks() {
        Station station = new Station("S002", "Porto Campanha");

        assertEquals("S002", station.getId());
        assertEquals("Porto Campanha", station.getName());
        assertFalse(station.hasLocation());
        assertEquals("", station.getLocation());
    }

    @Test
    public void ensureNullLocationIsHandled() {
        Station station = new Station("S003", "Braga", null);

        assertFalse(station.hasLocation());
        assertEquals("", station.getLocation());
    }

    @Test
    public void ensureIdAndNameValidationThrowsException() {
        try {
            new Station(null, "Name", "Loc");
            fail("Expected IllegalArgumentException for null ID");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Station("", "Name", "Loc");
            fail("Expected IllegalArgumentException for empty ID");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Station("   ", "Name", "Loc");
            fail("Expected IllegalArgumentException for blank ID");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Station("S001", null, "Loc");
            fail("Expected IllegalArgumentException for null name");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Station("S001", "", "Loc");
            fail("Expected IllegalArgumentException for empty name");
        } catch (IllegalArgumentException ignored) {}

        try {
            new Station("S001", "   ", "Loc");
            fail("Expected IllegalArgumentException for blank name");
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void ensureTrimWorks() {
        Station station = new Station("  S001  ", "  Name  ", "  Loc  ");

        assertEquals("S001", station.getId());
        assertEquals("Name", station.getName());
        assertEquals("Loc", station.getLocation());
    }

    @Test
    public void ensureEqualsWorks() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("S001", "Lisboa", "Lisboa");
        Station s3 = new Station("S002", "Porto", "Porto");

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
    }

    @Test
    public void ensureEqualsIsCaseInsensitiveForId() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("s001", "Porto", "Porto");

        assertEquals(s1, s2);
    }

    @Test
    public void ensureHashCodeWorks() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("S001", "Lisboa", "Lisboa");

        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void ensureToStringWorks() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("S002", "Porto");

        assertTrue(s1.toString().contains("Lisboa"));
        assertTrue(s1.toString().contains("S001"));
        assertTrue(s1.toString().contains("Lisboa"));

        assertTrue(s2.toString().contains("Porto"));
        assertTrue(s2.toString().contains("S002"));
        assertFalse(s2.toString().contains("-"));
    }
}
