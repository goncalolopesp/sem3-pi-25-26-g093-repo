package pt.ipp.isep.dei.pi25_26.g093.app.service;

import org.junit.Before;
import org.junit.Test;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.Coordinate;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PickPathComparison;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PickPathResult;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PickPathSequencer;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyItem;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.TrolleyPlan;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for USEI04 - Pick Path Sequencing.
 * 
 * These tests cover:
 * - Distance calculation
 * - Strategy A (Deterministic Sweep)
 * - Strategy B (Nearest Neighbour)
 * - Input validation
 * - Edge cases
 * - Result correctness
 */
public class PickPathUnitTest {

    private PickPathService service;
    private PickPathSequencer sequencer;

    @Before
    public void setUp() {
        service = new PickPathService();
        sequencer = new PickPathSequencer();
    }

    // ==================== DISTANCE CALCULATION TESTS ====================

    @Test
    public void testDistanceCalculation_SameAisle() {
        // Arrange: Two coordinates in same aisle
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(1, 5);
        
        // Act: Calculate distance using sequencer
        List<Coordinate> path = new ArrayList<>();
        path.add(c1);
        path.add(c2);
        int distance = sequencer.totalDistance(path);
        
        // Assert: Distance = |5 - 2| = 3
        // Path: (0,0) -> (1,2) -> (1,5)
        // (0,0) to (1,2): 0 + |1-0|*3 + 2 = 5
        // (1,2) to (1,5): |5-2| = 3
        // Total: 5 + 3 = 8
        assertEquals("Distance for same aisle should be |b1-b2|", 8, distance);
    }

    @Test
    public void testDistanceCalculation_DifferentAisles() {
        // Arrange: Two coordinates in different aisles
        Coordinate c1 = new Coordinate(1, 2);
        Coordinate c2 = new Coordinate(2, 3);
        
        // Act
        List<Coordinate> path = new ArrayList<>();
        path.add(c1);
        path.add(c2);
        int distance = sequencer.totalDistance(path);
        
        // Assert: Distance = b1 + |a1-a2|*3 + b2
        // Path: (0,0) -> (1,2) -> (2,3)
        // (0,0) to (1,2): 0 + |1-0|*3 + 2 = 5
        // (1,2) to (2,3): 2 + |2-1|*3 + 3 = 2 + 3 + 3 = 8
        // Total: 5 + 8 = 13
        assertEquals("Distance for different aisles should use formula", 13, distance);
    }

    @Test
    public void testDistanceCalculation_FromEntrance() {
        // Arrange: Single coordinate
        Coordinate c1 = new Coordinate(1, 2);
        
        // Act
        List<Coordinate> path = new ArrayList<>();
        path.add(c1);
        int distance = sequencer.totalDistance(path);
        
        // Assert: Distance from (0,0) to (1,2) = 0 + |1-0|*3 + 2 = 5
        assertEquals("Distance from entrance (0,0) should be calculated", 5, distance);
    }

    // ==================== STRATEGY A TESTS ====================

    @Test
    public void testStrategyA_DeterministicSweep_Sorting() {
        // Arrange: Coordinates in random order
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(2, 3));
        coordinates.add(new Coordinate(1, 5));
        coordinates.add(new Coordinate(1, 2));
        coordinates.add(new Coordinate(2, 1));
        
        // Act
        List<Coordinate> path = sequencer.strategy1Path(coordinates);
        
