package pt.ipp.isep.dei.pi25_26.g093.app.test;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickPathService;

/**
 * Test class for PickPathService functionality.
 * Demonstrates the USEI04 implementation with the example from the user story.
 */
public class PickPathServiceTest {

    public static void main(String[] args) {
        System.out.println("=== PICK PATH SEQUENCING SERVICE TEST ===\n");
        
        // Test with the example from the user story
        testExampleFromUserStory();
        
        // Test with duplicate coordinates
        testDuplicateCoordinates();
        
        // Test with empty plan
        testEmptyPlan();
    }
    
    /**
     * Tests the example from the user story: (1,8), (2,2), (3,4)
     * Expected results:
     * Strategy A: (1,8) → (2,2) → (3,4) = 33 units
     * Strategy B: (2,2) → (3,4) → (1,8) = 35 units
     */
    public static void testExampleFromUserStory() {
        System.out.println("--- TEST: User Story Example ---");
        
        TrolleyPlan plan = new TrolleyPlan(1, 100.0);
        plan.tryAdd(new TrolleyItem("ORD001", "SKU001", 1, 2.0, 1, 8));  // (1,8)
        plan.tryAdd(new TrolleyItem("ORD002", "SKU002", 1, 2.0, 2, 2));  // (2,2)
        plan.tryAdd(new TrolleyItem("ORD003", "SKU003", 1, 2.0, 3, 4));  // (3,4)
        
        PickPathService service = new PickPathService();
        PickPathComparison comparison = service.generatePickPath(plan);
        
        if (comparison != null) {
            System.out.println("Strategy A (Deterministic Sweep):");
            System.out.println("  Distance: " + comparison.getStrategyA().getTotalDistance());
            System.out.println("  Path: " + comparison.getStrategyA().getPath());
            
            System.out.println("\nStrategy B (Nearest Neighbour):");
            System.out.println("  Distance: " + comparison.getStrategyB().getTotalDistance());
            System.out.println("  Path: " + comparison.getStrategyB().getPath());
            
            System.out.println("\nBest strategy: " + comparison.getBestStrategy().getStrategyName());
            System.out.println("Improvement: " + comparison.getDistanceImprovement() + " units");
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    /**
     * Tests duplicate coordinate merging functionality.
     */
    public static void testDuplicateCoordinates() {
        System.out.println("--- TEST: Duplicate Coordinates ---");
        
        TrolleyPlan plan = new TrolleyPlan(1, 100.0);
        // Add multiple items to the same coordinate (1,8)
        plan.tryAdd(new TrolleyItem("ORD001", "SKU001", 2, 4.0, 1, 8));
        plan.tryAdd(new TrolleyItem("ORD002", "SKU002", 1, 2.0, 1, 8));  // Same coordinate
        plan.tryAdd(new TrolleyItem("ORD003", "SKU003", 1, 2.0, 2, 2));
        
        PickPathService service = new PickPathService();
        PickPathComparison comparison = service.generatePickPath(plan);
        
        if (comparison != null) {
            System.out.println("Unique coordinates found: " + comparison.getStrategyA().getPath().size());
            System.out.println("Strategy A path: " + comparison.getStrategyA().getPath());
            System.out.println("Strategy B path: " + comparison.getStrategyB().getPath());
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
    
    /**
     * Tests empty plan handling.
     */
    public static void testEmptyPlan() {
        System.out.println("--- TEST: Empty Plan ---");
        
        TrolleyPlan emptyPlan = new TrolleyPlan(1, 100.0);
        PickPathService service = new PickPathService();
        PickPathComparison comparison = service.generatePickPath(emptyPlan);
        
        if (comparison == null) {
            System.out.println("✓ Empty plan handled correctly");
        } else {
            System.out.println("✗ Empty plan not handled correctly");
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
    }
}
