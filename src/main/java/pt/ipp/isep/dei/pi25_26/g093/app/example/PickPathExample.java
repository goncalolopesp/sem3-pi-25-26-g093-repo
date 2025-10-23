package pt.ipp.isep.dei.pi25_26.g093.app.example;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import pt.ipp.isep.dei.pi25_26.g093.app.service.PickPathService;

/**
 * Example demonstrating the Pick Path Sequencing functionality (USEI04).
 * This example uses the coordinates from the user story: (1,8), (2,2), (3,4)
 */
public class PickPathExample {

    public static void main(String[] args) {
        System.out.println("=== PICK PATH SEQUENCING EXAMPLE (USEI04) ===\n");
        
        // Create a trolley plan with the example coordinates from the user story
        TrolleyPlan plan = createExamplePlan();
        
        // Create the pick path service
        PickPathService service = new PickPathService();
        
        // Generate and display the comparison
        System.out.println(service.generateDetailedReport(plan));
        
        // Demonstrate individual strategy access
        System.out.println("\n=== INDIVIDUAL DETAILS ===");
        PickPathComparison comparison = service.generatePickPath(plan);
        if (comparison != null) {
            System.out.println("Strategy A Result:");
            System.out.println(comparison.getStrategyA());
            System.out.println("\nStrategy B Result:");
            System.out.println(comparison.getStrategyB());
            System.out.println("\nRecommended best strategy:");
            System.out.println(comparison.getBestStrategy());
        }
    }
    
    /**
     * Creates an example trolley plan with the coordinates from the user story.
     * Uses the example: (1,8), (2,2), (3,4)
     */
    private static TrolleyPlan createExamplePlan() {
        TrolleyPlan plan = new TrolleyPlan(1, 100.0); // Trolley #1 with 100kg capacity
        
        // Add items with the example coordinates
        // Note: In real scenario, these would come from USEI03 picking plan
        plan.tryAdd(new TrolleyItem("ORD001", "SKU001", 2, 4.0, 1, 8));  // (1,8)
        plan.tryAdd(new TrolleyItem("ORD002", "SKU002", 1, 2.0, 2, 2));  // (2,2)
        plan.tryAdd(new TrolleyItem("ORD003", "SKU003", 3, 6.0, 3, 4));  // (3,4)
        
        // Add a duplicate coordinate to test merging functionality
        plan.tryAdd(new TrolleyItem("ORD004", "SKU004", 1, 2.0, 1, 8));  // Another (1,8)
        
        return plan;
    }
}
