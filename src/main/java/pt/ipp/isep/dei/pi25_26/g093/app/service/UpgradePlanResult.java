package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of the upgrade plan calculation.
 * 
 * <p>Contains either:
 * <ul>
 *   <li>A valid topological ordering of nodes (if no cycles exist)</li>
 *   <li>Information about detected cycles (if cycles exist)</li>
 * </ul>
 * 
 * @author Logistics on Rails Team
 */
public class UpgradePlanResult {
    
    private final boolean hasCycle;
    private final List<NetworkNode> topologicalOrder;
    private final List<NetworkNode> cycleNodes;
    private final List<NetworkEdge> cycleEdges;
    private final String complexityAnalysis;
    
    /**
     * Creates a result for a valid topological ordering (no cycles).
     * 
     * @param topologicalOrder ordered list of nodes for upgrade
     * @param complexityAnalysis time complexity analysis
     */
    public UpgradePlanResult(List<NetworkNode> topologicalOrder, String complexityAnalysis) {
        this.hasCycle = false;
        this.topologicalOrder = new ArrayList<>(topologicalOrder);
        this.cycleNodes = new ArrayList<>();
        this.cycleEdges = new ArrayList<>();
        this.complexityAnalysis = complexityAnalysis;
    }
    
    /**
     * Creates a result for a graph with cycles.
     * 
     * @param cycleNodes nodes involved in the cycle
     * @param cycleEdges edges involved in the cycle
     * @param complexityAnalysis time complexity analysis
     */
    public UpgradePlanResult(List<NetworkNode> cycleNodes, 
                            List<NetworkEdge> cycleEdges,
                            String complexityAnalysis) {
        this.hasCycle = true;
        this.topologicalOrder = new ArrayList<>();
        this.cycleNodes = new ArrayList<>(cycleNodes);
        this.cycleEdges = new ArrayList<>(cycleEdges);
        this.complexityAnalysis = complexityAnalysis;
    }
    
    /**
     * Returns whether the graph contains cycles.
     * 
     * @return true if cycles were detected, false otherwise
     */
    public boolean hasCycle() {
        return hasCycle;
    }
    
    /**
     * Returns the topological ordering of nodes (valid only if no cycles).
     * 
     * @return ordered list of nodes, or empty list if cycles exist
     */
    public List<NetworkNode> getTopologicalOrder() {
        return new ArrayList<>(topologicalOrder);
    }
    
    /**
     * Returns the nodes involved in a cycle (valid only if cycles exist).
     * 
     * @return list of nodes in the cycle, or empty list if no cycles
     */
    public List<NetworkNode> getCycleNodes() {
        return new ArrayList<>(cycleNodes);
    }
    
    /**
     * Returns the edges involved in a cycle (valid only if cycles exist).
     * 
     * @return list of edges in the cycle, or empty list if no cycles
     */
    public List<NetworkEdge> getCycleEdges() {
        return new ArrayList<>(cycleEdges);
    }
    
    /**
     * Returns the time complexity analysis.
     * 
     * @return complexity analysis string
     */
    public String getComplexityAnalysis() {
        return complexityAnalysis;
    }
}


