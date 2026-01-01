package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

/**
 * Represents a directed edge in the railway network graph.
 *
 * <p>An edge represents a dependency between two network nodes.
 * The direction A → B means that node A must be upgraded before node B.
 *
 * <p>Each edge includes:
 * <ul>
 *   <li>Source and destination node identifiers</li>
 *   <li>Distance in kilometers</li>
 *   <li>Capacity (maximum number of trains per day)</li>
 *   <li>Cost (could be negative, representing potential savings)</li>
 * </ul>
 *
 * <p>Two {@code NetworkEdge} objects are considered equal if they connect
 * the same source and destination nodes, regardless of their other attributes.
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
     * Constructs a new {@code NetworkEdge}.
     *
     * @param fromNodeId source node identifier (must be upgraded first)
     * @param toNodeId destination node identifier (depends on source)
     * @param distanceKm distance between nodes in kilometers (must be non-negative)
     * @param capacity maximum number of trains per day (must be non-negative)
     * @param cost edge cost (could be negative)
     * @throws IllegalArgumentException if node identifiers are null/empty,
     *         or if distance/capacity are invalid
     */
    public NetworkEdge(String fromNodeId, String toNodeId,
                       double distanceKm, int capacity, double cost) {

        if (fromNodeId == null || fromNodeId.isBlank()) {
            throw new IllegalArgumentException("fromNodeId cannot be null or empty");
        }
        if (toNodeId == null || toNodeId.isBlank()) {
            throw new IllegalArgumentException("toNodeId cannot be null or empty");
        }
        if (distanceKm < 0) {
            throw new IllegalArgumentException("distanceKm must be non-negative");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be non-negative");
        }

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
        return String.format(
                "NetworkEdge{%s -> %s, dist=%.2fkm, cap=%d, cost=%.2f}",
                fromNodeId, toNodeId, distanceKm, capacity, cost
        );
    }
}
