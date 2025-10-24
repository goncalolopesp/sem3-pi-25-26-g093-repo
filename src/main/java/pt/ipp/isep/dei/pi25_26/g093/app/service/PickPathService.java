package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.*;
import java.util.*;

/**
 * Service responsible for generating pick paths using both sequencing strategies.
 * Implements USEI04 - Pick Path Sequencing functionality.
 */
public class PickPathService {

    private final PickPathSequencer sequencer = new PickPathSequencer();

    /**
     * Generates pick paths for both strategies and returns a comparison.
     * Handles duplicate bay merging by summing quantities for same coordinates.
     *
     * @param plan the trolley plan containing items to pick
     * @return comparison of both strategies with the best one identified
     */
    public PickPathComparison generatePickPath(TrolleyPlan plan) {
        if (plan == null) {
            System.out.println("❌ Error: null trolley plan.");
            return null;
        }

        // Extract and merge duplicate coordinates
        List<Coordinate> uniqueCoordinates = extractAndMergeCoordinates(plan);

        if (uniqueCoordinates.isEmpty()) {
            System.out.println("⚠️  No items to pick.");
            return null;
        }

        // Strategy A — Deterministic Sweep
        List<Coordinate> strategyAPath = sequencer.strategy1Path(uniqueCoordinates);
        int strategyADistance = sequencer.totalDistance(strategyAPath);
        PickPathResult strategyAResult =
                new PickPathResult("Strategy A (Deterministic Sweep)", strategyAPath, strategyADistance);

        // Strategy B — Nearest Neighbour
        List<Coordinate> strategyBPath = sequencer.strategy2Path(uniqueCoordinates);
        int strategyBDistance = sequencer.totalDistance(strategyBPath);
        PickPathResult strategyBResult =
                new PickPathResult("Strategy B (Nearest Neighbour)", strategyBPath, strategyBDistance);

        // Return comparison
        return new PickPathComparison(strategyAResult, strategyBResult);
    }

    /**
     * Extracts coordinates from trolley items and merges duplicates (summing quantities).
     *
     * @param plan the trolley plan
     * @return list of unique coordinates
     */
    private List<Coordinate> extractAndMergeCoordinates(TrolleyPlan plan) {
        Map<Coordinate, Integer> coordinateQuantities = new LinkedHashMap<>(); // preserve order

        for (TrolleyItem item : plan.getItems()) {
            Coordinate coord = new Coordinate(item.getAisle(), item.getBay());
            coordinateQuantities.merge(coord, item.getQty(), Integer::sum);
        }

        return new ArrayList<>(coordinateQuantities.keySet());
    }

    /**
     * Generates a formatted report comparing both strategies.
     *
     * @param plan the trolley plan
     * @return formatted string with detailed results
     */
    public String generateDetailedReport(TrolleyPlan plan) {
        PickPathComparison comparison = generatePickPath(plan);
        if (comparison == null) {
            return "Unable to generate picking path report (no data).";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== PICK PATH SEQUENCING REPORT ===\n\n");
        report.append("Trolley Plan:\n").append(plan).append("\n");
        report.append("Unique pick locations: ").append(comparison.getStrategyA().getPath().size()).append("\n\n");

        // Strategy A
        report.append("STRATEGY A - DETERMINISTIC SWEEP\n");
        report.append("Total distance: ").append(comparison.getStrategyA().getTotalDistance()).append(" units\n");
        report.append("Path: ").append(comparison.getStrategyA().getPath()).append("\n\n");

        // Strategy B
        report.append("STRATEGY B - NEAREST NEIGHBOUR\n");
        report.append("Total distance: ").append(comparison.getStrategyB().getTotalDistance()).append(" units\n");
        report.append("Path: ").append(comparison.getStrategyB().getPath()).append("\n\n");

        // Comparison summary
        report.append("COMPARISON\n");
        report.append("Best strategy: ").append(comparison.getBestStrategy().getStrategyName()).append("\n");
        report.append("Distance improvement: ").append(comparison.getDistanceImprovement())
                .append(" units (")
                .append(String.format("%.1f", comparison.getImprovementPercentage()))
                .append("%)\n");

        return report.toString();
    }

    /**
     * Returns the best strategy result from the comparison.
     *
     * @param plan the trolley plan
     * @return the best PickPathResult, or null if no plan available
     */
    public PickPathResult getBestPickPath(TrolleyPlan plan) {
        PickPathComparison comparison = generatePickPath(plan);
        return (comparison != null) ? comparison.getBestStrategy() : null;
    }
}
