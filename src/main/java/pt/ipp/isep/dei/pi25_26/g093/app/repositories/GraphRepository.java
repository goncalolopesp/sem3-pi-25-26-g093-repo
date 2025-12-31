package pt.ipp.isep.dei.pi25_26.g093.app.repositories;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.*;
import java.util.HashMap;
import java.util.Map;

public class GraphRepository {
    private final Graph<CompleteStation, Double> network = new MapGraph<>(false);
    private final Map<Integer, CompleteStation> idToStation = new HashMap<>();
    private final Map<CompleteStation, double[]> stationCoords = new HashMap<>();

    public void addStation(int id, CompleteStation s, double x, double y) {
        if (network.addVertex(s)) {
            idToStation.put(id, s);
            stationCoords.put(s, new double[]{x, y});
        }
    }

    public CompleteStation getStationById(int id) { return idToStation.get(id); }

    public double[] getCoords(CompleteStation s) { return stationCoords.get(s); }

    public void addTrack(CompleteStation s1, CompleteStation s2, Double dist) {
        network.addEdge(s1, s2, dist);
    }

    public Graph<CompleteStation, Double> getGraph() { return network; }
}