        // Assert: Should be sorted by aisle (asc), then bay (asc)
        // Expected: (1,2), (1,5), (2,1), (2,3)
        assertEquals("Should have 4 coordinates", 4, path.size());
        assertEquals("First should be (1,2)", new Coordinate(1, 2), path.get(0));
        assertEquals("Second should be (1,5)", new Coordinate(1, 5), path.get(1));
        assertEquals("Third should be (2,1)", new Coordinate(2, 1), path.get(2));
        assertEquals("Fourth should be (2,3)", new Coordinate(2, 3), path.get(3));
    }

    @Test
    public void testStrategyA_EmptyList() {
        // Arrange: Empty coordinate list
        List<Coordinate> empty = new ArrayList<>();
        
        // Act
        List<Coordinate> path = sequencer.strategy1Path(empty);
        int distance = sequencer.strategy1Dis(empty);
        
        // Assert
        assertTrue("Path should be empty", path.isEmpty());
        assertEquals("Distance should be 0 for empty path", 0, distance);
    }

    @Test
    public void testStrategyA_SingleCoordinate() {
        // Arrange: Single coordinate
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(1, 2));
        
        // Act
        List<Coordinate> path = sequencer.strategy1Path(coordinates);
        int distance = sequencer.strategy1Dis(coordinates);
        
        // Assert
        assertEquals("Should have 1 coordinate", 1, path.size());
        assertEquals("Should be (1,2)", new Coordinate(1, 2), path.get(0));
        // Distance from (0,0) to (1,2) = 5
        assertEquals("Distance should be 5", 5, distance);
    }

    // ==================== STRATEGY B TESTS ====================

    @Test
    public void testStrategyB_NearestNeighbour_StartsFromNearest() {
        // Arrange: Multiple coordinates, nearest to (0,0) should be chosen first
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(2, 5));  // Distance from (0,0): 0 + |2-0|*3 + 5 = 11
        coordinates.add(new Coordinate(1, 1));   // Distance from (0,0): 0 + |1-0|*3 + 1 = 4 (nearest)
        coordinates.add(new Coordinate(1, 3));    // Distance from (0,0): 0 + |1-0|*3 + 3 = 6
        
        // Act
        List<Coordinate> path = sequencer.strategy2Path(coordinates);
        
        // Assert: First should be nearest to (0,0)
        assertEquals("First should be nearest to (0,0)", new Coordinate(1, 1), path.get(0));
        assertEquals("Should have 3 coordinates", 3, path.size());
    }

    @Test
    public void testStrategyB_NearestNeighbour_AllVisited() {
        // Arrange: Multiple coordinates
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(1, 2));
        coordinates.add(new Coordinate(2, 3));
        coordinates.add(new Coordinate(1, 5));
        
        // Act
        List<Coordinate> path = sequencer.strategy2Path(coordinates);
        
        // Assert: All coordinates are visited
        assertEquals("Should have 3 coordinates", 3, path.size());
        assertTrue("Should contain (1,2)", path.contains(new Coordinate(1, 2)));
        assertTrue("Should contain (2,3)", path.contains(new Coordinate(2, 3)));
        assertTrue("Should contain (1,5)", path.contains(new Coordinate(1, 5)));
        
        // Assert: No duplicates
        long uniqueCount = path.stream().distinct().count();
        assertEquals("Should have no duplicates", path.size(), uniqueCount);
    }

    @Test
    public void testStrategyB_EmptyList() {
        // Arrange: Empty coordinate list
        List<Coordinate> empty = new ArrayList<>();
        
        // Act
        List<Coordinate> path = sequencer.strategy2Path(empty);
        int distance = sequencer.strategy2Dis(empty);
        
        // Assert
        assertTrue("Path should be empty", path.isEmpty());
        assertEquals("Distance should be 0 for empty path", 0, distance);
    }

    @Test
    public void testStrategyB_SingleCoordinate() {
        // Arrange: Single coordinate
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(1, 2));
        
        // Act
        List<Coordinate> path = sequencer.strategy2Path(coordinates);
        int distance = sequencer.strategy2Dis(coordinates);
        
        // Assert
        assertEquals("Should have 1 coordinate", 1, path.size());
        assertEquals("Should be (1,2)", new Coordinate(1, 2), path.get(0));
        // Distance from (0,0) to (1,2) = 5
        assertEquals("Distance should be 5", 5, distance);
    }

    // ==================== PICKING ORDER TESTS ====================

    @Test
    public void testPickingOrder_SimplePathThroughIntermediate() {
        // Arrange: Plan with A and C, optimal path should go through intermediate
        // Graph: (0,0) -> (1,1) -> (1,3) -> (2,2)
        // Direct: (0,0) -> (1,1) -> (2,2) = 4 + 7 = 11
        // Through intermediate: (0,0) -> (1,1) -> (1,3) -> (2,2) = 4 + 2 + 8 = 14
        // But Strategy A will sort: (1,1), (1,3), (2,2)
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 1));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 2, 2));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        PickPathResult strategyA = comparison.getStrategyA();
        
        // Assert: Strategy A sorts by aisle: (1,1) then (2,2)
        List<Coordinate> path = strategyA.getPath();
        assertEquals("Should have 2 coordinates", 2, path.size());
        assertEquals("First should be (1,1)", new Coordinate(1, 1), path.get(0));
        assertEquals("Second should be (2,2)", new Coordinate(2, 2), path.get(1));
        
        // Distance: (0,0) to (1,1) = 4, (1,1) to (2,2) = 1 + 3 + 2 = 6, total = 10
        assertTrue("Total distance should be calculated", strategyA.getTotalDistance() > 0);
    }

    @Test
    public void testPickingOrder_OptimalPathCalculation() {
        // Arrange: Plan with two coordinates in same aisle
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 1));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 1, 3));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        PickPathResult strategyA = comparison.getStrategyA();
        
        // Assert: Path should be (1,1) -> (1,3)
        // Distance: (0,0) to (1,1) = 4, (1,1) to (1,3) = |3-1| = 2, total = 6
        List<Coordinate> path = strategyA.getPath();
        assertEquals("Should have 2 coordinates", 2, path.size());
        assertEquals("Distance should be 6", 6, strategyA.getTotalDistance());
    }

    // ==================== INPUT VALIDATION TESTS ====================

    @Test
    public void testValidation_NullPlan() {
        // Arrange: null plan
        
        // Act
        PickPathComparison result = service.generatePickPath(null);
        
        // Assert: Should return null (service handles null gracefully)
        assertNull("Should return null for null plan", result);
    }

    @Test
    public void testValidation_EmptyPlan() {
        // Arrange: Plan with no items
        TrolleyPlan emptyPlan = new TrolleyPlan(1, 1000.0);
        
        // Act
        PickPathComparison result = service.generatePickPath(emptyPlan);
        
        // Assert: Should return null (service handles empty plan)
        assertNull("Should return null for empty plan", result);
    }

    @Test
    public void testValidation_NullCoordinateList() {
        // Arrange: null coordinate list
        
        // Act
        List<Coordinate> pathA = sequencer.strategy1Path(null);
        List<Coordinate> pathB = sequencer.strategy2Path(null);
        int distanceA = sequencer.strategy1Dis(null);
        int distanceB = sequencer.strategy2Dis(null);
        
        // Assert: Should handle null gracefully
        assertTrue("Strategy A path should be empty for null input", pathA.isEmpty());
        assertTrue("Strategy B path should be empty for null input", pathB.isEmpty());
        assertEquals("Strategy A distance should be 0 for null", 0, distanceA);
        assertEquals("Strategy B distance should be 0 for null", 0, distanceB);
    }

    @Test
    public void testValidation_EmptyCoordinateList() {
        // Arrange: Empty coordinate list
        List<Coordinate> empty = new ArrayList<>();
        
        // Act
        List<Coordinate> pathA = sequencer.strategy1Path(empty);
        List<Coordinate> pathB = sequencer.strategy2Path(empty);
        int distanceA = sequencer.strategy1Dis(empty);
        int distanceB = sequencer.strategy2Dis(empty);
        
        // Assert
        assertTrue("Strategy A path should be empty", pathA.isEmpty());
        assertTrue("Strategy B path should be empty", pathB.isEmpty());
        assertEquals("Strategy A distance should be 0", 0, distanceA);
        assertEquals("Strategy B distance should be 0", 0, distanceB);
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    public void testEdgeCase_OnlyOneLocation() {
        // Arrange: Plan with single location
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 2));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Both strategies should have same path (only one coordinate)
        assertNotNull("Comparison should not be null", comparison);
        PickPathResult strategyA = comparison.getStrategyA();
        PickPathResult strategyB = comparison.getStrategyB();
        
        assertEquals("Both strategies should have 1 coordinate", 1, strategyA.getPath().size());
        assertEquals("Both strategies should have 1 coordinate", 1, strategyB.getPath().size());
        assertEquals("Both strategies should have same distance", 
                    strategyA.getTotalDistance(), strategyB.getTotalDistance());
        
        // Distance from (0,0) to (1,2) = 5
        assertEquals("Distance should be 5", 5, strategyA.getTotalDistance());
    }

    @Test
    public void testEdgeCase_AllNodesEqual() {
        // Arrange: Plan with all items at same coordinate
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 2));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 1, 2));
        plan.tryAdd(new TrolleyItem("O003", "SKU003", 1, 5.0, 1, 2));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Should merge to single coordinate
        assertNotNull("Comparison should not be null", comparison);
        PickPathResult strategyA = comparison.getStrategyA();
        
        assertEquals("Should have 1 unique coordinate (duplicates merged)", 1, strategyA.getPath().size());
        assertEquals("Should be (1,2)", new Coordinate(1, 2), strategyA.getPath().get(0));
        
        // Distance: (0,0) to (1,2) = 5
        assertEquals("Distance should be 5", 5, strategyA.getTotalDistance());
    }

    @Test
    public void testEdgeCase_RepeatedNodesInPlan() {
        // Arrange: Plan with repeated coordinates (should be merged)
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 2));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 2, 3));
        plan.tryAdd(new TrolleyItem("O003", "SKU003", 1, 5.0, 1, 2)); // Duplicate
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Duplicates should be merged
        assertNotNull("Comparison should not be null", comparison);
        PickPathResult strategyA = comparison.getStrategyA();
        
        assertEquals("Should have 2 unique coordinates", 2, strategyA.getPath().size());
        
        // Verify (1,2) appears only once
        long count12 = strategyA.getPath().stream()
            .filter(c -> c.getAisle() == 1 && c.getBay() == 2)
            .count();
        assertEquals("Coordinate (1,2) should appear only once", 1, count12);
    }

    @Test
    public void testEdgeCase_CoordinatesAtEntrance() {
        // Arrange: Plan with coordinate at entrance (0,0)
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 0, 0));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 1, 2));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Should handle (0,0) correctly
        assertNotNull("Comparison should not be null", comparison);
        PickPathResult strategyA = comparison.getStrategyA();
        
        assertTrue("Should contain (0,0)", strategyA.getPath().contains(new Coordinate(0, 0)));
        assertTrue("Should contain (1,2)", strategyA.getPath().contains(new Coordinate(1, 2)));
        
        // Distance from (0,0) to (0,0) = 0, then (0,0) to (1,2) = 5, total = 5
        // Or if sorted: (0,0) first, then (1,2)
        assertTrue("Total distance should be >= 5", strategyA.getTotalDistance() >= 5);
    }

    // ==================== RESULT CORRECTNESS TESTS ====================

    @Test
    public void testResultCorrectness_PathNotNull() {
        // Arrange
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 2));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Paths are not null
        assertNotNull("Comparison should not be null", comparison);
        assertNotNull("Strategy A path should not be null", comparison.getStrategyA().getPath());
        assertNotNull("Strategy B path should not be null", comparison.getStrategyB().getPath());
    }

    @Test
    public void testResultCorrectness_TotalDistanceMatchesPath() {
        // Arrange: Plan with known coordinates
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 1));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 1, 3));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        PickPathResult strategyA = comparison.getStrategyA();
        
        // Assert: Distance should match manual calculation
        // Path: (0,0) -> (1,1) -> (1,3)
        // (0,0) to (1,1): 0 + |1-0|*3 + 1 = 4
        // (1,1) to (1,3): |3-1| = 2
        // Total: 6
        assertEquals("Total distance should match path calculation", 6, strategyA.getTotalDistance());
    }

    @Test
    public void testResultCorrectness_OrderingRespectsStrategy() {
        // Arrange: Plan with coordinates that test sorting
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 2, 3));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 1, 5));
        plan.tryAdd(new TrolleyItem("O003", "SKU003", 1, 5.0, 1, 2));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        PickPathResult strategyA = comparison.getStrategyA();
        
        // Assert: Strategy A should sort by aisle, then bay
        List<Coordinate> path = strategyA.getPath();
        assertEquals("First should be (1,2)", new Coordinate(1, 2), path.get(0));
        assertEquals("Second should be (1,5)", new Coordinate(1, 5), path.get(1));
        assertEquals("Third should be (2,3)", new Coordinate(2, 3), path.get(2));
    }

    @Test
    public void testResultCorrectness_BothStrategiesComplete() {
        // Arrange
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 2));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 2, 3));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Both strategies have complete results
        PickPathResult strategyA = comparison.getStrategyA();
        PickPathResult strategyB = comparison.getStrategyB();
        
        assertNotNull("Strategy A should not be null", strategyA);
        assertNotNull("Strategy B should not be null", strategyB);
        assertNotNull("Strategy A path should not be null", strategyA.getPath());
        assertNotNull("Strategy B path should not be null", strategyB.getPath());
        assertTrue("Strategy A distance should be >= 0", strategyA.getTotalDistance() >= 0);
        assertTrue("Strategy B distance should be >= 0", strategyB.getTotalDistance() >= 0);
        
        // Both should have same number of coordinates
        assertEquals("Both strategies should have same number of coordinates",
                    strategyA.getPath().size(), strategyB.getPath().size());
    }

    @Test
    public void testResultCorrectness_BestStrategySelection() {
        // Arrange: Plan where one strategy should be better
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 1));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 1, 2));
        plan.tryAdd(new TrolleyItem("O003", "SKU003", 1, 5.0, 1, 3));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Best strategy is correctly identified
        PickPathResult best = comparison.getBestStrategy();
        assertNotNull("Best strategy should not be null", best);
        
        // Best should have minimum distance
        int bestDistance = best.getTotalDistance();
        int distanceA = comparison.getStrategyA().getTotalDistance();
        int distanceB = comparison.getStrategyB().getTotalDistance();
        
        assertTrue("Best distance should be <= Strategy A", bestDistance <= distanceA);
        assertTrue("Best distance should be <= Strategy B", bestDistance <= distanceB);
        assertTrue("Best distance should equal one of the strategies",
                  bestDistance == distanceA || bestDistance == distanceB);
    }

    @Test
    public void testResultCorrectness_DistanceImprovementCalculation() {
        // Arrange
        TrolleyPlan plan = new TrolleyPlan(1, 1000.0);
        plan.tryAdd(new TrolleyItem("O001", "SKU001", 1, 5.0, 1, 2));
        plan.tryAdd(new TrolleyItem("O002", "SKU002", 1, 5.0, 2, 3));
        
        // Act
        PickPathComparison comparison = service.generatePickPath(plan);
        
        // Assert: Distance improvement is calculated correctly
        int improvement = comparison.getDistanceImprovement();
        int distanceA = comparison.getStrategyA().getTotalDistance();
        int distanceB = comparison.getStrategyB().getTotalDistance();
        
        int expectedImprovement = Math.abs(distanceA - distanceB);
        assertEquals("Distance improvement should be absolute difference",
                    expectedImprovement, improvement);
        
        // Assert: Improvement percentage is valid
        double percentage = comparison.getImprovementPercentage();
        assertTrue("Improvement percentage should be >= 0", percentage >= 0.0);
        assertTrue("Improvement percentage should be <= 100", percentage <= 100.0);
    }
}

