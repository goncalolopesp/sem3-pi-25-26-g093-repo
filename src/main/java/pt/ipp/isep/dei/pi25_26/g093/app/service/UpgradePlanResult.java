package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Result of the upgrade plan calculation for USEI11.
 *
 * <p>This class represents the outcome of analyzing a directed railway
 * dependency graph. It supports two mutually exclusive scenarios:
 * <ul>
 *   <li>Acyclic graph: a valid topological ordering of all nodes</li>
 *   <li>Cyclic graph: identification of nodes and edges involved in cycles</li>
 * </ul>
 *
 * <p>The time complexity analysis of the executed algorithm is also included.
 *
 * @author Logistics on Rails Team
 */
public class UpgradePlanResult {

    private final boolean hasCycle;
    private final List<NetworkNode> topologicalOrder;
    private final Set<NetworkNode> cycleNodes;
    private final Set<NetworkEdge> cycleEdges;
    private final String complexityAnalysis;

    /**
     * Creates a result for an acyclic graph.
     *
     * @param topologicalOrder ordered list of nodes representing
     *                         a valid upgrade sequence
     * @param complexityAnalysis textual time complexity analysis
     * @throws IllegalArgumentException if topologicalOrder is null
     */
    public UpgradePlanResult(List<NetworkNode> topologicalOrder,
                             String complexityAnalysis) {

        if (topologicalOrder == null) {
            throw new IllegalArgumentException("Topological order cannot be null");
        }

        this.hasCycle = false;
        this.topologicalOrder = new ArrayList<>(topologicalOrder);
        this.cycleNodes = new HashSet<>();
        this.cycleEdges = new HashSet<>();
        this.complexityAnalysis = complexityAnalysis;
    }

    /**
     * Creates a result for a graph containing cycles.
     *
     * @param cycleNodes nodes involved in cycles
     * @param cycleEdges edges involved in cycles
     * @param complexityAnalysis textual time complexity analysis
     * @throws IllegalArgumentException if cycleNodes or cycleEdges are null
     */
    public UpgradePlanResult(Set<NetworkNode> cycleNodes,
                             Set<NetworkEdge> cycleEdges,
                             String complexityAnalysis) {

        if (cycleNodes == null || cycleEdges == null) {
            throw new IllegalArgumentException("Cycle nodes and edges cannot be null");
        }

        this.hasCycle = true;
        this.topologicalOrder = new ArrayList<>();
        this.cycleNodes = new HashSet<>(cycleNodes);
        this.cycleEdges = new HashSet<>(cycleEdges);
        this.complexityAnalysis = complexityAnalysis;
    }

    /**
     * Indicates whether the dependency graph contains cycles.
     *
     * @return {@code true} if cycles were detected; {@code false} otherwise
     */
    public boolean hasCycle() {
        return hasCycle;
    }

    /**
     * Returns the topological ordering of nodes.
     *
     * <p>This list is meaningful only if {@link #hasCycle()} returns {@code false}.
     *
     * @return ordered list of nodes, or an empty list if cycles exist
     */
    public List<NetworkNode> getTopologicalOrder() {
        return new ArrayList<>(topologicalOrder);
    }

    /**
     * Returns the nodes involved in cycles.
     *
     * <p>This set is meaningful only if {@link #hasCycle()} returns {@code true}.
     *
     * @return set of nodes involved in cycles, or an empty set if no cycles exist
     */
    public Set<NetworkNode> getCycleNodes() {
        return new HashSet<>(cycleNodes);
    }

    /**
     * Returns the edges involved in cycles.
     *
     * <p>This set is meaningful only if {@link #hasCycle()} returns {@code true}.
     *
     * @return set of edges involved in cycles, or an empty set if no cycles exist
     */
    public Set<NetworkEdge> getCycleEdges() {
        return new HashSet<>(cycleEdges);
    }

    /**
     * Returns the time complexity analysis of the executed algorithm.
     *
     * @return complexity analysis string
     */
    public String getComplexityAnalysis() {
        return complexityAnalysis;
    }
}
