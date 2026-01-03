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
 * <p>This service determines a valid upgrade order for Belgian railway stations
 * based on directed dependency relationships.
 *
 * <p>The implementation strictly follows the graph algorithms presented in the
 * ESINF lectures:
 * <ul>
 *   <li>Cycle detection using Depth-First Search (DFS) with back edge identification</li>
 *   <li>Topological sorting using Kahn’s Algorithm for directed acyclic graphs</li>
 * </ul>
 *
 * <p>All graph algorithms are reused from the provided {@code graphs} package,
 * as required by the project guidelines. No custom graph algorithms are implemented.
 *
 * <p>Expected behavior:
 * <ul>
 *   <li>If the graph is acyclic, return a topological ordering of all stations</li>
 *   <li>If cycles exist, return the stations and links involved in those cycles</li>
 * </ul>
 *
 * <p>Time Complexity:
 * <ul>
 *   <li>Cycle Detection (DFS): O(V + E)</li>
 *   <li>Topological Sort (Kahn): O(V + E)</li>
 * </ul>
 *
 * @author Logistics on Rails Team
 */
public class UpgradePlanService {

    /**
     * Calculates the upgrade plan for a railway network.
     *
     * @param nodes list of network nodes (stations)
     * @param edges list of directed dependency edges
     * @return an {@link UpgradePlanResult} containing either a topological order
     *         or cycle-related information
     */
    public UpgradePlanResult calculateUpgradePlan(List<NetworkNode> nodes,
                                                  List<NetworkEdge> edges) {

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be null or empty");
        }
        if (edges == null) {
            throw new IllegalArgumentException("Edges list cannot be null");
        }

        // Build directed graph using adjacency list representation
        Graph<NetworkNode, NetworkEdge> graph = buildGraph(nodes, edges);

        // Cycle detection using DFS with back edges (ESINF slides)
        boolean hasCycle = Algorithms.hasCycleDirected(graph);

        if (hasCycle) {
            Set<NetworkNode> cycleNodes = collectCycleNodes(graph);
            Set<NetworkEdge> cycleEdges = collectCycleEdges(graph);

            return new UpgradePlanResult(
                    cycleNodes,
                    cycleEdges,
                    generateComplexityAnalysis(
                            graph.numVertices(),
                            graph.numEdges(),
                            true
                    )
            );
        }

        // Topological sorting using Kahn’s Algorithm (ESINF slides)
        List<NetworkNode> topologicalOrder = Algorithms.topologicalSort(graph);

        // Ensure isolated vertices are included in the final ordering
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
                generateComplexityAnalysis(
                        graph.numVertices(),
                        graph.numEdges(),
                        false
                )
        );
    }

    /**
     * Builds a directed graph from the given nodes and edges.
     *
     * @param nodes list of network nodes
     * @param edges list of directed dependency edges
     * @return a directed graph representation of the railway network
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
     * detected during DFS traversal.
     *
     * @param graph the directed dependency graph
     * @return a set of nodes involved in cycles
     */
    private Set<NetworkNode> collectCycleNodes(Graph<NetworkNode, NetworkEdge> graph) {
        Set<NetworkNode> cycleNodes = new HashSet<>();

        // Back edges correspond to cycle-closing edges in DFS
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
     * Generates a textual analysis of the time and space complexity.
     *
     * @param nodeCount number of nodes (V)
     * @param edgeCount number of edges (E)
     * @param hasCycle whether cycles were detected
     * @return formatted complexity analysis
     */
    private String generateComplexityAnalysis(int nodeCount,
                                              int edgeCount,
                                              boolean hasCycle) {

        StringBuilder sb = new StringBuilder();
        sb.append("Time Complexity Analysis:\n");
        sb.append(String.format("  - Nodes (V): %d\n", nodeCount));
        sb.append(String.format("  - Edges (E): %d\n", edgeCount));

        if (hasCycle) {
            sb.append("  - Cycle Detection (DFS): O(V + E)\n");
        } else {
            sb.append("  - Topological Sort (Kahn): O(V + E)\n");
        }

        sb.append("  - Space Complexity (Adjacency List): O(V + E)\n");
        return sb.toString();
    }
}
