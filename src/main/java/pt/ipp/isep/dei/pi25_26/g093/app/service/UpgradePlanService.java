package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.MapGraph;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Algorithms;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;

import java.util.*;

/**
 * Service for calculating railway network upgrade plans (USEI11).
 *
 * <p>This service determines a valid upgrade order for railway network nodes
 * based on directed dependency relationships.
 *
 * <p>The service performs:
 * <ul>
 *   <li>Cycle detection in the dependency graph</li>
 *   <li>Topological sorting when the graph is acyclic</li>
 *   <li>Identification of nodes and edges involved in cycles</li>
 *   <li>Time complexity analysis</li>
 * </ul>
 *
 * <p>The implementation reuses algorithms provided by the {@code graphs} package:
 * <ul>
 *   <li>{@link Algorithms#hasCycleDirected(Graph)} for cycle detection</li>
 *   <li>{@link Algorithms#topologicalSort(Graph)} for upgrade ordering</li>
 *   <li>{@link Algorithms#directedBackEdges(Graph)} to identify cycle-related edges</li>
 * </ul>
 *
 * <p>Time Complexity:
 * <ul>
 *   <li>Cycle Detection: O(V + E)</li>
 *   <li>Topological Sort: O(V + E)</li>
 * </ul>
 *
 * @author Logistics on Rails Team
 */
public class UpgradePlanService {

    /**
     * Calculates the upgrade plan for a railway network.
     *
     * <p>If the dependency graph is acyclic, a topological ordering of all nodes
     * is returned, representing a valid upgrade sequence.
     *
     * <p>If cycles are detected, the method returns the set of nodes and edges
     * involved in those cycles.
     *
     * @param nodes list of network nodes
     * @param edges list of directed dependency edges
     * @return an {@link UpgradePlanResult} containing either:
     *         <ul>
     *           <li>a topological ordering of nodes, or</li>
     *           <li>cycle-related nodes and edges</li>
     *         </ul>
     * @throws IllegalArgumentException if the nodes list is null or empty,
     *         or if the edges list is null
     */
    public UpgradePlanResult calculateUpgradePlan(List<NetworkNode> nodes,
                                                  List<NetworkEdge> edges) {

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be null or empty");
        }
        if (edges == null) {
            throw new IllegalArgumentException("Edges list cannot be null");
        }

        // Build directed graph
        Graph<NetworkNode, NetworkEdge> graph = buildGraph(nodes, edges);

        // Detect cycles
        boolean hasCycle = Algorithms.hasCycleDirected(graph);

        if (hasCycle) {
            Set<NetworkNode> cycleNodes = collectCycleNodes(graph);
            Set<NetworkEdge> cycleEdges = collectCycleEdges(graph);

            return new UpgradePlanResult(
                    cycleNodes,
                    cycleEdges,
                    generateComplexityAnalysis(graph.numVertices(), graph.numEdges(), true)
            );
        }

        // Topological sort (acyclic case)
        List<NetworkNode> topologicalOrder = Algorithms.topologicalSort(graph);

        // Ensure isolated nodes are included
        if (topologicalOrder.size() < graph.numVertices()) {
            Set<NetworkNode> processed = new HashSet<>(topologicalOrder);
            for (NetworkNode node : graph.vertices()) {
                if (!processed.contains(node)) {
                    topologicalOrder.add(node);
                }
            }
        }

        return new UpgradePlanResult(
                topologicalOrder,
                generateComplexityAnalysis(graph.numVertices(), graph.numEdges(), false)
        );
    }

    /**
     * Builds a directed graph from the given nodes and edges.
     *
     * @param nodes list of network nodes
     * @param edges list of directed dependency edges
     * @return a directed graph representation of the network
     */
    private Graph<NetworkNode, NetworkEdge> buildGraph(List<NetworkNode> nodes,
                                                       List<NetworkEdge> edges) {

        Graph<NetworkNode, NetworkEdge> graph = new MapGraph<>(true);

        Map<String, NetworkNode> nodeMap = new HashMap<>();
        for (NetworkNode node : nodes) {
            nodeMap.put(node.getNodeId(), node);
            graph.addVertex(node);
        }

        for (NetworkEdge edge : edges) {
            NetworkNode fromNode = nodeMap.get(edge.getFromNodeId());
            NetworkNode toNode = nodeMap.get(edge.getToNodeId());

            if (fromNode != null && toNode != null) {
                graph.addEdge(fromNode, toNode, edge);
            }
        }

        return graph;
    }

    /**
     * Collects all network nodes involved in cycles.
     *
     * <p>The nodes are identified from the endpoints of back edges
     * detected during depth-first search.
     *
     * @param graph the directed dependency graph
     * @return a set of nodes involved in cycles
     */
    private Set<NetworkNode> collectCycleNodes(Graph<NetworkNode, NetworkEdge> graph) {
        Set<NetworkNode> cycleNodes = new HashSet<>();
        Set<Edge<NetworkNode, NetworkEdge>> backEdges =
                Algorithms.directedBackEdges(graph);

        for (Edge<NetworkNode, NetworkEdge> edge : backEdges) {
            cycleNodes.add(edge.getVOrig());
            cycleNodes.add(edge.getVDest());
        }
        return cycleNodes;
    }

    /**
     * Collects all network edges involved in cycles.
     *
     * <p>The edges correspond to back edges detected in the graph,
     * which represent dependencies that close cycles.
     *
     * @param graph the directed dependency graph
     * @return a set of edges involved in cycles
     */
    private Set<NetworkEdge> collectCycleEdges(Graph<NetworkNode, NetworkEdge> graph) {
        Set<NetworkEdge> cycleEdges = new HashSet<>();
        Set<Edge<NetworkNode, NetworkEdge>> backEdges =
                Algorithms.directedBackEdges(graph);

        for (Edge<NetworkNode, NetworkEdge> edge : backEdges) {
            if (edge.getWeight() != null) {
                cycleEdges.add(edge.getWeight());
            }
        }
        return cycleEdges;
    }

    /**
     * Generates a textual analysis of the time and space complexity
     * of the executed algorithm.
     *
     * @param nodeCount number of nodes (V)
     * @param edgeCount number of edges (E)
     * @param hasCycle whether cycles were detected
     * @return a formatted complexity analysis string
     */
    private String generateComplexityAnalysis(int nodeCount, int edgeCount, boolean hasCycle) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time Complexity Analysis:\n");
        sb.append(String.format("  - Nodes (V): %d\n", nodeCount));
        sb.append(String.format("  - Edges (E): %d\n", edgeCount));

        if (hasCycle) {
            sb.append("  - Cycle Detection: O(V + E)\n");
        } else {
            sb.append("  - Topological Sort: O(V + E)\n");
        }

        sb.append("  - Space Complexity: O(V + E)\n");
        return sb.toString();
    }
}
