package pt.ipp.isep.dei.pi25_26.g093.app.example;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.Coordinate;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PickPathSequencer;

/**
 * Manual calculation demonstration to verify the implementation
 * matches the user story example: (1,8), (2,2), (3,4)
 */
public class ManualCalculationDemo {

    public static void main(String[] args) {
        System.out.println("=== MANUAL CALCULATION DEMONSTRATION ===\n");
        
        // Create the sequencer
        PickPathSequencer sequencer = new PickPathSequencer();
        
        // Example coordinates from user story
        Coordinate coord1 = new Coordinate(1, 8);
        Coordinate coord2 = new Coordinate(2, 2);
        Coordinate coord3 = new Coordinate(3, 4);
        
        System.out.println("Example coordinates: (1,8), (2,2), (3,4)");
        System.out.println("Entry point: (0,0)\n");
        
        // Manual calculation for Strategy A (Deterministic Sweep)
        System.out.println("STRATEGY A - DETERMINISTIC SWEEP:");
        System.out.println("Order: (1,8) → (2,2) → (3,4)");
        
        // Distance from (0,0) to (1,8)
        int dist1 = sequencer.totalDistance(java.util.Arrays.asList(coord1));
        System.out.println("(0,0) → (1,8): " + dist1);
        
        // Distance from (1,8) to (2,2)
        int dist2 = sequencer.totalDistance(java.util.Arrays.asList(coord1, coord2)) - dist1;
        System.out.println("(1,8) → (2,2): " + dist2);
        
        // Distance from (2,2) to (3,4)
        int dist3 = sequencer.totalDistance(java.util.Arrays.asList(coord1, coord2, coord3)) - dist1 - dist2;
        System.out.println("(2,2) → (3,4): " + dist3);
        
        int totalA = dist1 + dist2 + dist3;
        System.out.println("Total Strategy A: " + totalA + " units");
        
        // Manual calculation for Strategy B (Nearest Neighbour)
        System.out.println("\nSTRATEGY B - NEAREST NEIGHBOUR:");
        
        // Find nearest to (0,0)
        int distTo1 = sequencer.totalDistance(java.util.Arrays.asList(coord1));
        int distTo2 = sequencer.totalDistance(java.util.Arrays.asList(coord2));
        int distTo3 = sequencer.totalDistance(java.util.Arrays.asList(coord3));
        
        System.out.println("Distances from (0,0):");
        System.out.println("  → (1,8): " + distTo1);
        System.out.println("  → (2,2): " + distTo2);
        System.out.println("  → (3,4): " + distTo3);
        
        // Nearest is (2,2), so path is (2,2) → (3,4) → (1,8)
        System.out.println("Order: (2,2) → (3,4) → (1,8)");
        
        int totalB = sequencer.totalDistance(java.util.Arrays.asList(coord2, coord3, coord1));
        System.out.println("Total Strategy B: " + totalB + " units");
        
        System.out.println("\nCOMPARISON:");
        System.out.println("Strategy A: " + totalA + " units");
        System.out.println("Strategy B: " + totalB + " units");
        System.out.println("Best: " + (totalA <= totalB ? "Strategy A" : "Strategy B"));
        System.out.println("Difference: " + Math.abs(totalA - totalB) + " units");
    }
}
