package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PickPathResult class (USEI04).
 */
class PickPathResultTest {

    @Test
    void ensureConstructorAndGettersWork() {
        List<Coordinate> path = Arrays.asList(
                new Coordinate(1, 2),
                new Coordinate(2, 3)
        );
        PickPathResult result = new PickPathResult("Test Strategy", path, 15);

        assertEquals("Test Strategy", result.getStrategyName());
        assertEquals(2, result.getPath().size());
        assertEquals(15, result.getTotalDistance());
    }

    @Test
    void ensureToStringIsFormattedCorrectly() {
        List<Coordinate> path = Arrays.asList(new Coordinate(1, 2));
        PickPathResult result = new PickPathResult("Strategy A", path, 10);

        String str = result.toString();
        assertTrue(str.contains("Strategy A"));
        assertTrue(str.contains("Distance=10"));
    }

    @Test
    void ensureEmptyPathIsHandled() {
        PickPathResult result = new PickPathResult("Test", Arrays.asList(), 0);
        assertTrue(result.getPath().isEmpty());
        assertEquals(0, result.getTotalDistance());
    }
}

