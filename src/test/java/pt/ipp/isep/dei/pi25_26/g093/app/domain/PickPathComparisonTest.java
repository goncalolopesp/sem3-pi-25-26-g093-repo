package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the PickPathComparison class (USEI04) — JUnit 4 version.
 */
public class PickPathComparisonTest {

    @Test
    public void ensureConstructorAndGettersWork() {
        List<Coordinate> pathA = Arrays.asList(
                new Coordinate(1, 2),
                new Coordinate(3, 4)
        );
        List<Coordinate> pathB = Arrays.asList(
                new Coordinate(2, 3),
                new Coordinate(1, 5)
        );

        PickPathResult resultA = new PickPathResult("Strategy A", pathA, 20);
        PickPathResult resultB = new PickPathResult("Strategy B", pathB, 25);

        PickPathComparison comparison = new PickPathComparison(resultA, resultB);

        assertNotNull(comparison.getStrategyA());
        assertNotNull(comparison.getStrategyB());
        assertEquals(2, comparison.getStrategyA().getPath().size());
        assertEquals(2, comparison.getStrategyB().getPath().size());
    }

    @Test
    public void ensureBestStrategySelectsShorterDistance() {
        List<Coordinate> pathA = Arrays.asList(new Coordinate(1, 2));
        List<Coordinate> pathB = Arrays.asList(new Coordinate(2, 3));

        PickPathResult resultA = new PickPathResult("Strategy A", pathA, 15);
        PickPathResult resultB = new PickPathResult("Strategy B", pathB, 10);

        PickPathComparison comparison = new PickPathComparison(resultA, resultB);

        assertEquals("Strategy B", comparison.getBestStrategy().getStrategyName());
        assertEquals(10, comparison.getBestStrategy().getTotalDistance(), 0.001);
    }

    @Test
    public void ensureDistanceImprovementIsCalculatedCorrectly() {
        List<Coordinate> pathA = Arrays.asList(new Coordinate(1, 2));
        List<Coordinate> pathB = Arrays.asList(new Coordinate(2, 3));

        PickPathResult resultA = new PickPathResult("Strategy A", pathA, 30);
        PickPathResult resultB = new PickPathResult("Strategy B", pathB, 20);

        PickPathComparison comparison = new PickPathComparison(resultA, resultB);

        assertEquals(10, comparison.getDistanceImprovement(), 0.001);
    }

    @Test
    public void ensureImprovementPercentageIsCalculatedCorrectly() {
        List<Coordinate> pathA = Arrays.asList(new Coordinate(1, 2));
        List<Coordinate> pathB = Arrays.asList(new Coordinate(2, 3));

        PickPathResult resultA = new PickPathResult("Strategy A", pathA, 100);
        PickPathResult resultB = new PickPathResult("Strategy B", pathB, 80);

        PickPathComparison comparison = new PickPathComparison(resultA, resultB);

        // Expected: (100 - 80) / 100 * 100 = 20%
        assertEquals(20.0, comparison.getImprovementPercentage(), 0.1);
    }

    @Test
    public void ensureImprovementPercentageReturnsZeroWhenBothEqual() {
        List<Coordinate> path = Arrays.asList(new Coordinate(1, 2));

        PickPathResult resultA = new PickPathResult("Strategy A", path, 50);
        PickPathResult resultB = new PickPathResult("Strategy B", path, 50);

        PickPathComparison comparison = new PickPathComparison(resultA, resultB);

        assertEquals(0.0, comparison.getImprovementPercentage(), 0.001);
    }

    @Test
    public void ensureToStringContainsAllSections() {
        List<Coordinate> pathA = Arrays.asList(new Coordinate(1, 2));
        List<Coordinate> pathB = Arrays.asList(new Coordinate(2, 3));

        PickPathResult resultA = new PickPathResult("Strategy A", pathA, 20);
        PickPathResult resultB = new PickPathResult("Strategy B", pathB, 25);

        PickPathComparison comparison = new PickPathComparison(resultA, resultB);
        String report = comparison.toString();

        assertTrue(report.contains("Strategy A"));
        assertTrue(report.contains("Strategy B"));
        assertTrue(report.contains("Best Strategy"));
    }
}
