package pt.ipp.isep.dei.pi25_26.g093.app.domain;

import java.util.Objects;

/**
 * Represents a node in the railway network graph.
 *
 * <p>A node can represent a station, terminal, or freight yard.
 *
 * <p>Each node has:
 * <ul>
 *   <li>A unique identifier (nodeId)</li>
 *   <li>A descriptive name</li>
 *   <li>Geographical coordinates (latitude, longitude)</li>
 *   <li>SVG coordinates for visualization purposes</li>
 * </ul>
 *
 * <p>Two {@code NetworkNode} objects are considered equal if they share
 * the same {@code nodeId}, regardless of other attributes.
 *
 * @author Logistics on Rails Team
 */
public class NetworkNode {

    private final String nodeId;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final double svgX;
    private final double svgY;

    /**
     * Constructs a new {@code NetworkNode}.
     *
     * @param nodeId unique identifier for the node
     * @param name name of the node (station, terminal, freight yard)
     * @param latitude geographical latitude (range: -90 to 90)
     * @param longitude geographical longitude (range: -180 to 180)
     * @param svgX X coordinate for SVG/Graphviz visualization
     * @param svgY Y coordinate for SVG/Graphviz visualization
     * @throws IllegalArgumentException if arguments are invalid
     */
    public NetworkNode(String nodeId, String name,
                       double latitude, double longitude,
                       double svgX, double svgY) {

        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("nodeId cannot be null or empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be between -180 and 180");
        }
        if (Double.isNaN(svgX) || Double.isNaN(svgY)) {
            throw new IllegalArgumentException("SVG coordinates must be valid numbers");
        }

        this.nodeId = nodeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.svgX = svgX;
        this.svgY = svgY;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getSvgX() {
        return svgX;
    }

    public double getSvgY() {
        return svgY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NetworkNode that = (NetworkNode) obj;
        return Objects.equals(nodeId, that.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId);
    }

    @Override
    public String toString() {
        return String.format("NetworkNode{id='%s', name='%s'}", nodeId, name);
    }
}
