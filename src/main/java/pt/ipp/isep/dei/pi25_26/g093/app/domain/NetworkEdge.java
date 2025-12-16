package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

/**
 * Represents a directed edge in the railway network graph.
 * An edge represents a connection between two nodes with dependency constraints.
 * 
 * <p>Each edge has:
 * <ul>
 *   <li>Source and destination nodes (directed dependency)</li>
 *   <li>Distance in kilometers</li>
 *   <li>Capacity (maximum number of trains per day)</li>
 *   <li>Cost (can be negative, representing potential savings or upgrades)</li>
 * </ul>
 * 
 * <p>The direction A → B means that node A must be upgraded before node B.
 * 
 * @author Logistics on Rails Team
 */
public class NetworkEdge {
    
    private final String fromNodeId;
    private final String toNodeId;
    private final double distanceKm;
    private final int capacity;
    private final double cost;
    
    /**
     * Constructs a new NetworkEdge.
     * 
     * @param fromNodeId source node identifier (must be upgraded first)
     * @param toNodeId destination node identifier (depends on source)
     * @param distanceKm distance between nodes in kilometers
     * @param capacity maximum number of trains per day
     * @param cost edge cost (can be negative for savings/upgrades)
     */
    public NetworkEdge(String fromNodeId, String toNodeId,
                      double distanceKm, int capacity, double cost) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.distanceKm = distanceKm;
        this.capacity = capacity;
        this.cost = cost;
    }
    
    public String getFromNodeId() {
        return fromNodeId;
    }
    
    public String getToNodeId() {
        return toNodeId;
    }
    
    public double getDistanceKm() {
        return distanceKm;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public double getCost() {
        return cost;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NetworkEdge that = (NetworkEdge) obj;
        return Objects.equals(fromNodeId, that.fromNodeId) &&
               Objects.equals(toNodeId, that.toNodeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fromNodeId, toNodeId);
    }
    
    @Override
    public String toString() {
        return String.format("NetworkEdge{%s -> %s, dist=%.2fkm, cap=%d, cost=%.2f}",
                fromNodeId, toNodeId, distanceKm, capacity, cost);
    }
}


