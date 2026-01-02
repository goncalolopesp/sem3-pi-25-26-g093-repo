package pt.ipp.isep.dei.pi25_26.g093.app.utilities;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkEdge;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.NetworkNode;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Loads the railway network data required for USEI11 from CSV files.
 *
 * <p>This class converts CSV data into domain objects (NetworkNode and
 * NetworkEdge), using {@link CSVParser1} as a generic CSV parser.
 */
public class RailwayNetworkCsvLoader {

    private final String stationsCsvPath;
    private final String linesCsvPath;

    public RailwayNetworkCsvLoader(String stationsCsvPath, String linesCsvPath) {
        this.stationsCsvPath = stationsCsvPath;
        this.linesCsvPath = linesCsvPath;
    }

    /**
     * Loads all stations as NetworkNode objects.
     */
    public List<NetworkNode> loadNodes() throws FileNotFoundException {
        CSVParser1 parser = new CSVParser1(stationsCsvPath);
        List<Map<String, String>> data = parser.getCSVData();

        List<NetworkNode> nodes = new ArrayList<>();

        for (Map<String, String> row : data) {
            String id = row.get("Station id");
            String name = row.get("Station");

            double lat = Double.parseDouble(row.get("Lat"));
            double lon = Double.parseDouble(row.get("Lon"));
            double x = Double.parseDouble(row.get("CoordX"));
            double y = Double.parseDouble(row.get("CoordY"));

            nodes.add(new NetworkNode(id, name, lat, lon, x, y));
        }

        return nodes;
    }

    /**
     * Loads all railway connections as NetworkEdge objects.
     *
     * <p>Edges that reference non-existing stations are ignored.
     */
    public List<NetworkEdge> loadEdges(List<NetworkNode> nodes)
            throws FileNotFoundException {

        Set<String> validStationIds = new HashSet<>();
        for (NetworkNode node : nodes) {
            validStationIds.add(node.getNodeId());
        }

        CSVParser1 parser = new CSVParser1(linesCsvPath);
        List<Map<String, String>> data = parser.getCSVData();

        List<NetworkEdge> edges = new ArrayList<>();

        for (Map<String, String> row : data) {
            String from = row.get("departure_stid");
            String to = row.get("arrival_stid");

            if (!validStationIds.contains(from) || !validStationIds.contains(to)) {
                continue; // ignore invalid references
            }

            double distance = Double.parseDouble(row.get("dist"));
            int capacity = Integer.parseInt(row.get("capacity"));
            double cost = Double.parseDouble(row.get("cost"));

            edges.add(new NetworkEdge(from, to, distance, capacity, cost));
        }

        return edges;
    }
}
