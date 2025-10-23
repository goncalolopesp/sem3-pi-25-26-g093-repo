package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import java.util.*;

/**
 * Service responsible for generating pick paths using both sequencing strategies.
 * Implements USEI04 - Pick Path Sequencing functionality.
 */
public class PickPathService {

    private PickPathSequencer sequencer = new PickPathSequencer();

    /**
     * Generates pick paths for both strategies and returns a comparison.
     * Handles duplicate bay merging by summing quantities for same coordinates.
     * 
     * @param plan the trolley plan containing items to pick
     * @return comparison of both strategies with the best one identified
     */
    public PickPathComparison generatePickPath(TrolleyPlan plan) {
        if (plan == null) {
            System.out.println("Error: null plan.");
            return null;
        }

        // Extract and merge duplicate coordinates
        List<Coordinate> uniqueCoordinates = extractAndMergeCoordinates(plan);
        
        if (uniqueCoordinates.isEmpty()) {
            System.out.println("No items to pick.");
            return null;
        }

        // Execute Strategy A - Deterministic Sweep
        List<Coordinate> strategyAPath = sequencer.strategy1Path(uniqueCoordinates);
        int strategyADistance = sequencer.totalDistance(strategyAPath);
        PickPathResult strategyAResult = new PickPathResult("Strategy A (Deterministic Sweep)", 
                strategyAPath, strategyADistance);

        // Execute Strategy B - Nearest Neighbour
        List<Coordinate> strategyBPath = sequencer.strategy2Path(uniqueCoordinates);
        int strategyBDistance = sequencer.totalDistance(strategyBPath);
        PickPathResult strategyBResult = new PickPathResult("Strategy B (Nearest Neighbour)", 
                strategyBPath, strategyBDistance);

        // Create comparison and return
        return new PickPathComparison(strategyAResult, strategyBResult);
    }

    /**
     * Extracts coordinates from trolley items and merges duplicates.
     * For duplicate coordinates, quantities are summed according to the requirements.
     * 
     * @param plan the trolley plan
     * @return list of unique coordinates
     */
    private List<Coordinate> extractAndMergeCoordinates(TrolleyPlan plan) {
        // Use a map to merge duplicate coordinates and sum quantities
        Map<Coordinate, Integer> coordinateQuantities = new HashMap<>();
        
        for (TrolleyItem item : plan.getItems()) {
            Coordinate coord = new Coordinate(item.getAisle(), item.getBay());
            coordinateQuantities.merge(coord, item.getQty(), Integer::sum);
        }
        
        // Convert to list of coordinates (quantities are merged, but we only need coordinates for path)
        return new ArrayList<>(coordinateQuantities.keySet());
    }

    /**
     * Generates a detailed report of the pick path comparison.
     * 
     * @param plan the trolley plan
     * @return formatted string with detailed results
     */
    public String generateDetailedReport(TrolleyPlan plan) {
        PickPathComparison comparison = generatePickPath(plan);
        if (comparison == null) {
            return "Unable to generate picking plan.";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== PICK PATH SEQUENCING REPORT ===\n\n");
        
        // Basic info
        report.append("Trolley Plan: ").append(plan.toString()).append("\n");
        report.append("Number of unique coordinates: ").append(comparison.getStrategyA().getPath().size()).append("\n\n");
        
        // Strategy A details
        report.append("STRATEGY A - DETERMINISTIC SWEEP:\n");
        report.append("Total distance: ").append(comparison.getStrategyA().getTotalDistance()).append(" units\n");
        report.append("Path: ").append(comparison.getStrategyA().getPath()).append("\n\n");
        
        // Strategy B details
        report.append("STRATEGY B - NEAREST NEIGHBOUR:\n");
        report.append("Total distance: ").append(comparison.getStrategyB().getTotalDistance()).append(" units\n");
        report.append("Path: ").append(comparison.getStrategyB().getPath()).append("\n\n");
        
        // Comparison
        report.append("COMPARISON:\n");
        report.append("Best strategy: ").append(comparison.getBestStrategy().getStrategyName()).append("\n");
        report.append("Distance improvement: ").append(comparison.getDistanceImprovement()).append(" units (");
        report.append(String.format("%.1f", comparison.getImprovementPercentage())).append("%)\n");
        
        return report.toString();
    }

    /**
     * Simple method to get just the best strategy result.
     * 
     * @param plan the trolley plan
     * @return the best pick path result
     */
    public PickPathResult getBestPickPath(TrolleyPlan plan) {
        PickPathComparison comparison = generatePickPath(plan);
        return comparison != null ? comparison.getBestStrategy() : null;
    }
}
