package pt.ipp.isep.dei.pi25_26.g093.app.controller;

import pt.ipp.isep.dei.pi25_26.g093.app.domain.CompleteStation;
import pt.ipp.isep.dei.pi25_26.g093.app.domain.PathResult;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Edge;
import pt.ipp.isep.dei.pi25_26.g093.app.graphs.Graph;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.GraphRepository;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RiskAwareShortestPathController {

    private final GraphRepository repo = Repositories.getInstance().getGraphRepository();

    public void generateDotFileForPath(List<CompleteStation> path, String filename) {
        try (FileWriter writer = new FileWriter(new File(filename + ".dot"))) {
            writer.write("graph path {\n");
            writer.write("  layout=neato;\n");
            writer.write("  overlap=false;\n");
            writer.write("  splines=true;\n");

            for (CompleteStation station : path) {
                double[] xy = repo.getCoords(station);
                double scaleFactor = 1.5;
                String formattedLine = String.format(Locale.US,
                        "  \"%s\" [pos=\"%.3f,%.3f!\"];\n",
                        station.getStation(),
                        (xy[0] / 1000) * scaleFactor,
                        (xy[1] / 1000) * scaleFactor);
                writer.write(formattedLine);
            }

            for (int i = 0; i < path.size() - 1; i++) {
                CompleteStation src = path.get(i);
                CompleteStation dest = path.get(i + 1);
                double edgeCost = getEdgeCost(src, dest);
                String formattedEdge = String.format(Locale.US,
                        "  \"%s\" -- \"%s\" [label=\"%.2f\"];\n",
                        src.getStation(),
                        dest.getStation(),
                        edgeCost);
                writer.write(formattedEdge);
            }

            writer.write("}\n");
        } catch (IOException e) {
            System.out.println("Error generating DOT file: " + e.getMessage());
        }
    }

    public boolean generatePathImage(String filename) {
        try {

            ProcessBuilder pb = new ProcessBuilder("neato", "-Tsvg", filename + ".dot", "-o", filename + ".svg");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            return process.waitFor() == 0;

        } catch (IOException | InterruptedException e) {
            System.out.println("Error generating image of the path: " + e.getMessage());
            return false;
        }
    }

    public PathResult computeShortestPathWithRisk(CompleteStation start, CompleteStation end) {
        Graph<CompleteStation, Double> g = repo.getGraph();
        if (g.numVertices() == 0) return new PathResult(false, "Graph is empty.");

        Map<CompleteStation, Double> dist = new HashMap<>();
        Map<CompleteStation, CompleteStation> path = new HashMap<>();
        CompleteStation cycleNode = null;

        for (CompleteStation station : g.vertices()) {
            dist.put(station, Double.POSITIVE_INFINITY);
            path.put(station, null);
        }

        dist.put(start, 0.0);

        for (int i = 0; i < g.numVertices() - 1; i++) {
            for (Edge<CompleteStation, Double> edge : g.edges()) {
                CompleteStation u = edge.getVOrig();
                CompleteStation v = edge.getVDest();
                double weight = edge.getWeight() + getRiskPenalty(u, v);

                if (dist.get(u) + weight < dist.get(v)) {
                    dist.put(v, dist.get(u) + weight);
                    path.put(v, u);
                }
            }
        }

        for (Edge<CompleteStation, Double> edge : g.edges()) {
            CompleteStation u = edge.getVOrig();
            CompleteStation v = edge.getVDest();
            double weight = edge.getWeight() + getRiskPenalty(u, v);

            if (dist.get(u) + weight < dist.get(v)) {
                cycleNode = v;
                break;
            }
        }

        if (cycleNode != null) {

            for (int i = 0; i < g.numVertices(); i++) {
                cycleNode = path.get(cycleNode);
            }

            List<CompleteStation> cycleStations = new ArrayList<>();
            List<Edge<CompleteStation, Double>> cycleEdges = new ArrayList<>();

            CompleteStation current = cycleNode;
            do {
                cycleStations.add(current);
                CompleteStation prev = path.get(current);
                if (prev == null) break;

                for (Edge<CompleteStation, Double> e : g.outgoingEdges(prev)) {
                    if (e.getVDest().equals(current)) {
                        cycleEdges.add(e);
                        break;
                    }
                }

                current = prev;
            } while (!current.equals(cycleNode));


            Collections.reverse(cycleStations);
            Collections.reverse(cycleEdges);

            return new PathResult(cycleStations, cycleEdges);
        }


        List<CompleteStation> pathList = new ArrayList<>();
        CompleteStation current = end;
        while (current != null) {
            pathList.add(current);
            current = path.get(current);
        }
        Collections.reverse(pathList);

        generateDotFileForPath(pathList, "shortest_path");

        generatePathImage("shortest_path");

        return new PathResult(true, pathList, dist.get(end));
    }

    public double getEdgeCost(CompleteStation station, CompleteStation nextStation) {
        for (Edge<CompleteStation, Double> edge : repo.getGraph().outgoingEdges(station)) {
            if (edge.getVDest().equals(nextStation)) {
                return edge.getWeight();
            }
        }
        return -1;
    }

    public CompleteStation findStationByName(String stationName) {
        for (CompleteStation station : repo.getGraph().vertices()) {
            if (station.getStation().equalsIgnoreCase(stationName)) {
                return station;
            }
        }
        return null;
    }

    public double getRiskPenalty(CompleteStation u, CompleteStation v) {

        if (isHighRiskSegment(u, v)) {
            return 1.0;
        }
        return 0.0;
    }

    private boolean isHighRiskSegment(CompleteStation u, CompleteStation v) {

        double edgeCost = getEdgeCost(u, v);

        if (edgeCost > 5){

            return true;

        }

        return false;
    }
}