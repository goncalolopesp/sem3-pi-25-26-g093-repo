package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PickPathSequencer class (USEI04).
 */
class PickPathSequencerTest {

    private final PickPathSequencer sequencer = new PickPathSequencer();

    @Test
    void ensureTotalDistanceIsZeroWhenListIsNullOrEmpty() {
        assertEquals(0, sequencer.totalDistance(null));
        assertEquals(0, sequencer.totalDistance(new ArrayList<>()));
    }

    @Test
    void ensureTotalDistanceCalculatedCorrectly() {
        List<Coordinate> coords = Arrays.asList(
                new Coordinate(1, 8),
                new Coordinate(2, 2),
                new Coordinate(3, 4)
        );

        int total = sequencer.totalDistance(coords);
        // Expected 33 (as per example in project document)
        assertEquals(33, total, "Total distance should match example (33)");
    }

    @Test
    void ensureStrategy1OrdersByAisleThenBay() {
        List<Coordinate> coords = Arrays.asList(
                new Coordinate(3, 4),
                new Coordinate(1, 8),
                new Coordinate(2, 2)
        );

        List<Coordinate> ordered = sequencer.strategy1Path(coords);
        List<Coordinate> expected = Arrays.asList(
                new Coordinate(1, 8),
                new Coordinate(2, 2),
                new Coordinate(3, 4)
        );

        assertEquals(expected, ordered, "Strategy A must sort ascending by aisle then bay");
    }

    @Test
    void ensureStrategy1DistanceMatchesExpectedExample() {
        List<Coordinate> coords = Arrays.asList(
                new Coordinate(1, 8),
                new Coordinate(2, 2),
                new Coordinate(3, 4)
        );

        int result = sequencer.strategy1Dis(coords);
        assertEquals(33, result, "Strategy A total distance should be 33 as per example");
    }

    @Test
    void ensureStrategy2PathMatchesExampleOrder() {
        List<Coordinate> coords = Arrays.asList(
                new Coordinate(1, 8),
                new Coordinate(2, 2),
                new Coordinate(3, 4)
        );

        List<Coordinate> path = sequencer.strategy2Path(coords);

        // Expected path: (2,2) → (3,4) → (1,8)
        List<Coordinate> expected = Arrays.asList(
                new Coordinate(2, 2),
                new Coordinate(3, 4),
                new Coordinate(1, 8)
        );

        assertEquals(expected, path, "Strategy B path should follow the nearest-neighbour order (2,2)-(3,4)-(1,8)");
    }

    @Test
    void ensureStrategy2DistanceMatchesExample() {
        List<Coordinate> coords = Arrays.asList(
                new Coordinate(1, 8),
                new Coordinate(2, 2),
                new Coordinate(3, 4)
        );

        int total = sequencer.strategy2Dis(coords);
        // Expected 35 (as per example in project document)
        assertEquals(35, total, "Strategy B total distance should be 35 as per example");
    }

    @Test
    void ensureBothStrategiesHandleEmptyList() {
        assertEquals(0, sequencer.strategy1Dis(new ArrayList<>()));
        assertEquals(0, sequencer.strategy2Dis(new ArrayList<>()));
        assertTrue(sequencer.strategy1Path(new ArrayList<>()).isEmpty());
        assertTrue(sequencer.strategy2Path(new ArrayList<>()).isEmpty());
    }

    @Test
    void ensureSameAisleDistanceIsCalculatedCorrectly() throws Exception {
        // Using reflection to access private method indirectly via totalDistance
        List<Coordinate> coords = Arrays.asList(
                new Coordinate(1, 2),
                new Coordinate(1, 8)
        );

        int distance = sequencer.totalDistance(coords);
        // Expected: (0,0)->(1,2)=5 + (1,2)->(1,8)=6 => 11
        assertEquals(11, distance, "Distance in same aisle should be calculated as |b1-b2|");
    }
}
