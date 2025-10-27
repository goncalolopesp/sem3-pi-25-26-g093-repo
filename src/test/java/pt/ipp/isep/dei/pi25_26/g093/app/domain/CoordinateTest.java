package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Coordinate class.
 */
class CoordinateTest {

    @Test
    void ensureConstructorAndGettersWork() {
        Coordinate c = new Coordinate(2, 5);

        assertEquals(2, c.getAisle(), "Aisle should be stored correctly");
        assertEquals(5, c.getBay(), "Bay should be stored correctly");
    }

    @Test
    void ensureToStringIsFormattedCorrectly() {
        Coordinate c = new Coordinate(1, 3);

        String expected = "(1 , 3)";
        assertEquals(expected, c.toString(), "toString() must follow the format '(aisle , bay)'");
    }

    @Test
    void ensureEqualsReturnsTrueForSameValues() {
        Coordinate c1 = new Coordinate(4, 7);
        Coordinate c2 = new Coordinate(4, 7);

        assertEquals(c1, c2, "Coordinates with same aisle and bay should be equal");
        assertEquals(c1.hashCode(), c2.hashCode(), "Equal objects must have equal hashCodes");
    }

    @Test
    void ensureEqualsReturnsFalseForDifferentValues() {
        Coordinate c1 = new Coordinate(4, 7);
        Coordinate c2 = new Coordinate(5, 7);
        Coordinate c3 = new Coordinate(4, 8);

        assertNotEquals(c1, c2, "Different aisle should mean not equal");
        assertNotEquals(c1, c3, "Different bay should mean not equal");
    }

    @Test
    void ensureEqualsHandlesNullAndDifferentClass() {
        Coordinate c = new Coordinate(1, 1);

        assertNotEquals(c, null, "Coordinate must not be equal to null");
        assertNotEquals(c, "(1 , 1)", "Coordinate must not be equal to different class object");
    }
}
