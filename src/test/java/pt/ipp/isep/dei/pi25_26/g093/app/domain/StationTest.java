package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Station class (USPL03).
 */
class StationTest {

    @Test
    void ensureConstructorAndGettersWork() {
        Station station = new Station("S001", "Lisboa Oriente", "Lisboa");
        
        assertEquals("S001", station.getId());
        assertEquals("Lisboa Oriente", station.getName());
        assertEquals("Lisboa", station.getLocation());
        assertTrue(station.hasLocation());
    }

    @Test
    void ensureConstructorWithoutLocationWorks() {
        Station station = new Station("S002", "Porto Campanha");
        
        assertEquals("S002", station.getId());
        assertEquals("Porto Campanha", station.getName());
        assertFalse(station.hasLocation());
        assertEquals("", station.getLocation());
    }

    @Test
    void ensureNullLocationIsHandled() {
        Station station = new Station("S003", "Braga", null);
        
        assertFalse(station.hasLocation());
        assertEquals("", station.getLocation());
    }

    @Test
    void ensureIdAndNameValidationThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Station(null, "Name", "Loc"));
        assertThrows(IllegalArgumentException.class, () -> new Station("", "Name", "Loc"));
        assertThrows(IllegalArgumentException.class, () -> new Station("   ", "Name", "Loc"));
        assertThrows(IllegalArgumentException.class, () -> new Station("S001", null, "Loc"));
        assertThrows(IllegalArgumentException.class, () -> new Station("S001", "", "Loc"));
        assertThrows(IllegalArgumentException.class, () -> new Station("S001", "   ", "Loc"));
    }

    @Test
    void ensureTrimWorks() {
        Station station = new Station("  S001  ", "  Name  ", "  Loc  ");
        
        assertEquals("S001", station.getId());
        assertEquals("Name", station.getName());
        assertEquals("Loc", station.getLocation());
    }

    @Test
    void ensureEqualsWorks() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("S001", "Lisboa", "Lisboa");
        Station s3 = new Station("S002", "Porto", "Porto");
        
        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
    }

    @Test
    void ensureEqualsIsCaseInsensitiveForId() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("s001", "Porto", "Porto");
        
        assertEquals(s1, s2);
    }

    @Test
    void ensureHashCodeWorks() {
        Station s1 = new Station("S001", "Lisboa", "Lisboa");
        Station s2 = new Station("S001", "Lisboa", "Lisboa");
        
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void ensureToStringWorks() {
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

