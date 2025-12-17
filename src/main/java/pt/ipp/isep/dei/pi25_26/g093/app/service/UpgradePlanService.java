package pt.ipp.isep.dei.pi25_26.g093.app.service;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;

import java.util.*;

/**
 * Service for calculating railway network upgrade plans (USEI11).
 * 
 * <p>This service implements:
 * <ul>
 *   <li>Topological sorting to determine upgrade order</li>
 *   <li>Cycle detection using DFS with back edge detection</li>
 *   <li>Time complexity analysis</li>
 * </ul>
 * 
 * <p>Time Complexity:
 * <ul>
 *   <li>Topological Sort: O(V + E) where V = nodes, E = edges</li>
 *   <li>Cycle Detection: O(V + E) using DFS</li>
 * </ul>
 * 
 * @author Logistics on Rails Team
 */
public class UpgradePlanService {
    
    /**
     * Calculates the upgrade plan for the given network.
     * 
     * <p>If the graph is acyclic, returns a valid topological ordering.
     * If cycles are detected, returns information about the cycles.
     * 
     * @param nodes list of network nodes
     * @param edges list of directed edges (dependencies)
     * @return UpgradePlanResult containing either topological order or cycle information
     * @throws IllegalArgumentException if nodes or edges are null or empty
     */
    public UpgradePlanResult calculateUpgradePlan(List<NetworkNode> nodes, 
                                                   List<NetworkEdge> edges) {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be null or empty");
        }
        if (edges == null) {
            throw new IllegalArgumentException("Edges list cannot be null");
        }
        
        // Build adjacency list and node map
        Map<String, NetworkNode> nodeMap = buildNodeMap(nodes);
        Map<String, List<NetworkEdge>> adjacencyList = buildAdjacencyList(edges);
        
        // Detect cycles first
        CycleDetectionResult cycleResult = detectCycles(nodeMap, adjacencyList);
        
        if (cycleResult.hasCycle()) {
            return new UpgradePlanResult(
                cycleResult.getCycleNodes(),
                cycleResult.getCycleEdges(),
                generateComplexityAnalysis(nodes.size(), edges.size(), true)
            );
        }
        
        // Perform topological sort
        List<NetworkNode> topologicalOrder = topologicalSort(nodeMap, adjacencyList);
        
