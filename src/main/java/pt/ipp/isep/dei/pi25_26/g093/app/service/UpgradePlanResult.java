package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Encapsulates the result of the Directed Line Upgrade Plan computation (USEI11).
 *
 * <p>This class represents one of two mutually exclusive outcomes:
 * <ul>
 *   <li><b>Acyclic graph</b>: a valid topological ordering of all stations</li>
 *   <li><b>Cyclic graph</b>: the set of stations and links involved in cycles</li>
 * </ul>
 *
 * <p>This structure directly reflects the expected returns defined in the
 * USEI11 user story.
 *
 * <p>The class is immutable after construction.
 */
public class UpgradePlanResult {

    private final List<NetworkNode> topologicalOrder;
    private final Set<NetworkNode> cycleNodes;
    private final Set<NetworkEdge> cycleEdges;
    private final String complexityAnalysis;

    /**
     * Constructs a result for an acyclic graph.
     *
     * <p>This constructor must be used when a valid topological ordering
     * exists for the dependency graph.
     *
     * @param topologicalOrder ordered list of stations
     * @param complexityAnalysis textual complexity analysis
     */
    public UpgradePlanResult(List<NetworkNode> topologicalOrder,
                             String complexityAnalysis) {

        this.topologicalOrder = topologicalOrder;
        this.cycleNodes = Collections.emptySet();
        this.cycleEdges = Collections.emptySet();
        this.complexityAnalysis = complexityAnalysis;
    }

    /**
     * Constructs a result for a graph containing cycles.
     *
     * <p>This constructor must be used when no complete topological
     * ordering exists due to directed cycles.
     *
     * @param cycleNodes stations involved in cycles
     * @param cycleEdges links involved in cycles
     * @param complexityAnalysis textual complexity analysis
     */
    public UpgradePlanResult(Set<NetworkNode> cycleNodes,
                             Set<NetworkEdge> cycleEdges,
                             String complexityAnalysis) {

        this.topologicalOrder = Collections.emptyList();
        this.cycleNodes = cycleNodes;
        this.cycleEdges = cycleEdges;
        this.complexityAnalysis = complexityAnalysis;
    }

    /**
     * Indicates whether the dependency graph contains cycles.
     *
     * @return true if cycles were detected, false otherwise
     */
    public boolean hasCycle() {
        return !cycleNodes.isEmpty();
    }

    /**
     * Returns the topological ordering of stations.
     *
     * <p>This list is non-empty only if {@link #hasCycle()} returns false.
     *
     * @return list of stations in upgrade order
     */
    public List<NetworkNode> getTopologicalOrder() {
        return topologicalOrder;
    }

    /**
     * Returns the stations involved in cycles.
     *
     * <p>This set is non-empty only if {@link #hasCycle()} returns true.
     *
     * @return set of stations involved in cycles
     */
    public Set<NetworkNode> getCycleNodes() {
        return cycleNodes;
    }

    /**
     * Returns the directed links involved in cycles.
     *
     * @return set of dependency edges that close cycles
     */
    public Set<NetworkEdge> getCycleEdges() {
        return cycleEdges;
    }

    /**
     * Returns the complexity analysis of the executed algorithm.
     *
     * @return textual complexity analysis
     */
    public String getComplexityAnalysis() {
        return complexityAnalysis;
    }
}
