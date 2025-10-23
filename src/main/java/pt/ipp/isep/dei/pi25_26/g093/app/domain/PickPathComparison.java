package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.List;

/**
 * Represents the comparison between both pick path sequencing strategies.
 * Contains results for both Strategy A (Deterministic Sweep) and Strategy B (Nearest Neighbour).
 */
public class PickPathComparison {
    private final PickPathResult strategyA;
    private final PickPathResult strategyB;
    private final PickPathResult bestStrategy;

    public PickPathComparison(PickPathResult strategyA, PickPathResult strategyB) {
        this.strategyA = strategyA;
        this.strategyB = strategyB;
        this.bestStrategy = (strategyA.getTotalDistance() <= strategyB.getTotalDistance()) ? strategyA : strategyB;
    }

    public PickPathResult getStrategyA() {
        return strategyA;
    }

    public PickPathResult getStrategyB() {
        return strategyB;
    }

    public PickPathResult getBestStrategy() {
        return bestStrategy;
    }

    public int getDistanceImprovement() {
        return Math.abs(strategyA.getTotalDistance() - strategyB.getTotalDistance());
    }

    public double getImprovementPercentage() {
        int maxDistance = Math.max(strategyA.getTotalDistance(), strategyB.getTotalDistance());
        if (maxDistance == 0) return 0.0;
        return (getDistanceImprovement() / (double) maxDistance) * 100.0;
    }

    @Override
    public String toString() {
        return String.format("Pick Path Comparison:\n" +
                "Strategy A (Deterministic Sweep): %s\n" +
                "Strategy B (Nearest Neighbour): %s\n" +
                "Best Strategy: %s\n" +
                "Distance Improvement: %d units (%.1f%%)",
                strategyA, strategyB, bestStrategy, 
                getDistanceImprovement(), getImprovementPercentage());
    }
}