        return new UpgradePlanResult(
            topologicalOrder,
            generateComplexityAnalysis(nodes.size(), edges.size(), false)
        );
    }
    
    /**
     * Builds a map from node ID to NetworkNode for O(1) lookup.
     * 
     * @param nodes list of nodes
     * @return map of node ID to NetworkNode
     */
    private Map<String, NetworkNode> buildNodeMap(List<NetworkNode> nodes) {
        Map<String, NetworkNode> nodeMap = new HashMap<>();
        for (NetworkNode node : nodes) {
            nodeMap.put(node.getNodeId(), node);
        }
        return nodeMap;
    }
    
    /**
     * Builds an adjacency list representation of the graph.
     * 
     * @param edges list of edges
     * @return map from node ID to list of outgoing edges
     */
    private Map<String, List<NetworkEdge>> buildAdjacencyList(List<NetworkEdge> edges) {
        Map<String, List<NetworkEdge>> adjacencyList = new HashMap<>();
        
        for (NetworkEdge edge : edges) {
            adjacencyList.computeIfAbsent(edge.getFromNodeId(), k -> new ArrayList<>())
                         .add(edge);
        }
        
        return adjacencyList;
    }
    
    /**
     * Detects cycles in the directed graph using DFS with back edge detection.
     * 
     * <p>Algorithm: Modified DFS that tracks:
     * <ul>
     *   <li>WHITE: unvisited node</li>
     *   <li>GRAY: node currently in DFS recursion stack (back edge found)</li>
     *   <li>BLACK: node completely processed</li>
     * </ul>
     * 
     * <p>Time Complexity: O(V + E)
     * 
     * @param nodeMap map of all nodes
     * @param adjacencyList adjacency list representation
     * @return CycleDetectionResult containing cycle information if found
     */
    private CycleDetectionResult detectCycles(Map<String, NetworkNode> nodeMap,
                                              Map<String, List<NetworkEdge>> adjacencyList) {
        
        Map<String, NodeColor> color = new HashMap<>();
        Map<String, NetworkEdge> parentEdge = new HashMap<>();
        List<NetworkNode> cycleNodes = new ArrayList<>();
        List<NetworkEdge> cycleEdges = new ArrayList<>();
        
        // Initialize all nodes as WHITE
        for (String nodeId : nodeMap.keySet()) {
            color.put(nodeId, NodeColor.WHITE);
        }
        
        // Perform DFS from each unvisited node
        for (String nodeId : nodeMap.keySet()) {
            if (color.get(nodeId) == NodeColor.WHITE) {
                if (dfsCycleDetection(nodeId, nodeMap, adjacencyList, 
                                     color, parentEdge, cycleNodes, cycleEdges)) {
                    return new CycleDetectionResult(true, cycleNodes, cycleEdges);
                }
            }
        }
        
        return new CycleDetectionResult(false, new ArrayList<>(), new ArrayList<>());
    }
    
    /**
     * Recursive DFS helper for cycle detection.
     * 
     * @param currentNodeId current node being visited
     * @param nodeMap map of all nodes
     * @param adjacencyList adjacency list
     * @param color color map (WHITE/GRAY/BLACK)
     * @param parentEdge map tracking parent edge for each node
     * @param cycleNodes output list for cycle nodes
     * @param cycleEdges output list for cycle edges
     * @return true if cycle detected, false otherwise
     */
    private boolean dfsCycleDetection(String currentNodeId,
                                     Map<String, NetworkNode> nodeMap,
                                     Map<String, List<NetworkEdge>> adjacencyList,
                                     Map<String, NodeColor> color,
                                     Map<String, NetworkEdge> parentEdge,
                                     List<NetworkNode> cycleNodes,
                                     List<NetworkEdge> cycleEdges) {
        
        // Mark current node as GRAY (in recursion stack)
        color.put(currentNodeId, NodeColor.GRAY);
        
        // Check all outgoing edges
        List<NetworkEdge> outgoingEdges = adjacencyList.getOrDefault(currentNodeId, new ArrayList<>());
        
        for (NetworkEdge edge : outgoingEdges) {
            String neighborId = edge.getToNodeId();
            
            // If neighbor is GRAY, we found a back edge (cycle)
            if (color.get(neighborId) == NodeColor.GRAY) {
                // Reconstruct cycle
                reconstructCycle(currentNodeId, neighborId, nodeMap, 
                               parentEdge, cycleNodes, cycleEdges, edge);
                return true;
            }
            
            // If neighbor is WHITE, continue DFS
            if (color.get(neighborId) == NodeColor.WHITE) {
                parentEdge.put(neighborId, edge);
                if (dfsCycleDetection(neighborId, nodeMap, adjacencyList, 
                                     color, parentEdge, cycleNodes, cycleEdges)) {
                    return true;
                }
            }
        }
        
        // Mark current node as BLACK (completely processed)
        color.put(currentNodeId, NodeColor.BLACK);
        return false;
    }
    
    /**
     * Reconstructs the cycle path from the detected back edge.
     * 
     * @param fromNodeId node where back edge starts
     * @param toNodeId node where back edge ends (cycle start)
     * @param nodeMap map of all nodes
     * @param parentEdge map tracking parent edges
     * @param cycleNodes output list for cycle nodes
     * @param cycleEdges output list for cycle edges
     * @param backEdge the back edge that closes the cycle
     */
    private void reconstructCycle(String fromNodeId, String toNodeId,
                                 Map<String, NetworkNode> nodeMap,
                                 Map<String, NetworkEdge> parentEdge,
                                 List<NetworkNode> cycleNodes,
                                 List<NetworkEdge> cycleEdges,
                                 NetworkEdge backEdge) {
        
        // Build path from toNodeId to fromNodeId using parent edges
        List<String> path = new ArrayList<>();
        String current = fromNodeId;
        Set<String> visited = new HashSet<>();
        
        // Trace back from fromNodeId to toNodeId
        while (current != null && !current.equals(toNodeId) && !visited.contains(current)) {
            visited.add(current);
            path.add(current);
            NetworkEdge edge = parentEdge.get(current);
            
            if (edge != null) {
                current = edge.getFromNodeId();
            } else {
                break;
            }
        }
        
        // Build cycle: path nodes + fromNodeId + toNodeId (via back edge)
        // Add nodes in reverse order (from toNodeId to fromNodeId)
        java.util.Collections.reverse(path);
        
        // Add toNodeId at the start
        cycleNodes.add(nodeMap.get(toNodeId));
        
        // Add path nodes
        for (String nodeId : path) {
            cycleNodes.add(nodeMap.get(nodeId));
        }
        
        // Add fromNodeId
        if (!fromNodeId.equals(toNodeId)) {
            cycleNodes.add(nodeMap.get(fromNodeId));
        }
        
        // Add edges: parent edges in order, then back edge
        for (int i = 0; i < path.size(); i++) {
            String nodeId = path.get(i);
            NetworkEdge edge = parentEdge.get(nodeId);
            if (edge != null) {
                cycleEdges.add(edge);
            }
        }
        
        // Add back edge to close the cycle
        cycleEdges.add(backEdge);
    }
    
    /**
     * Performs topological sort using Kahn's algorithm (BFS-based).
     * 
     * <p>Algorithm:
     * <ol>
     *   <li>Calculate in-degree for each node</li>
     *   <li>Add all nodes with in-degree 0 to queue</li>
     *   <li>Process queue: remove node, add to result, decrement in-degree of neighbors</li>
     *   <li>Add neighbors with in-degree 0 to queue</li>
     * </ol>
     * 
     * <p>Time Complexity: O(V + E)
     * 
     * @param nodeMap map of all nodes
     * @param adjacencyList adjacency list representation
     * @return topologically sorted list of nodes
     */
    private List<NetworkNode> topologicalSort(Map<String, NetworkNode> nodeMap,
                                             Map<String, List<NetworkEdge>> adjacencyList) {
        
        // Calculate in-degrees
        Map<String, Integer> inDegree = new HashMap<>();
        
        // Initialize all in-degrees to 0
        for (String nodeId : nodeMap.keySet()) {
            inDegree.put(nodeId, 0);
        }
        
        // Count in-degrees
        for (List<NetworkEdge> edges : adjacencyList.values()) {
            for (NetworkEdge edge : edges) {
                String toNodeId = edge.getToNodeId();
                inDegree.put(toNodeId, inDegree.get(toNodeId) + 1);
            }
        }
        
        // Queue for nodes with in-degree 0
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        List<NetworkNode> result = new ArrayList<>();
        
        // Process queue
        while (!queue.isEmpty()) {
            String currentNodeId = queue.poll();
            result.add(nodeMap.get(currentNodeId));
            
            // Process outgoing edges
            List<NetworkEdge> outgoingEdges = adjacencyList.getOrDefault(currentNodeId, new ArrayList<>());
            for (NetworkEdge edge : outgoingEdges) {
                String neighborId = edge.getToNodeId();
                int newInDegree = inDegree.get(neighborId) - 1;
                inDegree.put(neighborId, newInDegree);
                
                if (newInDegree == 0) {
                    queue.offer(neighborId);
                }
            }
        }
        
        // If result size != node count, there might be isolated nodes
        // Add any remaining nodes (shouldn't happen in acyclic graph, but handle gracefully)
        if (result.size() < nodeMap.size()) {
            Set<String> processedIds = new HashSet<>();
            for (NetworkNode node : result) {
                processedIds.add(node.getNodeId());
            }
            
            for (String nodeId : nodeMap.keySet()) {
                if (!processedIds.contains(nodeId)) {
                    result.add(nodeMap.get(nodeId));
                }
            }
        }
        
        return result;
    }
    
    /**
     * Generates a time complexity analysis string.
     * 
     * @param nodeCount number of nodes (V)
     * @param edgeCount number of edges (E)
     * @param hasCycle whether cycles were detected
     * @return complexity analysis string
     */
    private String generateComplexityAnalysis(int nodeCount, int edgeCount, boolean hasCycle) {
        StringBuilder sb = new StringBuilder();
        sb.append("Time Complexity Analysis:\n");
        sb.append(String.format("  - Nodes (V): %d\n", nodeCount));
        sb.append(String.format("  - Edges (E): %d\n", edgeCount));
        
        if (hasCycle) {
            sb.append("  - Cycle Detection: O(V + E) = O(")
              .append(nodeCount).append(" + ").append(edgeCount)
              .append(") = O(").append(nodeCount + edgeCount).append(")\n");
        } else {
            sb.append("  - Topological Sort: O(V + E) = O(")
              .append(nodeCount).append(" + ").append(edgeCount)
              .append(") = O(").append(nodeCount + edgeCount).append(")\n");
        }
        
        sb.append("  - Space Complexity: O(V + E) for adjacency list\n");
        
        return sb.toString();
    }
    
    /**
     * Enum for node colors in DFS cycle detection.
     */
    private enum NodeColor {
        WHITE,  // Unvisited
        GRAY,   // Currently in recursion stack
        BLACK   // Completely processed
    }
    
    /**
     * Internal class to hold cycle detection results.
     */
    private static class CycleDetectionResult {
        private final boolean hasCycle;
        private final List<NetworkNode> cycleNodes;
        private final List<NetworkEdge> cycleEdges;
        
        public CycleDetectionResult(boolean hasCycle, 
                                   List<NetworkNode> cycleNodes,
                                   List<NetworkEdge> cycleEdges) {
            this.hasCycle = hasCycle;
            this.cycleNodes = new ArrayList<>(cycleNodes);
            this.cycleEdges = new ArrayList<>(cycleEdges);
        }
        
        public boolean hasCycle() {
            return hasCycle;
        }
        
        public List<NetworkNode> getCycleNodes() {
            return new ArrayList<>(cycleNodes);
        }
        
        public List<NetworkEdge> getCycleEdges() {
            return new ArrayList<>(cycleEdges);
        }
    }
}

